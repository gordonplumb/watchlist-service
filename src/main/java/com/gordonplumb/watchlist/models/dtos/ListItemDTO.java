package com.gordonplumb.watchlist.models.dtos;

import java.util.List;

public class ListItemDTO {
    private long id;
    private int tmdbId;
    private String title;
    private String[] tags;
    private int runtime;
    private Boolean watched;

    public ListItemDTO() {}

    public ListItemDTO(String title, String[] tags, int runtime, boolean watched) {
        this.title = title;
        this.tags = tags;
        this.runtime = runtime;
        this.watched = watched;
    }

    public ListItemDTO(long id, String title, String[] tags, int runtime, boolean watched) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.runtime = runtime;
        this.watched = watched;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }
}
