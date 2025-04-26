package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.User;
import com.harmoniqscrum.model.view.HarmoniqView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    private HarmoniqFACADE facade;
    private HarmoniqView view;

    public LoginController(HarmoniqFACADE facade, HarmoniqView view) {
        this.facade = facade;
        this.view = view;
    }

    public void handleLogin(String username, String password) {
        User loggedInUser = facade.login(username, password);

        if (loggedInUser != null) {
            System.out.println("Login successful for: " + loggedInUser.getUsername());
            // Create the controller for the next screen, passing view
            DashboardController dashboardController = new DashboardController(facade, this.view);
            // Tell the view to switch and pass the new controller
            view.showMainScreen(dashboardController);
        } else {
            System.out.println("Login failed for username: " + username);
             // Show error message in the UI
             Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Login Failed");
             alert.setHeaderText(null);
             alert.setContentText("Invalid username or password. Please try again.");
             alert.showAndWait();
            // Optionally, clear password field or add specific error labels in the view
            // view.showError("Invalid username or password."); // Using the existing method for now
        }
    }
    
    /**
     * Handles the user logout process.
     * Clears the session in the facade and returns to the login screen.
     */
    public void handleLogout() {
        System.out.println("Logging out user: " + (facade.getCurrentUser() != null ? facade.getCurrentUser().getUsername() : "N/A"));
        facade.logout(); // Use the existing logout method
        view.showLoginScreen(); // Show the login screen again
    }
} 