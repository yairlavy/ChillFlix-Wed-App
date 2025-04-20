#ifndef STATUSHANDLER_H
#define STATUSHANDLER_H

#include <unordered_map>
#include <string>
using namespace std;


class StatusHandler {
private:
    // Map for storing status codes and their corresponding messages
    static const unordered_map<int,string> statusMessages;

public:
    // Function to get the status message corresponding to a code
    static string getStatusMessage(int code);
};

#endif // STATUSHANDLER_H
