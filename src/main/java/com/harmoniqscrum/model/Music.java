package com.harmoniqscrum.model;

public class Music {
    
    /**
     * Plays a single musical note
     * 
     * @param note The note to play (e.g., "C", "D", "E", etc.)
     */
    public void playNote(String note) {
        ToneGenerator.play(note, 90); 
    }
} 