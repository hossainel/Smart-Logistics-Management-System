package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.MainApplication;
import com.example.smartlogisticssystem.MyD;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Triggered by the onAction="#login" declaration in your FXML template
     */
    @FXML
    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText(); // Keep raw password input for matching

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Validation Error", "Please provide both your Username and Password.");
            return;
        }

        // Validate credentials cleanly against your relational data context
        if (authenticateUser(username, password)) {
            // Success! Command the primary stage runtime to expand and change to the application Dashboard view
            MainApplication.changeToDashboardView();
        } else {
            showErrorAlert("Authentication Failed", "The credentials provided are invalid. Please try again.");
            passwordField.clear(); // Wipe the password field for safety
        }
    }

    /**
     * Safe parameterized database authentication check
     */
    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        // Fetch connection context on-demand for thread stability
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a matching record exists
            }

        } catch (SQLException e) {
            System.err.println("Database Login Authentication Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * UI Helper method to show clean popup feedback dialogs to users
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
