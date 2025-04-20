#include <gtest/gtest.h>
#include "GETCommand.h"  // Include the header for RecommendCommand
#include "Users.h"             // Include the header for Users (or the equivalent class)
#include "Movies.h"            // Include the header for Movies (or the equivalent class)
#include "DataManagement.h"    // Include the header for DataManagement
#include <unordered_set>
#include <map>
#include <sstream>


// Test 1: Testing shared movies functionality
TEST(GETCommandTest, FindSharedMovies) {
    map<int, unordered_set<int>> userData; // To hold user-movie data

    Movies movies;
    Users users;
    DataManagement dataManagement(userData);  // Initializes DataManagement with the map
        
    // Initialize Users and load user data
    dataManagement.LoadUsersData();
    users.LoadUsersData(userData);  // Pass map reference to load the user data into Users
        
    // Add movies to users for testing purposes
    users.AddMoviesToUser(1, "101");
    users.AddMoviesToUser(2, "101 102");
    users.AddMoviesToUser(3, "103");
    
    GETCommand getCommand(movies, users, dataManagement);
    
    // Assuming findSharedMovies takes userID and movieID as parameters
    // Here we are looking for shared movies for user 1 with movie 102
    auto sharedMovies = getCommand.findSharedMovies(1, 101);  
    
    // Verify that the shared movie count matches what we expect
    ASSERT_EQ(sharedMovies.size(), 1);  // Only one shared movie expected
    
    // Verify the shared movie ID is correct (it should be 102 as per the test data setup)
    ASSERT_EQ(sharedMovies[0].sharedMoviesCount, 1); // Ensure that the count of shared movies is 1
    ASSERT_EQ(sharedMovies[0].userID, 2); // User 2 shares movie 101 with user 1
}

// Test 2: Execute recommendation and validate new recommendations
TEST(GETCommandTest, ExecuteRecommendsMovies) {
    map<int, unordered_set<int>> userData; // To hold user-movie data

    Movies movies;
    Users users;
    DataManagement dataManagement(userData);  // Initializes DataManagement with the map
        
    // Initialize Users and load user data
    dataManagement.LoadUsersData();
    users.LoadUsersData(userData);  // Pass map reference to load the user data into Users
        
    // Add movies to users for testing purposes
    users.AddMoviesToUser(1, "101");
    users.AddMoviesToUser(2, "101 102");
    users.AddMoviesToUser(3, "103");
    
    GETCommand getCommand(movies, users, dataManagement);

    // Simulate recommending movies for user 1 with a watched movie
    string input = "1 101";

    // Execute the function which should print to stdout
    getCommand.execute(input);

    // Expecting that movie 102 is recommended for user 1
    EXPECT_EQ(getCommand.newMoviesToWatch[0].movieID, 102);  
}


// Test 3: Execute recommendation for movie that dont exist
TEST(GETCommandTest, ExecuteRecommendsMovies_InvalidMovie) {
    map<int, unordered_set<int>> userData; // To hold user-movie data

    Movies movies;
    Users users;
    DataManagement dataManagement(userData);  // Initializes DataManagement with the map
        
    // Initialize Users and load user data
    dataManagement.LoadUsersData();
    users.LoadUsersData(userData);  // Pass map reference to load the user data into Users
        
    // Add movies to users for testing purposes
    users.AddMoviesToUser(1, "101");
    users.AddMoviesToUser(2, "101 102");
    users.AddMoviesToUser(3, "103");
    
    GETCommand getCommand(movies, users, dataManagement);

    // Simulate recommending movies for user  with a invalid movie
    string input = "1 104";

    // Execute the function which should print to stdout
    getCommand.execute(input);

    // Expecting that movie 102 is recommended for user 1
    EXPECT_EQ(getCommand.newMoviesToWatch.empty(), true);  
}

// Test 4: Execute recommendation for user that dont exist
TEST(GETCommandTest, ExecuteRecommendsMovies_InvalidUser) {
    map<int, unordered_set<int>> userData; // To hold user-movie data

    Movies movies;
    Users users;
    DataManagement dataManagement(userData);  // Initializes DataManagement with the map
        
    // Initialize Users and load user data
    dataManagement.LoadUsersData();
    users.LoadUsersData(userData);  // Pass map reference to load the user data into Users
        
    // Add movies to users for testing purposes
    users.AddMoviesToUser(1, "101");
    users.AddMoviesToUser(2, "101 102");
    users.AddMoviesToUser(3, "103");
    
    GETCommand getCommand(movies, users, dataManagement);

    // Simulate recommending movies for invalid user id with a watched movie
    string input = "4 101";

    // Execute the function which should print to stdout
    getCommand.execute(input);

    // Expecting that is empty
    EXPECT_EQ(getCommand.newMoviesToWatch.empty(), true);  
}