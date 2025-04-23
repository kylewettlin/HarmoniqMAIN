package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.view.HarmoniqView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CreateAccountController {

    private HarmoniqFACADE facade;
    private HarmoniqView view; // To potentially navigate back

    public CreateAccountController(HarmoniqFACADE facade, HarmoniqView view) {
        this.facade = facade;
        this.view = view;
    }

    /**
     * Handles the request to create a new user account.
     * 
     * @param username The desired username.
     * @param email The user's email.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param role The selected role ("student" or "teacher").
     */
    public void handleCreateAccount(String username, String email, String password, 
                                    String firstName, String lastName, String role) {

        // Basic Validation (add more robust checks as needed)
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() || // TODO: Add email format validation
            password == null || password.isEmpty() ||
            firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            role == null) {
            
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        // Call facade to create account (using default theme/color for now)
        // TODO: Add UI elements for theme/color selection later if needed
        boolean success = facade.createAccount(firstName.trim(), lastName.trim(), username.trim(), 
                                            email.trim(), password, role.toLowerCase(), 
                                            "light", "blue"); // Default theme/color

        if (success) {
            showAlert(AlertType.INFORMATION, "Account Created", "Account for '" + username + "' created successfully!");
            // Navigate back to login screen
            view.showLoginScreen(); // Assumes showLoginScreen() exists or calls initializeUI()
        } else {
            // Typically means username is taken, but could be other issues
            showAlert(AlertType.ERROR, "Creation Failed", "Could not create account. The username '" + username + "' might already be taken.");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 