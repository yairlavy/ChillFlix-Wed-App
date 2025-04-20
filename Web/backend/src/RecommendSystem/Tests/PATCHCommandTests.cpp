#include <gtest/gtest.h>
#include "PATCHCommand.h"
#include "Movies.h"
#include "Users.h"
#include "DataManagement.h"
#include <unordered_set>
#include <map>
#include <sstream>

// Test Case 1: Attempting to Create a new user with movies
TEST(PATCHCommandTest, CreateNewUserWithMovies) {
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    PATCHCommand patchCommand(movies, users, dataManagement);

    // Add a new user with movies
    string input = "1 101 102 103";
    string response = patchCommand.execute(input);

    // Expect an error response since the user dont exists
    EXPECT_EQ(response, "404 Not Found");

    // Verify that the user has not been added to the list
    EXPECT_TRUE(user_movies.find(1) == user_movies.end());
    EXPECT_FALSE(user_movies[1].count(101) == 1);
    EXPECT_FALSE(user_movies[1].count(102) == 1);
    EXPECT_FALSE(user_movies[1].count(103) == 1);
}

// Test Case 2: Attempting to add movies to a user that already exists
TEST(PATCHCommandTest, AddMoviesToUserAlreadyExists) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    PATCHCommand patchCommand(movies, users, dataManagement);

    // Try to add movies to user
    string input = "1 102 103";
    string response = patchCommand.execute(input);

    // Check the response to ensure user was created successfully
    EXPECT_EQ(response, "204 No Content");

    // Verify that movies were added to the existing user
    EXPECT_TRUE(user_movies.find(1) != user_movies.end());
    EXPECT_TRUE(user_movies[1].count(101) == 1);
    EXPECT_TRUE(user_movies[1].count(102) == 1);
    EXPECT_TRUE(user_movies[1].count(103) == 1);
}

// Test Case 3: Attempting to patch a exist user id with invalid movie IDs
TEST(PATCHCommandTest, InvalidMovieID) {
    map<int, unordered_set<int>> user_movies = {{2, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    PATCHCommand patchCommand(movies, users, dataManagement);

    // Simulate invalid movie ID
    string input = "2 abc 103";
    string response = patchCommand.execute(input);

    // Expect an error because movie ID 'abc' is invalid
    EXPECT_EQ(response, "400 Bad Request");

    // Verify that no movies were added to the user
    EXPECT_FALSE(user_movies.find(2) == user_movies.end());
    EXPECT_TRUE(user_movies[2].count(101) == 1);
    EXPECT_FALSE(user_movies[2].count(103) == 1);
}

// Test Case 4: try the patch but witnout giving movies
TEST(PATCHCommandTest, NoMovieIDsProvided) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    PATCHCommand patchCommand(movies, users, dataManagement);

    // Try to create a user without providing movie IDs
    string input = "1";
    string response = patchCommand.execute(input);

    // Expect an error response because no movie IDs were provided
    EXPECT_EQ(response, "400 Bad Request");
}