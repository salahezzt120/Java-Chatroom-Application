import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ServerSignUpApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Sign Up");

        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button signUpButton = new Button("Sign Up");

        GridPane grid = new GridPane();
        grid.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(signUpButton, 1, 2);

        signUpButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.ERROR, "Sign Up Error", "Please enter a username and password.");
                return;
            }

            if (DatabaseHandler.serverSignUp(username, password)) {
                showAlert(AlertType.INFORMATION, "Sign Up Successful", "User created successfully!");
                primaryStage.close(); // Close sign-up window
                openServerLogin(); // Optionally open login window
            } else {
                showAlert(AlertType.ERROR, "Sign Up Failed", "The username is already taken or there was an error.");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(AlertType alertType, String title, String message) {
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
