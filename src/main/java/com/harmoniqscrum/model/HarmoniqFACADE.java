package com.harmoniqscrum.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.harmoniqscrum.model.view.HarmoniqView;
import com.harmoniqscrum.controller.LoginController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main facade class for the Harmoniq application.
 * Acts as the entry point and coordinates between UI and business logic.
 */
public class HarmoniqFACADE extends Application {
    
    private static HarmoniqFACADE instance;
    private User currentUser;
    private PlaybackEngine playbackEngine;
    private HarmoniqView view;

    /**
     * Constructor
     */
    public HarmoniqFACADE() {
        playbackEngine = new PlaybackEngine();
    }

    /**
     * Singleton getInstance method
     * 
     * @return The singleton instance
     */
    public static HarmoniqFACADE getInstance() {
        if (instance == null) {
            instance = new HarmoniqFACADE();
        }
        return instance;
    }
    
    /**
     * Application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        Application.launch(args);
    }
    
    /**
     * JavaFX start method
     * 
     * @param stage The primary stage
     */
    @Override
    public void start(Stage stage) {
        instance = this;
        
        // Create View and Controller
        this.view = new HarmoniqView(stage, this);
        LoginController loginController = new LoginController(this, this.view);
        
        // Pass controller to the view
        this.view.setLoginController(loginController);
        
        // Initialize the UI (which now happens inside HarmoniqView constructor)
        // The view's constructor already calls initializeUI(), so no need to call it here.
    }
    
    /**
     * JavaFX stop method
     */
    @Override
    public void stop() {
        if (playbackEngine != null) {
            playbackEngine.shutdown();
        }
    }
    
    /**
     * Play a song by title
     * 
     * @param title The title of the song to play
     */
    public void playSong(String title) {
        boolean success = playbackEngine.playSongByTitle(title);
        if (success) {
            if (view != null) {
                view.updatePlaybackStatus(true);
            }
        } else {
            if (view != null) {
                view.showError("Song not found: " + title);
            }
        }
    }
    
    /**
     * Login a user
     * 
     * @param username Username
     * @param password Password
     * @return The logged in user or null if login failed
     */
    public User login(String name, String password) {
        return UserList.getInstance().authenticateUser(name, password);
    }
    
    /**
     * Create a new user account
     * 
     * @param firstName First name
     * @param lastName Last name
     * @param username Username
     * @param email Email
     * @param password Password
     * @param role Role
     * @param theme Theme
     * @param highlightColor Highlight color
     * @return true if account was created, false if username already exists
     */
    public boolean createAccount(String firstName, String lastName, String username,
                               String email, String password, String role,
                               String theme, String highlightColor) {
        return UserList.getInstance().addUser(firstName, lastName, username,
                                             email, password, role, theme, highlightColor);
    }
    
    /**
     * Log out the current user
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Search for songs
     * 
     * @param query The search query
     * @return List of matching songs
     */
    public List<Song> searchSongs(String query) {
        return SongDatabase.getInstance().searchSongs(query);
    }
    
    /**
     * Search for songs by composer
     * 
     * @param composer The composer name
     * @return List of matching songs
     */
    public List<Song> searchSongsByComposer(String composer) {
        List<Song> allSongs = SongDatabase.getInstance().getSongs();
        List<Song> result = new ArrayList<>();
        
        for (Song song : allSongs) {
            if (song.getComposer().toLowerCase().contains(composer.toLowerCase())) {
                result.add(song);
            }
        }
        
        return result;
    }
    
    /**
     * Find a song by title
     * 
     * @param title The exact song title
     * @return The song or null if not found
     */
    public Song findSongByTitle(String title) {
        List<Song> allSongs = SongDatabase.getInstance().getSongs();
        
        for (Song song : allSongs) {
            if (song.getTitle().equals(title)) {
                return song;
            }
        }
        
        return null;
    }
    
    /**
     * Save a song
     * 
     * @param song The song to save
     */
    public void saveSong(Song song) {
        SongDatabase.getInstance().saveSong(song);
    }
    
    /**
     * Save all songs to the JSON file
     * 
     * @param songs List of songs to save
     */
    public void saveSongs(List<Song> songs) {
        for (Song song : songs) {
            SongDatabase.getInstance().saveSong(song);
        }
        // In a real implementation, we would save to JSON file here
        DataWriter.saveSongs(songs);
    }
    
    /**
     * Delete a song
     * 
     * @param song The song to delete
     */
    public void deleteSong(Song song) {
        SongDatabase.getInstance().deleteSong(song);
    }
    
    /**
     * Adjust playback speed
     * 
     * @param speed Speed percentage (100 = normal)
     */
    public void adjustPlaybackSpeed(int speed) {
        playbackEngine.adjustSpeed(speed);
    }
    
    /**
     * Set metronome speed
     * 
     * @param bpm Beats per minute
     */
    public void setMetronomeSpeed(int bpm) {
        playbackEngine.setMetronomeSpeed(bpm);
    }
    
    /**
     * Create a new song
     * 
     * @param title Song title
     * @param composer Composer name
     * @param tempo Tempo in BPM
     * @param keySignature Key signature
     * @param numerator Time signature numerator
     * @param denominator Time signature denominator
     * @return The created song
     */
    public Song createSong(String title, String composer, int tempo, 
                          String keySignature, int numerator, int denominator) {
        return SongDatabase.getInstance().createSong(title, composer, tempo, 
                                                    keySignature, numerator, denominator);
    }

    /**
     * Create a song with genre
     * 
     * @param title Song title
     * @param composer Composer name
     * @param genre Song genre
     * @param tempo Tempo in BPM
     * @param keySignature Key signature
     * @param numerator Time signature numerator
     * @param denominator Time signature denominator
     * @return The created song
     */
    public Song createSong(String title, String composer, String genre, int tempo, 
                          String keySignature, int numerator, int denominator) {
        return SongDatabase.getInstance().createSong(title, composer, genre, tempo, 
                                                    keySignature, numerator, denominator);
    }
    
    /**
     * Creates a new composition
     * 
     * @param composition The composition to create
     */
    public void createComposition(Composition composition) {
        Music.createComposition(composition);
    }

    /**
     * Creates a new lesson for a student
     * 
     * @param lesson The lesson to create
     * @param student The student taking the lesson
     */
    public void createLesson(Lesson lesson, Student student) {
        if (currentUser != null && "teacher".equals(currentUser.getRole())) {
            Teacher teacher = (Teacher) currentUser;
            teacher.createAndAssignLesson(lesson, student);
        }
    }

    /**
     * Gets all lessons for a user
     * 
     * @param user The user to retrieve lessons for
     * @return List of lessons for the user
     */
    public List<Lesson> getLessonForUser(User user) {
        if (user instanceof Student) {
            return ((Student) user).getAllLessons();
        }
        return new ArrayList<>();  // Return empty list for non-student users
    }
}
