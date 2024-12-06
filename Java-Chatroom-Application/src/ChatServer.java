import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Initialize database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "salah");

            // Start server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Setup input and output streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Read and handle client messages
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("USERNAME:")) {
                        username = message.substring("USERNAME:".length());
                        // Notify other clients that a new user has joined
                        broadcast(username + " has joined the chat.");
                        updateStatus(username, "online");
                    } else if (message.startsWith("STATUS:")) {
                        String status = message.substring("STATUS:".length());
                        updateStatus(username, status);
                        broadcast(username + " is now " + status + ".");
                    } else {
                        // Broadcast the message to all clients
                        broadcast(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Remove client handler and update status
                clientHandlers.remove(this);
                if (username != null) {
                    updateStatus(username, "offline");
                    broadcast(username + " has left the chat.");
                }
            }
        }

        private void broadcast(String message) {
            synchronized (clientHandlers) {
                for (ClientHandler clientHandler : clientHandlers) {
                    clientHandler.out.println(message);
                }
            }
        }

        private void updateStatus(String username, String status) {
            synchronized (connection) {
                try {
                    String query = "UPDATE users SET status = ? WHERE username = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, status);
                        stmt.setString(2, username);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
