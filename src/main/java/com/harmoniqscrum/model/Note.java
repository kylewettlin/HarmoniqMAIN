package com.harmoniqscrum.model;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

/**
 * Represents a musical note with JFugue integration and guitar-specific functionality
 */
public class Note {
    private String pitch;      // Note pitch (e.g., "C", "D", "E")
    private double duration;   // Note duration in beats
    private int volume;        // Note volume (0-127 MIDI scale)
    private int octave;        // Octave
    
    // Guitar-specific fields
    private int string;        // Guitar string number (0-5)
    private int fret;         // Guitar fret number
    
    // Constants for default values
    private static final int DEFAULT_VOLUME = 100;
    private static final int DEFAULT_OCTAVE = 5;
    private static final double DEFAULT_DURATION = 0.25; // Sixteenth note
    
    // Guitar-specific constants
    private static final String[] NOTES = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private static final String[] TUNING = {"E", "B", "G", "D", "A", "E"}; // Standard guitar tuning
    private static final int[] TUNING_OCT = {2, 2, 1, 1, 1, 0}; // Octaves for each string
    
    /**
     * Default constructor
     */
    public Note() {
        this.pitch = "C";
        this.duration = DEFAULT_DURATION;
        this.volume = DEFAULT_VOLUME;
        this.octave = DEFAULT_OCTAVE;
    }
    
    /**
     * Constructor with pitch
     * 
     * @param pitch The pitch of the note (e.g. "C", "D#")
     */
    public Note(String pitch) {
        this.pitch = pitch.toUpperCase();
        this.duration = DEFAULT_DURATION;
        this.volume = DEFAULT_VOLUME;
        this.octave = DEFAULT_OCTAVE;
    }
    
    /**
     * Constructor for guitar-specific note creation
     * 
     * @param string Guitar string number (0-5)
     * @param fret Fret number
     */
    public Note(int string, int fret) {
        this.string = string;
        this.fret = fret;
        this.duration = DEFAULT_DURATION;
        this.volume = DEFAULT_VOLUME;
        calculatePitchFromFret();
    }
    
    /**
     * Full constructor
     * 
     * @param pitch The pitch of the note
     * @param duration The duration in beats
     * @param volume The volume (0-127)
     * @param octave The octave (0-10)
     */
    public Note(String pitch, double duration, int volume, int octave) {
        this.pitch = pitch.toUpperCase();
        this.duration = duration;
        this.volume = validateVolume(volume);
        this.octave = octave;
    }
    
    /**
     * Calculate pitch and octave based on guitar string and fret
     */
    private void calculatePitchFromFret() {
        octave = TUNING_OCT[string];
        String stringNote = TUNING[string];
        int startIndex = 0;
        
        // Find the starting note index
        for (int j = 0; j < NOTES.length; j++) {
            if (NOTES[j].equals(stringNote)) {
                startIndex = j;
                break;
            }
        }
        
        // Calculate the resulting note and octave
        int noteIndex = startIndex;
        for (int i = 0; i < fret + 1; i++) {
            noteIndex++;
            if (noteIndex == NOTES.length) {
                octave++;
                noteIndex = 0;
            }
        }
        
        this.pitch = NOTES[noteIndex];
    }
    
    /**
     * Play this note using JFugue
     */
    public void play() {
        Player player = new Player();
        player.play(toPattern());
    }
    
    /**
     * Convert this note to a JFugue Pattern
     * 
     * @return A JFugue Pattern representing this note
     */
    public Pattern toPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch);
        sb.append(octave);
        sb.append(getDurationString());
        
        if (volume != DEFAULT_VOLUME) {
            sb.append(" a").append(volume);
        }
        
        return new Pattern(sb.toString());
    }
    
    /**
     * Get a display string for the note including octave
     * 
     * @return String representation of the note with octave
     */
    public String getPitchDisplay() {
        return pitch + ", " + octave + " octave";
    }
    
    /**
     * Convert the duration to a JFugue duration string
     * 
     * @return JFugue duration string
     */
    private String getDurationString() {
        if (duration == 1.0) {
            return "q"; // quarter note
        } else if (duration == 0.5) {
            return "i"; // eighth note
        } else if (duration == 0.25) {
            return "s"; // sixteenth note
        } else if (duration == 2.0) {
            return "h"; // half note
        } else if (duration == 4.0) {
            return "w"; // whole note
        } else {
            // Custom duration
            return "/" + String.valueOf(duration);
        }
    }
    
    /**
     * Validate and adjust volume to be within MIDI range (0-127)
     * 
     * @param volume Input volume
     * @return Adjusted volume within valid range
     */
    private int validateVolume(int volume) {
        if (volume < 0) {
            return 0;
        } else if (volume > 127) {
            return 127;
        }
        return volume;
    }
    
    // Getters and setters
    
    public String getPitch() {
        return pitch;
    }
    
    public void setPitch(String pitch) {
        this.pitch = pitch.toUpperCase();
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
        this.volume = validateVolume(volume);
    }
    
    public int getOctave() {
        return octave;
    }
    
    public void setOctave(int octave) {
        this.octave = octave;
    }
    
    public int getString() {
        return string;
    }
    
    public void setString(int string) {
        this.string = string;
        if (fret >= 0) {
            calculatePitchFromFret();
        }
    }
    
    public int getFret() {
        return fret;
    }
    
    public void setFret(int fret) {
        this.fret = fret;
        if (string >= 0) {
            calculatePitchFromFret();
        }
    }
}
