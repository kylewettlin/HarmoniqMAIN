package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.view.HarmoniqView;
// Import Lesson model when created
// import com.harmoniqscrum.model.Lesson;
import com.harmoniqscrum.model.Song;
import java.util.List;
import java.util.ArrayList;

public class LessonsController {

    private HarmoniqFACADE facade;
    private HarmoniqView view; // Need view reference to update grid
    private List<Song> allAssignedSongs; // Store the full list

    public LessonsController(HarmoniqFACADE facade, HarmoniqView view) {
        this.facade = facade;
        this.view = view;
        // Fetch and store the full list upon initialization
        fetchAllAssignedSongs(); 
    }

    // Helper to fetch and store all assigned songs
    private void fetchAllAssignedSongs() {
        System.out.println("LessonsController: Fetching and caching assigned lesson songs...");
        this.allAssignedSongs = facade.getAssignedLessonSongsForCurrentUser();
        if (this.allAssignedSongs == null) {
             this.allAssignedSongs = new ArrayList<>(); // Ensure it's not null
             System.err.println("Warning: Facade returned null for assigned songs.");
         }
    }

    /**
     * Retrieves the list of songs assigned as lessons for the current user.
     * Now returns the cached list.
     * 
     * @return A list of Song objects assigned as lessons.
     */
    public List<Song> getAssignedSongs() { 
        // Return the cached list. Fetch happens in constructor/refresh.
        if (this.allAssignedSongs == null) {
             // Should not happen if constructor logic works, but as fallback:
             fetchAllAssignedSongs(); 
         }
         return this.allAssignedSongs;
    }
    
    /**
     * Handles the search request from the view for lessons.
     * Filters the cached list of assigned songs.
     * @param query The search query entered by the user.
     */
    public void handleLessonSearch(String query) {
        System.out.println("Searching assigned lessons for query: " + query);
        List<Song> filteredResults;
        
        if (query == null || query.trim().isEmpty()) {
             // If query is empty, show all assigned songs
             filteredResults = this.allAssignedSongs;
         } else {
             // Otherwise, filter the cached list
             String lowerCaseQuery = query.toLowerCase();
             filteredResults = new ArrayList<>();
             if (this.allAssignedSongs != null) {
                 for (Song song : this.allAssignedSongs) {
                     // Search by title containing the query (case-insensitive)
                     if (song.getTitle() != null && song.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                         filteredResults.add(song);
                     }
                     // TODO: Add search by composer or other fields if needed
                 }
             }
         }
         
         // Update the view with the filtered results
         if (view != null) {
             view.updateLessonGrid(filteredResults);
         } else {
             System.err.println("LessonsController: View is null, cannot update lesson grid.");
         }
    }

} 