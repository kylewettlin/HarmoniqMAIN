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
import com.harmoniqscrum.controller.StudioController;
import com.harmoniqscrum.model.Song;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Cursor;
import java.text.DecimalFormat;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ButtonType;

/**
 * Main view for the Harmoniq application.
 * Implements the MVC pattern by handling UI components and user interactions.
 */
public class HarmoniqView {
    private Stage stage;
    private HarmoniqFACADE facade;
    private LoginController loginController;
    private DashboardController dashboardController;
    private StudioController studioController;
    private Node currentlyExpandedDetails = null;
    private Node currentlyHighlightedEntry = null;
    private static final String BASE_STYLE = "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);";
    private static final String HIGHLIGHT_STYLE = BASE_STYLE + " -fx-border-color: #003366; -fx-border-width: 1;";
    
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
        logoView.setFitHeight(150);
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
        logoView.setFitHeight(70);
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

        // --- Center: Search and Song List --- 
        VBox centerArea = new VBox(15);
        centerArea.setPadding(new Insets(20));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search here");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchButton, searchField);
        
        centerArea.getChildren().add(searchBar);

        // Song List Area (inside ScrollPane)
        VBox songListVBox = new VBox(10);
        songListVBox.setStyle("-fx-padding: 10;");
        
        List<Song> songs = this.dashboardController.getSongs();
        if (songs == null || songs.isEmpty()) {
            songListVBox.getChildren().add(new Label("No songs found."));
        } else {
            for (Song song : songs) {
                 songListVBox.getChildren().add(createSongEntry(song));
            }
        }

        // Create ScrollPane for the song list
        ScrollPane songListScrollPane = new ScrollPane();
        songListScrollPane.setContent(songListVBox);
        songListScrollPane.setFitToWidth(true);
        songListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        songListScrollPane.setStyle("-fx-background-color: #f4f4f4; -fx-background: #f4f4f4; -fx-border-radius: 5;");
        VBox.setVgrow(songListScrollPane, Priority.ALWAYS);

        centerArea.getChildren().add(songListScrollPane);
        mainLayout.setCenter(centerArea);

        // Wire up Studio Button
        studioButton.setOnAction(e -> {
            // Create new controller instance when navigating to studio
            this.studioController = new StudioController(facade);
            showStudioScreen(this.studioController); 
        });

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
        VBox songEntryContainer = new VBox();
        songEntryContainer.setStyle(BASE_STYLE);

        HBox summaryBox = new HBox(15);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: transparent;");
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
        detailsBox.setStyle("-fx-background-color: transparent;");

        String genreText = "N/A";
        if (song.getGenres() != null && !song.getGenres().isEmpty()) {
            genreText = song.getGenres().stream().collect(Collectors.joining(", "));
        }

        DecimalFormat df = new DecimalFormat("#.0");
        String ratingText = (song.getRating() > 0.0) ? df.format(song.getRating()) + "/10" : "N/A";

        detailsBox.getChildren().add(new Label("Rating: " + ratingText));
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
        
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white;");
        
        boolean isDefaultSong = "Symphony No. 5".equalsIgnoreCase(song.getTitle()) && 
                                "Beethoven".equalsIgnoreCase(song.getComposer());
        
        deleteButton.setDisable(isDefaultSong);
        if (isDefaultSong) {
             deleteButton.setTooltip(new javafx.scene.control.Tooltip("Default song cannot be deleted."));
        }
        
