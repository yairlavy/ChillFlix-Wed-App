package com.example.androidchillflix.main.api;

import com.example.androidchillflix.main.models.CategoryWithMoviesResponse;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.Movies;
import com.example.androidchillflix.main.models.RecommendedMoviesResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface moviesApi {
    @GET("movies")
    Call<CategoryWithMoviesResponse> getPromotedData();

    @GET("movies/search/{query}")
    Call<Movies> searchMovies(@Path("query") String query);

    @GET("movies/{id}")
    Call<Movie> getMovieData(@Path("id") String _id);

    @PUT("movies/{id}")
    Call<Movie> updateMovie(@Path("id") String id,
                            @Body Movie movie);

    @GET("movies/all")
    Call<List<Movie>> getAllMovies();

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") String id);
    @POST("movies")
    Call<Movie> createMovie(@Body Movie movie);

    @POST("movies/{id}/recommend")
    Call<Void> addMovieToWatchlist(@Path("id") String movieId);

    @DELETE("movies/{id}/recommend")
    Call<Void> removeMovieFromWatchlist(@Path("id") String movieId);

    @GET("movies/{id}/recommend")
    Call<RecommendedMoviesResponse> getRecommendMovies(@Path("id") String movieId);

//    @Multipart
//    @POST("movies")
//    Call<Movie> createMovie(
//            @Part("data") RequestBody data,
//            @Part MultipartBody.Part poster,
//            @Part MultipartBody.Part backdrop,
//            @Part MultipartBody.Part trailer
//    );

}
