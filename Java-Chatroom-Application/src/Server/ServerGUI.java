package Server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServerGUI extends Application {

    private static final int PORT = 12345;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "salah";

    private ListView<String> onlineListView = new ListView<>();
    private ListView<String> offlineListView = new ListView<>();
    private ListView<String> busyListView = new ListView<>();
    private Button startButton = new Button("Start Server");
    private Button stopButton = new Button("Stop Server");
    private Button manageUsersButton = new Button("Manage Users");
    private ServerSocket serverSocket;
    private Set<ClientHandler> clientHandlers = new HashSet<>();
    private boolean serverRunning = false;

    private Timeline refreshTimeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Dashboard");

        startButton.setOnAction(e -> startServer());
        stopButton.setOnAction(e -> stopServer());
        manageUsersButton.setOnAction(e -> showUserManagementDialog(primaryStage));

        Label onlineLabel = new Label("Online Users");
        Label offlineLabel = new Label("Offline Users");
        Label busyLabel = new Label("Busy Users");

        VBox vbox = new VBox(10, 
            onlineLabel, onlineListView, 
            offlineLabel, offlineListView, 
            busyLabel, busyListView, 
            manageUsersButton, 
            startButton, stopButton
        );
        vbox.setPadding(new javafx.geometry.Insets(10));

        Scene scene = new Scene(vbox, 400, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeRefreshTimeline();
        refreshUserLists(); // Initial load
    }

    private void initializeRefreshTimeline() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(10), e -> refreshUserLists());
        refreshTimeline = new Timeline(keyFrame);
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void startServer() {
        if (serverRunning) {
            showAlert(AlertType.INFORMATION, "Server Already Running", "The server is already running.");
            return;
        }

        try {
            serverSocket = new ServerSocket(PORT);
            serverRunning = true;
            startButton.setDisable(true);
            stopButton.setDisable(false);
            showAlert(AlertType.INFORMATION, "Server Started", "The server is now running.");

            // Start server thread
            new Thread(() -> {
                while (serverRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket);
                        clientHandlers.add(clientHandler);
                        new Thread(clientHandler).start();
                    } catch (IOException e) {
                        if (serverRunning) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Server Error", "Failed to start the server.");
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (!serverRunning) {
            showAlert(AlertType.INFORMATION, "Server Not Running", "The server is not running.");
            return;
        }

        try {
            serverRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.close();
            }
            clientHandlers.clear();
            startButton.setDisable(false);
            stopButton.setDisable(true);
            showAlert(AlertType.INFORMATION, "Server Stopped", "The server has been stopped.");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Server Error", "Failed to stop the server.");
            e.printStackTrace();
        }
    }

    private void refreshUserLists() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username, status FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                onlineListView.getItems().clear();
                offlineListView.getItems().clear();
                busyListView.getItems().clear();

                while (rs.next()) {
                    String username = rs.getString("username");
                    String status = rs.getString("status");

                    if ("online".equals(status)) {
                        onlineListView.getItems().add(username);
                    } else if ("offline".equals(status)) {
                        offlineListView.getItems().add(username);
                    } else if ("busy".equals(status)) {
                        busyListView.getItems().add(username);
                    }
                }
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to fetch user data.");
            e.printStackTrace();
        }
    }

    private void showUserManagementDialog(Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("User Management");

        TableView<User> userTableView = new TableView<>();
        userTableView.setItems(getUserList());

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        userTableView.getColumns().addAll(usernameColumn, statusColumn);

        Button addButton = new Button("Add User");
        Button updateButton = new Button("Update User");
        Button deleteButton = new Button("Delete User");
        Button refreshButton = new Button("Refresh");

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, refreshButton);
        VBox vbox = new VBox(10, userTableView, buttonBox);
        vbox.setPadding(new javafx.geometry.Insets(10));

        Scene scene = new Scene(vbox, 500, 400);
        dialog.setScene(scene);
        dialog.show();

        // Add button actions here
        addButton.setOnAction(e -> showUserDialog(dialog, null, userTableView));
        updateButton.setOnAction(e -> {
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showUserDialog(dialog, selectedUser, userTableView);
            } else {
                showAlert(AlertType.WARNING, "No Selection", "Please select a user to update.");
            }
        });
        deleteButton.setOnAction(e -> {
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                deleteUser(selectedUser);
                userTableView.setItems(getUserList()); // Refresh table
            } else {
                showAlert(AlertType.WARNING, "No Selection", "Please select a user to delete.");
            }
        });
        refreshButton.setOnAction(e -> userTableView.setItems(getUserList())); // Refresh the user list
    }

    private ObservableList<User> getUserList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username, status FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String status = rs.getString("status");
                    users.add(new User(username, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void showUserDialog(Stage owner, User user, TableView<User> userTableView) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(user == null ? "Add User" : "Update User");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField statusField = new TextField();

        if (user != null) {
            usernameField.setText(user.getUsername());
            statusField.setText(user.getStatus());
        }

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        VBox vbox = new VBox(10, new Label("Username:"), usernameField, new Label("Password:"), passwordField,
                new Label("Status:"), statusField, new HBox(10, saveButton, cancelButton));
        vbox.setPadding(new javafx.geometry.Insets(10));

        Scene scene = new Scene(vbox, 300, 200);
        dialog.setScene(scene);
        dialog.show();

        saveButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String status = statusField.getText();

            if (user == null) {
                addUser(username, password, status);
            } else {
                updateUser(user.getUsername(), username, password, status);
            }
            userTableView.setItems(getUserList()); // Refresh the table
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());
    }

    private void addUser(String username, String password, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO users (username, password, status) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, status);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(String oldUsername, String newUsername, String password, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE users SET username = ?, password = ?, status = ? WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, password);
                stmt.setString(3, status);
                stmt.setString(4, oldUsername);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private java.io.PrintWriter out;
        private java.io.BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                out = new java.io.PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("USERNAME:")) {
                        username = message.substring("USERNAME:".length());
                        broadcast(username + " has joined the chat.");
                        updateStatus(username, "online");
                    } else if (message.startsWith("STATUS:")) {
                        String status = message.substring("STATUS:".length());
                        updateStatus(username, status);
                        broadcast(username + " is now " + status + ".");
                    } else {
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
                clientHandlers.remove(this);
                if (username != null) {
                    updateStatus(username, "offline");
                    broadcast(username + " has left the chat.");
                }
            }
        }

        private void broadcast(String message) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.out.println(message);
            }
        }

        private void updateStatus(String username, String status) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "UPDATE users SET status = ? WHERE username = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, status);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void close() throws IOException {
            socket.close();
        }
    }

    public static class User {
        private final StringProperty username;
        private final StringProperty status;

        public User(String username, String status) {
            this.username = new SimpleStringProperty(username);
            this.status = new SimpleStringProperty(status);
        }

        public String getUsername() {
            return username.get();
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }
    }
}
