package com.karim.moviewatchlist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.karim.moviewatchlist.R;
import com.karim.moviewatchlist.api.RetrofitClient;
import com.karim.moviewatchlist.api.TmdbApi;
import com.karim.moviewatchlist.api.TmdbMovie;
import com.karim.moviewatchlist.api.TmdbMovieDetails;
import com.karim.moviewatchlist.api.TmdbSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieActivity extends AppCompatActivity {

    // Keys for sending the chosen movie data back to AddEditMovieActivity
    public static final String RESULT_TITLE = "result_title";
    public static final String RESULT_GENRE = "result_genre";
    public static final String RESULT_NOTES = "result_notes";
    public static final String RESULT_POSTER_URL = "result_poster_url";

    private TextInputEditText editSearch;
    private TextView textStatus;
    private SearchAdapter adapter;

    private final TmdbApi api = RetrofitClient.getApi();

    // For debouncing — only search 500ms after the user stops typing
    private final android.os.Handler searchHandler = new android.os.Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        editSearch = findViewById(R.id.editSearch);
        textStatus = findViewById(R.id.textStatus);
        RecyclerView recyclerView = findViewById(R.id.searchResultsRecyclerView);

        adapter = new SearchAdapter(this::onResultChosen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Trigger search when the user types (with 500ms debounce)
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    adapter.setResults(null);
                    textStatus.setText("Type a movie name to search");
                    textStatus.setVisibility(View.VISIBLE);
                    return;
                }
                searchRunnable = () -> performSearch(query);
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void performSearch(String query) {
        textStatus.setText("Searching...");
        textStatus.setVisibility(View.VISIBLE);
        adapter.setResults(null);

        api.searchMovies(RetrofitClient.API_KEY, query).enqueue(new Callback<TmdbSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbSearchResponse> call,
                                   @NonNull Response<TmdbSearchResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    textStatus.setText("Search failed. Check your API key or internet.");
                    return;
                }
                List<TmdbMovie> results = response.body().getResults();
                if (results == null || results.isEmpty()) {
                    textStatus.setText("No results for \"" + query + "\"");
                    return;
                }
                textStatus.setVisibility(View.GONE);
                adapter.setResults(results);
            }

            @Override
            public void onFailure(@NonNull Call<TmdbSearchResponse> call, @NonNull Throwable t) {
                textStatus.setText("Network error. Check your connection.");
            }
        });
    }

    /**
     * When the user taps a search result, fetch full details (to get proper genre names),
     * then send the data back to AddEditMovieActivity.
     */
    private void onResultChosen(TmdbMovie movie) {
        Toast.makeText(this, "Loading details...", Toast.LENGTH_SHORT).show();

        api.getMovieDetails(movie.getId(), RetrofitClient.API_KEY).enqueue(new Callback<TmdbMovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<TmdbMovieDetails> call,
                                   @NonNull Response<TmdbMovieDetails> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    // Fall back to the basic info if the details call fails
                    sendResultBack(movie.getTitle(), "", movie.getOverview(), movie.getFullPosterUrl());
                    return;
                }
                TmdbMovieDetails details = response.body();
                sendResultBack(
                        details.getTitle(),
                        details.getGenresAsString(),
                        details.getOverview(),
                        details.getFullPosterUrl()
                );
            }

            @Override
            public void onFailure(@NonNull Call<TmdbMovieDetails> call, @NonNull Throwable t) {
                sendResultBack(movie.getTitle(), "", movie.getOverview(), movie.getFullPosterUrl());
            }
        });
    }

    private void sendResultBack(String title, String genre, String notes, String posterUrl) {
        Intent result = new Intent();
        result.putExtra(RESULT_TITLE, title);
        result.putExtra(RESULT_GENRE, genre);
        result.putExtra(RESULT_NOTES, notes);
        result.putExtra(RESULT_POSTER_URL, posterUrl);
        setResult(RESULT_OK, result);
        finish();
    }
}