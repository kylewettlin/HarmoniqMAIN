package com.harmoniqscrum.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        var label = new Label("Welcome to Harmoniq!");
        
        Button playButton = new Button("Play Mary Had a Little Lamb");
        playButton.setOnAction(e -> playMusic());
        
        var root = new VBox(10, label, playButton);
        root.setStyle("-fx-padding: 20px;");
        
        var scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Harmoniq Music Player");
        stage.show();
    }
    
    private void playMusic() {
        new Thread(() -> {
            try {
                System.out.println("Playing Mary Had a Little Lamb...");
                MusicPlayer.playSong();
            } catch (Exception e) {
                System.out.println("Error playing music: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
} 