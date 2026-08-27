package com.karim.moviewatchlist.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbMovieDetails {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("genres")
    private List<Genre> genres;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return posterPath; }
    public String getReleaseDate() { return releaseDate; }
    public List<Genre> getGenres() { return genres; }

    public String getFullPosterUrl() {
        if (posterPath == null || posterPath.isEmpty()) return null;
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    /**
     * Returns genres as a comma-separated string, e.g. "Action, Sci-Fi, Thriller".
     */
    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(genres.get(i).getName());
        }
        return sb.toString();
    }

    // Nested class — represents one genre object inside the response
    public static class Genre {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() { return id; }
        public String getName() { return name; }
    }
}