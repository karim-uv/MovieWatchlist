package com.karim.moviewatchlist.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karim.moviewatchlist.R;
import com.karim.moviewatchlist.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public Movie getMovieAt(int position) {
        return movies.get(position);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final View statusBar;
        private final ImageView imagePoster;
        private final TextView textPosterFallback;
        private final TextView textTitle;
        private final TextView textGenre;
        private final TextView textRating;
        private final TextView textStatus;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            statusBar = itemView.findViewById(R.id.statusBar);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textPosterFallback = itemView.findViewById(R.id.textPosterFallback);
            textTitle = itemView.findViewById(R.id.textTitle);
            textGenre = itemView.findViewById(R.id.textGenre);
            textRating = itemView.findViewById(R.id.textRating);
            textStatus = itemView.findViewById(R.id.textStatus);
        }

        void bind(Movie movie, OnMovieClickListener listener) {
            textTitle.setText(movie.getTitle());
            textGenre.setText(movie.getGenre());

            // Build the star rating string
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                stars.append(i < movie.getRating() ? "★" : "☆");
            }
            textRating.setText(stars.toString());

            // Status text + colored bar
            int statusColor;
            String statusText;
            if (movie.isWatched()) {
                statusColor = ContextCompat.getColor(itemView.getContext(), R.color.watched);
                statusText = itemView.getContext().getString(R.string.status_watched);
            } else {
                statusColor = ContextCompat.getColor(itemView.getContext(), R.color.want_to_watch);
                statusText = itemView.getContext().getString(R.string.status_to_watch);
            }
            statusBar.setBackgroundColor(statusColor);
            textStatus.setText(statusText);

            // Poster — load from URL if available, otherwise show the emoji fallback
            String posterUrl = movie.getPosterUrl();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                imagePoster.setVisibility(View.VISIBLE);
                textPosterFallback.setVisibility(View.GONE);
                Glide.with(itemView.getContext())
                        .load(posterUrl)
                        .into(imagePoster);
            } else {
                imagePoster.setImageDrawable(null);
                imagePoster.setVisibility(View.GONE);
                textPosterFallback.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        }
    }
}