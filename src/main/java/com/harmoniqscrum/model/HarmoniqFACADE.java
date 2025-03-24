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
    private Map<String, User> users;
    private User currentUser;
    private List<Song> songs;
    private PlaybackEngine playbackEngine;
    private HarmoniqView view;

    /**
     * Constructor
     */
    public HarmoniqFACADE() {
        users = new HashMap<>();
        songs = new ArrayList<>();
        playbackEngine = new PlaybackEngine();
        
        // Initialize data
        loadUsers();
        loadSongs();
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
     * Load users from data storage
     */
    private void loadUsers() {
        // Load users from UserList
        UserList userList = UserList.getInstance();
        ArrayList<User> userArray = userList.getUsers();
        
        // Populate the users map
        for (User user : userArray) {
            users.put(user.getUsername(), user);
        }
    }
    
    /**
     * Load songs from data storage
     */
    private void loadSongs() {
        // Future implementation will load from Songs.json
        
        // Create default song for testing
        Song marySong = new Song("Mary Had a Little Lamb", "Traditional");
        marySong.setTempo(120);
        
        // Add notes to the song (E D C D E E E, etc.)
        // Mary Had a Little Lamb melody
        String[] notes = {
            "E", "D", "C", "D", "E", "E", "E", 
            "D", "D", "D", 
            "E", "G", "G", 
            "E", "D", "C", "D", "E", "E", "E", "E", "D", "D", "E", "D", "C"
        };
        
        for (String noteName : notes) {
            marySong.addNote(new Note(noteName));
        }
        
        songs.add(marySong);
    }
    
    /**
     * Play a song by title
     * 
     * @param title The title of the song to play
     */
    public void playSong(String title) {
        for (Song song : songs) {
            if (song.getTitle().equals(title)) {
                playbackEngine.play(song);
                if (view != null) {
                    view.updatePlaybackStatus(true);
                }
                return;
            }
        }
        
        if (view != null) {
            view.showError("Song not found: " + title);
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
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
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
        List<Song> results = new ArrayList<>();
        
        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                song.getComposer().toLowerCase().contains(query.toLowerCase())) {
                results.add(song);
            }
        }
        
        return results;
    }
    
    /**
     * Save a song
     * 
     * @param song The song to save
     */
    public void saveSong(Song song) {
        // Check if song already exists
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(song.getTitle())) {
                songs.set(i, song); // Replace existing
                return;
            }
        }
        
        // Add new song
        songs.add(song);
        
        // Future implementation will save to Songs.json
    }
    
    /**
     * Delete a song
     * 
     * @param song The song to delete
     */
    public void deleteSong(Song song) {
        songs.removeIf(s -> s.getTitle().equals(song.getTitle()));
        
        // Future implementation will update Songs.json
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
        Song song = new Song(title, composer);
        song.setTempo(tempo);
        // Set other properties as needed
        saveSong(song);
        return song;
    }

    /**
     * Creates a new composition
     * 
     * @param composition The composition to create
     */
    public void createComposition(Composition composition) {
        // Implementation to be added
    }

    /**
     * Creates a new lesson for a student
     * 
     * @param lesson The lesson to create
     * @param student The student taking the lesson
     */
    public void createLesson(Lesson lesson, Student student) {
        // Implementation to be added
    }

    /**
     * Gets all lessons for a user
     * 
     * @param user The user to retrieve lessons for
     * @return List of lessons for the user
     */
    public List<Lesson> getLessonForUser(User user) {
        // Implementation to be added
        return new ArrayList<>();  // Return empty list for now
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
        Song song = new Song(title, composer);
        song.setTempo(tempo);
        
        // Add genre to the song's genres list
        List<String> genres = new ArrayList<>();
        genres.add(genre);
        song.setGenres(genres);
        
        // Set other properties
        song.setKeySignature(keySignature);
        song.setSignature(numerator, denominator);
        
        saveSong(song);
    }
}
