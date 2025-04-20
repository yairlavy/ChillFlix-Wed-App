#include <gtest/gtest.h>
#include "POSTCommand.h"
#include "Movies.h"
#include "Users.h"
#include "DataManagement.h"
#include <unordered_set>
#include <map>
#include <sstream>

// Test Case 1: Creating a new user with movies
TEST(POSTCommandTest, CreateNewUserWithMovies) {
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    POSTCommand postCommand(movies, users, dataManagement);

    // Add a new user with movies
    string input = "1 101 102 103";
    string response = postCommand.execute(input);

    // Check the response to ensure user was created successfully
    EXPECT_EQ(response, "201 Created");

    // Verify that the user has been added with the correct movies
    EXPECT_TRUE(user_movies.find(1) != user_movies.end());
    EXPECT_TRUE(user_movies[1].count(101) == 1);
    EXPECT_TRUE(user_movies[1].count(102) == 1);
    EXPECT_TRUE(user_movies[1].count(103) == 1);
}

// Test Case 2: Attempting to add movies to a user that already exists
TEST(POSTCommandTest, AddMoviesToUserAlreadyExists) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    POSTCommand postCommand(movies, users, dataManagement);

    // Try to create a user that already exists
    string input = "1 102 103";
    string response = postCommand.execute(input);

    // Expect an error response since the user already exists
    EXPECT_EQ(response, "404 Not Found");

    // Verify that no movies were added to the existing user
    EXPECT_TRUE(user_movies.find(1) != user_movies.end());
    EXPECT_TRUE(user_movies[1].count(101) == 1);
    EXPECT_FALSE(user_movies[1].count(102) == 1);
    EXPECT_FALSE(user_movies[1].count(103) == 1);
}

// Test Case 3: Attempting to create a user with invalid movie IDs
TEST(POSTCommandTest, InvalidMovieID) {
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    POSTCommand postCommand(movies, users, dataManagement);

    // Simulate invalid movie ID
    string input = "2 abc 103";
    string response = postCommand.execute(input);

    // Expect an error because movie ID 'abc' is invalid
    EXPECT_EQ(response, "400 Bad Request");

    // User 2 should not be created
    EXPECT_FALSE(user_movies.find(2) != user_movies.end());  
}

// Test Case 4: Creating a user with no movie IDs
TEST(POSTCommandTest, NoMovieIDsProvided) {
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    POSTCommand postCommand(movies, users, dataManagement);

    // Try to create a user without providing movie IDs
    string input = "3";
    string response = postCommand.execute(input);

    // Expect an error response because no movie IDs were provided
    EXPECT_EQ(response, "400 Bad Request");

    // Verify that user 3 was not created
    EXPECT_FALSE(user_movies.find(3) != user_movies.end());
}