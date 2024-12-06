import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class LoginApp {

    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public Scene getLoginScene(Stage primaryStage) {
        VBox loginBox = new VBox(10);
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");
        Button backButton = new Button("Back");

        loginBox.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, loginButton, signUpButton, backButton);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check if the server is running before attempting login
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
        
        backButton.setOnAction(e -> primaryStage.setScene(new MainApp().getMainScene(primaryStage)));

        return new Scene(loginBox, 300, 200);
    }

    private boolean isServerRunning() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            return true; // Server is running
        } catch (IOException e) {
            return false; // Server is not running or unreachable
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
