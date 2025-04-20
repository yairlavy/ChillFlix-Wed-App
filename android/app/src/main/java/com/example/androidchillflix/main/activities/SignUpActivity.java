package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.userApi;
import com.example.androidchillflix.main.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, usernameEditText, passwordEditText;
    private ImageView avatar1;
    private ImageView avatar2;
    private ImageView avatar3;
    private String selectedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_activity);

        // Initialize views
        nameEditText = findViewById(R.id.name_input);
        emailEditText = findViewById(R.id.email_input);
        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        Button signUpButton = findViewById(R.id.sign_up_button);
        TextView signInLink = findViewById(R.id.sign_in_link);
        ImageView goToHome = findViewById(R.id.logo);
        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        avatar3 = findViewById(R.id.avatar3);

        // Add underline to "Sign In" link
        signInLink.setPaintFlags(signInLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Default selected avatar
        selectedAvatar = "avatar1";

        // Prefill email if passed via intent
        Intent receivedIntent = getIntent(); // Rename this intent to avoid conflict
        String prefilledEmail = receivedIntent.getStringExtra("EMAIL");
        if (prefilledEmail != null && !prefilledEmail.isEmpty()) {
            emailEditText.setText(prefilledEmail);
        }

        // Avatar selection logic
        avatar1.setOnClickListener(v -> {
            selectedAvatar = "avatar1.png";
            highlightAvatar(avatar1, avatar2, avatar3);
        });

        avatar2.setOnClickListener(v -> {
            selectedAvatar = "avatar2.png";
            highlightAvatar(avatar2, avatar1, avatar3);
        });

        avatar3.setOnClickListener(v -> {
            selectedAvatar = "avatar3.png";
            highlightAvatar(avatar3, avatar1, avatar2);
        });

        // Sign-Up Button Click Listener
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!isValidEmail(email)) {
                Toast.makeText(SignUpActivity.this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!name.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                // Create a new User object
                User newUser = new User(username, email, password, name, selectedAvatar);

                // Use Retrofit to send a POST request
                userApi getUserApiService = RetrofitClient.getUserApiService(this);
                getUserApiService.createUser(newUser).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(SignUpActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                            Intent goToSignInIntent = new Intent(SignUpActivity.this, SignInActivity.class); // Renamed intent
                            startActivity(goToSignInIntent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to create user: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Home
        goToHome.setOnClickListener(v -> {
            Intent goToHomeIntent = new Intent(SignUpActivity.this, HomeActivity.class); // Renamed intent
            startActivity(goToHomeIntent);
            finish();
        });

        // Navigate to Login
        signInLink.setOnClickListener(v -> {
            Intent goToSignInIntent = new Intent(SignUpActivity.this, SignInActivity.class); // Renamed intent
            startActivity(goToSignInIntent);
            finish();
        });
    }

    private boolean isValidEmail(String email) {
        // Validates if the email contains '@gmail.com'
        return email.contains("@gmail.com") && email.contains("@");
    }

    private void highlightAvatar(ImageView selected, ImageView... others) {
        selected.setBackgroundResource(R.drawable.avatar_highlight); // Add a highlight drawable
        for (ImageView other : others) {
            other.setBackgroundResource(0); // Clear background for other avatars
        }
    }
}
