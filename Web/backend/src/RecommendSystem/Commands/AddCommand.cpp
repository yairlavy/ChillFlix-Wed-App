#include "AddCommand.h"

// Constructor implementation
AddCommand::AddCommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef)
    : movies(moviesRef), users(usersRef), dataManagement(dataManagementRef){}

// Implementation of the execute method
string AddCommand::execute(const std::string& input) {
    long int userID;
    string moviesIDs;
        
    if (input.empty()) {
        //return "Error: No input provided.";
        return StatusHandler::getStatusMessage(400);
    }

    istringstream stream(input);


    stream >> userID;

    //cout << "User ID: " << userID << endl; // (DEBUG)

    // Collect and validate movie IDs
    string movieID;
    bool validCommand = false;

    while (stream >> movieID) {
        validCommand = true; // At least one movie ID is required
                
        try {
            long int movieIDInt = stoi(movieID);
            if (!movies.isMovieValid(movieIDInt)) {
                //return "Error: Invalid movie ID: " + movieID;
                return StatusHandler::getStatusMessage(400);
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
    
    // Add movies to the user's list
    users.AddMoviesToUser(userID, moviesIDs);

    // Update the user-movie file
    dataManagement.updateUserMoviesFile(userID);

    // Print updated user-movie data (DEBUG)
    //   users.PrintUser_Movies(); 
    //    cout << "Movies added successfully.";

    // If everything is successful, return an empty string
    return ""; 

}
