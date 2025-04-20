#ifndef ICOMMAND_H  // Check if ICOMMAND_H is not defined
#define ICOMMAND_H  // Define ICOMMAND_H

#include <string>

using namespace std;
class ICommand {
public:
    virtual string execute(const std::string& input) = 0;  // Pure virtual method to execute a command
    virtual ~ICommand() = default;  // Virtual destructor for cleanup
};

#endif // ICOMMAND_H
