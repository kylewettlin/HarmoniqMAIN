package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles playback of songs, metronome control, and playback options
 * such as speed, looping, and playback by title.
 */
public class PlaybackEngine {
    /** Current playback speed (100 = normal speed) */
    private int speed;

    /** The current song being played */
    private Song song;

    /** History or presets of playback speeds */
    private ArrayList<Integer> playbackSpeed;

    /** Indicates whether playback is looping */
    private boolean isLooping;

    /** Beats per minute for the metronome */
    private int metronomeSpeed;

    /**
     * Default constructor for PlaybackEngine
     */
    public PlaybackEngine() {
        this.speed = 100;
        this.playbackSpeed = new ArrayList<>();
        this.isLooping = false;
        this.metronomeSpeed = 120;
    }

    /**
     * Constructs a PlaybackEngine with a specified song
     * @param song The song to initialize with
     */
    public PlaybackEngine(Song song) {
        this();
        this.song = song;
    }

    /**
     * Sets the current song
     * @param song The song to play
     */
    public void setSong(Song song) {
        this.song = song;
    }

    /**
     * Gets the current song
     * @return The song currently loaded
     */
    public Song getSong() {
        return song;
    }

    /**
     * Gets the current playback speed
     * @return The playback speed percentage
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Checks if playback is looping
     * @return true if looping is enabled
     */
    public boolean isLooping() {
        return isLooping;
    }

    /**
     * Enables or disables looping playback
     * @param isLooping true to enable loop mode
     */
    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    /**
     * Adjusts the playback speed
     * @param speed New speed percentage
     */
    public void adjustSpeed(int speed){
        this.speed = speed;
    }

    /**
     * Sets the BPM for the metronome
     * @param bpm Beats per minute
     */
    public void setMetronomeSpeed(int bpm){
        this.metronomeSpeed = bpm;
    }

    /**
     * Gets the metronome BPM
     * @return Metronome speed in BPM
     */
    public int getMetronomeSpeed() {
        return metronomeSpeed;
    }

    /**
     * Triggers metronome playback
     */
    public void playMetronome(){
        // Metronome playback implementation
    }

    /**
     * Starts looping playback between two positions
     * @param start Start index
     * @param end End index
     */
    public void loopPlayback(int start, int end){
        this.isLooping = true;
        // Implement loop playback logic
    }

    /**
     * Pauses playback
     */
    public void pause(){
        // Pause playback implementation
    }

    /**
     * Shuts down the playback engine
     */
    public void shutdown(){
        // Shutdown playback implementation
    }

    /**
     * Plays the provided song
     * @param song The song to play
     */
    public void play(Song song){
        this.song = song;
        System.out.println("Playing: " + song.getTitle());

        try {
            song.play();
        } catch (Exception e) {
            System.err.println("Error playing song: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Searches and plays a song by title
     * @param title Title of the song to play
     * @return true if song is found and played, false otherwise
     */
    public boolean playSongByTitle(String title) {
        SongDatabase songDb = SongDatabase.getInstance();
        List<Song> songs = songDb.getSongs();

        for (Song song : songs) {
            if (song.getTitle().equals(title)) {
                play(song);
                return true;
            }
        }

        return false;
    }
}
