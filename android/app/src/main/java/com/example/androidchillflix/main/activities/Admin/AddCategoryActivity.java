package com.example.androidchillflix.main.activities.Admin;

import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.categoriesApi;
import com.example.androidchillflix.main.models.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;
    private CheckBox checkboxPromoted;
    private categoriesApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etCategoryName = findViewById(R.id.etCategoryName);
        checkboxPromoted = findViewById(R.id.checkboxPromoted);
        Button btnSaveCategory = findViewById(R.id.btnSaveCategory);

        apiService = RetrofitClient.getCategoriesApiService(this);
        btnSaveCategory.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String categoryName = etCategoryName.getText().toString().trim();
        boolean isPromoted = checkboxPromoted.isChecked();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setPromoted(isPromoted);

        apiService.createCategory(newCategory).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddCategoryActivity.this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddCategoryActivity.this, "Failed to save category: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                Toast.makeText(AddCategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
