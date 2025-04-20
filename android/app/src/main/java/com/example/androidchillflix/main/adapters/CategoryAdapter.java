package com.example.androidchillflix.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.models.CategoryWithMovies;
import com.example.androidchillflix.main.models.Movie;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<CategoryWithMovies> categoryList;

    public CategoryAdapter(Context context, List<CategoryWithMovies> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryWithMovies categoryWithMovies = categoryList.get(position);
        holder.categoryTitle.setText(categoryWithMovies.getCategory().getName());
        holder.updateMovies(categoryWithMovies.getMovieList());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView movieSliderRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            movieSliderRecyclerView = itemView.findViewById(R.id.movieSliderRecyclerView);
        }

        public void updateMovies(List<Movie> fullMovies) {
            MovieSliderAdapter movieSliderAdapter = new MovieSliderAdapter(itemView.getContext(), fullMovies);
            movieSliderRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            movieSliderRecyclerView.setAdapter(movieSliderAdapter);
        }
    }
}
