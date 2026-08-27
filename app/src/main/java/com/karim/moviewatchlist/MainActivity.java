package com.karim.moviewatchlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.karim.moviewatchlist.data.Movie;
import com.karim.moviewatchlist.ui.AddEditMovieActivity;
import com.karim.moviewatchlist.ui.MovieAdapter;
import com.karim.moviewatchlist.ui.MovieDetailActivity;
import com.karim.moviewatchlist.ui.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter adapter;

    private TextView movieCount;
    private View emptyState;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        recyclerView = findViewById(R.id.movieRecyclerView);
        movieCount = findViewById(R.id.movieCount);
        emptyState = findViewById(R.id.emptyState);
        ExtendedFloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        adapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up ViewModel — observe all movies
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getAllMovies().observe(this, this::updateList);

        // FAB → open add screen
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditMovieActivity.class);
            startActivity(intent);
        });
    }

    private void updateList(List<Movie> movies) {
        adapter.setMovies(movies);
        int count = movies.size();
        movieCount.setText(count == 1 ? "1 movie" : count + " movies");
        emptyState.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(movies.isEmpty() ? View.GONE : View.VISIBLE);
    }
}