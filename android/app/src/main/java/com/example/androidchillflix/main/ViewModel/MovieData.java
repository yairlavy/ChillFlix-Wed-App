// MovieData.java
package com.example.androidchillflix.main.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.Movies;
import com.example.androidchillflix.main.models.RecommendedMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieData extends ViewModel {

    private moviesApi apiService;

    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    public LiveData<List<Movie>> getMovies() {
        return moviesLiveData;
    }

    private final MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();
    public LiveData<Movie> getMovie() {return movieLiveData; }

    public void fetchMoviesBySearch(Context context, String query) {
        if (apiService == null) {
            apiService = RetrofitClient.getInstance(context).create(moviesApi.class);
        }
        apiService.searchMovies(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MovieData", "✅ Response received: " + response.body());

                    List<String> allMovieIds = new ArrayList<>();
                    for (Movie movie : response.body().getMovieList()) {
                        allMovieIds.add(movie.getId()); // Collecting movie IDs
                    }

                    if (allMovieIds.isEmpty()) {
                        // No movies found
                        moviesLiveData.setValue(new ArrayList<>());
                    } else {

                        // Now fetch full movie details using the IDs
                        fetchMoviesByIds(context, allMovieIds);
                    }
                } else {
                    Log.e("MovieData", " Search failed! Response code: " + response.code());
                    moviesLiveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                moviesLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchMoviesByIds(Context context, List<String> movieIds) {
        if (apiService == null) {
            apiService = RetrofitClient.getMoviesApiService(context);
        }

        List<Movie> fullMovies = new ArrayList<>();
        for (String movieId : movieIds) {
            apiService.getMovieData(movieId).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        fullMovies.add(response.body());

                        // Update LiveData when all movies are fetched
                        if (fullMovies.size() == movieIds.size()) {
                            moviesLiveData.setValue(fullMovies);
                        }
                    } else {
                        Log.e("MovieData", "Failed to fetch movie: " + movieId);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                    Log.e("MovieData", "Error fetching movie: " + movieId + " - " + t.getMessage());
                }
            });
        }
    }

    public void fetchMovieById(Context context, String movieId) {
        if (apiService == null) {
            apiService = RetrofitClient.getMoviesApiService(context);
        }
        apiService.getMovieData(movieId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Log.e("MovieData", "Error fetching movie: " + movieId + " - " + t.getMessage());
            }
        });
    }

    // Fetch recommended movies (IDs) and then full details
    public void fetchRecommendedMovies(Context context, String movieId) {
        if (apiService == null) {
            apiService = RetrofitClient.getMoviesApiService(context);
        }
        apiService.getRecommendMovies(movieId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecommendedMoviesResponse> call, @NonNull Response<RecommendedMoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecommendedMoviesResponse recommendedResponse = response.body();
                    List<String> recommendedIds = recommendedResponse.getMovieRecommendations();

                    // If no recommendations are returned, update LiveData with an empty list
                    if (recommendedIds == null || recommendedIds.isEmpty()) {
                        moviesLiveData.setValue(new ArrayList<>());
                        return;
                    }

                    fetchMoviesByIds(context, recommendedIds);
                } else {
                    Log.e("MovieData", "Failed to fetch recommended movies. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendedMoviesResponse> call, @NonNull Throwable t) {
                Log.e("MovieData", "Error fetching recommended movies: " + t.getMessage());
            }
        });
    }
}

