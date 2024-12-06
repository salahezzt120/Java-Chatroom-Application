import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxApp {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private TextArea chatArea;
    private TextField messageField;
    private ListView<String> userListView;

    public Scene getChatScene(Stage primaryStage, String username) {
        this.username = username;

        VBox chatBox = new VBox(10);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        messageField = new TextField();
        Button sendButton = new Button("Send");
        Button statusButton = new Button("Go Offline");
        Button saveButton = new Button("Save Chat Log");
        Button busyButton = new Button("Go Busy"); // New button for "Go Busy"
        userListView = new ListView<>();

        ScrollPane chatScrollPane = new ScrollPane(chatArea);
        chatScrollPane.setFitToWidth(true);

        HBox inputBox = new HBox(10, messageField, sendButton);
        VBox rightPane = new VBox(10, statusButton, busyButton, saveButton, new Label("Online Users:"), userListView);
        HBox mainLayout = new HBox(10, chatScrollPane, rightPane);

        chatBox.getChildren().addAll(mainLayout, inputBox);

        // Set up socket connection
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("USERNAME:" + username); // Send username to server

            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        chatArea.appendText(message + "\n");
                        updateUserList();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Unable to connect to chat server.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send button action
        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                out.println(username + ": " + message);
                messageField.clear();
            }
        });

        // Status button action
        statusButton.setOnAction(e -> {
            String newStatus = statusButton.getText().equals("Go Offline") ? "offline" : "online";
            out.println("STATUS:" + newStatus);
            statusButton.setText(newStatus.equals("offline") ? "Go Online" : "Go Offline");
        });

        // Busy button action
        busyButton.setOnAction(e -> {
            out.println("STATUS:busy");
            statusButton.setText("Go Online"); // Change status button text to "Go Online" when busy
        });

        // Save chat log button action
        saveButton.setOnAction(e -> saveChatLog());

        return new Scene(chatBox, 800, 600);
    }

    private void updateUserList() {
        // This method should ideally fetch the user list from the server
        // For now, we'll simulate it with some dummy data
        List<String> users = new ArrayList<>();
        users.add("User1");
        users.add("User2");
        userListView.getItems().setAll(users);
    }

    private void saveChatLog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chat Log");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // Show the save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Ensure the file has a .txt extension
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(chatArea.getText());
                showAlert(Alert.AlertType.INFORMATION, "Saved", "Chat log saved successfully.");
            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace for debugging
                showAlert(Alert.AlertType.ERROR, "Save Error", "Unable to save chat log.");
            }
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
