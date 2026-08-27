package com.karim.moviewatchlist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.karim.moviewatchlist.R;
import com.karim.moviewatchlist.data.Movie;

public class AddEditMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private MovieViewModel viewModel;
    private TextInputEditText editTitle, editGenre, editNotes;
    private RatingBar ratingBar;
    private MaterialSwitch switchWatched;
    private TextView screenTitle;

    private int editingId = -1;
    private Movie editingMovie = null;

    // Holds the poster URL from a TMDB search (null if no search was used)
    private String currentPosterUrl = null;

    // Launcher for the search activity result
    private final ActivityResultLauncher<Intent> searchLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String title = data.getStringExtra(SearchMovieActivity.RESULT_TITLE);
                            String genre = data.getStringExtra(SearchMovieActivity.RESULT_GENRE);
                            String notes = data.getStringExtra(SearchMovieActivity.RESULT_NOTES);
                            String posterUrl = data.getStringExtra(SearchMovieActivity.RESULT_POSTER_URL);

                            if (title != null) editTitle.setText(title);
                            if (genre != null) editGenre.setText(genre);
                            if (notes != null) editNotes.setText(notes);
                            currentPosterUrl = posterUrl;

                            Toast.makeText(this, "Movie loaded from TMDB", Toast.LENGTH_SHORT).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        editTitle = findViewById(R.id.editTitle);
        editGenre = findViewById(R.id.editGenre);
        editNotes = findViewById(R.id.editNotes);
        ratingBar = findViewById(R.id.ratingBar);
        switchWatched = findViewById(R.id.switchWatched);
        screenTitle = findViewById(R.id.screenTitle);
        MaterialButton buttonSave = findViewById(R.id.buttonSave);
        MaterialButton buttonCancel = findViewById(R.id.buttonCancel);
        MaterialButton buttonSearchOnline = findViewById(R.id.buttonSearchOnline);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Are we editing an existing movie?
        editingId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (editingId != -1) {
            screenTitle.setText(R.string.title_edit_movie);
            viewModel.getMovieById(editingId).observe(this, movie -> {
                if (movie != null && editingMovie == null) {
                    editingMovie = movie;
                    editTitle.setText(movie.getTitle());
                    editGenre.setText(movie.getGenre());
                    editNotes.setText(movie.getNotes());
                    ratingBar.setRating(movie.getRating());
                    switchWatched.setChecked(movie.isWatched());
                    currentPosterUrl = movie.getPosterUrl();
                }
            });
        } else {
            screenTitle.setText(R.string.title_add_movie);
        }

        // Open the TMDB search screen
        buttonSearchOnline.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchMovieActivity.class);
            searchLauncher.launch(intent);
        });

        buttonSave.setOnClickListener(v -> save());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void save() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String genre = editGenre.getText() != null ? editGenre.getText().toString().trim() : "";
        String notes = editNotes.getText() != null ? editNotes.getText().toString().trim() : "";
        int rating = (int) ratingBar.getRating();
        boolean watched = switchWatched.isChecked();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(genre)) {
            Toast.makeText(this, "Please enter a genre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingMovie != null) {
            editingMovie.setTitle(title);
            editingMovie.setGenre(genre);
            editingMovie.setNotes(notes);
            editingMovie.setRating(rating);
            editingMovie.setWatched(watched);
            editingMovie.setPosterUrl(currentPosterUrl);
            viewModel.update(editingMovie);
        } else {
            Movie movie = new Movie(title, genre, rating, notes, watched,
                    System.currentTimeMillis(), currentPosterUrl);
            viewModel.insert(movie);
        }

        finish();
    }
}