package com.gordonplumb.watchlist.models;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueListAndTmdbId", columnNames = { "watchlist_id", "tmdb_id" })})
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "watchlist_id")
    private Watchlist watchlist;
    private int tmdbId;
    @Column(nullable = false)
    private String title;
    private String tags; // csv
    private int runtime;
    private boolean watched;

    public ListItem() {}

    public ListItem(Watchlist watchlist, int tmdbId, String title, String tags, int runtime, boolean watched) {
        this.watchlist = watchlist;
        this.tmdbId = tmdbId;
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

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
