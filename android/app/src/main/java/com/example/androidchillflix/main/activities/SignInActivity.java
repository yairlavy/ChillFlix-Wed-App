package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.api.userApi;
import com.example.androidchillflix.main.models.LoginRequest;
import com.example.androidchillflix.main.models.LoginResponse;
import com.example.androidchillflix.main.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin_activity);

        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        Button signInButton = findViewById(R.id.sign_in_button);
        TextView signUpLink = findViewById(R.id.sign_up_link);
        ImageView goToHome = findViewById(R.id.logo);
        signUpLink.setPaintFlags(signUpLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Initialize TokenManager
        TokenManager tokenManager = new TokenManager(this);

        signInButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                LoginRequest loginRequest = new LoginRequest(username, password);
                userApi getUserApiService = RetrofitClient.getUserApiService(this);

                getUserApiService.loginUser(loginRequest).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String token = response.body().getToken();
                            tokenManager.saveToken(token); // Save token in SharedPreferences

                            // Verify that the token is stored
                            String storedToken = tokenManager.getToken();
                            Log.d("TokenCheck", "Stored Token: " + storedToken);

                            Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to BrowseActivity
                            Intent intent = new Intent(SignInActivity.this, BrowseActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        Toast.makeText(SignInActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
// Navigate to Home
        goToHome.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

// Navigate to Register
        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish(); // Close the current activity to avoid stacking
        });

    }
}
