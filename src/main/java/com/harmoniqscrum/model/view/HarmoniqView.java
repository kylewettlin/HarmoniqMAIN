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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import com.harmoniqscrum.controller.CreateAccountController;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import com.harmoniqscrum.model.User;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.harmoniqscrum.controller.LessonsController;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ListCell;
import com.harmoniqscrum.utility.MusicXmlGenerator;
import com.harmoniqscrum.utility.SheetMusicRenderer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;

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
    private CreateAccountController createAccountController;
    private LessonsController lessonsController;
    private Node currentlyExpandedDetails = null;
    private Node currentlyHighlightedEntry = null;
    private Node currentlySelectedLessonTile = null;
    private static final String BASE_STYLE = "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);";
    private static final String HIGHLIGHT_STYLE = BASE_STYLE + " -fx-border-color: #003366; -fx-border-width: 1;";
    private Stage profilePopupStage;
    private Stage studentSelectionPopupStage;
    private Stage sheetMusicPopupStage;
    
    // Styles for lesson tiles
    private static final String LESSON_TILE_BASE = "-fx-background-radius: 15;";
    private static final String LESSON_TILE_STYLE = LESSON_TILE_BASE + "-fx-background-color: #003366;";
    
    // Define shadow effects
    private final DropShadow defaultLessonShadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.15));
    private final DropShadow selectedLessonShadow = new DropShadow(20, Color.rgb(0, 0, 0, 0.4));

    public HarmoniqView(Stage stage, HarmoniqFACADE facade) {
        this.stage = stage;
        this.facade = facade;
    }
    
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
        initializeUI();
    }
    
    private void initializeUI() {
        showLoginScreen();
    }
    
    public void showLoginScreen() {
        VBox loginContent = createLoginLayout(); // Get the content VBox
        
        // --- Root ScrollPane ---
        ScrollPane rootScrollPane = new ScrollPane();
        rootScrollPane.setContent(loginContent);
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // --- Update Scene ---
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(rootScrollPane, 600, 600); // Use ScrollPane as root
            stage.setScene(scene);
        } else {
            scene.setRoot(rootScrollPane); // Use ScrollPane as root
        }
        stage.setTitle("Harmoniq Login");
        stage.setWidth(600);
        stage.setHeight(600); 
        stage.centerOnScreen();
        if (!stage.isShowing()) {
             stage.show();
        }
    }
    
    private VBox createLoginLayout() {
        VBox root = new VBox(20); // This is the content VBox now
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        // Logo
        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);

        // Welcome Label
        Label welcomeLabel = new Label("Welcome back to Harmoniq");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #003366;");

        // Login Form Box
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setPadding(new Insets(20));
        loginBox.setMaxWidth(300);
        loginBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white;"); // Added white background

        String labelStyle = "-fx-text-fill: black;"; // Style for labels

        Label userLabel = new Label("Username:");
        userLabel.setStyle(labelStyle); // Explicitly set text color
        TextField userField = new TextField();
        userField.setPromptText("Please enter your username");

        Label passLabel = new Label("Password:");
        passLabel.setStyle(labelStyle); // Explicitly set text color
        PasswordField passField = new PasswordField();
        passField.setPromptText("Please enter your password");
        passField.setOnAction(e -> {
            if (loginController != null) {
                loginController.handleLogin(userField.getText(), passField.getText());
            }
        });

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setMinWidth(loginBox.getMaxWidth() - 40);
        loginButton.setOnAction(e -> {
            if (loginController != null) {
                loginController.handleLogin(userField.getText(), passField.getText());
            }
        });

        Hyperlink createAccountLink = new Hyperlink("Don't have an account? Create one here.");
        createAccountLink.setOnAction(e -> {
            this.createAccountController = new CreateAccountController(facade, this);
            showCreateAccountScreen(this.createAccountController);
        });

        loginBox.getChildren().addAll(
            userLabel, userField,
            passLabel, passField,
            new VBox(5),
            createAccountLink,
            new VBox(10),
            loginButton
        );

        root.getChildren().addAll(logoView, welcomeLabel, loginBox);
        return root; // Return the content VBox
    }
    
    public void showMainScreen(DashboardController controller) {
        this.dashboardController = controller;
        this.currentlyExpandedDetails = null;
        
        // --- Main Layout (Content for ScrollPane) ---
        BorderPane mainLayout = new BorderPane();
        // No padding here, padding will be handled by content areas

        // --- Top Area (StackPane: Logo + Navigation HBox) ---
        StackPane topStackPane = new StackPane();
        topStackPane.setStyle("-fx-background-color: white;"); // Or your desired background

        // Large Logo (Bottom Layer)
        Image logoImage = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(120); // Significantly larger logo
        logoView.setPreserveRatio(true);
        StackPane.setAlignment(logoView, Pos.TOP_LEFT); // Align logo to top-left
        // Add margin to logo if needed: StackPane.setMargin(logoView, new Insets(10)); 

        // Navigation HBox (Top Layer)
        HBox navHBox = new HBox(20);
        navHBox.setAlignment(Pos.CENTER_LEFT);
        // Adjust padding: Top pushes below logo, Left pushes right of logo
        navHBox.setPadding(new Insets(40, 20, 10, 150)); // Example padding (adjust as needed)
        navHBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;"); // Bottom border

        Button songsButton = new Button("Songs"); songsButton.setDisable(true); // Disable on main screen
        Button lessonsButton = new Button("Lessons");
        Button studioButton = new Button("Studio");
        lessonsButton.setOnAction(e -> {
            this.lessonsController = new LessonsController(facade);
            showLessonsScreen(this.lessonsController);
        });
        // Wire up Studio Button action (moved logic here)
         studioButton.setOnAction(e -> {
             this.studioController = new StudioController(facade);
             showStudioScreen(this.studioController);
         });
        // TODO: Add action for lessonsButton

        Region spacer = new Region(); 
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button profileButton = new Button("👤");
        profileButton.setStyle("-fx-font-size: 18px;"); 
        profileButton.setOnAction(e -> showProfilePopup());
        
        navHBox.getChildren().addAll(songsButton, lessonsButton, studioButton, spacer, profileButton);

        // Add logo first (bottom), then nav HBox (top)
        topStackPane.getChildren().addAll(logoView, navHBox);

        mainLayout.setTop(topStackPane); // Set StackPane as the top element

        // --- Center: Search and Song List ScrollPane ---
        VBox centerArea = new VBox(15);
        centerArea.setPadding(new Insets(20)); // Add padding back to center area

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search here");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
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

        // --- Root ScrollPane (Contains mainLayout) ---
        ScrollPane rootScrollPane = new ScrollPane();
        rootScrollPane.setContent(mainLayout); 
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        // --- Update Scene ---
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
             currentScene.setRoot(rootScrollPane); // Set ScrollPane as root
             stage.setTitle("Harmoniq - Dashboard");
             stage.setWidth(800);
             stage.setHeight(600);
             stage.centerOnScreen();
        } else {
             Scene newScene = new Scene(rootScrollPane, 800, 600); // Set ScrollPane as root
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

        // Define Play and Delete buttons first
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

        // Create the button HBox
        Region buttonSpacer = new Region();
        HBox.setHgrow(buttonSpacer, Priority.ALWAYS);
        HBox buttonBox = new HBox(10, buttonSpacer, deleteButton, playButton); // Add delete/play first
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(5, 0, 0, 0));
        
        // Add "Assign as Lesson" button conditionally
        if (facade.getCurrentUser() != null && "teacher".equalsIgnoreCase(facade.getCurrentUser().getRole())) {
             Button assignButton = new Button("Assign Lesson");
             assignButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
             assignButton.setOnAction(e -> {
                 // Trigger the controller to handle the assign attempt
                 if (dashboardController != null) {
                      dashboardController.handleAssignLessonAttempt(song);
                 }
             });
             buttonBox.getChildren().add(0, assignButton); // Add to buttonBox
        }

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
        
        // --- Studio Layout (Content for ScrollPane) ---
        BorderPane studioLayout = new BorderPane();
        // No padding here, padding will be handled by content areas

        // --- Top Area (StackPane: Logo + Navigation HBox) ---
        StackPane topStackPane = new StackPane();
        topStackPane.setStyle("-fx-background-color: white;"); // Match dashboard background

        // Large Logo (Bottom Layer)
        Image logoImage = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(120); // Match dashboard logo size
        logoView.setPreserveRatio(true);
        StackPane.setAlignment(logoView, Pos.TOP_LEFT);

        // Navigation HBox (Top Layer)
        HBox navHBox = new HBox(20);
        navHBox.setAlignment(Pos.CENTER_LEFT);
        // Use same padding as dashboard for consistency
        navHBox.setPadding(new Insets(40, 20, 10, 150)); 
        navHBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;"); // Bottom border

        // Nav buttons
        Button songsButton = new Button("Songs");
        songsButton.setOnAction(e -> {
            if (this.dashboardController != null) {
                 showMainScreen(this.dashboardController); // Switch back
            } else {
                System.err.println("Dashboard controller not available to switch back.");
                // TODO: Could potentially recreate dashboardController if needed
            }
        });
        Button lessonsButton = new Button("Lessons");
        lessonsButton.setOnAction(e -> {
            this.lessonsController = new LessonsController(facade);
            showLessonsScreen(this.lessonsController);
        });
        Button studioButton = new Button("Studio");
        studioButton.setDisable(true); // Disable studio button when on studio page

        Region spacer = new Region(); 
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button profileButton = new Button("👤");
        profileButton.setStyle("-fx-font-size: 18px;"); 
        profileButton.setOnAction(e -> showProfilePopup());
        
        navHBox.getChildren().addAll(songsButton, lessonsButton, studioButton, spacer, profileButton);
        
        // Add logo first, then nav HBox
        topStackPane.getChildren().addAll(logoView, navHBox);
        
        studioLayout.setTop(topStackPane); // Set StackPane as top

        // --- Center: Form ScrollPane ---
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
        TextField composerField = new TextField(); composerField.setPromptText("Enter composer name");
        formGrid.add(composerLabel, 0, rowIndex);
        formGrid.add(composerField, 1, rowIndex++);

        // Tempo
        Label tempoLabel = new Label("Tempo (BPM):"); tempoLabel.setStyle(labelStyle);
        Spinner<Integer> tempoSpinner = new Spinner<>(40, 240, 120); tempoSpinner.setEditable(true);
        formGrid.add(tempoSpinner, 1, rowIndex++);

        // Key Signature
        Label keySigLabel = new Label("Key Signature:"); keySigLabel.setStyle(labelStyle);
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

        studioLayout.setCenter(formScrollPane);
        
        // --- Root ScrollPane ---
        ScrollPane rootScrollPane = new ScrollPane();
        rootScrollPane.setContent(studioLayout); // Put the BorderPane inside
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        // --- Update Scene --- 
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(rootScrollPane); // Set Root ScrollPane as root
            stage.setTitle("Harmoniq - Studio"); 
            stage.setWidth(800);
            stage.setHeight(700); 
            stage.centerOnScreen();
        } else {
            Scene newScene = new Scene(rootScrollPane, 800, 700);
            stage.setScene(newScene);
            stage.setTitle("Harmoniq - Studio"); 
            stage.show();
        }
    }

    public void showCreateAccountScreen(CreateAccountController controller) {
        this.createAccountController = controller;

        // --- Content Container (holds logo and form, goes INSIDE scrollpane) ---
        VBox pageContent = new VBox(20);
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setPadding(new Insets(40)); // Padding for the whole content area

        // Logo
        Image logo = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);
        pageContent.getChildren().add(logoView); // Add logo to content VBox

        // Form Box
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(350);
        formBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white;"); // Added white background to form box
        String labelStyle = "-fx-text-fill: black;";

        // Username
        Label userLabel = new Label("Username:"); userLabel.setStyle(labelStyle);
        TextField userField = new TextField(); userField.setPromptText("Please enter your username");
        formBox.getChildren().addAll(userLabel, userField);
        // Email
        Label emailLabel = new Label("Email:"); emailLabel.setStyle(labelStyle);
        TextField emailField = new TextField(); emailField.setPromptText("Please enter your email");
        formBox.getChildren().addAll(emailLabel, emailField);
        // Password
        Label passLabel = new Label("Password:"); passLabel.setStyle(labelStyle);
        PasswordField passField = new PasswordField(); passField.setPromptText("Please enter your password");
        formBox.getChildren().addAll(passLabel, passField);
        // First Name
        Label firstNameLabel = new Label("First Name:"); firstNameLabel.setStyle(labelStyle);
        TextField firstNameField = new TextField(); firstNameField.setPromptText("Please enter your first name");
        formBox.getChildren().addAll(firstNameLabel, firstNameField);
        // Last Name
        Label lastNameLabel = new Label("Last Name:"); lastNameLabel.setStyle(labelStyle);
        TextField lastNameField = new TextField(); lastNameField.setPromptText("Please enter your last name");
        formBox.getChildren().addAll(lastNameLabel, lastNameField);
        // Role Selection
        Label roleLabel = new Label("Signing up as:"); roleLabel.setStyle(labelStyle);
        ToggleButton studentButton = new ToggleButton("Student");
        ToggleButton teacherButton = new ToggleButton("Teacher");
        ToggleGroup roleGroup = new ToggleGroup();
        studentButton.setToggleGroup(roleGroup);
        teacherButton.setToggleGroup(roleGroup);
        studentButton.setSelected(true); 
        HBox roleBox = new HBox(10, studentButton, teacherButton);
        formBox.getChildren().addAll(roleLabel, roleBox);
        // Spacer
        formBox.getChildren().add(new VBox(15)); 
        // Create Account Button
        Button createButton = new Button("Create Account");
        createButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        createButton.setMinWidth(formBox.getMaxWidth() - 40); 
        createButton.setOnAction(e -> {
            String selectedRole = "student"; // Default
            if (teacherButton.isSelected()) {
                selectedRole = "teacher";
            }
            if (createAccountController != null) {
                createAccountController.handleCreateAccount(
                    userField.getText(), 
                    emailField.getText(), 
                    passField.getText(), 
                    firstNameField.getText(), 
                    lastNameField.getText(), 
                    selectedRole
                );
            }
        });
        formBox.getChildren().add(createButton);
        // Back to Login Link
        Hyperlink backLink = new Hyperlink("Back to Login");
        backLink.setOnAction(e -> showLoginScreen());
        HBox backLinkBox = new HBox(backLink);
        backLinkBox.setAlignment(Pos.CENTER);
        backLinkBox.setPadding(new Insets(10,0,0,0));
        formBox.getChildren().add(backLinkBox);
        // --- End of formBox population ---
        
        pageContent.getChildren().add(formBox); // Add formBox to the content VBox

        // --- Root ScrollPane (Contains pageContent) ---
        ScrollPane rootScrollPane = new ScrollPane();
        rootScrollPane.setContent(pageContent); // Set the content VBox as scrollable content
        rootScrollPane.setFitToWidth(true); // Allow content VBox to use full width for centering
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Prevent horizontal scrolling

        // --- Update Scene --- 
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(rootScrollPane, 600, 750); // Use ScrollPane as root
            stage.setScene(scene);
        } else {
            scene.setRoot(rootScrollPane); // Set ScrollPane as the new root
        }
        stage.setTitle("Harmoniq - Create Account");
        // Width/Height are set on the Stage, ScrollPane adapts
        stage.setWidth(600); 
        stage.setHeight(750);
        stage.centerOnScreen();
         if (!stage.isShowing()) {
             stage.show();
        }
    }

    public void showProfilePopup() {
        User currentUser = facade.getCurrentUser();
        if (currentUser == null) {
            // Should not happen if profile button is only visible when logged in
            System.err.println("Cannot show profile popup: No user logged in.");
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Not Logged In");
             alert.setHeaderText(null);
             alert.setContentText("Please log in to view profile settings.");
             alert.showAndWait();
            return;
        }

        // Prevent opening multiple popups
        if (profilePopupStage != null && profilePopupStage.isShowing()) {
            profilePopupStage.toFront();
            return;
        }

        profilePopupStage = new Stage();
        profilePopupStage.initModality(Modality.WINDOW_MODAL); // Block interaction with main window
        profilePopupStage.initOwner(stage); // Set owner window
        profilePopupStage.setTitle("User Profile");

        VBox popupLayout = new VBox(15);
        popupLayout.setPadding(new Insets(20));
        popupLayout.setAlignment(Pos.CENTER_LEFT);
        String labelStyle = "-fx-text-fill: black;";

        // Display User Info
        Label usernameLabel = new Label("Username: " + currentUser.getUsername());
        usernameLabel.setStyle(labelStyle);
        Label roleLabel = new Label("Role: " + currentUser.getRole());
        roleLabel.setStyle(labelStyle);

        popupLayout.getChildren().addAll(usernameLabel, roleLabel, new Separator()); // Add a separator

        // Theme Selection
        Label themeLabel = new Label("App Theme:");
        themeLabel.setStyle(labelStyle);
        ChoiceBox<String> themeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Light", "Dark"));
        themeChoiceBox.setValue(currentUser.getTheme() != null ? currentUser.getTheme() : "Light"); // Set current theme
        HBox themeBox = new HBox(10, themeLabel, themeChoiceBox);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        popupLayout.getChildren().add(themeBox);
        
        // TODO: Add Highlight Color Picker later if needed

        // Save Button
        Button saveButton = new Button("Save Preferences");
        saveButton.setOnAction(e -> {
            String selectedTheme = themeChoiceBox.getValue();
            boolean changed = false;
            if (!selectedTheme.equals(currentUser.getTheme())) {
                 currentUser.setTheme(selectedTheme);
                 changed = true;
            }
            // Add logic for highlight color saving here if implemented

            if (changed) {
                facade.saveUserPreferences(); // Save the updated user list
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Preferences Saved");
                alert.setHeaderText(null);
                alert.setContentText("Theme preference saved. It will apply on next login.");
                alert.showAndWait();
            }
            profilePopupStage.close(); // Close the popup
        });
        
        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> profilePopupStage.close());
        
        // Sign Out Button
        Button signOutButton = new Button("Sign Out");
        signOutButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;"); // Red background
        signOutButton.setOnAction(e -> {
            profilePopupStage.close(); // Close the popup first
            if (loginController != null) {
                loginController.handleLogout(); // Delegate logout logic to controller
            } else {
                 System.err.println("LoginController is null, cannot log out.");
                 // Optionally show an error alert here
                 showErrorAlert("Logout Error", "Cannot sign out at this time.");
            }
        });

        HBox buttonPane = new HBox(10, signOutButton, saveButton, closeButton);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.setPadding(new Insets(20, 0, 0, 0));

        popupLayout.getChildren().add(buttonPane);

        Scene popupScene = new Scene(popupLayout);
        profilePopupStage.setScene(popupScene);
        profilePopupStage.sizeToScene(); // Adjust size
        profilePopupStage.setResizable(false); 
        profilePopupStage.show();
    }

    public void showLessonsScreen(LessonsController controller) {
        this.lessonsController = controller;
        this.currentlySelectedLessonTile = null;

        // --- Lessons Layout (Content for ScrollPane) ---
        BorderPane lessonsLayout = new BorderPane();

        // --- Top Area (Reusing StackPane structure) ---
        StackPane topStackPane = new StackPane();
        topStackPane.setStyle("-fx-background-color: white;");
        Image logoImage = new Image(getClass().getResourceAsStream("/Logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(120);
        logoView.setPreserveRatio(true);
        StackPane.setAlignment(logoView, Pos.TOP_LEFT);
        HBox navHBox = new HBox(20);
        navHBox.setAlignment(Pos.CENTER_LEFT);
        navHBox.setPadding(new Insets(40, 20, 10, 150));
        navHBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");
        Button songsButton = new Button("Songs");
        songsButton.setOnAction(e -> {
             if (this.dashboardController != null) {
                 showMainScreen(this.dashboardController);
             } else {
                // Potentially recreate controller if needed
                 System.err.println("Dashboard controller null, cannot switch.");
             }
        });
        Button lessonsButtonNav = new Button("Lessons"); lessonsButtonNav.setDisable(true);
        Button studioButton = new Button("Studio");
        studioButton.setOnAction(e -> {
            this.studioController = new StudioController(facade);
            showStudioScreen(this.studioController);
        });
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button profileButton = new Button("👤");
        profileButton.setStyle("-fx-font-size: 18px;");
        profileButton.setOnAction(e -> showProfilePopup());
        navHBox.getChildren().addAll(songsButton, lessonsButtonNav, studioButton, spacer, profileButton);
        topStackPane.getChildren().addAll(logoView, navHBox);
        lessonsLayout.setTop(topStackPane);

        // --- Center Area: Search + Lessons Grid + Lesson Details ---
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search lesson");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        searchBar.getChildren().addAll(searchButton, searchField);
        centerContent.getChildren().add(searchBar);

        // HBox to hold Lesson Grid (left) and Details (right)
        HBox lessonsArea = new HBox(30);
        VBox.setVgrow(lessonsArea, Priority.ALWAYS);

        // Left Side: Lesson Selection Grid (using TilePane)
        TilePane lessonGrid = new TilePane();
        lessonGrid.setPadding(new Insets(10));
        lessonGrid.setHgap(20);
        lessonGrid.setVgap(20);
        lessonGrid.setPrefColumns(2);

        List<Song> assignedSongs = this.lessonsController.getAssignedSongs();
        Song firstSong = null;
        
        lessonGrid.getChildren().clear();
        if (assignedSongs == null || assignedSongs.isEmpty()) {
            lessonGrid.getChildren().add(new Label("No lessons assigned."));
        } else {
            firstSong = assignedSongs.get(0);
            for (Song assignedSong : assignedSongs) {
                 Node lessonTile = createLessonTile(assignedSong);
                 if (assignedSong.equals(firstSong)) {
                     lessonTile.setEffect(selectedLessonShadow); // Apply selected shadow initially
                     currentlySelectedLessonTile = lessonTile; // Track it
                 } else {
                      lessonTile.setEffect(defaultLessonShadow); // Apply default shadow
                 }
                 lessonGrid.getChildren().add(lessonTile);
            }
        }
        ScrollPane gridScrollPane = new ScrollPane(lessonGrid);
        gridScrollPane.setFitToWidth(true);
        gridScrollPane.setFitToHeight(true);
        gridScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gridScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        HBox.setHgrow(gridScrollPane, Priority.NEVER); // Don't let grid scrollpane grow horizontally

        // Right Side: Lesson Details Pane
        VBox detailsPane = new VBox(15);
        detailsPane.setId("lessonDetailsPane");
        detailsPane.setPadding(new Insets(20));
        detailsPane.setStyle("-fx-background-color: white; -fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 10;");
        HBox.setHgrow(detailsPane, Priority.ALWAYS);
        
        // Set initial content (placeholder text)
        Label initialLabel = new Label(
            assignedSongs == null || assignedSongs.isEmpty() ? 
            "No lessons assigned yet." : 
            "Select a lesson from the left."
        );
        initialLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        initialLabel.setStyle("-fx-text-fill: grey;");
        detailsPane.getChildren().add(initialLabel);
        detailsPane.setAlignment(Pos.CENTER);
        
        lessonsArea.getChildren().addAll(gridScrollPane, detailsPane);
        centerContent.getChildren().add(lessonsArea);
        
        lessonsLayout.setCenter(centerContent);

        // --- Root ScrollPane (Contains lessonsLayout) ---
        ScrollPane rootScrollPane = new ScrollPane(lessonsLayout);
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        // --- Update Scene ---
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(rootScrollPane);
        } else {
            Scene newScene = new Scene(rootScrollPane, 1000, 750);
            stage.setScene(newScene);
        }
        stage.setTitle("Harmoniq - Lessons");
        stage.setWidth(1000);
        stage.setHeight(750);
        stage.centerOnScreen();
         if (!stage.isShowing()) {
             stage.show();
        }

        // Update details pane for the first song (if it exists) after scene is set
        if (firstSong != null) {
            updateLessonDetailsPane(firstSong); 
        }
    }
    
    private VBox createLessonTile(Song lessonSong) {
        VBox tile = new VBox();
        tile.setPrefSize(200, 150);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle(LESSON_TILE_STYLE); // Apply the blue style directly
        // Shadow is set in showLessonsScreen or click handler
        
        Label titleLabel = new Label(lessonSong.getTitle() != null ? lessonSong.getTitle() : "Untitled");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE); // Set text color to white directly
        
        tile.getChildren().add(titleLabel);
        tile.setCursor(Cursor.HAND);

        tile.setOnMouseClicked(e -> {
            // Reset previous selection's shadow
            if (currentlySelectedLessonTile != null && currentlySelectedLessonTile != tile) {
                 currentlySelectedLessonTile.setEffect(defaultLessonShadow); // Reset to default shadow
            }
            // Apply selected shadow to current tile
            tile.setEffect(selectedLessonShadow);
            currentlySelectedLessonTile = tile;
            
            updateLessonDetailsPane(lessonSong);
        });
        
        return tile;
    }

    // Add helper method to update the details pane
    private void updateLessonDetailsPane(Song song) {
        Node lookupResult = null;
        try {
            // Find the detailsPane in the current scene
            lookupResult = stage.getScene().getRoot().lookup("#lessonDetailsPane");
        } catch (Exception e) {
            System.err.println("Error looking up #lessonDetailsPane: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("UI Error", "Could not find the lesson details area.");
            return; // Cannot proceed
        }
        
        if (!(lookupResult instanceof VBox)) {
             System.err.println("Could not find details pane (#lessonDetailsPane) as VBox to update.");
             showErrorAlert("UI Error", "Lesson details area has unexpected type.");
             return; // Cannot proceed
        }
        
        VBox detailsPane = (VBox) lookupResult;
        
        try {
            detailsPane.getChildren().clear();
            detailsPane.setAlignment(Pos.TOP_LEFT); // Reset alignment

             // Re-populate with details from the selected song
             Label detailTitle = new Label(song.getTitle() != null ? song.getTitle() : "Untitled Lesson"); // Null check
             detailTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
             detailTitle.setWrapText(true);
             
             Separator titleSeparator = new Separator(); // Create separator
             titleSeparator.setPadding(new Insets(5, 0, 10, 0)); // Add some vertical space around it

             // Use GridPane for cleaner key-value display
             GridPane detailsGrid = new GridPane();
             detailsGrid.setHgap(10);
             detailsGrid.setVgap(8);
             detailsGrid.setPadding(new Insets(10, 0, 10, 0));

             int rowIndex = 0;
             String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #333;";
             String valueStyle = "-fx-text-fill: #555;";

             // Composer
             Label composerKey = new Label("Composer:"); composerKey.setStyle(labelStyle);
             Label composerValue = new Label(song.getComposer() != null ? song.getComposer() : "N/A"); composerValue.setStyle(valueStyle);
             detailsGrid.add(composerKey, 0, rowIndex);
             detailsGrid.add(composerValue, 1, rowIndex++);

             // Key Signature
             Label keyKey = new Label("Key:"); keyKey.setStyle(labelStyle);
             Label keyValue = new Label(song.getKeySignature() != null ? song.getKeySignature() : "N/A"); keyValue.setStyle(valueStyle);
             detailsGrid.add(keyKey, 0, rowIndex);
             detailsGrid.add(keyValue, 1, rowIndex++);

             // Tempo
             Label tempoKey = new Label("Tempo:"); tempoKey.setStyle(labelStyle);
             Label tempoValue = new Label(song.getTempo() > 0 ? song.getTempo() + " BPM" : "N/A"); tempoValue.setStyle(valueStyle); // Check tempo > 0
             detailsGrid.add(tempoKey, 0, rowIndex);
             detailsGrid.add(tempoValue, 1, rowIndex++);
             
             // Time Signature
             Label timeSigKey = new Label("Time Signature:"); timeSigKey.setStyle(labelStyle);
             String timeSigText = "N/A";
             if (song.getTimeSignature() != null) {
                 timeSigText = song.getTimeSignature().toString();
             }
             Label timeSigValue = new Label(timeSigText); timeSigValue.setStyle(valueStyle);
             detailsGrid.add(timeSigKey, 0, rowIndex);
             detailsGrid.add(timeSigValue, 1, rowIndex++);

             // Genres
             String genreText = "N/A";
             if (song.getGenres() != null && !song.getGenres().isEmpty()) {
                 // Wrap genre collection access in try-catch just in case
                 try {
                     genreText = song.getGenres().stream().collect(Collectors.joining(", "));
                 } catch (Exception ex) {
                     System.err.println("Error processing genres: " + ex.getMessage());
                     genreText = "Error";
                 }
             } 
             Label genreKey = new Label("Genre(s):"); genreKey.setStyle(labelStyle);
             Label genreValue = new Label(genreText); genreValue.setStyle(valueStyle);
             genreValue.setWrapText(true);
             detailsGrid.add(genreKey, 0, rowIndex);
             detailsGrid.add(genreValue, 1, rowIndex++);
             
             // View Sheet Music Button Area
             Separator contentSeparator = new Separator(); 
             Button viewSongButton = new Button("View Sheet Music");
             viewSongButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
             
             ProgressIndicator loadingIndicator = new ProgressIndicator(-1.0);
             loadingIndicator.setVisible(false);
             loadingIndicator.setMaxSize(25, 25);
             
             HBox buttonArea = new HBox(10, viewSongButton, loadingIndicator);
             buttonArea.setAlignment(Pos.CENTER_LEFT);

             // --- Action for the button --- 
             viewSongButton.setOnAction(e -> {
                // Disable button, show loading
                viewSongButton.setDisable(true);
                loadingIndicator.setVisible(true);
                
                SheetMusicRenderer renderer = new SheetMusicRenderer();
                Task<File> renderTask = new Task<File>() {
                    @Override
                    protected File call() throws Exception {
                        return renderer.renderSongToPng(song);
                    }
                };

                renderTask.setOnSucceeded(workerStateEvent -> {
                    File pngFile = renderTask.getValue();
                    if (pngFile != null && pngFile.exists()) {
                        try {
                            Image sheetMusicImage = new Image(new FileInputStream(pngFile));
                            showSheetMusicPopup(sheetMusicImage, pngFile);
                        } catch (FileNotFoundException ex) {
                            showErrorAlert("Rendering Error", "Could not load rendered image file.");
                        } finally {
                             viewSongButton.setDisable(false);
                             loadingIndicator.setVisible(false);
                        }
                    } else {
                        showErrorAlert("Rendering Failed", "LilyPond failed to generate the sheet music image.");
                        viewSongButton.setDisable(false);
                        loadingIndicator.setVisible(false);
                    }
                });

                renderTask.setOnFailed(workerStateEvent -> {
                    Throwable exception = renderTask.getException();
                    System.err.println("Rendering Task Failed: " + exception.getMessage());
                    exception.printStackTrace();
                    showErrorAlert("Rendering Error", "An error occurred during sheet music generation: " + exception.getMessage());
                    viewSongButton.setDisable(false);
                    loadingIndicator.setVisible(false);
                });

                new Thread(renderTask).start();
             });
             // --- End of Action --- 

            detailsPane.getChildren().addAll(detailTitle, titleSeparator, detailsGrid, contentSeparator, buttonArea);

        } catch (Exception ex) {
            // Catch any unexpected errors during pane population
            System.err.println("!!! Critical Error updating lesson details pane for song: " + (song != null ? song.getTitle() : "[null song]") + " !!!");
            ex.printStackTrace();
            // Show an error message in the UI
            detailsPane.getChildren().clear(); // Clear potentially half-populated pane
            Label errorLabel = new Label("Error displaying lesson details.\nCheck console for more information.");
            errorLabel.setStyle("-fx-text-fill: red;");
            detailsPane.getChildren().add(errorLabel);
            detailsPane.setAlignment(Pos.CENTER);
            // Optionally show an alert too
            // showErrorAlert("UI Error", "Failed to display lesson details: " + ex.getMessage());
        }
    }

    // Method to display the sheet music popup
    private void showSheetMusicPopup(Image sheetMusicImage, File imageFile) {
         if (sheetMusicPopupStage != null && sheetMusicPopupStage.isShowing()) {
             sheetMusicPopupStage.toFront();
             return;
         }
         sheetMusicPopupStage = new Stage();
         sheetMusicPopupStage.initOwner(stage);
         sheetMusicPopupStage.setTitle("Sheet Music");

         ImageView imageView = new ImageView(sheetMusicImage);
         imageView.setPreserveRatio(true);
         // imageView.setFitWidth(800); // Optionally set a fit width

         ScrollPane scrollPane = new ScrollPane(imageView);
         scrollPane.setFitToWidth(true);
         scrollPane.setFitToHeight(true); 
         scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

         Scene scene = new Scene(scrollPane); // Use a reasonable default size
         sheetMusicPopupStage.setScene(scene);
         
         // Set stage size based on image, but with limits
         double imgWidth = sheetMusicImage.getWidth();
         double imgHeight = sheetMusicImage.getHeight();
         double maxWidth = 1000; // Max popup width
         double maxHeight = 800; // Max popup height
         
         double stageWidth = Math.min(imgWidth + 40, maxWidth); // Add padding
         double stageHeight = Math.min(imgHeight + 40, maxHeight);
         
         sheetMusicPopupStage.setWidth(stageWidth);
         sheetMusicPopupStage.setHeight(stageHeight);
         sheetMusicPopupStage.setMinWidth(400); // Min sensible size
         sheetMusicPopupStage.setMinHeight(300);

         // Ensure temp file is deleted when popup closes
         sheetMusicPopupStage.setOnHidden(e -> {
             if (imageFile != null) {
                 try {
                     Files.deleteIfExists(imageFile.toPath());
                     System.out.println("Deleted temp sheet music file: " + imageFile.getName());
                     // Also delete the temp directory if desired and empty
                     // Path parentDir = imageFile.getParentFile().toPath();
                     // Files.deleteIfExists(parentDir);
                 } catch (IOException ioException) {
                     System.err.println("Warning: Failed to delete temp sheet music file: " + ioException.getMessage());
                 }
             }
         });

         sheetMusicPopupStage.show();
    }
    
     // Helper for showing error alerts
     private void showErrorAlert(String title, String content) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle(title);
         alert.setHeaderText(null);
         alert.setContentText(content);
         alert.showAndWait();
     }

    // --- Method to show the Student Selection Popup ---
    public void showStudentSelectionPopup(Song songToAssign, List<User> students) {
        // Prevent multiple popups
        if (studentSelectionPopupStage != null && studentSelectionPopupStage.isShowing()) {
            studentSelectionPopupStage.toFront();
            return;
        }

        studentSelectionPopupStage = new Stage();
        studentSelectionPopupStage.initModality(Modality.WINDOW_MODAL);
        studentSelectionPopupStage.initOwner(stage);
        studentSelectionPopupStage.setTitle("Assign '" + songToAssign.getTitle() + "'");

        VBox popupLayout = new VBox(15);
        popupLayout.setPadding(new Insets(20));
        popupLayout.setMinWidth(350);

        Label instructionLabel = new Label("Select a student to assign this lesson to:");
        instructionLabel.setStyle("-fx-text-fill: black;");

        ListView<User> studentListView = new ListView<>();
        studentListView.setItems(FXCollections.observableArrayList(students));
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Optional: Custom cell factory to show name nicely (User already has good toString())
        /* studentListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? null : user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")");
            }
        }); */
        
        popupLayout.getChildren().addAll(instructionLabel, studentListView);

        // Buttons
        Button assignBtn = new Button("Assign");
        assignBtn.setDefaultButton(true);
        assignBtn.setOnAction(e -> {
            User selectedStudent = studentListView.getSelectionModel().getSelectedItem();
            if (selectedStudent == null) {
                 Alert alert = new Alert(Alert.AlertType.WARNING);
                 alert.setTitle("No Selection");
                 alert.setHeaderText(null);
                 alert.setContentText("Please select a student from the list.");
                 alert.showAndWait();
                 return;
            }
            
            // Call controller to perform assignment
            boolean success = dashboardController.assignLessonToStudent(songToAssign, selectedStudent);
            
            if (success) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Lesson Assigned");
                 alert.setHeaderText(null);
                 alert.setContentText("Successfully assigned '" + songToAssign.getTitle() + "' to " + selectedStudent.getUsername() + ".");
                 alert.showAndWait();
                 studentSelectionPopupStage.close();
            } else {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Assignment Failed");
                 alert.setHeaderText(null);
                 alert.setContentText("Could not assign lesson. See logs for details.");
                 alert.showAndWait();
                 // Keep popup open for potential retry or cancellation
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setCancelButton(true);
        cancelBtn.setOnAction(e -> studentSelectionPopupStage.close());

        HBox buttonPane = new HBox(10, cancelBtn, assignBtn);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.setPadding(new Insets(15, 0, 0, 0));

        popupLayout.getChildren().add(buttonPane);

        Scene popupScene = new Scene(popupLayout);
        studentSelectionPopupStage.setScene(popupScene);
        studentSelectionPopupStage.sizeToScene();
        studentSelectionPopupStage.setResizable(false);
        studentSelectionPopupStage.show();
    }
} 