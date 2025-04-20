#ifndef APP_H
#define APP_H

#include "IMenu.h"            // Interface for menu
#include "ICommand.h"         // Interface for commands
#include <map>                // For map
#include <string>             // For string
#include <iostream>

using namespace std;

class App {
private:
    IMenu* menu;                          // Pointer to a menu object
    map<string, ICommand*> commands; // Map of command keys to ICommand objects

public:
    // Constructor
    App(IMenu* menu, const map<string, ICommand*>& commands);
    ~App();

    // Runs the application
    void run();
};

#endif // APP_H
