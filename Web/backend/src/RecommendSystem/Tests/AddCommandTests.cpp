#include "AddCommand.h"
#include "Movies.h"
#include "Users.h"
#include "DataManagement.h"
#include <gtest/gtest.h>
using namespace std;

// Test creating a new user with movies
TEST(UsersTest, CreateNewUserWithMovies) {
    map<int, unordered_set<int>> userData;
    
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(userData); 

    // Add a new user with movies
    users.AddMoviesToUser(10, "101 102 103");

    // Verify the user was created with the movies
    EXPECT_TRUE(userData.find(10) != userData.end());
    EXPECT_TRUE(userData[10].count(101) == 1);
    EXPECT_TRUE(userData[10].count(102) == 1);
    EXPECT_TRUE(userData[10].count(103) == 1);
}

// Test adding movies to an existing user
TEST(UsersTest, AddMoviesToExistingUser) {
    map<int, unordered_set<int>> userData = {{10, {101}}};
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(userData); 

    // Add movies to an existing user
    users.AddMoviesToUser(10, "102 103");

    // Verify the movies were added
    EXPECT_TRUE(userData[10].count(101) == 1);
    EXPECT_TRUE(userData[10].count(102) == 1);
    EXPECT_TRUE(userData[10].count(103) == 1);
}

// Test adding duplicate movies
TEST(UsersTest, AddDuplicateMovies) {
    map<int, unordered_set<int>> userData = {{10, {101}}};
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(userData); 

    // Add duplicate movies
    users.AddMoviesToUser(10, "101 102 101");

    // Verify duplicate movies were not added twice
    EXPECT_EQ(userData[10].count(101), 1); // Movie 101 appears only once
    EXPECT_TRUE(userData[10].count(102) == 1); // Movie 102 was added
}

// Test adding movies with an empty string
TEST(UsersTest, AddMoviesWithEmptyString) {
    map<int, unordered_set<int>> userData = {{10, {101}}};
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(userData); 

    // Attempt to add an empty string
    users.AddMoviesToUser(10, "");

    // Verify the state did not change
    EXPECT_EQ(userData[10].size(), 1); // Only movie 101 exists
}

// Test invalid command
TEST(CommandTest, InvalidCommand) {
    set<int> valid_movie_ids;
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(user_movies); 
    Movies movies;
    AddCommand addCommand(movies, users, dataManagement);

    // Attempt an invalid command
    string invalidCommand = "foo 10 101";
    addCommand.execute(invalidCommand); // Execute the invalid command

    // Verify no user was created or modified
    EXPECT_TRUE(user_movies.find(10) == user_movies.end());
}

// Test command with missing data
TEST(CommandTest, MissingDataInCommand) {
    set<int> valid_movie_ids;
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(user_movies); 
    Movies movies;
    AddCommand addCommand(movies, users, dataManagement);

    // Attempt a command with missing data
    string incompleteCommand = "add";
    addCommand.execute(incompleteCommand); // Execute the incomplete command

    // Verify no user was created or modified
    EXPECT_TRUE(user_movies.find(10) == user_movies.end());
}

// Test valid command must have at least one movie
TEST(CommandTest, ValidCommandMustHaveAtLeastOneMovie) {
    set<int> valid_movie_ids;
    map<int, unordered_set<int>> user_movies;
    DataManagement dataManagement(user_movies);
    Users users;
    // Pass the map pointer to Users
    users.LoadUsersData(user_movies); 
    Movies movies;
    AddCommand addCommand(movies, users, dataManagement);

    // Attempt a command without any movies
    string noMoviesCommand = "10";
    addCommand.execute(noMoviesCommand); // Execute the command without movies

    // Verify no user was created or modified
    EXPECT_TRUE(user_movies.find(10) == user_movies.end());
}
