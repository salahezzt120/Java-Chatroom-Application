import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server Main Menu");

        // Create buttons for Login and Sign Up
        Button loginButton = new Button("Server Login");
        Button signUpButton = new Button("Server Sign Up");

        // Button actions
        loginButton.setOnAction(e -> openServerLogin(primaryStage));
        signUpButton.setOnAction(e -> openServerSignUp(primaryStage));

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.getChildren().addAll(loginButton, signUpButton);

        // Scene Setup
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openServerLogin(Stage primaryStage) {
        ServerLoginApp serverLoginApp = new ServerLoginApp();
        try {
            serverLoginApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openServerSignUp(Stage primaryStage) {
        ServerSignUpApp serverSignUpApp = new ServerSignUpApp();
        try {
            serverSignUpApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
