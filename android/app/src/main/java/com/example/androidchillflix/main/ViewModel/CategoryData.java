package com.example.androidchillflix.main.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.categoriesApi;
import com.example.androidchillflix.main.models.Category;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.Movies;
import com.example.androidchillflix.main.models.MoviesResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryData extends ViewModel {
    private final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> categoryMoviesLiveData = new MutableLiveData<>();
    private categoriesApi apiService;

    // Getter for categories
    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
    }

    // Getter for movies in a category
    public LiveData<List<String>> getCategoryMovies() {
        return categoryMoviesLiveData;
    }

    // Fetch all categories
    public void fetchCategories(Context context) {
        if (apiService == null) {
            apiService = RetrofitClient.getCategoriesApiService(context);
        }

        apiService.getAllCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesLiveData.setValue(response.body());
                } else {
                    Log.e("CategoryData", "Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.e("CategoryData", "Error fetching categories: " + t.getMessage());
            }
        });
    }

    // Fetch category by ID and get the associated movies
    public void fetchCategoryById(Context context, String categoryId) {
        if (apiService == null) {
            apiService = RetrofitClient.getCategoriesApiService(context);
        }

        apiService.getCategoryData(categoryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawResponse = new Gson().toJson(response.body());
                        Log.d("CategoryData", "Raw Response: " + rawResponse);

                        Movies moviesObject = response.body().getMovies();
                        if (moviesObject != null && moviesObject.getMovieList() != null && !moviesObject.getMovieList().isEmpty()) {
                            List<Movie> movieList = moviesObject.getMovieList();

                            List<String> movieIds = new ArrayList<>();
                            for (Movie movie : movieList) {
                                movieIds.add(movie.getId());
                            }

                            Log.d("CategoryData", "Movie IDs: " + movieIds);

                            MovieData movieData = new MovieData();
                            movieData.fetchMoviesByIds(context, movieIds);

                            movieData.getMovies().observeForever(movies -> {
                                if (movies != null) {
                                    Log.d("CategoryData", "Fetched full movie details: " + movies);
                                    categoryMoviesLiveData.setValue(movieIds);
                                }
                            });
                        } else {
                            Log.e("CategoryData", "Movie list is null or empty");
                            categoryMoviesLiveData.setValue(new ArrayList<>());
                        }
                    } catch (Exception e) {
                        Log.e("CategoryData", "Error parsing response: " + e.getMessage());
                    }
                } else {
                    Log.e("CategoryData", "Failed to fetch category data for ID: " + categoryId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e("CategoryData", "Error fetching category data: " + t.getMessage());
            }
        });
    }
}
