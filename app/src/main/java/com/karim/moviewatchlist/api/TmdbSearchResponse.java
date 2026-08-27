package com.karim.moviewatchlist.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbSearchResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<TmdbMovie> results;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() { return page; }
    public List<TmdbMovie> getResults() { return results; }
    public int getTotalPages() { return totalPages; }
    public int getTotalResults() { return totalResults; }
}