package com.gordonplumb.watchlist.services;

import com.gordonplumb.watchlist.config.JwtService;
import com.gordonplumb.watchlist.models.*;
import com.gordonplumb.watchlist.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponseFactory.failure("An account already exists with this email");
        }
        User user = new User(
            request.getName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseFactory.success(savedUser.getId(), savedUser.getName(), jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseFactory.success(user.getId(), user.getName(), jwtToken);
    }
}
