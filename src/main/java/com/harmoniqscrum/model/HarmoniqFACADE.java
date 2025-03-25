package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.stage.Stage;

import com.harmoniqscrum.model.view.HarmoniqView;

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
        launch(args);
    }
    
    /**
     * JavaFX start method
     * 
     * @param stage The primary stage
     */
    @Override
    public void start(Stage stage) {
        instance = this;
        view = new HarmoniqView(stage, this);
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
    public User login(String username, String password) {
        UserList userList = UserList.getInstance();
        User user = userList.authenticateUser(username, password);
        
        if (user != null) {
            currentUser = user;
            return user;
        }
        return null;
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
     * Save a song
     * 
     * @param song The song to save
     */
    public void saveSong(Song song) {
        SongDatabase.getInstance().saveSong(song);
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
     */
    public void createSong(String title, String composer, String genre, int tempo, 
    String keySignature, int numerator, int denominator) {
        SongDatabase.getInstance().createSong(title, composer, genre, tempo, 
                keySignature, numerator, denominator);
    }
}
