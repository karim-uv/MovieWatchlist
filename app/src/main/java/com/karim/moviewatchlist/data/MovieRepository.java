package com.karim.moviewatchlist.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieRepository {

    private final MovieDao movieDao;
    private final ExecutorService executor;

    // Constructor — gets the DAO from the database
    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getInstance(application);
        movieDao = db.movieDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ===== Methods that READ data =====
    // These return LiveData, which is safe to call on the main thread

    public LiveData<List<Movie>> getAllMovies() {
        return movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getWantToWatchMovies() {
        return movieDao.getWantToWatchMovies();
    }

    public LiveData<List<Movie>> getWatchedMovies() {
        return movieDao.getWatchedMovies();
    }

    public LiveData<Movie> getMovieById(int movieId) {
        return movieDao.getMovieById(movieId);
    }

    // ===== Methods that WRITE data =====
    // These MUST run on a background thread, so we wrap them in executor.execute()

    public void insert(Movie movie) {
        executor.execute(() -> movieDao.insert(movie));
    }

    public void update(Movie movie) {
        executor.execute(() -> movieDao.update(movie));
    }

    public void delete(Movie movie) {
        executor.execute(() -> movieDao.delete(movie));
    }
}