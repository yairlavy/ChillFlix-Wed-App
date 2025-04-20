package com.example.androidchillflix.main.models;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoryWithMoviesResponse {
    private List<CategoryWithMovies> categoriesWithMovies;
    private List<String> watchedMovies;
    private int watchedMoviesCount;

    public List<CategoryWithMovies> getCategoriesWithMovies() {
        return categoriesWithMovies;
    }

    public List<String> getWatchedMovies() {
        return watchedMovies;
    }


    @NonNull
    @Override
    public String toString() {
        return "CategoryWithMoviesResponse{" +
                "categoriesWithMovies=" + categoriesWithMovies +
                ", watchedMovies=" + watchedMovies +
                ", watchedMoviesCount=" + watchedMoviesCount +
                '}';
    }
}
