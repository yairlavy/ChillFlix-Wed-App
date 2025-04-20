#ifndef HELPCOMMAND_H
#define HELPCOMMAND_H

#include "ICommand.h"          // Base ICommand interface
#include "StatusHandler.h"      // For Handle with Errors
#include <iostream>
#include <string>
#include <sstream>
using namespace std;

class HelpCommand : public ICommand {
public:
    // Constructor
    HelpCommand();

    // Overriding execute() method from ICommand
    string execute(const std::string& input) override;
};

#endif // HELPCOMMAND_H