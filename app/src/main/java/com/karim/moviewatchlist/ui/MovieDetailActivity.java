package com.karim.moviewatchlist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.karim.moviewatchlist.R;
import com.karim.moviewatchlist.data.Movie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private MovieViewModel viewModel;
    private Movie currentMovie;

    private TextView textTitle, textGenre, textRating, textNotes, textStatus, textDateAdded;
    private ImageView imagePosterLarge;
    private TextView textPosterFallbackLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        textTitle = findViewById(R.id.textTitle);
        textGenre = findViewById(R.id.textGenre);
        textRating = findViewById(R.id.textRating);
        textNotes = findViewById(R.id.textNotes);
        textStatus = findViewById(R.id.textStatus);
        textDateAdded = findViewById(R.id.textDateAdded);
        imagePosterLarge = findViewById(R.id.imagePosterLarge);
        textPosterFallbackLarge = findViewById(R.id.textPosterFallbackLarge);
        MaterialButton buttonEdit = findViewById(R.id.buttonEdit);
        MaterialButton buttonDelete = findViewById(R.id.buttonDelete);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            finish();
            return;
        }

        viewModel.getMovieById(movieId).observe(this, movie -> {
            if (movie == null) {
                finish();
                return;
            }
            currentMovie = movie;
            displayMovie(movie);
        });

        buttonEdit.setOnClickListener(v -> {
            if (currentMovie == null) return;
            Intent intent = new Intent(this, AddEditMovieActivity.class);
            intent.putExtra(AddEditMovieActivity.EXTRA_MOVIE_ID, currentMovie.getId());
            startActivity(intent);
        });

        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    private void displayMovie(Movie movie) {
        textTitle.setText(movie.getTitle());
        textGenre.setText(movie.getGenre());

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stars.append(i < movie.getRating() ? "★" : "☆");
        }
        textRating.setText(stars.toString());

        if (movie.getNotes() == null || movie.getNotes().isEmpty()) {
            textNotes.setText("—");
            textNotes.setTextColor(ContextCompat.getColor(this, R.color.text_tertiary));
        } else {
            textNotes.setText(movie.getNotes());
            textNotes.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }

        textStatus.setText(movie.isWatched() ? "WATCHED" : "TO WATCH");

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        textDateAdded.setText("Added " + sdf.format(new Date(movie.getDateAdded())));

        // Poster
        String posterUrl = movie.getPosterUrl();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            imagePosterLarge.setVisibility(View.VISIBLE);
            textPosterFallbackLarge.setVisibility(View.GONE);
            Glide.with(this)
                    .load(posterUrl)
                    .into(imagePosterLarge);
        } else {
            imagePosterLarge.setVisibility(View.GONE);
            textPosterFallbackLarge.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete() {
        if (currentMovie == null) return;
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.action_delete, (dialog, which) -> {
                    viewModel.delete(currentMovie);
                    finish();
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }
}