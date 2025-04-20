#ifndef DELETECOMMAND_H
#define DELETECOMMAND_H

#include "ICommand.h"          // Base ICommand interface
#include "Movies.h"            // For interacting with Movies
#include "Users.h"             // For interacting with Users
#include "DataManagement.h"    // For interacting with DataManagement
#include "StatusHandler.h"      // For Handle with Errors
#include <string>             
#include <iostream>
#include <sstream>
using namespace std;

class DELETECommand : public ICommand {
    Movies& movies;               // Reference to Movies object
    Users& users;                 // Reference to Users object
    DataManagement& dataManagement; // Reference to DataManagement object   
public:
    // Constructor
    DELETECommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef);

    // Overriding execute() method from ICommand
    string execute(const std::string& input) override;
};

#endif // DELETECOMMAND_H