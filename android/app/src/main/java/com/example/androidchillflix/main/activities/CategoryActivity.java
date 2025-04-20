package com.example.androidchillflix.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.CategoryData;
import com.example.androidchillflix.main.ViewModel.MovieData;
import com.example.androidchillflix.main.adapters.MovieGridAdapter;
import com.example.androidchillflix.main.components.BottomNavBar;
import com.example.androidchillflix.main.components.TopNavBar;

public class CategoryActivity extends AppCompatActivity {

    private MovieData movieData;
    private RecyclerView movieRecyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize  Navigation bars
        new BottomNavBar(this);
        new TopNavBar(this);

        movieRecyclerView = findViewById(R.id.recyclerView);
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Initialize ViewModels
        CategoryData categoryData = new ViewModelProvider(this).get(CategoryData.class);
        movieData = new ViewModelProvider(this).get(MovieData.class);

        // Get category ID from Intent
        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        String CategoryTitle = getIntent().getStringExtra("CATEGORY_TITLE");


        if (categoryId != null) {
            categoryData.fetchCategoryById(this, categoryId);
            categoryTitle.setText(CategoryTitle);
        } else {
            Toast.makeText(this, "Category ID not found", Toast.LENGTH_SHORT).show();
        }

        // Observe movie IDs from CategoryData
        categoryData.getCategoryMovies().observe(this, movieIds -> {
            Log.d("CategoryActivity", "Movie IDs: " + movieIds);
            if (movieIds != null && !movieIds.isEmpty()) {
                movieData.fetchMoviesByIds(this, movieIds);
            } else {
                Toast.makeText(this, "No movies found in this category", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe movies and display them
        movieData.getMovies().observe(this, movies -> {
            Log.d("CategoryActivity", "Movies loaded: " + movies.size());
            if (!movies.isEmpty()) {
                MovieGridAdapter adapter = new MovieGridAdapter(movies);
                movieRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

