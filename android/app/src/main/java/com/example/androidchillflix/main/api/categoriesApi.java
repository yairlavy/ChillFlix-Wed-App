package com.example.androidchillflix.main.api;

import com.example.androidchillflix.main.models.Category;
import com.example.androidchillflix.main.models.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface categoriesApi {
    @GET("categories")
    Call<List<Category>> getAllCategories();
    @GET("categories/{id}")
    Call<MoviesResponse> getCategoryData(@Path("id") String _id);

    @POST("categories")
    Call<Category> createCategory(@Body Category category); //

    @PATCH("categories/{id}")
    Call<Category> updateCategory(@Path("id") String id, @Body Category category);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") String id);
}
