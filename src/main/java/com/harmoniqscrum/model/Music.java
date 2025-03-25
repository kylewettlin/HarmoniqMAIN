package com.harmoniqscrum.model;

import org.jfugue.player.Player;
import org.jfugue.pattern.Pattern;

/**
 * Class for basic music operations using JFugue
 */
public class Music {
    private static Player player = new Player();
    
    /**
     * Plays a single musical note
     * 
     * @param note The note to play (e.g., "C", "D", "E", etc.)
     */
    public void playNote(String note) {
        Note n = new Note(note);
        n.play();
    }
    
    /**
     * Play a sequence of notes
     * 
     * @param notes Array of note names to play
     * @param duration Duration for each note
     */
    public static void playNotes(String[] notes, double duration) {
        Pattern pattern = new Pattern();
        
        for (String note : notes) {
            Note n = new Note(note);
            n.setDuration(duration);
            pattern.add(n.toPattern());
        }
        
        player.play(pattern);
    }
    
    /**
     * Play a chord
     * 
     * @param root Root note of the chord
     * @param quality Chord quality (MAJ, MIN, etc.)
     */
    public static void playChord(String root, String quality) {
        Chords chord = new Chords(root, quality);
        chord.play();
    }
    
    /**
     * Play a scale
     * 
     * @param key The key of the scale
     * @param octaves Number of octaves to play
     */
    public static void playScale(String key, int octaves) {
        // Format for a major scale in JFugue: "C D E F G A B"
        // Create a pattern with the scale
        StringBuilder scale = new StringBuilder();
        String[] majorScale = {"C", "D", "E", "F", "G", "A", "B"};
        
        for (int octave = 5; octave < 5 + octaves; octave++) {
            for (String note : majorScale) {
                scale.append(note).append(octave).append("q ");
            }
        }
        
        player.play(scale.toString());
    }
    
    /**
     * Creates and saves a new composition
     * 
     * @param composition The composition to create and save
     */
    public static void createComposition(Composition composition) {
        // Future implementation will handle the actual creation logic
        // For now, just save the composition
        composition.save();
    }
} 