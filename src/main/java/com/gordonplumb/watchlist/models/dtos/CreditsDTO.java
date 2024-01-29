package com.gordonplumb.watchlist.models.dtos;

public class CreditsDTO {
    private CastDTO[] cast;
    private CrewDTO[] crew;

    public CreditsDTO() {
    }

    public CastDTO[] getCast() {
        return cast;
    }

    public void setCast(CastDTO[] cast) {
        this.cast = cast;
    }

    public CrewDTO[] getCrew() {
        return crew;
    }

    public void setCrew(CrewDTO[] crew) {
        this.crew = crew;
    }
}
