package Cliant;

import database.DatabaseHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.Socket;

public class LoginApp {

    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public Scene getLoginScene(Stage primaryStage) {
        // Create and style the layout
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #f0f0f0;");

        // Create and style the username and password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(200);
        usernameField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(200);
        passwordField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Create and style the buttons
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(100);
        signUpButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        Button backButton = new Button("Back");
        backButton.setPrefWidth(100);
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        // Add labels and fields to the layout
        loginBox.getChildren().addAll(
            new Label("Login") {{
                setFont(new Font("Arial", 20));
                setTextFill(Color.web("#333333"));
            }},
            usernameField,
            passwordField,
            loginButton,
            signUpButton,
            backButton
        );

        // Set button actions
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (isServerRunning()) {
                if (databaseHandler.login(username, password)) {
                    primaryStage.setScene(new ChatBoxApp().getChatScene(primaryStage, username));
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Server Error", "Cannot connect to the server. Please try again later.");
            }
        });

        signUpButton.setOnAction(e -> primaryStage.setScene(new SignUpApp().getSignUpScene(primaryStage)));

        backButton.setOnAction(e -> {
            MainApp mainApp = new MainApp();
            mainApp.start(primaryStage); // Restart MainApp
        });

        return new Scene(loginBox, 400, 400);
    }

    private boolean isServerRunning() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
