package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing song database operations
 */
public class SongDatabase {
    private static SongDatabase instance;
    private List<Song> songs;
    
    private SongDatabase() {
        this.songs = new ArrayList<>();
        loadSongs();
    }
    
    /**
     * Get singleton instance
     */
    public static SongDatabase getInstance() {
        if (instance == null) {
            instance = new SongDatabase();
        }
        return instance;
    }
    
    public List<Song> getSongs() {
        return songs;
    }
    
    public void addSong(Song song) {
        songs.add(song);
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
    }
    
    /**
     * Load songs from data storage
     */
    private void loadSongs() {
        // Load songs from JSON file using DataLoader
        songs = DataLoader.getSongs();
        System.out.println("Loaded " + songs.size() + " songs from JSON file");
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
        song.setKeySignature(keySignature);
        song.setSignature(numerator, denominator);
        saveSong(song);
        return song;
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
        return song;
    }
}