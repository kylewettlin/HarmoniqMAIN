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
    private Node currentlyExpandedDetails = null;
    private Node currentlyHighlightedEntry = null;
    private static final String BASE_STYLE = "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);";
    private static final String HIGHLIGHT_STYLE = BASE_STYLE + " -fx-border-color: #003366; -fx-border-width: 1;";
    private Stage profilePopupStage;
    
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
        Button lessonsButton = new Button("Lessons"); // TODO: Add action later
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

        HBox buttonPane = new HBox(10, saveButton, closeButton);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.setPadding(new Insets(20, 0, 0, 0));

        popupLayout.getChildren().add(buttonPane);

        Scene popupScene = new Scene(popupLayout);
        profilePopupStage.setScene(popupScene);
        profilePopupStage.sizeToScene(); // Adjust size
        profilePopupStage.setResizable(false); 
        profilePopupStage.show();
    }
} 