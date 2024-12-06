import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignUpApp {

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    public Scene getSignUpScene(Stage primaryStage) {
        VBox signUpBox = new VBox(10);
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button signUpButton = new Button("Sign Up");
        Button backButton = new Button("Back");

        signUpBox.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, signUpButton, backButton);

        signUpButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (databaseHandler.signUp(username, password)) {
                primaryStage.setScene(new LoginApp().getLoginScene(primaryStage));
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign-Up Error", "Username already exists.");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(new MainApp().getMainScene(primaryStage)));

        return new Scene(signUpBox, 300, 200);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
