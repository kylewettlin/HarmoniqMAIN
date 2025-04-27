package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.Song;
import com.harmoniqscrum.model.User;
import java.util.List;
import com.harmoniqscrum.model.view.HarmoniqView;
import javafx.scene.control.Alert;

public class DashboardController {

    private HarmoniqFACADE facade;
    private HarmoniqView view;

    public DashboardController(HarmoniqFACADE facade, HarmoniqView view) {
        this.facade = facade;
        this.view = view;
    }

    /**
     * Retrieves the list of all songs from the facade/database.
     * @return A list of Song objects.
     */
    public List<Song> getSongs() {
        // Using searchSongs with an empty query to get all songs, 
        // assuming this behavior is implemented or defaulting to getSongs().
        // Adjust if necessary based on HarmoniqFACADE implementation.
        return facade.searchSongs(""); 
        // Alternative: return SongDatabase.getInstance().getSongs(); // If direct access is preferred/needed
    }
    
    /**
     * Requests the facade to play the specified song.
     * @param song The song to play.
     */
    public void playSong(Song song) {
        if (song != null) {
            System.out.println("Controller requesting playback for: " + song.getTitle());
            facade.playSong(song.getTitle()); 
            // Assuming facade.playSong(title) finds the song and plays it.
            // Alternatively, pass the whole song object if the facade requires it:
            // facade.play(song);
        }
    }
    
    /**
     * Requests the facade to delete the specified song.
     * @param song The song to delete.
     */
    public void deleteSong(Song song) {
        if (song != null) {
            System.out.println("Controller requesting deletion for: " + song.getTitle());
            // Call deleteSong via the facade
            facade.deleteSong(song); 
            // Remove direct database call:
            // SongDatabase.getInstance().deleteSong(song);
        }
    }
    
    /**
     * Retrieves the list of all students from the facade.
     * @return A list of student User objects.
     */
    public List<User> getAllStudents() {
         return facade.getAllStudents();
    }
    
    /**
     * Initiates the process of assigning a song as a lesson.
     * Fetches the student list and triggers the view to show the selection popup.
     * @param song The song to be assigned.
     */
    public void handleAssignLessonAttempt(Song song) {
        if (song == null) return;
        
        List<User> students = getAllStudents();
        if (students.isEmpty()) {
            // Show alert if no students exist
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Students Found");
            alert.setHeaderText(null);
            alert.setContentText("There are no students registered in the system to assign lessons to.");
            alert.showAndWait();
        } else {
            // Trigger the view to show the popup
            view.showStudentSelectionPopup(song, students);
        }
    }

    /**
     * Called by the view after a student is selected in the popup.
     * @param song The song being assigned.
     * @param student The selected student.
     * @return true if assignment was successful, false otherwise.
     */
    public boolean assignLessonToStudent(Song song, User student) {
        if (song == null || student == null) return false;
        return facade.assignLesson(song, student);
    }

    /**
     * Handles the search request from the view.
     * @param query The search query entered by the user.
     */
    public void handleSongSearch(String query) {
        System.out.println("Searching songs for query: " + query);
        List<Song> searchResults;
        if (query == null || query.trim().isEmpty()) {
             // If query is empty, get all songs
             searchResults = facade.searchSongs(""); // Or call a specific getAllSongs if available
         } else {
             // Otherwise, perform the search using the facade
             searchResults = facade.searchSongs(query);
         }
         
         // Update the view with the search results
         if (view != null) {
             view.updateSongList(searchResults);
         } else {
             System.err.println("DashboardController: View is null, cannot update song list.");
         }
    }
} 