package com.karim.moviewatchlist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    // Insert a new movie into the database
    @Insert
    void insert(Movie movie);

    // Update an existing movie
    @Update
    void update(Movie movie);

    // Delete a movie
    @Delete
    void delete(Movie movie);

    // Get all movies, newest first — returns LiveData so the UI updates automatically
    @Query("SELECT * FROM movies ORDER BY dateAdded DESC")
    LiveData<List<Movie>> getAllMovies();

    // Get only movies you want to watch
    @Query("SELECT * FROM movies WHERE watched = 0 ORDER BY dateAdded DESC")
    LiveData<List<Movie>> getWantToWatchMovies();

    // Get only movies you've already watched
    @Query("SELECT * FROM movies WHERE watched = 1 ORDER BY dateAdded DESC")
    LiveData<List<Movie>> getWatchedMovies();

    // Get a single movie by its ID (used for the detail / edit screen)
    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    LiveData<Movie> getMovieById(int movieId);
}