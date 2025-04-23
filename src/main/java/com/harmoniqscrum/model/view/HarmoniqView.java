package com.harmoniqscrum.model.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.controller.LoginController;
import com.harmoniqscrum.controller.DashboardController;
import com.harmoniqscrum.model.Song;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Cursor;

/**
 * Main view for the Harmoniq application.
 * Implements the MVC pattern by handling UI components and user interactions.
 */
public class HarmoniqView {
    private Stage stage;
    private HarmoniqFACADE facade;
    private LoginController loginController;
    private DashboardController dashboardController;
    private Node currentlyExpandedDetails = null;
    
    public HarmoniqView(Stage stage, HarmoniqFACADE facade) {
        this.stage = stage;
        this.facade = facade;
    }
    
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
        initializeUI();
    }
    
    private void initializeUI() {
        VBox loginRoot = createLoginLayout();

        var scene = new Scene(loginRoot, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Harmoniq Login");
        stage.show();
    }
    
    private VBox createLoginLayout() {
        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(100);
        logoView.setPreserveRatio(true);

        Label welcomeLabel = new Label("Welcome back to Harmoniq");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #003366;");

        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setPadding(new Insets(20));
        loginBox.setMaxWidth(300);
        loginBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 5;");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Please enter your username");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Please enter your password");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setMinWidth(loginBox.getMaxWidth() - 40);
        loginButton.setOnAction(e -> {
            if (loginController != null) {
                loginController.handleLogin(userField.getText(), passField.getText());
            }
        });

        Label createAccountLabel = new Label("Don't have an account? Create one here.");

        loginBox.getChildren().addAll(
            userLabel, userField,
            passLabel, passField,
            new VBox(5),
            createAccountLabel,
            new VBox(10),
            loginButton
        );

        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(logoView, welcomeLabel, loginBox);
        return root;
    }
    
    public void showMainScreen(DashboardController controller) {
        this.dashboardController = controller;
        this.currentlyExpandedDetails = null;
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");

        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        Button songsButton = new Button("Songs");
        Button lessonsButton = new Button("Lessons");
        Button studioButton = new Button("Studio");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button profileButton = new Button("👤");
        profileButton.setStyle("-fx-font-size: 18px;");

        topBar.getChildren().addAll(logoView, songsButton, lessonsButton, studioButton, spacer, profileButton);
        mainLayout.setTop(topBar);

        VBox centerArea = new VBox(15);
        centerArea.setPadding(new Insets(20));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search here");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchButton, searchField);
        
        VBox songListVBox = new VBox(10);
        songListVBox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-radius: 5;");
        
        List<Song> songs = this.dashboardController.getSongs();

        if (songs == null || songs.isEmpty()) {
            songListVBox.getChildren().add(new Label("No songs found."));
        } else {
            for (Song song : songs) {
                 songListVBox.getChildren().add(createSongEntry(song));
            }
        }
        
        centerArea.getChildren().addAll(searchBar, songListVBox);
        mainLayout.setCenter(centerArea);

        Scene currentScene = stage.getScene();
        if (currentScene != null) {
             currentScene.setRoot(mainLayout);
             stage.setTitle("Harmoniq - Dashboard");
             stage.setWidth(800);
             stage.setHeight(600);
             stage.centerOnScreen();
        } else {
             Scene newScene = new Scene(mainLayout, 800, 600);
             stage.setScene(newScene);
             stage.setTitle("Harmoniq - Dashboard"); 
             stage.show();
        }
    }

    private VBox createSongEntry(Song song) {
        HBox summaryBox = new HBox(15);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-border-color: transparent; -fx-border-width: 1; -fx-border-radius: 10;");
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        summaryBox.setCursor(Cursor.HAND);

        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(60, 60);
        imagePlaceholder.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 5;");
        
        VBox titleArtistBox = new VBox(5);
        Label titleLabel = new Label(song.getTitle() != null ? song.getTitle() : "Untitled");
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label artistLabel = new Label(song.getComposer() != null ? song.getComposer() : "Unknown Artist");
        titleArtistBox.getChildren().addAll(titleLabel, artistLabel);

        summaryBox.getChildren().addAll(imagePlaceholder, titleArtistBox);

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(0, 10, 10, 10 + 60 + 15));
        detailsBox.setStyle("-fx-background-color: white; -fx-background-radius: 0 0 10 10;");

        String genreText = "N/A";
        if (song.getGenres() != null && !song.getGenres().isEmpty()) {
            genreText = song.getGenres().stream().collect(Collectors.joining(", "));
        }

        detailsBox.getChildren().add(new Label("Rating: N/A"));
        detailsBox.getChildren().add(new Label("Genre: " + genreText));
        detailsBox.getChildren().add(new Label("Tempo: " + song.getTempo() + " BPM"));
        detailsBox.getChildren().add(new Label("Key: " + (song.getKeySignature() != null ? song.getKeySignature() : "N/A")));

        Button playButton = new Button("Play Song");
        playButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        playButton.setOnAction(e -> {
            if (dashboardController != null) {
                dashboardController.playSong(song);
            }
        });
        HBox buttonBox = new HBox(playButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(5, 0, 0, 0));
        detailsBox.getChildren().add(buttonBox);

        detailsBox.setVisible(false);
        detailsBox.setManaged(false);

        summaryBox.setOnMouseClicked(event -> {
            boolean isExpanding = !detailsBox.isVisible();

            if (currentlyExpandedDetails != null && currentlyExpandedDetails != detailsBox) {
                currentlyExpandedDetails.setVisible(false);
                currentlyExpandedDetails.setManaged(false);
                ((Node)currentlyExpandedDetails.getParent().getChildrenUnmodifiable().get(0)).setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-border-color: transparent; -fx-border-width: 1; -fx-border-radius: 10;");
            }

            detailsBox.setVisible(isExpanding);
            detailsBox.setManaged(isExpanding);

            if (isExpanding) {
                currentlyExpandedDetails = detailsBox;
                 summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 10 10 0 0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-border-color: #003366; -fx-border-width: 1; -fx-border-radius: 10 10 0 0;");
            } else {
                currentlyExpandedDetails = null;
                 summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-border-color: transparent; -fx-border-width: 1; -fx-border-radius: 10;");
            }
        });
        
        VBox songEntryContainer = new VBox();
        songEntryContainer.setStyle("-fx-background-color: transparent;");
        songEntryContainer.getChildren().addAll(summaryBox, detailsBox);

        return songEntryContainer;
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