package com.gordonplumb.watchlist.security;

import com.gordonplumb.watchlist.security.models.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestHeader(value = "Authorization") String idTokenString
    ) {
        return ResponseEntity.ok(service.authenticate(idTokenString));
    }
}
