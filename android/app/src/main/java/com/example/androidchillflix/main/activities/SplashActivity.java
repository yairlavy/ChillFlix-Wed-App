package com.example.androidchillflix.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchillflix.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Simulate loading with a handler
        Handler handler = new Handler();
        new Thread(() -> {
            while (progress < 100) {
                progress += 1;

                // Update progress bar
                handler.post(() -> progressBar.setProgress(progress));

                try {
                    Thread.sleep(30); // Simulate loading time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // After loading, move to HomeActivity
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }).start();
    }
}
