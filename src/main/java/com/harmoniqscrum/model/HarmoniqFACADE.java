package com.harmoniqscrum.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.harmoniqscrum.model.view.HarmoniqView;

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
        // Instead of launching JavaFX, directly run the usage scenarios
        HarmoniqFACADE facade = getInstance();
        facade.runScenarios();
    }
    

    public void runScenarios() {
        
        // Scenario 1 Account Creation and Login
        //Scenario1();
        
        // Scenario 2 Playing a Song
        //Scenario2();
        
        // Scenario 3 Making a Song
        Scenario3();
        
    }
    

    private void Scenario1() {
        System.out.println("\n---------- SCENARIO 1: CREATE ACCOUNT AND LOGIN ----------\n");
        
        UserList userList = UserList.getInstance();
        User fred = userList.getUser("ffred");
        if (fred == null) {
            System.out.println("Fred is not yet in the users.json file.");
        } else {
            System.out.println("Fred is already in the users.json file. Removing for demo purposes.");
            userList.removeUser(fred);
            userList.saveUsers();
        }
        
        // Fred attempts to create an account with the same username as Fellicia
        System.out.println("\nFred attempts to create an account with username 'ffredrickson':");
        boolean accountCreated = userList.addUser("Fred", "Fredrickson", "ffredrickson", 
                                                 "fred@example.com", "pass456", "student", "light", "green");
        
        if (!accountCreated) {
            System.out.println("Account creation failed: Username 'ffredrickson' is already taken.");
            
            // Fred tries again with a different username
            System.out.println("\nFred tries again with username 'ffred':");
            accountCreated = userList.addUser("Fred", "Fredrickson", "ffred", 
                                             "fred@example.com", "pass456", "student", "light", "green");
            
            if (accountCreated) {
                System.out.println("Account created successfully for Fred with username 'ffred'.");
                userList.saveUsers();
            } else {
                System.out.println("Account creation failed again.");
            }
        }
        
        // Fred logs out
        System.out.println("\nFred logs out of the system.");
        this.currentUser = null;
        
        // Show the users.json file
        System.out.println("\nContent of users.json now contains Fred:");
        showUsers();
        
        // Fred logs in
        System.out.println("\nFred logs in with username 'ffred' and password 'pass456':");
        User loggedInUser = login("ffred", "pass456");
        
        if (loggedInUser != null) {
            System.out.println("Login successful for " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
            this.currentUser = loggedInUser;
        } else {
            System.out.println("Login failed.");
        }
    }
    
    /**
     * Run the playing song scenario
     */
    private void Scenario2() {
        System.out.println("\n---------- SCENARIO 2: PLAYING A SONG ----------\n");
        
        // Ensure Fred is still logged in
        if (currentUser == null || !currentUser.getUsername().equals("ffred")) {
            System.out.println("Logging in as Fred first...");
            currentUser = login("ffred", "pass456");
        }
        
        // Get all songs from the database
        List<Song> allSongs = SongDatabase.getInstance().getSongs();
        
        // If no songs exist, create some demo songs
        if (allSongs.isEmpty()) {
            System.out.println("No songs found in the database. Creating demo songs.");
            createDemoSongs();
            allSongs = SongDatabase.getInstance().getSongs();
        }
        
        // Get the first composer or use "Unknown" if none exists
        String composer = "Unknown";
        if (!allSongs.isEmpty() && allSongs.get(0).getComposer() != null && !allSongs.get(0).getComposer().isEmpty()) {
            composer = allSongs.get(0).getComposer();
        }
        
        System.out.println("Fred searches for all songs by '" + composer + "':");
        List<Song> composerSongs = searchSongsByComposer(composer);
        
        // Display the songs
        System.out.println("\nSearch results for '" + composer + "':");
        for (Song song : composerSongs) {
            System.out.println("- " + song.getTitle());
        }
        
        // If we have songs, Fred selects the first one to play
        if (!composerSongs.isEmpty()) {
            Song selectedSong = composerSongs.get(0);
            System.out.println("\nFred selects '" + selectedSong.getTitle() + "' to play:");
            playbackEngine.play(selectedSong);
            
            // Fred exports the sheet music to a text file
            System.out.println("\nFred exports the sheet music to a text file:");
            exportSheetMusic(selectedSong, selectedSong.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "_Sheet_Music.txt");
            System.out.println("Sheet music exported successfully to '" + 
                              selectedSong.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "_Sheet_Music.txt'");
        } else {
            System.out.println("No songs by " + composer + " found. Creating a demo song.");
            Song demoSong = createDemoSong("Demo Song", composer);
            System.out.println("\nFred selects '" + demoSong.getTitle() + "' to play:");
            playbackEngine.play(demoSong);
            
            // Fred exports the sheet music to a text file
            System.out.println("\nFred exports the sheet music to a text file:");
            exportSheetMusic(demoSong, "Demo_Song_Sheet_Music.txt");
            System.out.println("Sheet music exported successfully to 'Demo_Song_Sheet_Music.txt'");
        }
    }
    
    /**
     * Run the song creation scenario
     */
    private void Scenario3() {
        System.out.println("\n---------- SCENARIO 3: MAKING A SONG ----------\n");
        
        // Fred logs out
        System.out.println("Fred logs out of the system.");
        this.currentUser = null;
        
        // Show Fellicia's entry in users.json
        System.out.println("\nFellicia's entry in the users.json file:");
        User fellicia = UserList.getInstance().getUser("ffredrickson");
        if (fellicia != null) {
            System.out.println("Name: " + fellicia.getFirstName() + " " + fellicia.getLastName());
            System.out.println("Username: " + fellicia.getUsername());
            System.out.println("Role: " + fellicia.getRole());
        } else {
            System.out.println("Fellicia not found in the system. Creating her account for the demo.");
            UserList.getInstance().addUser("Fellicia", "Fredrickson", "ffredrickson", "fellicia@example.com", 
                                         "password123", "student", "dark", "blue");
            UserList.getInstance().saveUsers();
            fellicia = UserList.getInstance().getUser("ffredrickson");
        }
        
        // Fellicia logs in
        System.out.println("\nFellicia logs in with username 'ffredrickson' and password 'password123':");
        fellicia = login("ffredrickson", "password123");
        
        if (fellicia != null) {
            System.out.println("Login successful for " + fellicia.getFirstName() + " " + fellicia.getLastName());
            this.currentUser = fellicia;
            
            // Create a unique song title to avoid duplicates
            String songTitle = "A horses journey " + System.currentTimeMillis();
            
            // Fellicia creates a new song
            System.out.println("\nFellicia creates a new song called '" + songTitle + "':");
            Song horsesJourney = new Song(songTitle, fellicia.getFirstName() + " " + fellicia.getLastName());
            horsesJourney.setTempo(90);
            horsesJourney.setKeySignature("C Major");
            horsesJourney.setSignature(4, 4);
            
            // Add genre
            List<String> genres = new ArrayList<>();
            genres.add("Classical");
            horsesJourney.setGenres(genres);
            
            // Add measures with notes
            System.out.println("\nFellicia adds 2 measures with notes:");
            
            // First measure
            System.out.println("Adding first measure with notes: C, E, G, C");
            Note c1 = new Note("C");
            c1.setDuration(1.0); // quarter note
            horsesJourney.addNote(c1);
            
            Note e1 = new Note("E");
            e1.setDuration(1.0);
            horsesJourney.addNote(e1);
            
            Note g1 = new Note("G");
            g1.setDuration(1.0);
            horsesJourney.addNote(g1);
            
            Note c2 = new Note("C");
            c2.setOctave(6);
            c2.setDuration(1.0);
            horsesJourney.addNote(c2);
            
            // Second measure
            System.out.println("Adding second measure with notes: A, F, D, A");
            Note a1 = new Note("A");
            a1.setDuration(1.0);
            horsesJourney.addNote(a1);
            
            Note f1 = new Note("F");
            f1.setDuration(1.0);
            horsesJourney.addNote(f1);
            
            Note d1 = new Note("D");
            d1.setDuration(1.0);
            horsesJourney.addNote(d1);
            
            Note a2 = new Note("A");
            a2.setOctave(5);
            a2.setDuration(1.0);
            horsesJourney.addNote(a2);
            
            // Now save the song after adding all notes
            SongDatabase.getInstance().saveSong(horsesJourney);
            
            // Play the song
            System.out.println("\nFellicia plays her new song:");
            horsesJourney.play();
            
            // Save all songs to JSON
            SongDatabase songDb = SongDatabase.getInstance();
            saveSongs(songDb.getSongs());
            
            // Fellicia logs out
            System.out.println("\nFellicia logs out of the system.");
            this.currentUser = null;
            
            // Show updates to the json files
            System.out.println("\nUpdates to users.json and songs.json:");
            showUsers();
            showSongs();
            
            // Fred logs in and plays Fellicia's song
            System.out.println("\nFred logs in and searches for '" + songTitle + "':");
            User fred = login("ffred", "pass456");
            
            if (fred != null) {
                System.out.println("Login successful for " + fred.getFirstName() + " " + fred.getLastName());
                this.currentUser = fred;
                
                List<Song> searchResults = searchSongs(songTitle);
                
                if (!searchResults.isEmpty()) {
                    System.out.println("Fred found Fellicia's song and is playing it:");
                    Song song = searchResults.get(0);
                    playbackEngine.play(song);
                } else {
                    System.out.println("Fred couldn't find Fellicia's song.");
                }
                
                // Fred logs out
                System.out.println("\nFred logs out of the system.");
                this.currentUser = null;
            } else {
                System.out.println("Fred couldn't log in.");
            }
        } else {
            System.out.println("Login failed for Fellicia.");
        }
    }
    
    /**
     * Load songs from JSON and play the first one
     */
    public void loadAndPlaySongs() {
        System.out.println("\nLoading songs from JSON...");
        
        // Get the song database instance (which loads songs via DataLoader)
        SongDatabase songDb = SongDatabase.getInstance();
        
        // Print available songs
        System.out.println("\nAvailable songs:");
        if (songDb.getSongs().isEmpty()) {
            System.out.println("No songs were loaded from the database.");
            return;
        }
        
        for (Song song : songDb.getSongs()) {
            System.out.println("- " + song.getTitle() + " by " + song.getComposer());
        }
        
        // Get first song
        Song firstSong = songDb.getSongs().get(0);
        
        // Play the song
        System.out.println("\nPlaying: " + firstSong.getTitle() + " by " + firstSong.getComposer());
        playbackEngine.play(firstSong);
    }
    
    /**
     * JavaFX start method
     * 
     * @param stage The primary stage
     */
    @Override
    public void start(Stage stage) {
        instance = this;
        
        // Skip UI initialization for now - just load and play songs
        loadAndPlaySongs();
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
     * Export the sheet music for a song to a text file
     * 
     * @param song The song to export
     * @param filename The output file name
     */
    public void exportSheetMusic(Song song, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            
            // Write header information
            writer.write("==============================================\n");
            writer.write(song.getTitle() + "\n");
            writer.write("Composed by: " + song.getComposer() + "\n");
            writer.write("Key: " + song.getKeySignature() + "\n");
            writer.write("Time: " + song.getTimeSignature().toString() + "\n");
            writer.write("Tempo: " + song.getTempo() + " BPM\n");
            writer.write("==============================================\n\n");
            
            // Write notes
            writer.write("Notes:\n");
            
            // Count measures based on time signature
            int notesPerMeasure = song.getTimeSignature().getNumerator();
            int currentMeasure = 1;
            int noteInMeasure = 0;
            
            for (int i = 0; i < song.getPattern().toString().split(" ").length; i++) {
                String noteStr = "?";
                String durationStr = "quarter";
                
                if (i < song.getPattern().toString().split(" ").length) {
                    noteStr = song.getPattern().toString().split(" ")[i];
                    
                    // Parse duration
                    if (noteStr.contains("q")) durationStr = "quarter";
                    else if (noteStr.contains("h")) durationStr = "half";
                    else if (noteStr.contains("w")) durationStr = "whole";
                    else if (noteStr.contains("i")) durationStr = "eighth";
                    else if (noteStr.contains("s")) durationStr = "sixteenth";
                }
                
                // Remove duration and other modifiers from note display
                noteStr = noteStr.replaceAll("[qhwis].*", "");
                
                if (noteInMeasure == 0) {
                    writer.write("\nMeasure " + currentMeasure + ":\n");
                }
                
                writer.write("  " + noteStr + " (" + durationStr + ")\n");
                
                noteInMeasure++;
                if (noteInMeasure >= notesPerMeasure) {
                    noteInMeasure = 0;
                    currentMeasure++;
                }
            }
            
            writer.close();
            System.out.println("Sheet music exported to file: " + filename);
            
        } catch (IOException e) {
            System.err.println("Error exporting sheet music: " + e.getMessage());
            e.printStackTrace();
        }
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
     * Create demo songs for testing
     */
    private void createDemoSongs() {
        SongDatabase songDb = SongDatabase.getInstance();
        
        // Create generic demo songs
        Song song1 = createDemoSong("Symphony No. 5", "Beethoven");
        Song song2 = createDemoSong("Four Seasons", "Vivaldi");
        Song song3 = createDemoSong("Moonlight Sonata", "Beethoven");
        
        // Save songs
        saveSongs(songDb.getSongs());
        
        System.out.println("Created demo songs");
    }
    
    /**
     * Helper method to create a single demo song with some notes
     */
    private Song createDemoSong(String title, String composer) {
        SongDatabase songDb = SongDatabase.getInstance();
        
        // Create song
        Song song = songDb.createSong(title, composer, 100, "C Major", 4, 4);
        
        // Add some basic notes
        Note c = new Note("C");
        c.setDuration(2.0); // half note
        song.addNote(c);
        
        Note e = new Note("E");
        e.setDuration(2.0);
        song.addNote(e);
        
        Note g = new Note("G");
        g.setDuration(4.0); // whole note
        song.addNote(g);
        
        // Save song
        songDb.saveSong(song);
        
        System.out.println("Created demo song: " + title + " by " + composer);
        return song;
    }
    
    /**
     * Show all users in the system
     */
    private void showUsers() {
        System.out.println("Current users in the system:");
        for (User user : UserList.getInstance().getUsers()) {
            System.out.println("- " + user.getFirstName() + " " + user.getLastName() + 
                             " (Username: " + user.getUsername() + ", Role: " + user.getRole() + ")");
        }
    }
    
    /**
     * Show all songs in the system
     */
    private void showSongs() {
        System.out.println("Current songs in the system:");
        for (Song song : SongDatabase.getInstance().getSongs()) {
            System.out.println("- " + song.getTitle() + " by " + song.getComposer());
        }
    }
}
