package com.gordonplumb.watchlist.tmdb.models;

public class CastDTO extends Credit {

    private String character;

    public CastDTO() {
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
