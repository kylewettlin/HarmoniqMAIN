package com.harmoniqscrum.model;

import java.util.ArrayList;

public class SongPlayer {
    public static void main(String[] args) {
        // Load songs from JSON file
        ArrayList<Song> songs = DataLoader.getSongs();
        
        if (songs.isEmpty()) {
            System.out.println("No songs were loaded from the file.");
            return;
        }
        
        // Get the first song (Sweet Child O' Mine)
        Song song = songs.get(0);
        System.out.println("Playing: " + song.getTitle() + " by " + song.getComposer());
        
        // Create a music player and play the song
        MusicPlayer player = new MusicPlayer();
        player.playSong(song);
    }
} 