package com.example.androidchillflix.main.activities.Admin;

import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.models.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteMovieActivity extends AppCompatActivity {

    private Spinner spinnerMoviesToDelete;
    private HashMap<String, String> movieData; // Maps movie names to IDs
    private moviesApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_movie);

        spinnerMoviesToDelete = findViewById(R.id.spinnerMoviesToDelete);
        Button btnDeleteMovie = findViewById(R.id.btnDeleteMovie);

        // Initialize API service
        apiService = RetrofitClient.getMoviesApiService(this);

        loadMovies();

        btnDeleteMovie.setOnClickListener(v -> deleteMovie());
    }

    private void loadMovies() {
        apiService.getAllMovies().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    ArrayList<String> movieNames = new ArrayList<>();
                    movieData = new HashMap<>();

                    for (Movie movie : movies) {
                        if (movie.getTitle() != null && movie.getId() != null) {
                            movieNames.add(movie.getTitle());
                            movieData.put(movie.getTitle(), movie.getId());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteMovieActivity.this,
                            R.layout.spinner_item, movieNames);
                    spinnerMoviesToDelete.setAdapter(adapter);
                } else {
                    Toast.makeText(DeleteMovieActivity.this, "Failed to load movies: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Toast.makeText(DeleteMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMovie() {
        String selectedMovie = spinnerMoviesToDelete.getSelectedItem().toString();

        if (selectedMovie.isEmpty()) {
            Toast.makeText(this, "Please select a movie to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        String movieId = movieData.get(selectedMovie);

        // Call the API to delete the movie
        apiService.deleteMovie(movieId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteMovieActivity.this, "Deleted " + selectedMovie + " successfully!", Toast.LENGTH_SHORT).show();
                    loadMovies(); // Refresh the movie list
                    finish();
                } else {
                    Toast.makeText(DeleteMovieActivity.this, "Failed to delete movie: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(DeleteMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
