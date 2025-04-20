#ifndef CONSOLEMENU_H
#define CONSOLEMENU_H

#include "IMenu.h"
#include <iostream>
#include <string>
using namespace std;

class ConsoleMenu : public IMenu {
public:
    // Method to get the next command from the user
    string nextCommand() override;
    
    // Method to display an error message
    void displayError(const string& error) override;
};

#endif // CONSOLEMENU_H