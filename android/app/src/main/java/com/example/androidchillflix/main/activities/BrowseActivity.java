package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.MovieData;
import com.example.androidchillflix.main.ViewModel.PromotedCategories;
import com.example.androidchillflix.main.adapters.CategoryAdapter;
import com.example.androidchillflix.main.components.BottomNavBar;
import com.example.androidchillflix.main.components.TopNavBar;
import com.example.androidchillflix.main.models.CategoryWithMovies;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.utils.ImageGlideHelper;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    private MovieData movieData;
    private RecyclerView categoriesRecyclerView;
    private ImageView randomMovieBackdrop;
    private TextView randomMovieTitle;
    private ProgressBar randomMovieProgressBar;
    private ProgressBar categoriesProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // Initialize Navigation bars
        new BottomNavBar(this);
        new TopNavBar(this);

        // Initialize views
        randomMovieBackdrop = findViewById(R.id.randomMovieBackdrop);
        categoriesRecyclerView = findViewById(R.id.categoriesContainer);
        randomMovieProgressBar = findViewById(R.id.randomMovieProgressBar);
        categoriesProgressBar = findViewById(R.id.categoriesProgressBar);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        PromotedCategories promotedCategories = new ViewModelProvider(this).get(PromotedCategories.class);
        movieData = new ViewModelProvider(this).get(MovieData.class);

        // Show progress bars while loading data
        randomMovieProgressBar.setVisibility(View.VISIBLE);
        categoriesProgressBar.setVisibility(View.VISIBLE);

        promotedCategories.getCategories().observe(this, categories -> {
            if (categories != null) {
                fetchFullMovieDetails(categories);
            } else {
                Log.e("BrowseActivity", "Failed to fetch categories");
                categoriesProgressBar.setVisibility(View.GONE); // Hide on error
            }
        });

        promotedCategories.fetchPromotedCategories(this);
    }

    private void fetchFullMovieDetails(List<CategoryWithMovies> categories) {
        List<String> allMovieIds = new ArrayList<>();

        // Collect all movie IDs from all categories
        for (CategoryWithMovies category : categories) {
            for (Movie movie : category.getMovieList()) {
                allMovieIds.add(movie.getId());
            }
        }

        // Fetch all movie details
        movieData.fetchMoviesByIds(this, allMovieIds);

        movieData.getMovies().observe(this, fullMovies -> {
            if (fullMovies != null) {
                // Map movie IDs to Movie objects for quick access
                for (CategoryWithMovies category : categories) {
                    List<Movie> updatedMovieList = new ArrayList<>();
                    for (Movie movie : category.getMovieList()) {
                        for (Movie fullMovie : fullMovies) {
                            if (movie.getId().equals(fullMovie.getId())) {
                                updatedMovieList.add(fullMovie);
                                break;
                            }
                        }
                    }
                    category.setMovieList(updatedMovieList);  // Update category with full movie details
                }

                // Display a random movie backdrop
                randomMovieTitle = findViewById(R.id.movieTitle);
                displayRandomMovieBackdrop(fullMovies);

                // Set the adapter with fully populated categories
                categoriesRecyclerView.setAdapter(new CategoryAdapter(this, categories));

                // Hide progress bars after loading
            }
            randomMovieProgressBar.setVisibility(View.GONE);
            categoriesProgressBar.setVisibility(View.GONE);
        });
    }

    private void displayRandomMovieBackdrop(List<Movie> movies) {
        int randomIndex = (int) (Math.random() * movies.size());
        Movie randomMovie = movies.get(randomIndex);

        ImageGlideHelper.loadImage(this, randomMovie.getBackdropPath(),
                ImageGlideHelper.ImageType.BACKDROP, randomMovieBackdrop);

        randomMovieTitle.setText(randomMovie.getTitle());

        randomMovieBackdrop.setOnClickListener(v -> {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", randomMovie.getId()); // Pass the movie ID
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}