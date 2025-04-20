package com.example.androidchillflix.main.activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.UploadResponse;
import com.example.androidchillflix.main.utils.FileUtils;
import com.example.androidchillflix.main.ViewModel.UploadAssets;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovieActivity extends AppCompatActivity {

    private static final int PICK_POSTER_REQUEST = 101;
    private static final int PICK_BACKDROP_REQUEST = 102;
    private static final int PICK_TRAILER_REQUEST = 103;

    // Spinner for selecting the movie to edit
    private Spinner spinnerMovies;
    // Fields similar to AddMovieActivity
    private EditText etMovieTitle, etOverview, etGenres, etRuntime, etPosterPath, etBackdropPath, etTrailer;
    private moviesApi apiService;
    // List to store the movies loaded from the server
    private List<Movie> moviesList;
    // The currently selected movie (full object)
    private Movie selectedMovie;
    // Files chosen by the user (for updating assets)
    private File posterFile, backdropFile, trailerFile;
    // Shared ViewModel for uploading asset files
    private UploadAssets uploadAssetsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        spinnerMovies = findViewById(R.id.spinnerMovies);
        etMovieTitle = findViewById(R.id.etMovieTitle);
        etOverview = findViewById(R.id.etOverview);
        etGenres = findViewById(R.id.etGenres);
        etRuntime = findViewById(R.id.etRuntime);
        etPosterPath = findViewById(R.id.etPosterPath);
        etBackdropPath = findViewById(R.id.etBackdropPath);
        etTrailer = findViewById(R.id.etTrailer);
        Button btnUpdateMovie = findViewById(R.id.btnUpdateMovie);
        Button btnSelectPoster = findViewById(R.id.btnSelectPoster);
        Button btnSelectBackdrop = findViewById(R.id.btnSelectBackdrop);
        Button btnSelectTrailer = findViewById(R.id.btnSelectTrailer);

        apiService = RetrofitClient.getMoviesApiService(this);
        uploadAssetsViewModel = new ViewModelProvider(this).get(UploadAssets.class);

        loadMovies();

        // When a movie is selected, populate all fields with its current data.
        spinnerMovies.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedMovie = moviesList.get(position);
                populateFields(selectedMovie);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedMovie = null;
            }
        });

        btnSelectPoster.setOnClickListener(v -> selectFile(PICK_POSTER_REQUEST));
        btnSelectBackdrop.setOnClickListener(v -> selectFile(PICK_BACKDROP_REQUEST));
        btnSelectTrailer.setOnClickListener(v -> selectFile(PICK_TRAILER_REQUEST));

        btnUpdateMovie.setOnClickListener(v -> updateMovie());
    }

    // Loads the list of movies from the server and populates the spinner.
    private void loadMovies() {
        apiService.getAllMovies().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moviesList = response.body();
                    ArrayList<String> movieTitles = new ArrayList<>();
                    for (Movie movie : moviesList) {
                        movieTitles.add(movie.getTitle());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditMovieActivity.this,
                            R.layout.spinner_item, movieTitles);
                    spinnerMovies.setAdapter(adapter);
                } else {
                    Toast.makeText(EditMovieActivity.this, "Failed to load movies: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Toast.makeText(EditMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Populates all fields with the selected movie's current data.
    private void populateFields(Movie movie) {
        if (movie != null) {
            etMovieTitle.setText(movie.getTitle());
            etOverview.setText(movie.getOverview());
            etGenres.setText(movie.getGenres() != null ? TextUtils.join(",", movie.getGenres()) : "");
            etRuntime.setText(movie.getRuntime());
            etPosterPath.setText(movie.getPosterPath());
            etBackdropPath.setText(movie.getBackdropPath());
            etTrailer.setText(movie.getTrailer());
        }
    }

    // Opens the file picker for selecting asset files.
    private void selectFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(requestCode == PICK_TRAILER_REQUEST ? "video/*" : "image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null && data.getData() != null) {
            File file = FileUtils.getFileFromUri(this, data.getData());
            if(file == null) {
                Toast.makeText(this, "Failed to get file", Toast.LENGTH_SHORT).show();
                return;
            }
            switch(requestCode) {
                case PICK_POSTER_REQUEST:
                    posterFile = file;
                    etPosterPath.setText(file.getName());
                    break;
                case PICK_BACKDROP_REQUEST:
                    backdropFile = file;
                    etBackdropPath.setText(file.getName());
                    break;
                case PICK_TRAILER_REQUEST:
                    trailerFile = file;
                    etTrailer.setText(file.getName());
                    break;
            }
        }
    }

    // Update the movie base on the new data
    private void updateMovie() {
        if (selectedMovie == null) {
            Toast.makeText(this, "No movie selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an updated movie object using current field values.
        Movie updatedMovie = new Movie();
        updatedMovie.setId(selectedMovie.getId());
        updatedMovie.setTitle(etMovieTitle.getText().toString().trim());
        updatedMovie.setOverview(etOverview.getText().toString().trim());
        updatedMovie.setGenres(Arrays.asList(etGenres.getText().toString().trim().split(",")));
        updatedMovie.setRuntime(etRuntime.getText().toString().trim());
        // By default, keep the current asset URLs.
        updatedMovie.setPosterPath(selectedMovie.getPosterPath());
        updatedMovie.setBackdropPath(selectedMovie.getBackdropPath());
        updatedMovie.setTrailer(selectedMovie.getTrailer());

        // If any new asset file is selected, upload them (only the provided ones are updated)
        if (posterFile != null || backdropFile != null || trailerFile != null) {
            uploadAssetsViewModel.uploadAssets(posterFile, backdropFile, trailerFile, new UploadAssets.UploadAssetsCallback() {
                @Override
                public void onUploadSuccess(UploadResponse response) {
                    runOnUiThread(() -> {
                        if(response.getStorageNames() != null) {
                            String posterStorage = response.getStorageNames().get("poster_path");
                            String backdropStorage = response.getStorageNames().get("backdrop_path");
                            String trailerStorage = response.getStorageNames().get("trailer");
                            if (posterStorage != null) {
                                updatedMovie.setPosterPath(posterStorage);
                            }
                            if (backdropStorage != null) {
                                updatedMovie.setBackdropPath(backdropStorage);
                            }
                            if (trailerStorage != null) {
                                updatedMovie.setTrailer(trailerStorage);
                            }
                        }
                        callUpdateMovieApi(updatedMovie);
                    });
                }
                @Override
                public void onUploadFailure(String errorMessage) {
                    runOnUiThread(() -> Toast.makeText(EditMovieActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            // No new asset files; update directly.
            callUpdateMovieApi(updatedMovie);
        }
    }

    // Calls the API to update the movie with the given data.
    private void callUpdateMovieApi(Movie movie) {
        apiService.updateMovie(movie.getId(), movie).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditMovieActivity.this, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditMovieActivity.this, "Failed to update movie: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(EditMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}