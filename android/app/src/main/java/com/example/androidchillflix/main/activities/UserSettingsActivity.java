package com.example.androidchillflix.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.userApi;
import com.example.androidchillflix.main.models.UploadResponse;
import com.example.androidchillflix.main.models.User;
import com.example.androidchillflix.main.utils.FileUtils;
import com.example.androidchillflix.main.utils.TokenManager;
import java.io.File;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingsActivity extends AppCompatActivity {
    private static final String TAG = "UserSettingsActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgProfile1, imgProfile2, imgProfile3, imgProfile;
    private EditText etUsername, etNewPassword;
    private userApi apiService;
    private String selectedAvatarName;
    private File selectedFile = null;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Initialize UI components
        imgProfile1 = findViewById(R.id.imgProfile1);
        imgProfile2 = findViewById(R.id.imgProfile2);
        imgProfile3 = findViewById(R.id.imgProfile3);
        imgProfile = findViewById(R.id.imgProfile);
        Button btnUploadAvatar = findViewById(R.id.btnUploadAvatar);
        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Initialize API service
        apiService = RetrofitClient.getUserApiService(this);

        // Load current user details (including the admin flag)
        TokenManager tokenManager = TokenManager.getInstance(this);
        String token = tokenManager.getToken();
        String userId = tokenManager.getUserId();
        Log.d(TAG, "onCreate: token = " + token + ", userId = " + userId);
        if (token != null && userId != null) {
            apiService.getUser("Bearer " + token, userId).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUser = response.body();
                        etUsername.setText(currentUser.getName());
                        Log.d(TAG, "Loaded user: " + currentUser.getName() + ", isAdmin = " + currentUser.isAdmin());
                    } else {
                        Toast.makeText(UserSettingsActivity.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(UserSettingsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "getUser onFailure: " + t.getMessage());
                }
            });
        }

        // Set click listeners for preset avatar selection
        imgProfile1.setOnClickListener(v -> selectAvatar(R.drawable.avatar1, imgProfile1));
        imgProfile2.setOnClickListener(v -> selectAvatar(R.drawable.avatar2, imgProfile2));
        imgProfile3.setOnClickListener(v -> selectAvatar(R.drawable.avatar3, imgProfile3));

        // Set click listener for uploading a custom avatar image
        btnUploadAvatar.setOnClickListener(v -> selectImage());

        // Save changes button listener
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void selectAvatar(int avatarId, ImageView selectedImageView) {
        Log.d(TAG, "selectAvatar: avatarId = " + avatarId);
        imgProfile.setImageResource(avatarId);

        // Clear previous selections
        imgProfile1.setBackground(null);
        imgProfile2.setBackground(null);
        imgProfile3.setBackground(null);

        // Highlight the selected avatar
        selectedImageView.setBackgroundResource(R.drawable.circle_border);

        // Update the selected avatar name based on the drawable resource
        if (avatarId == R.drawable.avatar1) {
            selectedAvatarName = "avatar1.png";
        } else if (avatarId == R.drawable.avatar2) {
            selectedAvatarName = "avatar2.png";
        } else if (avatarId == R.drawable.avatar3) {
            selectedAvatarName = "avatar3.png";
        }
        Log.d(TAG, "selectAvatar: selectedAvatarName = " + selectedAvatarName);
        // Clear any custom file selection if a preset is chosen
        selectedFile = null;
    }

    //Opens the image picker to select a custom avatar image.
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                Log.d(TAG, "onActivityResult: imageUri = " + imageUri);
                // Display the selected image
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                imgProfile.setImageBitmap(selectedImage);
                if (inputStream != null) {
                    inputStream.close();
                }

                // Use FileUtils to get the file from the Uri
                File file = FileUtils.getFileFromUri(this, imageUri);
                if (file != null) {
                    selectedFile = file;
                    Log.d(TAG, "onActivityResult: file created at " + file.getAbsolutePath());
                } else {
                    Log.e(TAG, "onActivityResult: Failed to create file from Uri");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onActivityResult: Exception - " + e.getMessage());
            }
        }
    }

    //Uploads the avatar file to the server.
    private void uploadAvatarFile(File file, UploadCallback callback) {
        Log.d(TAG, "uploadAvatarFile: " + file.getAbsolutePath());
        String fileType = "";
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            fileType = "image/jpg";
        } else if (fileName.endsWith(".png")) {
            fileType = "image/png";
        }

        Log.d(TAG, "uploadAvatarFile: file type = " + fileType);
        RequestBody requestFile = RequestBody.create(MediaType.parse(fileType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitClient.getUploadApiService(this).uploadAvatar(body).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String storageName = response.body().getStorageName();
                    Log.d(TAG, "uploadAvatarFile: Upload successful, storageName = " + storageName);
                    callback.onSuccess(storageName);
                } else {
                    String errorMsg = "Avatar upload failed: " + response.message();
                    Log.e(TAG, "uploadAvatarFile: " + errorMsg);
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                String errorMsg = "Upload error: " + t.getMessage();
                Log.e(TAG, "uploadAvatarFile: " + errorMsg);
                callback.onFailure(errorMsg);
            }
        });
    }

    //Interface for upload callback.
    private interface UploadCallback {
        void onSuccess(String storageName);
        void onFailure(String error);
    }

    /**
     * Gathers updated user information and sends a PATCH request to update the user's details.
     * If a custom file was selected, it first uploads that file and then uses the returned storage name.
     * The admin flag from the current user is preserved.
     */
    private void saveChanges() {
        String newUsername = etUsername.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (newUsername.isEmpty() && newPassword.isEmpty() && selectedFile == null && selectedAvatarName.isEmpty()) {
            Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        if (currentUser != null) {
            updatedUser.setAdmin(currentUser.isAdmin());
        }
        if (!newUsername.isEmpty()) {
            updatedUser.setName(newUsername);
        }
        if (!newPassword.isEmpty()) {
            updatedUser.setPassword(newPassword);
        }

        if (selectedFile != null) {
            uploadAvatarFile(selectedFile, new UploadCallback() {
                @Override
                public void onSuccess(String storageName) {
                    updatedUser.setProfilePicture(storageName);
                    UserUpdate(updatedUser);
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(UserSettingsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updatedUser.setProfilePicture(selectedAvatarName);
            UserUpdate(updatedUser);
        }
    }

    //Update user details.
    private void UserUpdate(User updatedUser) {
        TokenManager tokenManager = TokenManager.getInstance(this);
        String userId = tokenManager.getUserId();

        apiService.updateUserDetails(userId, updatedUser).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserSettingsActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserSettingsActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(UserSettingsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
        finish();
    }
}