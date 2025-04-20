#include "Users.h"

// Load user-movie data from the DataManagement by pointer
void Users::LoadUsersData(map<int, unordered_set<int>>& LoaduserMovies) {
    userMovies = &LoaduserMovies;  // Initialize userMovies to point at the provided map
}

/* Function to print all users and their movies *DEBUG FUNCTION*
void Users::PrintUser_Movies() {
    for (const auto& userId : *userMovies) { // run on the usermovies map that he point at
        cout << endl << endl << "Movies for User " << userId.first << ":" << endl;
        const auto& MyMovies = userMovies->at(userId.first);
        for (const auto& movieID : MyMovies) {
            cout << movieID << " ";
        }
    }
}
*/

// Function to check if a user ID is valid
bool Users::CheckIfUserID_OK(long int userID) {
    if (userID <= 0) { // Ensure the user ID is a positive integer
       // cout << "Error: User ID must be a positive integer." << endl;
        return false;
    }
    return true;
}

// Function to check if a user ID is exist
bool Users::CheckIfUserExist(long int userID) {
    if (userMovies && userMovies->find(userID) != userMovies->end()) { 
        return true;
    }
    return false;
}

// Add movies to user's list
void Users::AddMoviesToUser(long int userID, string moviesIDs) {
    if (!userMovies) return;

    istringstream stream(moviesIDs); //start stream to get movies ids on by one
    int movieID;
    while (stream >> movieID) {
        (*userMovies)[userID].insert(movieID);  // Insert movie ID into the user's set
    }
}

// Delete movies from the user's list
void Users::DeleteMoviesToUser(long int userID, string moviesIDs) {
    if (!userMovies) return;

    istringstream stream(moviesIDs); //start stream to get movies ids on by one
    int movieID;
    while (stream >> movieID) {
        (*userMovies)[userID].erase(movieID);  // erase movie ID from the user's set
    }
}

// Delete user if their movie list is empty
void Users::DeleteUser(long int userID) {
    // Ensure that userMovies is initialized and contains the user ID
    if (userMovies && userMovies->find(userID) != userMovies->end()) {
        // Erase the user from the map
        userMovies->erase(userID);
    }
}
