#include <gtest/gtest.h>
#include "DELETECommand.h"
#include "Movies.h"
#include "Users.h"
#include "DataManagement.h"
#include <unordered_set>
#include <map>
#include <sstream>

// Test Case 1: Attempting to Delete 1 movie from exisitng user with movies
TEST(DELETECommandTest, DeleteMovieFromUserWithMovies) {
    map<int, unordered_set<int>> user_movies = {{1, {100, 101, 102}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "1 101";
    string response = deleteCommand.execute(input);

    // Check the response to ensure movie was deleted successfully
    EXPECT_EQ(response, "204 No Content");

    // Verify that the movie add been deleted from user list
    EXPECT_TRUE(user_movies.find(1) != user_movies.end());
    EXPECT_TRUE(user_movies[1].count(100) == 1);
    EXPECT_FALSE(user_movies[1].count(101) == 1);
    EXPECT_TRUE(user_movies[1].count(102) == 1);
}

// Test Case 2: Attempting to delete more then 1 movie from exisitng user with movies
TEST(DELETECommandTest, DeleteMoviesFromUserWithMovies) {
    map<int, unordered_set<int>> user_movies = {{1, {100, 101, 102}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "1 100 102";
    string response = deleteCommand.execute(input);

    // Check the response to ensure movies was deleted successfully
    EXPECT_EQ(response, "204 No Content");

    // Verify that the movie add been deleted from user list
    EXPECT_TRUE(user_movies.find(1) != user_movies.end());
    EXPECT_FALSE(user_movies[1].count(100) == 1);
    EXPECT_TRUE(user_movies[1].count(101) == 1);
    EXPECT_FALSE(user_movies[1].count(102) == 1);
}

// Test Case 3: Attempting to delete All movies from exisitng user with movies
TEST(DELETECommandTest, DeleteAllMoviesFromUserWithMovies) {
    map<int, unordered_set<int>> user_movies = {{2, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies);
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "2 101";
    string response = deleteCommand.execute(input);

    // Check the response to ensure movie was deleted successfully
    EXPECT_EQ(response, "204 No Content");

    // Verify that no movies were added to the user
    EXPECT_FALSE(user_movies.find(2) == user_movies.end());
    EXPECT_FALSE(user_movies[2].count(101) == 1);
}

// Test Case 4: try to delete movie that dont in the user list (Work the same for exist user without movies)
TEST(DELETECommandTest, TryToDeleteMovieNotInUserList) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "1 102";
    string response = deleteCommand.execute(input);

    // Expect an error response since the user dont exists
    EXPECT_EQ(response, "404 Not Found");
}

// Test Case 5: try to delete movie from users that dont exist
TEST(DELETECommandTest, TryToDeleteMovieUnexistUser) {
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "1 102";
    string response = deleteCommand.execute(input);

    // Expect an error response since the user dont exists
    EXPECT_EQ(response, "404 Not Found");
}

// Test Case 6: try to enter invalid user id 
TEST(DELETECommandTest, InvalidUserID) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "d 102";
    string response = deleteCommand.execute(input);

    // Expect an error response since the user dont exists
    EXPECT_EQ(response, "400 Bad Request");
}

// Test Case 7: try to enter invalid movie id 
TEST(DELETECommandTest, InvalidMovieID) {
    map<int, unordered_set<int>> user_movies = {{1, {101}}};
    DataManagement dataManagement(user_movies);
    Users users;
    Movies movies;
    users.LoadUsersData(user_movies); 
    DELETECommand deleteCommand(movies, users, dataManagement);

    // delete a movie from a user
    string input = "1 f 101";
    string response = deleteCommand.execute(input);

    // Expect an error response since the user dont exists
    EXPECT_EQ(response, "400 Bad Request");
}