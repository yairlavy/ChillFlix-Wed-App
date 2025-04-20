package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.MovieData;
import com.example.androidchillflix.main.adapters.MovieSliderAdapter;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.moviesApi;
import com.example.androidchillflix.main.components.BottomNavBar;
import com.example.androidchillflix.main.components.TopNavBar;
import com.example.androidchillflix.main.config.Config;
import com.example.androidchillflix.main.models.User;
import com.example.androidchillflix.main.repositories.userRepository;
import com.example.androidchillflix.main.utils.ImageGlideHelper;
import com.example.androidchillflix.main.utils.TokenManager;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailsActivity";
    private ImageView imgMovieBanner;
    private TextView tvMovieTitle, tvMovieDuration, tvMovieGenres, tvMovieOverview;
    private Button btnWatchlist;
    private RecyclerView recyclerViewSimilarMovies;
    private moviesApi apiService;
    private boolean isInWatchlist = false; // default state
    private boolean watchlistStatusChanged = false; // default state
    private String movieId;
    private String token; // retrieved via TokenManager
    private TokenManager tokenManager; // now stored as a field
    private String trailerUrl = "";
    private TextView tvNoRecommendedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Initialize Navigation bars
        new BottomNavBar(this);
        new TopNavBar(this);

        // Initialize Views
        imgMovieBanner = findViewById(R.id.ivMovieBanner);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieDuration = findViewById(R.id.tvMovieDuration);
        tvMovieGenres = findViewById(R.id.tvMovieGenres);
        tvMovieOverview = findViewById(R.id.tvMovieOverview);
        Button btnPlay = findViewById(R.id.btnPlay);
        btnWatchlist = findViewById(R.id.btnWatchlist);
        recyclerViewSimilarMovies = findViewById(R.id.recyclerViewSimilarMovies);
        tvNoRecommendedMovies = findViewById(R.id.tvNoRecommendedMovies);

        // Initialize API service and ViewModel
        apiService = RetrofitClient.getMoviesApiService(this);
        MovieData movieData = new ViewModelProvider(this).get(MovieData.class);

        // Get movie ID from intent
        Intent intent = getIntent();
        movieId = intent.getStringExtra("MOVIE_ID");

        // Retrieve token using your TokenManager
        tokenManager = new TokenManager(this);
        token = tokenManager.getToken();
        Log.d(TAG, "Retrieved token: " + token);

        if (movieId != null) {
            // Fetch movie details using ViewModel
            movieData.fetchMovieById(this, movieId);
            movieData.getMovie().observe(this, movie -> {
                if (movie != null) {
                    tvMovieTitle.setText(movie.getTitle());
                    tvMovieDuration.setText(movie.getRuntime() + getString(R.string.min));
                    tvMovieGenres.setText(getString(R.string.genres) + movie.getGenres());
                    tvMovieOverview.setText(movie.getOverview());
                    trailerUrl = movie.getTrailer();

                    // Load the backdrop image
                    ImageGlideHelper.loadImage(this, movie.getBackdropPath(),
                            ImageGlideHelper.ImageType.BACKDROP, imgMovieBanner);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                }
            });

            // Fetch recommended movies
            movieData.fetchRecommendedMovies(this, movieId);
            movieData.getMovies().observe(this, recommendedMovies -> {
                if (recommendedMovies != null && !recommendedMovies.isEmpty()) {
                    MovieSliderAdapter adapter = new MovieSliderAdapter(MovieDetailsActivity.this, recommendedMovies);
                    recyclerViewSimilarMovies.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerViewSimilarMovies.setAdapter(adapter);
                } else {
                    recyclerViewSimilarMovies.setVisibility(View.GONE);
                    tvNoRecommendedMovies.setVisibility(View.VISIBLE);
                }
            });

            // Play Button Action
            btnPlay.setOnClickListener(v -> {
                String baseUrl = Config.getBaseUrl();
                // If the trailer URL is not null or empty, send it to the VideoPlayerActivity
                Intent playIntent = new Intent(MovieDetailsActivity.this, VideoPlayerActivity.class);
                playIntent.putExtra("videoUrl", trailerUrl != null && !trailerUrl.isEmpty() ?
                        baseUrl + "Assets/movieAssets/" + trailerUrl :
                        "android.resource://" + getPackageName() + "/" + R.raw.video); // Fallback to default video if no trailer
                startActivity(playIntent);
            });

            // Check for watchlist status
            checkWatchlistStatus();

            // Watchlist Toggle Button Action
            btnWatchlist.setOnClickListener(v -> {
                if (isInWatchlist) {
                    // Remove from watchlist
                    apiService.removeMovieFromWatchlist(movieId).enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(MovieDetailsActivity.this, "Movie removed from watchlist", Toast.LENGTH_SHORT).show();
                                // Refresh the status after removal
                                checkWatchlistStatus();
                                watchlistStatusChanged = true;
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, "Failed to remove from watchlist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Add to watchlist
                    apiService.addMovieToWatchlist(movieId).enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(MovieDetailsActivity.this, "Movie added to watchlist", Toast.LENGTH_SHORT).show();
                                // Refresh the status after adding
                                checkWatchlistStatus();
                                watchlistStatusChanged = true;
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, "Failed to add to watchlist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Movie ID is missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Check if the movie is in the user's watchlist and update the button accordingly.
    private void checkWatchlistStatus() {
        if (token != null && movieId != null) {
            String userId = tokenManager.getUserIdFromToken(token);
            Log.d(TAG, "user ID: " + userId);
            Log.d(TAG, "Movie ID: " + movieId);
            if (userId != null) {
                userRepository userRepo = new userRepository(this);
                userRepo.getUser("Bearer " + token, userId).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            Log.d(TAG, "User received: " + user.getUsername());
                            if (user.getWatchlist() != null) {
                                Collection<String> movieIds = user.getWatchlist().values();
                                Log.d(TAG, "Watchlist: " + movieIds);
                                if (movieIds.contains(movieId)) {
                                    isInWatchlist = true;
                                    btnWatchlist.setText(R.string.remove_from_watchlist);
                                    Log.d(TAG, "Movie is in watchlist.");
                                } else {
                                    isInWatchlist = false;
                                    btnWatchlist.setText(R.string.add_to_watchlist);
                                    Log.d(TAG, "Movie is not in watchlist.");
                                }
                            } else {
                                isInWatchlist = false;
                                btnWatchlist.setText(R.string.add_to_watchlist);
                                Log.d(TAG, "User watchlist is null.");
                            }
                        } else {
                            Log.d(TAG, "Response unsuccessful: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.d(TAG, "Failure in checkWatchlistStatus: " + t.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (watchlistStatusChanged) {
            Intent intent = new Intent(this, BrowseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}