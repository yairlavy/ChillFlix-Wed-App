#ifndef IMENU_H  // Check if IMENU_H is not defined
#define IMENU_H  // Define IMENU_H

#include <string>

using namespace std;


class IMenu {
public:
    virtual string nextCommand() = 0;

    virtual void displayError(const string& error) = 0;

    virtual ~IMenu() = default;
};

#endif // IMENU_H
