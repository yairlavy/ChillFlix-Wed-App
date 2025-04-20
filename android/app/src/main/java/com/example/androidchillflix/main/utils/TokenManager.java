package com.example.androidchillflix.main.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

/**
 * Manages saving, retrieving, and decoding tokens (JWT).
 */
public class TokenManager {

    private static final String PREF_NAME = "ChillFlixPrefs"; // SharedPreferences name
    private static final String KEY_TOKEN = "auth_token";     // Token key

    // Holds the single instance of TokenManager
    private static TokenManager instance;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    /**
     * Private constructor to enforce singleton usage.
     */
    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Returns the singleton instance of TokenManager.
     * If none exists, a new one is created.
     *
     * @param context application context
     * @return the TokenManager singleton
     */
    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Saves the token in SharedPreferences.
     *
     * @param token the JWT token to be saved
     */
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    /**
     * Retrieves the token from SharedPreferences.
     *
     * @return the saved token, or null if not found
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * Clears the token (e.g., when the user logs out).
     */
    public void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    /**
     * Decodes the stored token to extract the user ID.
     *
     * @return the user ID from the token payload, or null if not available
     */
    public String getUserId() {
        String token = getToken();
        if (token == null) {
            return null;
        }
        return getUserIdFromToken(token);
    }

    /**
     * Decodes a given token to extract the user ID field ("id").
     *
     * @param token the JWT token to decode
     * @return the user ID if present, otherwise null
     */
    public String getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payload = parts[1];
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
            JSONObject json = new JSONObject(decodedPayload);
            // Change "id" to match your JWT payload if needed
            return json.optString("id", null);
        } catch (Exception e) {
            return null;
        }
    }
}
