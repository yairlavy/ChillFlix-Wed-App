#ifndef DATAMANAGEMENT_H  // Changed to a more conventional name
#define DATAMANAGEMENT_H

#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <fstream>
#include <sstream>
#include <string>
#include <map>
#include <set>

using namespace std;

const string UserMoviesFileName = "../data/User-Movies.txt";

class DataManagement {
private:
    map<int, unordered_set<int>>& userMovies; // Reference to external map

public:

    // Constructor takes references to userMovies
    DataManagement(map<int, unordered_set<int>>& user_movies)
        : userMovies(user_movies) {
    }

    // Function to load user-movie data
    void LoadUsersData();

    // Function to update the user-movies file
    void updateUserMoviesFile(long int userID);

    // Function to get movies for a specific user
    unordered_set<int> getUserMovies(long int userID) const;

    // Function to get all users' movie data
    map<int, unordered_set<int>>& getAllUsersMovies() const;


    //DEBUG Function
    //void PrintUser_Movies(); // Function to print all users and their movie list

};

#endif // DATAMANAGEMENT_H