package com.example.androidchillflix.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.activities.MovieDetailsActivity;
import com.example.androidchillflix.main.models.Movie;
import com.example.androidchillflix.main.utils.ImageGlideHelper;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {

    private final List<Movie> movieList;

    public MovieGridAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_grid, parent, false);
        return new MovieViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        Context context = holder.itemView.getContext();

        if (movie.getTitle() != null && !movie.getTitle().isEmpty()) {
            holder.movieTitle.setText(movie.getTitle());
        } else {
            holder.movieTitle.setText(R.string.no_title_available);
        }

        ImageGlideHelper.loadImage(context, movie.getPosterPath(),
                ImageGlideHelper.ImageType.POSTER, holder.moviePoster);

        // Add click listener to open MovieActivity
        holder.moviePoster.setOnClickListener(v -> {
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
        private final ImageView moviePoster;
        private final TextView movieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
        }
    }
}
