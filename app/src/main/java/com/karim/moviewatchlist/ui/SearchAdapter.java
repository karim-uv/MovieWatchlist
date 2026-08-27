package com.karim.moviewatchlist.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karim.moviewatchlist.R;
import com.karim.moviewatchlist.api.TmdbMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<TmdbMovie> results = new ArrayList<>();
    private final OnResultClickListener listener;

    public interface OnResultClickListener {
        void onResultClick(TmdbMovie movie);
    }

    public SearchAdapter(OnResultClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(results.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<TmdbMovie> newResults) {
        this.results = newResults != null ? newResults : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagePoster;
        private final TextView textTitle;
        private final TextView textYear;
        private final TextView textRatingTmdb;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textYear = itemView.findViewById(R.id.textYear);
            textRatingTmdb = itemView.findViewById(R.id.textRatingTmdb);
        }

        void bind(TmdbMovie movie, OnResultClickListener listener) {
            textTitle.setText(movie.getTitle());
            textYear.setText(movie.getReleaseYear().isEmpty() ? "—" : movie.getReleaseYear());
            textRatingTmdb.setText(String.format(Locale.getDefault(), "★ %.1f", movie.getVoteAverage()));

            // Load poster with Glide
            String posterUrl = movie.getFullPosterUrl();
            if (posterUrl != null) {
                Glide.with(itemView.getContext())
                        .load(posterUrl)
                        .into(imagePoster);
            } else {
                imagePoster.setImageDrawable(null);
            }

            itemView.setOnClickListener(v -> listener.onResultClick(movie));
        }
    }
}