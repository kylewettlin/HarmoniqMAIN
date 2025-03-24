package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing song database operations
 */
public class SongDatabase {
    private List<Song> songs;
    
    public SongDatabase() {
        this.songs = new ArrayList<>();
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
}