        deleteButton.setOnAction(e -> {
             Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Song: " + song.getTitle());
            confirmAlert.setContentText("Are you sure you want to permanently delete this song?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (dashboardController != null) {
                        dashboardController.deleteSong(song);
                        
                        if (songEntryContainer.getParent() instanceof VBox) {
                            VBox parentVBox = (VBox) songEntryContainer.getParent();
                            parentVBox.getChildren().remove(songEntryContainer);
                            
                            if (currentlyExpandedDetails == detailsBox) {
                                currentlyExpandedDetails = null;
                                currentlyHighlightedEntry = null;
                            }
                        } else {
                             System.err.println("Could not find parent VBox to remove deleted song entry.");
                        }
                    }
                }
            });
        });

        Region buttonSpacer = new Region();
        HBox.setHgrow(buttonSpacer, Priority.ALWAYS);
        HBox buttonBox = new HBox(10, buttonSpacer, deleteButton, playButton);
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
                if(currentlyHighlightedEntry != null) {
                    currentlyHighlightedEntry.setStyle(BASE_STYLE);
                }
            }

            detailsBox.setVisible(isExpanding);
            detailsBox.setManaged(isExpanding);

            if (isExpanding) {
                currentlyExpandedDetails = detailsBox;
                currentlyHighlightedEntry = songEntryContainer;
                songEntryContainer.setStyle(HIGHLIGHT_STYLE);
            } else {
                currentlyExpandedDetails = null;
                currentlyHighlightedEntry = null;
                songEntryContainer.setStyle(BASE_STYLE);
            }
        });
        
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

    // Method to display the Studio/Composer screen
    public void showStudioScreen(StudioController controller) {
        this.studioController = controller; 
        
        BorderPane studioLayout = new BorderPane();
        studioLayout.setPadding(new Insets(10));

        // --- Top Bar (reuse from main screen logic if possible, or recreate) ---
        // For simplicity, recreating a similar top bar here
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;"); 

        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(70); 
        logoView.setPreserveRatio(true);

        // Make nav buttons switch back to dashboard or other views
        Button songsButton = new Button("Songs");
        songsButton.setOnAction(e -> {
            // Assuming dashboardController is still valid or recreated if needed
            if (this.dashboardController != null) {
                 showMainScreen(this.dashboardController); // Switch back
            } else {
                System.err.println("Dashboard controller not available to switch back.");
                // Potentially recreate dashboard controller here
            }
        });
        Button lessonsButton = new Button("Lessons"); // Add action later
        Button studioButton = new Button("Studio");
        studioButton.setDisable(true); // Disable studio button when on studio page

        Region spacer = new Region(); 
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button profileButton = new Button("👤");
        profileButton.setStyle("-fx-font-size: 18px;"); 
        topBar.getChildren().addAll(logoView, songsButton, lessonsButton, studioButton, spacer, profileButton);
        studioLayout.setTop(topBar);

        // --- Center: Song Editor Form (inside ScrollPane) ---
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(25));

        // Configure columns for alignment
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(100); // Label column
        col1.setHalignment(javafx.geometry.HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Input column
        formGrid.getColumnConstraints().addAll(col1, col2);

        int rowIndex = 0;
        String labelStyle = "-fx-text-fill: black;"; // Style for labels

        // Title
        Label titleLabel = new Label("Title:"); titleLabel.setStyle(labelStyle);
        formGrid.add(titleLabel, 0, rowIndex);
        TextField titleField = new TextField(); titleField.setPromptText("Enter song title");
        formGrid.add(titleField, 1, rowIndex++);
        
        // Composer
        Label composerLabel = new Label("Composer:"); composerLabel.setStyle(labelStyle);
        formGrid.add(composerLabel, 0, rowIndex);
        TextField composerField = new TextField(); composerField.setPromptText("Enter composer name");
        formGrid.add(composerField, 1, rowIndex++);

        // Tempo
        Label tempoLabel = new Label("Tempo (BPM):"); tempoLabel.setStyle(labelStyle);
        formGrid.add(tempoLabel, 0, rowIndex);
        Spinner<Integer> tempoSpinner = new Spinner<>(40, 240, 120); tempoSpinner.setEditable(true);
        formGrid.add(tempoSpinner, 1, rowIndex++);

        // Key Signature
        Label keySigLabel = new Label("Key Signature:"); keySigLabel.setStyle(labelStyle);
        formGrid.add(keySigLabel, 0, rowIndex);
        ComboBox<String> keySignatureBox = new ComboBox<>(FXCollections.observableArrayList(
            "C Major", "G Major", "D Major", "A Major", "E Major", "B Major", "F# Major", "C# Major",
            "F Major", "Bb Major", "Eb Major", "Ab Major", "Db Major", "Gb Major", "Cb Major",
            "A Minor", "E Minor", "B Minor", "F# Minor", "C# Minor", "G# Minor", "D# Minor", "A# Minor",
            "D Minor", "G Minor", "C Minor", "F Minor", "Bb Minor", "Eb Minor", "Ab Minor"
        ));
        keySignatureBox.setValue("C Major");
        formGrid.add(keySignatureBox, 1, rowIndex++);
        
        // Time Signature
        Label timeSigLabel = new Label("Time Signature:"); timeSigLabel.setStyle(labelStyle);
        formGrid.add(timeSigLabel, 0, rowIndex);
        Spinner<Integer> timeSigNumSpinner = new Spinner<>(1, 16, 4);
        Label slashLabel = new Label("/"); slashLabel.setStyle(labelStyle);
        Spinner<Integer> timeSigDenSpinner = new Spinner<>(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(2, 4, 8, 16)));
        timeSigDenSpinner.getValueFactory().setValue(4);
        HBox timeSigBox = new HBox(5, timeSigNumSpinner, slashLabel, timeSigDenSpinner);
        timeSigBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(timeSigBox, 1, rowIndex++);

        // Genres
        Label genresLabel = new Label("Genres:"); genresLabel.setStyle(labelStyle);
        formGrid.add(genresLabel, 0, rowIndex);
        TextField genresField = new TextField(); genresField.setPromptText("Enter genres, separated by commas");
        formGrid.add(genresField, 1, rowIndex++);

        // Lyrics
        Label lyricsLabel = new Label("Lyrics:"); lyricsLabel.setStyle(labelStyle);
        formGrid.add(lyricsLabel, 0, rowIndex);
        TextArea lyricsArea = new TextArea(); lyricsArea.setPromptText("Enter lyrics, one line per line");
        lyricsArea.setPrefRowCount(5);
        GridPane.setVgrow(lyricsArea, Priority.ALWAYS);
        formGrid.add(lyricsArea, 1, rowIndex++);

        // Notes
        Label notesLabel = new Label("Notes:"); notesLabel.setStyle(labelStyle);
        formGrid.add(notesLabel, 0, rowIndex);
        TextArea notesArea = new TextArea(); notesArea.setPromptText("Enter notes (e.g., JFugue format - Cq Eq Gq C5q)");
        notesArea.setPrefRowCount(8);
        GridPane.setVgrow(notesArea, Priority.ALWAYS);
        formGrid.add(notesArea, 1, rowIndex++);
        
        // Helper text for Notes
        Label notesHelpLabel = new Label(
            "JFugue Format Examples: C D E F G A B (middle octave, quarter notes default)\n" +
            "C5q = C, 5th Octave, Quarter | Eh = E, default Octave, Half | G#i = G sharp, Eighth\n" +
            "Durations: w=Whole, h=Half, q=Quarter(default), i=Eighth, s=Sixteenth, t=Thirty-second"
        );
        notesHelpLabel.setWrapText(true);
        notesHelpLabel.setStyle("-fx-font-size: 0.9em; -fx-text-fill: grey;");
        GridPane.setMargin(notesHelpLabel, new Insets(0, 0, 10, 0)); // Add bottom margin
        formGrid.add(notesHelpLabel, 1, rowIndex++); // Add below notes area, same column
        
        // Save Button
        Button saveButton = new Button("Save Song");
        saveButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> {
            if (studioController != null) {
                studioController.saveSong(
                    titleField.getText(),
                    composerField.getText(),
                    tempoSpinner.getValue().toString(), // Get value from spinner
                    keySignatureBox.getValue(), // Get value from combo box
                    timeSigNumSpinner.getValue().toString(),
                    timeSigDenSpinner.getValue().toString(),
                    genresField.getText(),
                    lyricsArea.getText(),
                    notesArea.getText()
                );
                // TODO: Add confirmation, clear fields or navigate away?
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Song Saved");
                 alert.setHeaderText(null);
                 alert.setContentText("Song '" + titleField.getText() + "' saved successfully!");
                 alert.showAndWait();
                 // Optionally switch back to song list
                 // showMainScreen(this.dashboardController);
            }
        });
        HBox buttonPane = new HBox(saveButton);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.setPadding(new Insets(10, 0, 0, 0));
        formGrid.add(buttonPane, 1, rowIndex); // Button is now at the original rowIndex + 1

        // Create ScrollPane for the form
        ScrollPane formScrollPane = new ScrollPane();
        formScrollPane.setContent(formGrid);
        formScrollPane.setFitToWidth(true); // Ensures grid uses available width
        formScrollPane.setFitToHeight(false); // Grid determines its own height
        formScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        formScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Make ScrollPane background transparent

        studioLayout.setCenter(formScrollPane); // Set ScrollPane as the center
        
        // --- Update Scene --- 
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(studioLayout);
            stage.setTitle("Harmoniq - Studio"); 
            // Reset stage size potentially?
             stage.setWidth(800);
             stage.setHeight(700); // Increase height for studio
             stage.centerOnScreen();
        } else {
            Scene newScene = new Scene(studioLayout, 800, 700);
            stage.setScene(newScene);
            stage.setTitle("Harmoniq - Studio"); 
            stage.show();
        }
    }
} 