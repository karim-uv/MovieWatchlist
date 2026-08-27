package com.karim.moviewatchlist.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbMovie {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;          // the synopsis

    @SerializedName("poster_path")
    private String posterPath;        // partial path, e.g. "/abc123.jpg"

    @SerializedName("release_date")
    private String releaseDate;       // "2010-07-16"

    @SerializedName("vote_average")
    private double voteAverage;       // 0.0 to 10.0

    @SerializedName("genre_ids")
    private List<Integer> genreIds;   // TMDB returns genre IDs, not names

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return posterPath; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public List<Integer> getGenreIds() { return genreIds; }

    public String getFullPosterUrl() {
        if (posterPath == null || posterPath.isEmpty()) return null;
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    /**
     * Returns just the year, e.g. "2010" — handy for UI display.
     */
    public String getReleaseYear() {
        if (releaseDate == null || releaseDate.length() < 4) return "";
        return releaseDate.substring(0, 4);
    }
}