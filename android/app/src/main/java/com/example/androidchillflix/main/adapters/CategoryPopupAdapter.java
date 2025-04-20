package com.example.androidchillflix.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.models.Category;  // Import the Category model

import java.util.List;

public class CategoryPopupAdapter extends RecyclerView.Adapter<CategoryPopupAdapter.ViewHolder> {

    private final List<Category> categories;
    private final OnCategoryClickListener listener;

    // Update the interface to pass both category ID and name
    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryId, String categoryName);
    }

    // Constructor updated to take a list of Category objects
    public CategoryPopupAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_popup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());

        // Pass both category ID and name when clicked
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category.getId(), category.getName()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
