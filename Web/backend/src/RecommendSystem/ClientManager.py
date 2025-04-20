import socket
import sys

class Client:
    """
    Client class to manage TCP communication with a server.
    """

    def __init__(self, server_ip: str, server_port: int):
        """
        Initialize the client with the server's IP address and port.

        Args:
            server_ip (str): The IP address of the server.
            server_port (int): The port number of the server.
        """
        self.server_ip = server_ip
        self.server_port = server_port
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def connect_to_server(self):
        """
        Connects to the server using a TCP socket.
        """
        try:
            self.client_socket.connect((self.server_ip, self.server_port))
            print(f"Connected to server {self.server_ip}:{self.server_port}")

        except ConnectionRefusedError:
            print(f"Failed to connect to server {self.server_ip}:{self.server_port}")
            raise

    def send_command(self, command: str) -> None:
        """Sends a command to the server.
        Args:command (str): The command to send to the server."""
        try:
            self.client_socket.sendall(command.encode('utf-8', errors='replace'))

        except Exception as e:
            print(f"Error sending command: {e}")
            raise

    def receive_response(self) -> str:
        """
        Receives a response from the server.

        Returns:
            str: The response received from the server.
        """
        try:
            response = self.client_socket.recv(4096).decode('utf-8', errors='replace')

            if not response:  # If no response or empty response
                return ""  # Return an empty string

            print(response)
            return response

        except Exception as e:
            print(f"Error receiving data from server: {e}")
            raise

    def close_connection(self) -> None:
        """
        Closes the connection to the server.
        """
        self.client_socket.close()
        print("Connection closed.")


def main():
    if len(sys.argv) < 3:
        print("Usage: python client.py <server_ip> <server_port>")
        sys.exit(1)

    server_ip = sys.argv[1]
    
    try:
        server_port = int(sys.argv[2])
        if server_port < 0 or server_port > 65535:
            raise ValueError
    except ValueError:
        print("Port must be a number between 0 and 65535.")
        sys.exit(1)
    
    client = Client(server_ip, server_port)

    try:
        client.connect_to_server()

        while True:
            user_input = input("")
            
            if not user_input.strip():
                #print("Empty input. Try again.")
                continue  # Skip empty input

            client.send_command(user_input)

            if user_input.lower() == "goodbye":
                print("You ended the connection.")
                break  # Exit the loop to end the connection

            response = client.receive_response()

            if response == "":  # Handle empty or invalid responses
                print("Server disconnected.")
                break  # Continue to the next iteration

    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        client.close_connection()

if __name__ == "__main__":
    main()