package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.Song;
import java.util.List;

public class DashboardController {

    private HarmoniqFACADE facade;

    public DashboardController(HarmoniqFACADE facade) {
        this.facade = facade;
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
    
    // Add methods here later for handling search, song selection etc.
} 