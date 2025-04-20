#include "POSTCommand.h"

// Constructor implementation
POSTCommand::POSTCommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef)
    : movies(moviesRef), users(usersRef), dataManagement(dataManagementRef){}

string POSTCommand::execute(const std::string& input) {
    long int userID;
        
    if (input.empty()) {
        return StatusHandler::getStatusMessage(400);
    }

    istringstream stream(input);

    // Validate user ID
    if (!(stream >> userID) || !users.CheckIfUserID_OK(userID)) {
        return StatusHandler::getStatusMessage(400);
    }

    // Check if the user exist
    if(users.CheckIfUserExist(userID)) {
        return StatusHandler::getStatusMessage(404);
    }

    // Get the rest of the input after user ID
    string remainingInput;
    getline(stream, remainingInput);
    
    // Remove leading whitespace if any
    if (!remainingInput.empty() && remainingInput[0] == ' ') {
        remainingInput.erase(0, 1);
    }

    // Create again the input as a single string for AddCommand
    string addInput = to_string(userID) + " " + remainingInput;

    // Execute AddCommand and capture its response
    AddCommand addCommand(movies, users, dataManagement);
    string response = addCommand.execute(addInput);
   
    if (!response.empty()) {
        return response; // Forward error message from AddCommand
    }

    return StatusHandler::getStatusMessage(201);
}
