package Server;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Main Menu");

        // Create buttons for Login and Sign Up
        Button loginButton = new Button("Server Login");
        Button signUpButton = new Button("Server Sign Up");

        // Style buttons
        styleButton(loginButton);
        styleButton(signUpButton);

        // Button actions
        loginButton.setOnAction(e -> openServerLogin());
        signUpButton.setOnAction(e -> openServerSignUp());

        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #F5F5F5;");
        layout.getChildren().addAll(loginButton, signUpButton);

        // Scene Setup
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleButton(Button button) {
        button.setFont(new Font("Arial", 14));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
    }

    private void openServerLogin() {
        Stage stage = new Stage();
        ServerLoginApp serverLoginApp = new ServerLoginApp();
        try {
            serverLoginApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openServerSignUp() {
        Stage stage = new Stage();
        ServerSignUpApp serverSignUpApp = new ServerSignUpApp();
        try {
            serverSignUpApp.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
