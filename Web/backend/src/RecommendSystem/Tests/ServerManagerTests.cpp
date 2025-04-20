#include <gtest/gtest.h>
#include "ServerManager.h"
#include "IMenu.h"
#include <thread>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string>
using namespace std;


// Minimal IMenu implementation
class DefaultMenu : public IMenu {
public:
    string nextCommand() override { return ""; }
    void displayError(const std::string&) override {}
};

class ServerConnectionTest : public ::testing::Test {
protected:
    ServerManager* serverManager;
    DefaultMenu menu;
    map<string, ICommand*> commands;
    thread serverThread;
    int serverPort = 8081;  // Fixed port for simplicity

    void SetUp() override {
        // Initialize and start the server
        serverManager = new ServerManager(serverPort, &menu, commands);
        serverThread = thread([this]() { serverManager->start(nullptr); });

        // Allow the server some time to start
        this_thread::sleep_for(chrono::milliseconds(200));
    }

    void TearDown() override {
        // Clean up resources
        if (serverManager) {
            delete serverManager;
            serverManager = nullptr;
        }
        if (serverThread.joinable()) {
            serverThread.detach();
        }
    }

    // Helper function to simulate client communication
    string sendClientMessage(const std::string& message) {
        int clientSocket = socket(AF_INET, SOCK_STREAM, 0);
        if (clientSocket < 0) throw runtime_error("Socket creation failed");

        sockaddr_in serverAddr {};
        serverAddr.sin_family = AF_INET;
        serverAddr.sin_port = htons(serverPort);
        serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1");

        if (connect(clientSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) != 0) {
            close(clientSocket);
            throw runtime_error("Failed to connect to server");
        }

        // Send the message
        send(clientSocket, message.c_str(), message.size(), 0);

        // Receive the response
        char buffer[1024] = {0};
        int bytesRead = read(clientSocket, buffer, sizeof(buffer));
        close(clientSocket);

        if (bytesRead <= 0) throw runtime_error("No response from server");
        return string(buffer, bytesRead);
    }
};

// Single test: Verify server responds with "400 Bad Request" for an invalid command
TEST_F(ServerConnectionTest, ClientSendsInvalidCommand) {
    string response = sendClientMessage("INVALID_COMMAND\n");
    EXPECT_EQ(response, "400 Bad Request");
}
