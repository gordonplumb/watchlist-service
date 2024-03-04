package com.gordonplumb.watchlist.security;

import com.gordonplumb.watchlist.security.models.AuthenticationResponse;

public class AuthenticationResponseFactory {
    public static AuthenticationResponse success(String userId, String name, String token) {
        return new AuthenticationResponse(userId, name, token);
    }

    public static AuthenticationResponse failure(String error) {
        return new AuthenticationResponse(error);
    }
}
