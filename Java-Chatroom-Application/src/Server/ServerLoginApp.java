package Server;

import database.DatabaseHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ServerLoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Login");

        // Create UI elements
        Label userLabel = new Label("Username:");
        userLabel.setFont(new Font("Arial", 14));
        userLabel.setTextFill(Color.DARKBLUE);
        
        TextField userTextField = new TextField();
        userTextField.setFont(new Font("Arial", 14));
        userTextField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordLabel.setTextFill(Color.DARKBLUE);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setFont(new Font("Arial", 14));
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Arial", 14));
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setStyle("-fx-background-color: #E8EAF6;");

        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        // Login button action
        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter your username and password.");
                return;
            }

            if (DatabaseHandler.serverLogin(username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                primaryStage.close(); // Close login window
                openServerGUI(); // Open server GUI
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
            }
        });

        // Set scene
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openServerGUI() {
        ServerGUI serverGUI = new ServerGUI();
        serverGUI.start(new Stage());
    }
    
}
