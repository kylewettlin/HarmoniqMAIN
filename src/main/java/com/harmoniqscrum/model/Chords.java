package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.List;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;

/**
 * Represents musical chords with JFugue integration
 */
public class Chords {
    private String root;
    private String quality;
    private double duration;
    private int volume;
    private List<Note> notes;
    
    /**
     * Default constructor
     */
    public Chords() {
        this.root = "C";
        this.quality = "MAJ";
        this.duration = 1.0; // quarter note
        this.volume = 100;
        this.notes = new ArrayList<>();
    }
    
    /**
     * Constructor with root and quality
     * 
     * @param root Root note (e.g., "C", "D")
     * @param quality Chord quality (e.g., "MAJ", "MIN", "DIM")
     */
    public Chords(String root, String quality) {
        this();
        this.root = root;
        this.quality = quality;
    }
    
    /**
     * Full constructor
     * 
     * @param root Root note
     * @param quality Chord quality
     * @param duration Duration in beats
     * @param volume Volume (0-127)
     */
    public Chords(String root, String quality, double duration, int volume) {
        this(root, quality);
        this.duration = duration;
        this.volume = volume;
    }
    
    /**
     * Play this chord using JFugue
     */
    public void play() {
        Player player = new Player();
        player.play(toPattern());
    }
    
    /**
     * Convert this chord to a JFugue Pattern
     * 
     * @return JFugue Pattern representing this chord
     */
    public Pattern toPattern() {
        StringBuilder sb = new StringBuilder();
        
        // Format is Root+Quality+Duration
        sb.append(root).append(quality);
        
        // Add duration if not default
        if (duration != 1.0) {
            sb.append("/").append(duration);
        }
        
        // Add volume if not default
        if (volume != 100) {
            sb.append(" a").append(volume);
        }
        
        return new Pattern(sb.toString());
    }
    
    /**
     * Create a chord progression
     * 
     * @param chords List of chords in the progression
     * @param key The key for the progression
     * @return A pattern containing the chord progression
     */
    public static Pattern createProgression(List<Chords> chords, String key) {
        StringBuilder progression = new StringBuilder();
        
        // Build a chord progression string like "I IV V"
        for (Chords chord : chords) {
            progression.append(chord.root).append(chord.quality).append(" ");
        }
        
        // Create JFugue chord progression
        ChordProgression cp = new ChordProgression(progression.toString().trim());
        
        // Set the key
        cp.setKey(key);
        
        // Return the pattern
        return cp.getPattern();
    }
    
    /**
     * Add a note to this chord
     * 
     * @param note Note to add
     */
    public void addNote(Note note) {
        notes.add(note);
    }
    
    /**
     * Remove a note from this chord
     * 
     * @param index Index of the note to remove
     */
    public void removeNote(int index) {
        if (index >= 0 && index < notes.size()) {
            notes.remove(index);
        }
    }
    
    // Getters and setters
    
    public String getRoot() {
        return root;
    }
    
    public void setRoot(String root) {
        this.root = root;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public void setQuality(String quality) {
        this.quality = quality;
    }
    
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public void setVolume(int volume) {
        this.volume = volume;
    }
}
