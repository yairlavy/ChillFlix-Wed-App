#include "HelpCommand.h"

// Constructor
HelpCommand::HelpCommand() {}

// Implementation of execute()
string HelpCommand::execute(const std::string& input) {
    // Making sure only help will activate this command (allow empty spaces)
    string empty;
    istringstream stream(input);
    while (stream >> empty ) {
        return StatusHandler::getStatusMessage(400);
    }
    ostringstream output; // Stream to collect output
    // Prepend 200 OK response
    output << StatusHandler::getStatusMessage(200)<< "\n\n";

    // Display help information
    output << "DELETE, arguments: [userid] [movieid]...\n";
    output << "GET, arguments: [userid] [movieid]\n";
    output << "PATCH, arguments: [userid] [movieid]...\n";
    output << "POST, arguments: [userid] [movieid]...\n";
    output << "help\n";

    return output.str();
}