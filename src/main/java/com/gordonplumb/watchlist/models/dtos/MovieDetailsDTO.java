package com.gordonplumb.watchlist.models.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class MovieDetailsDTO {
    private int id;
    private String title;
    private String overview;
    private int runtime;
    private GenreDTO[] genres;
    private CreditsDTO credits;
    @JsonAlias({ "release_date" })
    private String releaseDate;

    public MovieDetailsDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public GenreDTO[] getGenres() {
        return genres;
    }

    public void setGenres(GenreDTO[] genres) {
        this.genres = genres;
    }

    public CreditsDTO getCredits() {
        return credits;
    }

    public void setCredits(CreditsDTO credits) {
        this.credits = credits;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
