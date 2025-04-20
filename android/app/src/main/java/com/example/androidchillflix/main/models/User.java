package com.example.androidchillflix.main.models;

import java.util.Map;

/**
 * Represents a User object with relevant user information.
 * This version includes fields for oldPassword and newPassword
 * to handle password update flows if required by the server.
 */
public class User {

    // Unique identifier from the database
    private String _id;

    // Username chosen by the user
    private String username;

    // Email address of the user
    private String email;

    // Current password
    private String password;

    // Display name or full name of the user
    private String name;

    // Profile picture URL or path
    private String profilePicture;

    // A map of items (e.g., movies) in the user's watchlist
    private Map<String, String> watchlist;

    // Indicates whether the user has admin privileges
    private boolean isAdmin;

    /**
     * Default no-args constructor.
     * Useful for scenarios where you only need to set some fields dynamically.
     */
    public User() {
    }

    /**
     * Constructor to initialize a User with basic information.
     *
     * @param username        the user's username
     * @param email           the user's email
     * @param password        the user's password
     * @param name            the user's display/full name
     * @param profilePicture  the user's profile picture path or URL
     */
    public User(String username, String email, String password, String name, String profilePicture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the user's ID.
     * @return a string representing the unique ID from the database
     */
    public String getId() {
        return _id;
    }

    /**
     * Sets the user's ID (if needed for client-side operations).
     * @param id the unique ID from the database
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Returns the username.
     * @return the username as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the new username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the email address.
     * @return the email address as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     * @param email the new email address to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the current password.
     * @return the password as a string
     */
    public String getPassword() {
        return password;
    }
    public  void setPassword(String p){
         this.password=p;
    }

    /**
     * Sets the current password.
     * @param password the new password to be set
     */

    /**
     * Returns the display or full name.
     * @return the user's name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display or full name.
     * @param name the new name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the profile picture path or URL.
     * @return the profile picture as a string
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the profile picture path or URL.
     * @param profilePicture the new profile picture to be set
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the watchlist map.
     * @return a map of watchlist items
     */
    public Map<String, String> getWatchlist() {
        return watchlist;
    }

    /**
     * Sets the watchlist map.
     * @param watchlist a map of items to add to the watchlist
     */
    public void setWatchlist(Map<String, String> watchlist) {
        this.watchlist = watchlist;
    }

    /**
     * Checks if the user is an admin.
     * @return true if the user has admin privileges, otherwise false
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets whether the user is an admin.
     * @param admin a boolean indicating admin status
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
