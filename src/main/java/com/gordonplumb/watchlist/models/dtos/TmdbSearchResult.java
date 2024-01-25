package com.gordonplumb.watchlist.models.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class TmdbSearchResult {
    private int page;
    private TmdbSearchItem[] results;
    @JsonAlias({ "total_pages" })
    private int totalPages;
    @JsonAlias({ "total_results" })
    private int totalResults;

    public TmdbSearchResult() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public TmdbSearchItem[] getResults() {
        return results;
    }

    public void setResults(TmdbSearchItem[] results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
