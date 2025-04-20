#ifndef PATCHCOMMAND_H
#define PATCHCOMMAND_H

#include "ICommand.h"          // Base ICommand interface
#include "Movies.h"            // For interacting with Movies
#include "Users.h"             // For interacting with Users
#include "DataManagement.h"    // For interacting with DataManagement
#include "StatusHandler.h"      // For Handle with Errors
#include <string>             
#include <iostream>
#include <sstream>
#include "AddCommand.h"
using namespace std;

class PATCHCommand : public ICommand {
    Movies& movies;               // Reference to Movies object
    Users& users;                 // Reference to Users object
    DataManagement& dataManagement; // Reference to DataManagement object    
public:
    // Constructor
    PATCHCommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef);

    // Overriding execute() method from ICommand
    string execute(const std::string& input) override;
};

#endif // PATCHCOMMAND_H