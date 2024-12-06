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

public class SignUpApp {

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    public Scene getSignUpScene(Stage primaryStage) {
        // Create and style the layout
        VBox signUpBox = new VBox(15);
        signUpBox.setPadding(new Insets(20));
        signUpBox.setAlignment(Pos.CENTER);
        signUpBox.setStyle("-fx-background-color: #f0f0f0;");

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
        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(100);
        signUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        Button backButton = new Button("Back");
        backButton.setPrefWidth(100);
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        // Add labels and fields to the layout
        signUpBox.getChildren().addAll(
            new Label("Sign Up") {{
                setFont(new Font("Arial", 20));
                setTextFill(Color.web("#333333"));
            }},
            usernameField,
            passwordField,
            signUpButton,
            backButton
        );

        // Set button actions
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (databaseHandler.signUp(username, password)) {
                primaryStage.setScene(new LoginApp().getLoginScene(primaryStage));
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign-Up Error", "Username already exists.");
            }
        });

        backButton.setOnAction(e -> {
            MainApp mainApp = new MainApp();
            mainApp.start(primaryStage); // Restart MainApp
        });

        return new Scene(signUpBox, 400, 400);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
