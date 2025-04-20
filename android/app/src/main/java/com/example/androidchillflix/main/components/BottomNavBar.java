package com.example.androidchillflix.main.components;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.ViewModel.CategoryData;
import com.example.androidchillflix.main.activities.Admin.AdminDashboard;
import com.example.androidchillflix.main.activities.BrowseActivity;
import com.example.androidchillflix.main.activities.CategoryActivity;
import com.example.androidchillflix.main.activities.HomeActivity;
import com.example.androidchillflix.main.activities.UserSettingsActivity;
import com.example.androidchillflix.main.adapters.CategoryPopupAdapter;
import com.example.androidchillflix.main.models.User;
import com.example.androidchillflix.main.repositories.userRepository;
import com.example.androidchillflix.main.utils.ImageGlideHelper;
import com.example.androidchillflix.main.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNavBar {

    private final LinearLayout profileContainer;
    private final ImageView profileImage;
    private final TextView profileName, homeSection, categoriesSection;
    private final Activity activity;
    private final CategoryData categoryData;
    private final TokenManager tokenManager;
    private User currentUser;
    private final SwitchCompat switchTheme;

    public BottomNavBar(Activity activity) {
        this.activity = activity;

        // Retrieve views from the updated XML
        profileContainer = activity.findViewById(R.id.profile_container);
        profileImage = activity.findViewById(R.id.profile_image);
        profileName = activity.findViewById(R.id.profile_name);
        homeSection = activity.findViewById(R.id.home_section);
        categoriesSection = activity.findViewById(R.id.categories_section);
        switchTheme = activity.findViewById(R.id.switchTheme);

        tokenManager = new TokenManager(activity);

        // Initialize ViewModel for categories
        categoryData = new ViewModelProvider((ViewModelStoreOwner) activity).get(CategoryData.class);
        categoryData.fetchCategories(activity);

        setListeners();
        updateUserProfile();

        if (activity instanceof BrowseActivity) {
            switchTheme.setVisibility(View.VISIBLE);
            initThemeSwitch();
        } else {
            switchTheme.setVisibility(View.GONE);
        }
    }

    // Fetches the user data from the API and updates the profile container.
    private void updateUserProfile() {
        String token = tokenManager.getToken();
        if (token == null) {
            Log.e("BottomNavBar", "Token is null, cannot fetch user info.");
            return;
        }
        String userId = tokenManager.getUserIdFromToken(token);
        if (userId == null) {
            Log.e("BottomNavBar", "Failed to extract userId from token.");
            return;
        }
        Log.d("BottomNavBar", "Fetching user info with token: " + token + ", userId: " + userId);

        userRepository userRepo = new userRepository(activity);
        userRepo.getUser("Bearer " + token, userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    Log.d("BottomNavBar", "User fetched successfully: " + currentUser.getName());

                    // Update UI if activity is still alive
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        profileName.setText(currentUser.getName());

                        // Load avatar with Glide
                        ImageGlideHelper.loadImage(activity, currentUser.getProfilePicture(),
                                ImageGlideHelper.ImageType.AVATAR, profileImage);
                    }
                } else {
                    Log.e("BottomNavBar", "Failed to load user info. Response code: "
                            + response.code() + " - " + response.message());
                    Toast.makeText(activity, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("BottomNavBar", "Error loading user info: " + t.getMessage(), t);
                Toast.makeText(activity, "Error loading user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sets the listeners for profile container, home, and categories.
    private void setListeners() {
        // Profile container popup
        profileContainer.setOnClickListener(v -> showProfilePopup());

        homeSection.setOnClickListener(v -> {
//            Toast.makeText(activity, "Home clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, BrowseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        });

        categoriesSection.setOnClickListener(v -> {
//            Toast.makeText(activity, "Categories clicked", Toast.LENGTH_SHORT).show();
            showCategoriesPopup();
        });
    }

    // Displays a popup window with logout, settings, and admin options.
    private void showProfilePopup() {
        View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_profile, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.setClippingEnabled(true);

        int offsetY = -profileContainer.getHeight() * 2
                - (int) (40 * activity.getResources().getDisplayMetrics().density);
        popupWindow.showAsDropDown(profileContainer, 0, offsetY);

        TextView tvLogout = popupView.findViewById(R.id.tvLogout);
        TextView tvSettings = popupView.findViewById(R.id.tvSettings);
        TextView tvAdmin = popupView.findViewById(R.id.tvAdmin);

        // Hide the admin option if the user is not an admin
        if (currentUser != null && !currentUser.isAdmin()) {
            tvAdmin.setVisibility(View.GONE);
        }

        tvLogout.setOnClickListener(v -> {
            popupWindow.dismiss();
            Toast.makeText(activity, "Logging out...", Toast.LENGTH_SHORT).show();
            tokenManager.clearToken();
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
            activity.finish();
        });

        tvSettings.setOnClickListener(v -> {
            popupWindow.dismiss();
//            Toast.makeText(activity, "Settings clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, UserSettingsActivity.class);
            activity.startActivity(intent);
            activity.finish();
        });

        tvAdmin.setOnClickListener(v -> {
            popupWindow.dismiss();
//            Toast.makeText(activity, "Admin clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, AdminDashboard.class);
            activity.startActivity(intent);
            activity.finish();
        });
    }

    // Displays a popup with categories.
    private void showCategoriesPopup() {
        View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_categories, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                true
        );
        popupWindow.setClippingEnabled(false);
        popupWindow.showAsDropDown(categoriesSection, -50, -categoriesSection.getHeight() - 10);

        RecyclerView popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        categoryData.getCategories().observe((androidx.lifecycle.LifecycleOwner) activity, categories -> {
            if (categories != null && !categories.isEmpty()) {
                CategoryPopupAdapter adapter = new CategoryPopupAdapter(categories, (categoryId, categoryName) -> {
                    popupWindow.dismiss();
//                    Toast.makeText(activity, "Navigating to " + categoryName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, CategoryActivity.class);
                    intent.putExtra("CATEGORY_ID", categoryId);
                    intent.putExtra("CATEGORY_TITLE", categoryName);
                    activity.startActivity(intent);
                });
                popupRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(activity, "No categories available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Initializes the theme switch (Dark Mode / Light Mode)
    private void initThemeSwitch() {
        SharedPreferences prefs = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);

        // Set initial state of the switch
        if (isDarkMode) {
            switchTheme.setChecked(true);
            switchTheme.setText(R.string.dark_mode);
        } else {
            switchTheme.setChecked(false);
            switchTheme.setText(R.string.light_mode);
        }

        // Listen for changes
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch to Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveThemePreference(true);
                switchTheme.setText(R.string.dark_mode);
            } else {
                // Switch to Light Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveThemePreference(false);
                switchTheme.setText(R.string.light_mode);
            }
            Intent intent = new Intent(activity, BrowseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        });
    }

    // Saves the user preference for dark mode.
    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences prefs = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        prefs.edit()
                .putBoolean("dark_mode", isDarkMode)
                .apply();
    }
}
