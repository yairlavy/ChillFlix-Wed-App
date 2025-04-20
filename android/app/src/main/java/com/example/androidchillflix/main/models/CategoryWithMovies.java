package com.example.androidchillflix.main.models;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoryWithMovies {
    private Category category;
    private List<Movie> movieList;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public String toString() {
        return "CategoryWithMovies{" +
                "category=" + category +
                ", movieList=" + movieList +
                '}';
    }
}
