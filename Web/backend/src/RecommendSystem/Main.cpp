#include "ConsoleMenu.h"
#include "App.h"
#include "Movies.h"
#include "Users.h"
#include "DataManagement.h"
#include "AddCommand.h"
#include "GETCommand.h"
#include "HelpCommand.h"
#include "ServerManager.h"
#include "POSTCommand.h"
#include "PATCHCommand.h"
#include "DELETECommand.h"

#include <unordered_set>
#include <map>
#include <string>
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {

    if (argc < 2) {
        cerr << "Please provide a number as a port." << endl;
        return 1;
    }

    int NumOfPort;
    try {
        // Convert the argument to an integer
        NumOfPort = stoi(argv[1]);
        
        //validation check
        if (NumOfPort < 0 || NumOfPort > 65535) {
            cerr << "The port must be between 0 and 65535." << endl;
            return 1;
        }
        cout << "The port provided is: " << NumOfPort << endl;
    } catch (const invalid_argument& e) {
        cerr << "Invalid argument: not a number." << endl;
        return 1;
    } catch (const out_of_range& e) {
        cerr << "Invalid argument: number out of range." << endl;
        return 1;
    }

    // Map to hold user ID and a set of movie IDs that the user has watched
    map<int, unordered_set<int>> userMovies;

    // Create main components
    Movies movies;
    Users users;
    DataManagement dataManagement(userMovies);

    // Load data
    dataManagement.LoadUsersData();
    users.LoadUsersData(userMovies);

    // Create menu
    ConsoleMenu menu;

    // Create commands map
    map<string, ICommand*> commands;

    // Add commands
    ICommand* postCommand = new POSTCommand(movies, users, dataManagement);
    commands["POST"] = postCommand;

    ICommand* patchCommand = new PATCHCommand(movies, users, dataManagement);
    commands["PATCH"] = patchCommand;

    ICommand* deleteCommand = new DELETECommand(movies, users, dataManagement);
    commands["DELETE"] = deleteCommand;

    ICommand* getCommand = new GETCommand(movies, users, dataManagement);
    commands["GET"] = getCommand;

    ICommand* helpCommand = new HelpCommand();
    commands["help"] = helpCommand;

    // Create app
    App app(&menu, commands);

    // Start server
    ServerManager serverManager(NumOfPort, &menu, commands);
    serverManager.start(&app);

    // Clean up
    delete postCommand;
    delete patchCommand;
    delete deleteCommand;
    delete getCommand;
    delete helpCommand;

    return 0;
}