#include "ServerManager.h"

ServerManager::ServerManager(int port, IMenu* menu, const std::map<std::string, ICommand*>& commands)
    : port(port), serverSocket(-1), menu(menu), commands(commands), threadPool(MAX_CLIENTS) {
    cout << "ServerManager initialized on port " << port << endl;
}

ServerManager::~ServerManager() {
    if (serverSocket != -1) {
        cout << "Closing server socket...." << endl;
        close(serverSocket);
    }
}

// Function to split the received message into commandKey and remainingInput
void splitMessage(const std::string& receivedMessage, std::string& commandKey, std::string& remainingInput) {
    istringstream messageStream(receivedMessage);

    // Extract the command key (first word)
    if (!(messageStream >> commandKey)) {
        commandKey.clear(); // If no command key, clear it
    }

    // Extract the remaining input
    getline(messageStream, remainingInput);

    // Trim leading space from the remaining input
    if (!remainingInput.empty() && remainingInput[0] == ' ') {
        remainingInput.erase(0, 1);
    }
}

void ServerManager::handleClient(int clientSocket, App* app) {
    char buffer[BUFFER_SIZE] = {0};

    while (true) {
        // Clear the buffer
        memset(buffer, 0, BUFFER_SIZE);

        // Receive command from the client
        int bytesRead = read(clientSocket, buffer, sizeof(buffer));
        
        if (bytesRead <= 0) {
            // Client disconnected
            cout << "Client disconnected." << endl;
            break;  // Exit the loop if the client disconnects
        }

        // Convert the received string to a C++ string
        string receivedMessage(buffer);

        // Variables for the split message
        string commandKey;
        string remainingInput;

        // Split the received message
        splitMessage(receivedMessage, commandKey, remainingInput);

        // If command is empty, continue waiting for input
        if (commandKey.empty()) {
            cout << "Empty command received." << endl;
            string errorResponse = StatusHandler::getStatusMessage(400);
            send(clientSocket, errorResponse.c_str(), errorResponse.size(), 0);
            continue;  // Don't exit, continue waiting for more input
        }

        cout << "Received command: " << commandKey << " with input: " << remainingInput << endl;

        string response;
        // Execute the corresponding command if it exists
        if (commands.find(commandKey) != commands.end()) {
            cout << "Command found. Executing..." << endl;
            response = commands[commandKey]->execute(remainingInput);
            
            // If return value is empty but the command is good
            if (response.empty()) {
                cout << "Command executed successfully, no response to send." << endl;
                string errorResponse = StatusHandler::getStatusMessage(404);
                send(clientSocket, errorResponse.c_str(), errorResponse.size(), 0);
            } else {
                // Send the valid response
                cout << "Sending response to client: " << response << endl;
                send(clientSocket, response.c_str(), response.size(), 0);
            }
        } else {
            cout << "Command not found: " << commandKey << endl;
            // If the command does not exist, send an error response
            string errorResponse = StatusHandler::getStatusMessage(400);
            send(clientSocket, errorResponse.c_str(), errorResponse.size(), 0);
        }
    }

    close(clientSocket);  // Close the client connection after the loop ends
    cout << "Closed connection for client socket." << endl;
}

void ServerManager::start(App* app) {
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);

    // Create socket
    if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    cout << "Socket created successfully." << endl;

    // Configure server address
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(port);

    // Bind the socket
    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
        perror("Bind failed");
        return;
    }
    cout << "Socket bound to port " << port << endl;

    // Listen for incoming connections
    if (listen(serverSocket, MAX_CLIENTS) < 0) {
        perror("Listen failed");
        return;
    }
    cout << "Server is listening on port " << port << endl;

    // Accept and handle client connections
    while (true) {
        int clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientAddrLen);
        if (clientSocket < 0) {
            perror("Accept failed");
            continue;
        }

        cout << "New connection from " << inet_ntoa(clientAddr.sin_addr) << ":" << ntohs(clientAddr.sin_port) << endl;

        threadPool.addTask(clientSocket, [this, app](int socket) {
            this->handleClient(socket, app);
        });
    }

    close(serverSocket); // Close the server socket
    cout << "Server socket closed." << endl;
}
