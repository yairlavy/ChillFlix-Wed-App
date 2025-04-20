package com.example.androidchillflix.main.config;

// Configuration class for storing the base URL of the API
public class Config {

    // Base URL for the API
    private static final String BASE_URL = "http://10.0.2.2:8181/api/";

    //10.0.2.2
    // Public method to retrieve the base URL
    public static String getBaseUrl() {
        return BASE_URL; // Return the base URL
    }
}