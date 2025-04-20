package com.example.androidchillflix.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.activities.MovieDetailsActivity;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.utils.ImageGlideHelper;

import java.util.List;

public class MovieSliderAdapter extends RecyclerView.Adapter<MovieSliderAdapter.MovieViewHolder> {

    private final Context context;
    private final List<Movie> movieList;

    public MovieSliderAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());

        ImageGlideHelper.loadImage(context, movie.getPosterPath(),
                ImageGlideHelper.ImageType.POSTER, holder.moviePosterImageView);

        // Show a Toast with the movie Name when clicking on the poster
        holder.moviePosterImageView.setOnClickListener(v -> Toast.makeText(context, "Movie Name: " + movie.getTitle(), Toast.LENGTH_SHORT).show());


       // Add click listener to open MovieActivity
        holder.moviePosterImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getId()); // Pass the movie ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePosterImageView;
        TextView movieTitle;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.moviePosterImageView);
            movieTitle = itemView.findViewById(R.id.movieTitle);

        }
    }
}
