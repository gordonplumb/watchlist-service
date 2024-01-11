package com.gordonplumb.watchlist.models;

public class AuthenticationResponseFactory {
    public static AuthenticationResponse success(long userId, String name, String token) {
        return new AuthenticationResponse(userId, name, token);
    }

    public static AuthenticationResponse failure(String error) {
        return new AuthenticationResponse(error);
    }
}
