package com.harmoniqscrum.model.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import com.harmoniqscrum.model.HarmoniqFACADE;

/**
 * Main view for the Harmoniq application.
 * Implements the MVC pattern by handling UI components and user interactions.
 */
public class HarmoniqView {
    private Stage stage;
    private HarmoniqFACADE facade;
    
    public HarmoniqView(Stage stage, HarmoniqFACADE facade) {
        this.stage = stage;
        this.facade = facade;
        initializeUI();
    }
    
    private void initializeUI() {
        // Logo
        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(100); // Adjust size as needed
        logoView.setPreserveRatio(true);

        // Welcome Label
        Label welcomeLabel = new Label("Welcome back to Harmoniq");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #003366;"); // Basic styling

        // Login Form Box
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setPadding(new Insets(20));
        loginBox.setMaxWidth(300); // Limit width for better appearance
        loginBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 5;");

        // Username
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Please enter your username");

        // Password
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Please enter your password");

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setMinWidth(loginBox.getMaxWidth() - 40); // Match box padding

        // Create Account Link (simple label for now)
        Label createAccountLabel = new Label("Don't have an account? Create one here."); // Placeholder link

        loginBox.getChildren().addAll(
            userLabel, userField,
            passLabel, passField,
            new VBox(5), // Spacer
            createAccountLabel,
            new VBox(10), // Spacer
            loginButton
        );


        // Main Layout
        VBox root = new VBox(20); // Spacing between elements
        root.setAlignment(Pos.TOP_CENTER); // Center content vertically
        root.setPadding(new Insets(40)); // Padding around the whole VBox
        root.getChildren().addAll(logoView, welcomeLabel, loginBox);


        var scene = new Scene(root, 600, 500); // Adjusted scene size
        stage.setScene(scene);
        stage.setTitle("Harmoniq Login"); // Updated title
        stage.show();
    }
    
    /**
     * Request to play a song
     * @param songTitle The title of the song to play
     */
    private void playSong(String songTitle) {
        facade.playSong(songTitle);
    }
    
    /**
     * Show an error message to the user
     * @param message Error message to display
     */
    public void showError(String message) {
        // Future implementation could use a dialog or alert
        System.err.println("ERROR: " + message);
    }
    
    /**
     * Update the UI based on playback status
     * @param isPlaying Whether music is currently playing
     */
    public void updatePlaybackStatus(boolean isPlaying) {
        // Future implementation could update button states, show visualization, etc.
    }
} 