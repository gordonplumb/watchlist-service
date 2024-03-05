package com.gordonplumb.watchlist.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.gordonplumb.watchlist.exceptions.BadRequestException;
import com.gordonplumb.watchlist.exceptions.ForbiddenException;
import com.gordonplumb.watchlist.security.models.AuthenticationResponse;
import com.gordonplumb.watchlist.user.UserRepository;
import com.gordonplumb.watchlist.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationService {

    @Value("${google.clientId}")
    private String CLIENT_ID;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationService(
        UserRepository userRepository,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse authenticate(String idTokenString) {
        if (idTokenString == null || !idTokenString.startsWith("Bearer")) {
            throw new BadRequestException("Missing authorization header");
        }
        NetHttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString.substring(7));
        } catch (Exception e) {
            throw new ForbiddenException("Authentication failed");
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            String email = payload.getEmail();

            if (userId != null) {
                User user = userRepository.findByGoogleId(userId).orElse(null);
                if (user == null) {
                    user = new User(
                        userId,
                        (String) payload.get("name"),
                        email
                    );
                    userRepository.save(user);
                }
                String jwtToken = jwtService.generateToken(user);
                return AuthenticationResponseFactory.success(user.getId(), user.getName(), jwtToken);
            }
        }

        throw new ForbiddenException("Authentication failed");
    }
}
