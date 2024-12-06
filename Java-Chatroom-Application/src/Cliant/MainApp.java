package Cliant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome");

        // Load the welcome screen
        showWelcomeScreen(primaryStage);
    }

    private void showWelcomeScreen(Stage primaryStage) {
        // Create and style the layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f0f0;");

        // Create and style buttons
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(150);
        signUpButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10;");

        // Set button actions
        loginButton.setOnAction(e -> showLoginScreen(primaryStage));
        signUpButton.setOnAction(e -> showSignUpScreen(primaryStage));

        // Add buttons to the layout
        layout.getChildren().addAll(
            new Label("Welcome to the Chat Room") {{
                setFont(new Font("Arial", 24));
                setTextFill(Color.web("#333333"));
            }},
            loginButton,
            signUpButton
        );

        // Set scene and show
        Scene welcomeScene = new Scene(layout, 400, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private void showLoginScreen(Stage primaryStage) {
        // Create the login screen
        LoginApp loginApp = new LoginApp();
        Scene loginScene = loginApp.getLoginScene(primaryStage);
        primaryStage.setScene(loginScene);
    }

    private void showSignUpScreen(Stage primaryStage) {
        // Create the sign-up screen
        SignUpApp signUpApp = new SignUpApp();
        Scene signUpScene = signUpApp.getSignUpScene(primaryStage);
        primaryStage.setScene(signUpScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
