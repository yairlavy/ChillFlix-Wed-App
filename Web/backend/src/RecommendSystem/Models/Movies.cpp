#include "Movies.h"

// Function to check if movie valid
bool Movies::isMovieValid(long int movieID) {
    //return validMovieIDs->find(movieID) != validMovieIDs->end();
    
        if (movieID <= 0) { // Ensure the user ID is a positive integer
        //cout << "Error: Movie ID must be a positive integer." << endl;
        return false;
    }
    return true;
}