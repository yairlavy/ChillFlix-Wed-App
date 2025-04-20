package com.example.androidchillflix.main.activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchillflix.R;


import java.util.List;

import com.example.androidchillflix.main.activities.BrowseActivity;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.api.categoriesApi;
import com.example.androidchillflix.main.api.userApi;

import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.Category;
import com.example.androidchillflix.main.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboard extends AppCompatActivity {

    private TextView tvAppStats;
    private LinearLayout adminPanel;

    private int movieCount = 0;
    private int userCount = 0;
    private int categoryCount = 0;

    private userApi apiService1;
    private moviesApi apiService2;
    private categoriesApi apiService3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvAppStats = findViewById(R.id.tvAppStats);
        adminPanel = findViewById(R.id.adminPanel);
        Button btnOpenAdminPanel = findViewById(R.id.btnOpenAdminPanel);

        apiService2 = RetrofitClient.getMoviesApiService(this);
        apiService3 = RetrofitClient.getCategoriesApiService(this);
        apiService1 = RetrofitClient.getUserApiService(this);

        // Load live application statistics from the server using API calls
        loadAppStats();

        btnOpenAdminPanel.setOnClickListener(v -> {
            // Toggle the visibility of the admin panel
            adminPanel.setVisibility(adminPanel.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        setButtonActions();
    }

    // Load live statistics from the server
    private void loadAppStats() {
        // Get API service instances from RetrofitClient


        // API call to fetch all movies
        apiService2.getAllMovies().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieCount = response.body().size();
                } else {
                    movieCount = 0;
                }
                updateStatsDisplay();
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                movieCount = 0;
                updateStatsDisplay();
            }
        });

        // API call to fetch all categories
        apiService3.getAllCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryCount = response.body().size();
                } else {
                    categoryCount = 0;
                }
                updateStatsDisplay();
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                categoryCount = 0;
                updateStatsDisplay();
            }
        });

        // API call to fetch all users (make sure UserApi includes getAllUsers() method)
        apiService1.getAllUsers().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userCount = response.body().size();
                } else {
                    userCount = 0;
                }
                updateStatsDisplay();
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                userCount = 0;
                updateStatsDisplay();
            }
        });
    }

    // Update the UI with the latest statistics
    private void updateStatsDisplay() {
        String stats = "Movies: " + movieCount + "\n" +
                "Users: " + userCount + "\n" +
                "Categories: " + categoryCount + "\n" ;
        tvAppStats.setText(stats);
    }

    // Set onClick listeners for the admin panel buttons
    private void setButtonActions() {
        findViewById(R.id.btnAddMovie).setOnClickListener(v -> openActivity(AddMovieActivity.class));
        findViewById(R.id.btnEditMovie).setOnClickListener(v -> openActivity(EditMovieActivity.class));
        findViewById(R.id.btnDeleteMovie).setOnClickListener(v -> openActivity(DeleteMovieActivity.class));
        findViewById(R.id.btnAddCategory).setOnClickListener(v -> openActivity(AddCategoryActivity.class));
        findViewById(R.id.btnEditCategory).setOnClickListener(v -> openActivity(EditCategoryActivity.class));
        findViewById(R.id.btnDeleteCategory).setOnClickListener(v -> openActivity(DeleteCategoryActivity.class));
    }

    // Helper method to open a new activity
    private void openActivity(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
        finish();
    }
}
