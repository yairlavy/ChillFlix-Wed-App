#include "StatusHandler.h"

// Definition of the static StatusMessages map
const unordered_map<int,string> StatusHandler::statusMessages={
    {400, "400 Bad Request"},
    {404, "404 Not Found"},
    {204, "204 No Content"},
    {201, "201 Created"},
    {200, "200 Ok"}
};
// Implementation of getStatusMessage
string StatusHandler::getStatusMessage(int code){
    auto it= statusMessages.find(code);
    if(it != statusMessages.end()){
        return it->second;
    }
    //not found the error code, return a default error message
    return "500 Internal Server Error\n";
}