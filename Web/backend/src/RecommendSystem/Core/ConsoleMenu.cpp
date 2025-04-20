#include "ConsoleMenu.h"

// Implementation of the nextCommand method
string ConsoleMenu::nextCommand() {
    string command;
    //cout << "Enter a command: "; //(DEBUG)
    cin >> command;
    return command;
}

// Implementation of the displayError method
//void ConsoleMenu::displayError(const string& error) {
   // cerr << "Error: " << error << endl;
//}

// Display an error message
void ConsoleMenu::displayError(const string& error) {
    //cerr << "Error: " << error << endl;
}