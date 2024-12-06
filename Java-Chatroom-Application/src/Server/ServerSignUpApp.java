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

public class ServerSignUpApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Sign Up");

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

        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(new Font("Arial", 14));
        signUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

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
        grid.add(signUpButton, 1, 2);

        // Sign up button action
        signUpButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Please enter a username and password.");
                return;
            }

            if (DatabaseHandler.serverSignUp(username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "User created successfully!");
                primaryStage.close(); // Close sign-up window
                openServerLogin(); // Optionally open login window
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "The username is already taken or there was an error.");
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

    private void openServerLogin() {
        ServerLoginApp serverLoginApp = new ServerLoginApp();
        try {
            serverLoginApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
