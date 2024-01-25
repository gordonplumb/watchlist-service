package com.gordonplumb.watchlist.models.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class TmdbSearchItem {
    private int id;
    @JsonAlias({ "genre_ids"})
    private int[] genreIds;
    private String title;
    private String overview;

    @JsonAlias({ "release_date" })
    private String releaseDate;

    public TmdbSearchItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
