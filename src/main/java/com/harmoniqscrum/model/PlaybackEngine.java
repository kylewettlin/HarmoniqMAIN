package com.harmoniqscrum.model;

import java.util.ArrayList;

public class PlaybackEngine {
    private int speed;
    private Song song;
    private ArrayList<Integer> playbackSpeed;
    private boolean isLooping;
    private int metronomeSpeed;
    
    public PlaybackEngine() {
        this.speed = 100; // Default speed
        this.playbackSpeed = new ArrayList<>();
        this.isLooping = false;
        this.metronomeSpeed = 120; // Default BPM
    }
    
    public PlaybackEngine(Song song) {
        this();
        this.song = song;
    }
    
    public void setSong(Song song) {
        this.song = song;
    }
    
    public Song getSong() {
        return song;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public boolean isLooping() {
        return isLooping;
    }
    
    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    public void adjustSpeed(int speed){
        this.speed = speed;
    }
    
    public void setMetronomeSpeed(int bpm){
        this.metronomeSpeed = bpm;
    }
    
    public int getMetronomeSpeed() {
        return metronomeSpeed;
    }
    
    public void playMetronome(){
        // Metronome playback implementation
    }
    
    public void loopPlayback(int start, int end){
        this.isLooping = true;
        // Implement loop playback logic
    }
    
    public void pause(){
        // Pause playback implementation
    }

    public void shutdown(){
        // Shutdown playback implementation
    }
    
    /**
     * Play a song
     * 
     * @param song The song to play
     */
    public void play(Song song){
        this.song = song;
        // Implement song playback logic
        System.out.println("Playing: " + song.getTitle());
    }
}
