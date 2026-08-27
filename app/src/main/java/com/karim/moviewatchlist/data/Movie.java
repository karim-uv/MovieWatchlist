package com.karim.moviewatchlist.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String genre;
    private int rating;
    private String notes;
    private boolean watched;
    private long dateAdded;
    private String posterUrl;   // NEW — URL to the movie poster image (can be null)

    public Movie(String title, String genre, int rating, String notes,
                 boolean watched, long dateAdded, String posterUrl) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.notes = notes;
        this.watched = watched;
        this.dateAdded = dateAdded;
        this.posterUrl = posterUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getRating() { return rating; }
    public String getNotes() { return notes; }
    public boolean isWatched() { return watched; }
    public long getDateAdded() { return dateAdded; }
    public String getPosterUrl() { return posterUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRating(int rating) { this.rating = rating; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setWatched(boolean watched) { this.watched = watched; }
    public void setDateAdded(long dateAdded) { this.dateAdded = dateAdded; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}