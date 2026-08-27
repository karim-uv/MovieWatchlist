package com.karim.moviewatchlist.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.karim.moviewatchlist.data.Movie;
import com.karim.moviewatchlist.data.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final MovieRepository repository;

    // Cached LiveData — the UI observes these
    private final LiveData<List<Movie>> allMovies;
    private final LiveData<List<Movie>> wantToWatchMovies;
    private final LiveData<List<Movie>> watchedMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
        wantToWatchMovies = repository.getWantToWatchMovies();
        watchedMovies = repository.getWatchedMovies();
    }

    // ===== Read methods — UI calls these to observe data =====

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Movie>> getWantToWatchMovies() {
        return wantToWatchMovies;
    }

    public LiveData<List<Movie>> getWatchedMovies() {
        return watchedMovies;
    }

    public LiveData<Movie> getMovieById(int movieId) {
        return repository.getMovieById(movieId);
    }

    // ===== Write methods — UI calls these to modify data =====

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }
}