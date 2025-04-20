#include "App.h"

// Constructor implementation
App::App(IMenu* menu, const map<string, ICommand*>& commands)
    : menu(menu), commands(commands) {}
// Destructor
App::~App() {
    for (auto& command : commands) {
        delete command.second;
    }
}
// Implementation of the run() method
void App::run() {
    while (true) {
        // Get the next command from the menu
        string commandKey = menu->nextCommand();

        // Execute the corresponding command if it exists
        if (commands.find(commandKey) != commands.end()) {
            string input;
            getline(cin, input);
            commands[commandKey]->execute(input);
        } else {
            // Display an error message for invalid commands
           // menu->displayError("Invalid command!");
        }
    }
}
