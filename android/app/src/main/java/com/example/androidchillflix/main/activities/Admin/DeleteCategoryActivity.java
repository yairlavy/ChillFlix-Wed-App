package com.example.androidchillflix.main.activities.Admin;

import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.categoriesApi;
import com.example.androidchillflix.main.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteCategoryActivity extends AppCompatActivity {

    private Spinner spinnerCategoriesToDelete;
    private HashMap<String, String> categoryData; // Maps category names to IDs
    private categoriesApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        spinnerCategoriesToDelete = findViewById(R.id.spinnerCategoriesToDelete);
        Button btnDeleteCategory = findViewById(R.id.btnDeleteCategory);

        // Initialize API service
        apiService = RetrofitClient.getCategoriesApiService(this);

        loadCategories();

        btnDeleteCategory.setOnClickListener(v -> deleteCategory());
    }

    private void loadCategories() {
        apiService.getAllCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    ArrayList<String> categoryNames = new ArrayList<>();
                    categoryData = new HashMap<>();

                    for (Category category : categories) {
                        if (category.getName() != null && category.getId() != null) {
                            categoryNames.add(category.getName());
                            categoryData.put(category.getName(), category.getId());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteCategoryActivity.this,
                            R.layout.spinner_item, categoryNames);
                    spinnerCategoriesToDelete.setAdapter(adapter);
                } else {
                    Toast.makeText(DeleteCategoryActivity.this, "Failed to load categories: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(DeleteCategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory() {
        String selectedCategory = spinnerCategoriesToDelete.getSelectedItem().toString();

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please select a category to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoryId = categoryData.get(selectedCategory);

        // Call the API to delete the category
        apiService.deleteCategory(categoryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteCategoryActivity.this, "Deleted " + selectedCategory + " successfully!", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Refresh the category list
                    finish();
                } else {
                    Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(DeleteCategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
