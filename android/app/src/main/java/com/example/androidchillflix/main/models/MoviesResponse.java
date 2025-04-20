package com.example.androidchillflix.main.models;

public class MoviesResponse {
    private final Category category;
    private final Movie movies;

    public MoviesResponse(Category category, Movie movies) {
        this.category = category;
        this.movies = movies;
    }

    public Category getCategory() {
        return category;
    }

    public Movie getMovies() {
        return movies;
    }
}
