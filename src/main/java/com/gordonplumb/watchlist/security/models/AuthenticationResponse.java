package com.gordonplumb.watchlist.security.models;

public class AuthenticationResponse {
    private String name;
    private String id;
    private String token;

    private String error;

    public AuthenticationResponse(String error) {
        this.error = error;
    }

    public AuthenticationResponse(String id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
