package com.example.androidchillflix.main.activities.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.categoriesApi;
import com.example.androidchillflix.main.models.Category;
import com.example.androidchillflix.main.ViewModel.CategoryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {
    private Spinner spinnerCategories;
    private EditText etCategoryName;
    private CheckBox checkboxPromoted;
    private categoriesApi apiService;
    private List<Category> categoriesList;
    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        spinnerCategories = findViewById(R.id.spinnerCategories);
        etCategoryName = findViewById(R.id.etCategoryName);
        checkboxPromoted = findViewById(R.id.checkboxPromoted);
        Button btnUpdateCategory = findViewById(R.id.btnUpdateCategory);

        // Initialize API service
        apiService = RetrofitClient.getCategoriesApiService(this);

        // Initialize the CategoryData ViewModel
        CategoryData categoryDataViewModel = new ViewModelProvider(this).get(CategoryData.class);
        categoryDataViewModel.fetchCategories(this);
        categoryDataViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                categoriesList = categories;
                ArrayList<String> categoryNames = new ArrayList<>();
                for (Category c : categoriesList) {
                    categoryNames.add(c.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        EditCategoryActivity.this,
                        R.layout.spinner_item,
                        categoryNames
                );
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spinnerCategories.setAdapter(adapter);
            }
        });

        // When a category is selected, populate the fields with its data
        spinnerCategories.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoriesList.get(position);
                populateFields(selectedCategory);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedCategory = null;
            }
        });

        btnUpdateCategory.setOnClickListener(v -> updateCategory());
    }

    //Populate the EditText and CheckBox with the selected category's data.
    private void populateFields(Category category) {
        if (category == null) return;
        etCategoryName.setText(category.getName());
        checkboxPromoted.setChecked(category.isPromoted());
    }

    //Gather updated data from fields and call the update API.
    private void updateCategory() {
        if (selectedCategory == null) {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the category fields from user input
        String newName = etCategoryName.getText().toString().trim();
        boolean isPromoted = checkboxPromoted.isChecked();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedCategory.setName(newName);
        selectedCategory.setPromoted(isPromoted);

        // Call the API to update the category.
        apiService.updateCategory(selectedCategory.getId(), selectedCategory)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditCategoryActivity.this, "Category updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditCategoryActivity.this,
                                    "Failed to update category: " + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                        Toast.makeText(EditCategoryActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
