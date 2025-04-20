package com.example.androidchillflix.main.models;

import androidx.annotation.NonNull;
import java.util.List;

public class Movie extends Movies {
    private String _id;
    private String title;
    private String runtime;
    private List<String> genres;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private String trailer;

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRuntime() { return runtime; }
    public void setRuntime(String runtime) { this.runtime = runtime; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getPosterPath() { return poster_path; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    public String getBackdropPath() { return backdrop_path; }
    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }

    public String getTrailer() { return trailer; }
    public void setTrailer(String trailer) { this.trailer = trailer; }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genres=" + genres +
                ", overview='" + overview + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", trailer='" + trailer + '\'' +
                '}';
    }
}
