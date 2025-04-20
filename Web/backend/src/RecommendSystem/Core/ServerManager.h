#ifndef SERVER_MANAGER_H
#define SERVER_MANAGER_H

#include "ConsoleMenu.h"
#include "App.h"
#include "ThreadPool.h"
#include "StatusHandler.h"      // For Handle with Errors

#include <string>
#include <thread>
#include <iostream>
#include <sstream>
#include <map>
#include <sstream>   // For std::istringstream
#include <cstring>   // For memset
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define MAX_CLIENTS 20
#define BUFFER_SIZE 4096

using namespace std;

class ServerManager {
private:

    int port;
    int serverSocket;

    IMenu* menu;                          // Pointer to a menu object
    map<string, ICommand*> commands; // Map of command keys to ICommand objects
    ThreadPool threadPool;  

    void handleClient(int clientSocket, App* app);

public:
    ServerManager(int port, IMenu* menu, const map<string, ICommand*>& commands);
    ~ServerManager();
    void start(App* app);

};

#endif // SERVER_MANAGER_H