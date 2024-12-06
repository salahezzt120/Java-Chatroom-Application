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

public class ServerLoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Login");

        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        GridPane grid = new GridPane();
        grid.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.ERROR, "Login Error", "Please enter your username and password.");
                return;
            }

            if (DatabaseHandler.serverLogin(username, password)) {
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                primaryStage.close(); // Close login window
                openServerGUI(); // Open server GUI
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
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

    private void openServerGUI() {
        ServerGUI serverGUI = new ServerGUI();
        serverGUI.start(new Stage());
    }

    
}
