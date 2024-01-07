package com.gordonplumb.watchlist.models;

public class AuthenticationResponseFactory {
    public static AuthenticationResponse success(String token) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    public static AuthenticationResponse failure(String error) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setError(error);
        return response;
    }
}
