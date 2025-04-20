#ifndef USERS_H
#define USERS_H

#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <fstream>
#include <sstream>
#include <string>
#include <map>
#include <set>
using namespace std;

class Users {
    map<int, unordered_set<int>>* userMovies; // Pointer to the map managed by DataManagement

public:
    Users() : userMovies(nullptr) {} //Constructor

    void LoadUsersData(map<int, unordered_set<int>>& LoaduserMovies); // Function to load Users-Movies data
    //void PrintUser_Movies(); // Function to print all users and their movie list
    bool CheckIfUserID_OK(long int UserID); // Function to check if enter id is positive int
    bool CheckIfUserExist(long int userID); //Function to check if enter user id is exist
    void AddMoviesToUser (long int userID, string moviesIDs); // Function to add movies to the map
    void DeleteMoviesToUser (long int userID, string moviesIDs); // Function to remove movies from the map
    void DeleteUser(long int userID); // Function to delete user if their movie list is empty

};

#endif //USERS_H