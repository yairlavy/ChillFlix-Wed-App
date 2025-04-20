package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.example.androidchillflix.R;
import com.example.androidchillflix.main.utils.TokenManager;

public class HomeActivity extends AppCompatActivity {

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_activity);

        // Check if token exists
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Log.d("TokenCheck", "No token found. Staying on HomeActivity.");
            // No token, stay on HomeActivity
        } else {
            // Check if the token is expired
            JWT jwt = new JWT(token);
            if (jwt.isExpired(10)) { // Check with a 10-second leeway
                Log.d("TokenCheck", "Token expired. Staying on HomeActivity.");
                tokenManager.clearToken(); // Clear expired token
            } else {
                Log.d("TokenCheck", "Token found: " + token + ". Navigating to BrowseActivity.");
                // Token exists, navigate to BrowseActivity
                Intent intent = new Intent(HomeActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();  // Close HomeActivity
            }
        }

        // Initialize UI elements
        emailInput = findViewById(R.id.email_input);
        Button loginButton = findViewById(R.id.signIn_button);
        Button registerButton = findViewById(R.id.signUp_button);
        Button getStartedButton = findViewById(R.id.get_started_button);

        // Navigate to Login
        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(loginIntent);
        });

        // Navigate to Register
        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(HomeActivity.this, SignUpActivity.class);
            startActivity(registerIntent);
        });

        // Get Started button click listener
        getStartedButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (isValidEmail(email)) {
                Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
                intent.putExtra("EMAIL", email); // Pass the email to SignUpActivity
                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return email.contains("@gmail.com"); // Simple validation for Gmail addresses
    }
}
