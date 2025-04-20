package com.example.androidchillflix.main.activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.UploadAssets;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.models.UploadResponse;
import com.example.androidchillflix.main.utils.FileUtils;

import java.io.File;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovieActivity extends AppCompatActivity {

    private static final int PICK_POSTER_REQUEST = 101;
    private static final int PICK_BACKDROP_REQUEST = 102;
    private static final int PICK_TRAILER_REQUEST = 103;
    private static final String TAG = "AddMovieActivity";
    private EditText etMovieTitle, etOverview, etGenres, etPosterPath, etBackdropPath, etTrailer, etRuntime;
    private moviesApi apiService;
    private File posterFile, backdropFile, trailerFile;
    private UploadAssets uploadAssetsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        etMovieTitle = findViewById(R.id.etMovieTitle);
        etOverview = findViewById(R.id.etOverview);
        etGenres = findViewById(R.id.etGenres);
        etPosterPath = findViewById(R.id.etPosterPath);
        etBackdropPath = findViewById(R.id.etBackdropPath);
        etTrailer = findViewById(R.id.etTrailer);
        etRuntime = findViewById(R.id.etRuntime);
        Button btnAddMovie = findViewById(R.id.btnAddMovie);
        Button btnSelectPoster = findViewById(R.id.btnSelectPoster);
        Button btnSelectBackdrop = findViewById(R.id.btnSelectBackdrop);
        Button btnSelectTrailer = findViewById(R.id.btnSelectTrailer);

        apiService = RetrofitClient.getMoviesApiService(this);

        uploadAssetsViewModel = new ViewModelProvider(this).get(UploadAssets.class);

        btnAddMovie.setOnClickListener(v -> uploadAssets());
        btnSelectPoster.setOnClickListener(v -> selectFile(PICK_POSTER_REQUEST));
        btnSelectBackdrop.setOnClickListener(v -> selectFile(PICK_BACKDROP_REQUEST));
        btnSelectTrailer.setOnClickListener(v -> selectFile(PICK_TRAILER_REQUEST));
    }

    private void selectFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(requestCode == PICK_TRAILER_REQUEST ? "video/*" : "image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            File file = FileUtils.getFileFromUri(this, data.getData());
            if (file == null) {
                Toast.makeText(this, "Failed to get file", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (requestCode) {
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

    private void uploadAssets() {
        // Check if no file has been selected
        if (posterFile == null && backdropFile == null && trailerFile == null) {
            saveMovie(null);
        } else {
            uploadAssetsViewModel.uploadAssets(posterFile, backdropFile, trailerFile, new UploadAssets.UploadAssetsCallback() {
                @Override
                public void onUploadSuccess(UploadResponse response) {
                    runOnUiThread(() -> {
                        Toast.makeText(AddMovieActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        saveMovie(response);
                    });
                }

                @Override
                public void onUploadFailure(String errorMessage) {
                    runOnUiThread(() -> Toast.makeText(AddMovieActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void saveMovie(UploadResponse uploadResponse) {
        String title = etMovieTitle.getText().toString().trim();
        String runtime = etRuntime.getText().toString().trim();
        String genresInput = etGenres.getText().toString().trim();

        if (title.isEmpty()) {
            etMovieTitle.setError("Title is required");
            return;
        }

        Movie newMovie = new Movie();

        newMovie.setTitle(title);
        newMovie.setRuntime(runtime);
        newMovie.setGenres(Arrays.asList(genresInput.split(",")));
        newMovie.setOverview(etOverview.getText().toString().trim());

        // Extract storage names from the upload response's map
        if(uploadResponse != null) {
            String posterStorageName = uploadResponse.getStorageNames().get("poster_path");
            String backdropStorageName = uploadResponse.getStorageNames().get("backdrop_path");
            String trailerStorageName = uploadResponse.getStorageNames().get("trailer");

            newMovie.setPosterPath(posterStorageName);
            newMovie.setBackdropPath(backdropStorageName);
            newMovie.setTrailer(trailerStorageName);
        }

        Log.d(TAG, "Saving movie: " + newMovie);

        apiService.createMovie(newMovie).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Movie saved successfully!");
                    Toast.makeText(AddMovieActivity.this, "Movie saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Failed to save movie: " + response.message());
                    Toast.makeText(AddMovieActivity.this, "Failed to save movie: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Log.e(TAG, "Error saving movie: " + t.getMessage());
                Toast.makeText(AddMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}