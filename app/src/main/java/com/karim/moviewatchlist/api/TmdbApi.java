package com.karim.moviewatchlist.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {

    /**
     * Search movies by title.
     * Endpoint: https://api.themoviedb.org/3/search/movie?api_key=...&query=...
     */
    @GET("3/search/movie")
    Call<TmdbSearchResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String searchQuery
    );

    /**
     * Get full details for a single movie (includes proper genre names).
     * Endpoint: https://api.themoviedb.org/3/movie/{id}?api_key=...
     */
    @GET("3/movie/{movie_id}")
    Call<TmdbMovieDetails> getMovieDetails(
            @retrofit2.http.Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
}