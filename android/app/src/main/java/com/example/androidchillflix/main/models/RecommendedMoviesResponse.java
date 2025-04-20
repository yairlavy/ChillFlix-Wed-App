package com.example.androidchillflix.main.models;

import java.util.List;

public class RecommendedMoviesResponse {
    private List<String> MovieRecommendations;

    public List<String> getMovieRecommendations() {
        return MovieRecommendations;
    }

    public void setMovieRecommendations(List<String> movieRecommendations) {
        MovieRecommendations = movieRecommendations;
    }
}

