package com.example.androidchillflix.main.api;

import com.example.androidchillflix.main.models.LoginRequest;
import com.example.androidchillflix.main.models.LoginResponse;
import com.example.androidchillflix.main.models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface userApi {
    @POST("users")
    Call<User> createUser(@Body User user);

    @POST("tokens")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("users/{id}")
    Call<User> getUser(
            @Header("Authorization") String token,
            @Path("id") String userId
    );

    @PATCH("users/{id}")
    Call<User> updateUserDetails(@Path("id") String id,
                                 @Body User user);

    @GET("users/all")
    Call<List<User>> getAllUsers();
}
