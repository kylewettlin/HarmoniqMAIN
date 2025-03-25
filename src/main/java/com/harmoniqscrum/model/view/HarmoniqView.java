package com.harmoniqscrum.model.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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
        var label = new Label("Welcome to Harmoniq!");
        
        Button playButton = new Button("Play Mary Had a Little Lamb");
        playButton.setOnAction(e -> playSong("Mary Had a Little Lamb"));
        
        var root = new VBox(10, label, playButton);
        root.setStyle("-fx-padding: 20px;");
        
        var scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Harmoniq Music Player");
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