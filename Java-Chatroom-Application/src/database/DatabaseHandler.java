package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String DB_USER = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = "salah"; // Change to your MySQL password

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Login method for chat users
    public static boolean login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a record is found, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sign up method for chat users
    public static boolean signUp(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login method for server users
    public static boolean serverLogin(String username, String password) {
        String query = "SELECT * FROM server_users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a record is found, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sign up method for server users
    public static boolean serverSignUp(String username, String password) {
        String query = "INSERT INTO server_users (username, password) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void updateStatus(String username, String status) {
        String query = "UPDATE users SET status = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
