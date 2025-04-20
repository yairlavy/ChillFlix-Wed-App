package com.example.androidchillflix.main.repositories;

import android.content.Context;

import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.userApi;
import com.example.androidchillflix.main.models.LoginRequest;
import com.example.androidchillflix.main.models.LoginResponse;
import com.example.androidchillflix.main.models.User;

import retrofit2.Call;

public class userRepository {
    private userApi getUserApiService;

    // Updated constructor to require Context
    public userRepository(Context context) {
        getUserApiService = RetrofitClient.getUserApiService(context);
    }

    public Call<User> createUser(User user) {
        return getUserApiService.createUser(user);
    }

    public Call<LoginResponse> loginUser(LoginRequest loginRequest) {
        return getUserApiService.loginUser(loginRequest);
    }

    public Call<User> getUser(String token, String userId) {
        return getUserApiService.getUser(token, userId);
    }
}
