#include "DELETECommand.h"

// Constructor implementation
DELETECommand::DELETECommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef)
    : movies(moviesRef), users(usersRef), dataManagement(dataManagementRef){}

// Implementation of the execute method
string DELETECommand::execute(const std::string& input) {
    long int userID;
    string moviesIDs;
        
    if (input.empty()) {
        //return "Error: No input provided.";
        return StatusHandler::getStatusMessage(400);
    }

    istringstream stream(input);

    // Validate user ID
    if (!(stream >> userID) || !users.CheckIfUserID_OK(userID)) {
        return StatusHandler::getStatusMessage(400);
    }
    

    //cout << "User ID: " << userID << endl; // (DEBUG)

    // Collect and validate movie IDs
    string movieID;
    bool validCommand = false;

    unordered_set UserMovieList = dataManagement.getUserMovies(userID);

    if(UserMovieList.empty()) {
        return StatusHandler::getStatusMessage(404);
    }

    while (stream >> movieID) {
        validCommand = true; // At least one movie ID is required
                
        try {
            long int movieIDInt = stoi(movieID);
            if (!movies.isMovieValid(movieIDInt)) {
                //return "Error: Invalid movie ID: " + movieID;
                return StatusHandler::getStatusMessage(400);
            }
            if(UserMovieList.find(movieIDInt) == UserMovieList.end()) {
                return StatusHandler::getStatusMessage(404);
            }

            moviesIDs += movieID + " ";
        } catch (const exception& e) {
            //return "Error: Invalid movie ID format: " + movieID;
            return StatusHandler::getStatusMessage(400);
        }
    }

    if (!validCommand) {
        // Command must contain at least one movie ID
        return StatusHandler::getStatusMessage(400);
    }
    
    // Delete movies from the user's list
    users.DeleteMoviesToUser(userID, moviesIDs);

    // Check if the user movie list is empty, if so delete the user
    unordered_set UpdatedMovieList = dataManagement.getUserMovies(userID);
    if (UpdatedMovieList.empty()){
        users.DeleteUser(userID);
    }


    // Update the user-movie file
    dataManagement.updateUserMoviesFile(userID);

    // Print updated user-movie data (DEBUG)
    //   users.PrintUser_Movies(); 
    //    cout << "Movies added successfully.";

    // If everything is successful
    return StatusHandler::getStatusMessage(204); 
}
