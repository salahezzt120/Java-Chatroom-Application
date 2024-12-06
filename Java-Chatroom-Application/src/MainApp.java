import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Welcome");

        // Load the welcome screen
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        // Welcome message
        VBox layout = new VBox(20);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");

        // Button actions
        loginButton.setOnAction(e -> showLoginScreen());
        signUpButton.setOnAction(e -> showSignUpScreen());

        layout.getChildren().addAll(loginButton, signUpButton);

        Scene welcomeScene = new Scene(layout, 300, 200);
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private void showLoginScreen() {
        // Create the login screen
        LoginApp loginApp = new LoginApp();
        Scene loginScene = loginApp.getLoginScene(primaryStage);
        primaryStage.setScene(loginScene);
    }

    private void showSignUpScreen() {
        // Create the sign-up screen
        SignUpApp signUpApp = new SignUpApp();
        Scene signUpScene = signUpApp.getSignUpScene(primaryStage);
        primaryStage.setScene(signUpScene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    Scene getMainScene(Stage primaryStage) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
