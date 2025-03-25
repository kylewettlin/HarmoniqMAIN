package com.harmoniqscrum.model;

import java.util.concurrent.TimeUnit;

public class MusicPlayer {
    
    public void playSong(Song song) {
        System.out.println("Playing: " + song.getTitle() + " by " + song.getComposer());
        song.play();
    }
} 