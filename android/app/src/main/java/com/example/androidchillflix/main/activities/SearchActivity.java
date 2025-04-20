package com.example.androidchillflix.main.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.MovieData;
import com.example.androidchillflix.main.adapters.MovieGridAdapter;
import com.example.androidchillflix.main.components.BottomNavBar;
import com.example.androidchillflix.main.components.TopNavBar;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView searchRecyclerView;
    private ProgressBar searchProgressBar;
    private TextView noResultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI Elements
        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        noResultsText = findViewById(R.id.noResultsText);
        TextView searchResultsTitle = findViewById(R.id.searchResultsTitle);

        // Set up RecyclerView
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Set up Bottom Navigation Bar
        new BottomNavBar(this);
        new TopNavBar(this);

        // Get Search Query
        String query = getIntent().getStringExtra("search_query");
        if (query != null && !query.isEmpty()) {
            searchResultsTitle.setText(getString(R.string.search_results_for) + query + "\"");
            fetchSearchResults(query);
        } else {
            Toast.makeText(this, "Invalid search query", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchSearchResults(String query) {
        searchProgressBar.setVisibility(View.VISIBLE);
        MovieData movieData = new ViewModelProvider(this).get(MovieData.class);
        movieData.fetchMoviesBySearch(this, query);

        movieData.getMovies().observe(this, movies -> {
            searchProgressBar.setVisibility(View.GONE);

            if (movies != null && !movies.isEmpty()) {
                searchRecyclerView.setAdapter(new MovieGridAdapter(movies));
                noResultsText.setVisibility(View.GONE);
            } else {
                noResultsText.setVisibility(View.VISIBLE);
            }
        });
    }
}
