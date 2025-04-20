package com.example.androidchillflix.main.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.models.Category;
import com.example.androidchillflix.main.models.CategoryWithMovies;
import com.example.androidchillflix.main.models.CategoryWithMoviesResponse;
import com.example.androidchillflix.main.models.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotedCategories extends ViewModel {
    private final MutableLiveData<List<CategoryWithMovies>> categoriesLiveData = new MutableLiveData<>();
    private moviesApi apiService;

    public LiveData<List<CategoryWithMovies>> getCategories() {
        return categoriesLiveData;
    }

    public void fetchPromotedCategories(Context context) {
        if (apiService == null) {
            apiService = RetrofitClient.getMoviesApiService(context);
        }

        apiService.getPromotedData().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWithMoviesResponse> call, @NonNull Response<CategoryWithMoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryWithMovies> categoryList = response.body().getCategoriesWithMovies();
                    List<String> watchedMoviesIds = response.body().getWatchedMovies();

                    // Filter out categories with empty movie lists
                    categoryList.removeIf(category -> category.getMovieList().isEmpty());

                    // Add Watchlist category if there are watched movies
                    if (!watchedMoviesIds.isEmpty()) {
                        CategoryWithMovies watchlistCategory = getWatchListCategory(watchedMoviesIds);
                        categoryList.add(0, watchlistCategory);  // Add to the start
                    }

                    categoriesLiveData.setValue(categoryList);
                } else {
                    Log.e("PromotedCategories", "Response unsuccessful or body is null");
                    categoriesLiveData.setValue(null);
                }
            }

            @NonNull
            private CategoryWithMovies getWatchListCategory(List<String> watchedMoviesIds) {
                Category watchlistCategory = new Category();
                watchlistCategory.setName("Watchlist");
                watchlistCategory.setPromoted(true);

                List<Movie> watchedMovies = new ArrayList<>();
                for (String movieId : watchedMoviesIds) {
                    Movie movie = new Movie();
                    movie.setId(movieId);
                    watchedMovies.add(movie);
                }

                CategoryWithMovies categoryWithMovies = new CategoryWithMovies();
                categoryWithMovies.setCategory(watchlistCategory);
                categoryWithMovies.setMovieList(watchedMovies);

                return categoryWithMovies;
            }

            @Override
            public void onFailure(@NonNull Call<CategoryWithMoviesResponse> call, @NonNull Throwable t) {
                Log.e("PromotedCategories", "API call failed: " + t.getMessage());
                categoriesLiveData.setValue(null);
            }
        });
    }
}
