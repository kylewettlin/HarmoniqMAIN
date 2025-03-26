package com.harmoniqscrum.model;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Represents a musical song with JFugue integration
 */
public class Song {
    private UUID songId;
    private String title;
    private String composer;
    private int tempo;
    private List<String> genres;
    private List<String> lyrics;
    private double rating;
    private String keySignature;
    private TimeSignature timeSignature;
    private List<Note> notes;
    private Pattern pattern;

    /**
     * Default constructor
     */
    public Song() {
        this.songId = UUID.randomUUID();
        this.title = "Untitled";
        this.composer = "Unknown";
        this.tempo = 120;
        this.genres = new ArrayList<>();
        this.lyrics = new ArrayList<>();
        this.rating = 0.0;
        this.keySignature = "C";
        this.timeSignature = new TimeSignature(4, 4);
        this.notes = new ArrayList<>();
        this.pattern = new Pattern();
    }

    /**
     * Constructor with basic info
     */
    public Song(String title, String composer) {
        this();
        this.title = title;
        this.composer = composer;
    }

    /**
     * Full constructor with all fields
     */
    public Song(UUID songId, String title, String composer, int tempo, 
               List<String> genres, double rating, String keySignature, 
               TimeSignature timeSignature, List<String> lyrics) {
        this();
        this.songId = songId;
        this.title = title;
        this.composer = composer;
        this.tempo = tempo;
        this.genres = genres;
        this.rating = rating;
        this.keySignature = keySignature;
        this.timeSignature = timeSignature;
        this.lyrics = lyrics;
    }

    /**
     * Represents a time signature with numerator and denominator
     */
    public static class TimeSignature {
        private int numerator;
        private int denominator;

        public TimeSignature(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }

        @Override
        public String toString() {
            return numerator + "/" + denominator;
        }
    }

    /**
     * Play the song using JFugue
     */
    public void play() {
        try {
            System.out.println("Song.play() called for: " + title);
            System.out.println("Number of notes: " + notes.size());
            updatePattern();
            
            // Create a temporary pattern with settings for playback
            Pattern playbackPattern = new Pattern();
            
            // Add tempo
            playbackPattern.add("T" + tempo);
            
            // Add time signature if not standard 4/4
            if (timeSignature.getNumerator() != 4 || timeSignature.getDenominator() != 4) {
                playbackPattern.add("TIME:" + timeSignature.getNumerator() + "/" + timeSignature.getDenominator());
            }
            
            // Add key signature if specified
            if (keySignature != null && !keySignature.isEmpty()) {
                // Strip "Minor" or "Major" for JFugue format
                String key = keySignature.replaceAll("\\s+Minor|\\s+Major", "");
                if (keySignature.contains("Minor")) {
                    key += "min";
                }
                playbackPattern.add("KEY:" + key);
            }
            
            // Add the notes pattern
            playbackPattern.add(pattern);
            
            System.out.println("Pattern: " + playbackPattern.toString());
            Player player = new Player();
            player.play(playbackPattern);
            System.out.println("Song playback completed successfully");
        } catch (Exception e) {
            System.err.println("Error playing song: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a note to the song
     */
    public void addNote(Note note) {
        notes.add(note);
        updatePattern();
    }

    /**
     * Remove a note from the song
     */
    public void removeNote(int index) {
        if (index >= 0 && index < notes.size()) {
            notes.remove(index);
            updatePattern();
        }
    }

    /**
     * Update the JFugue pattern based on current notes
     */
    private void updatePattern() {
        Pattern newPattern = new Pattern();
        
        // Only include the notes in the pattern for sheet music display
        // Add each note to the pattern
        for (Note note : notes) {
            try {
                Pattern notePattern = note.toPattern();
                newPattern.add(notePattern);
            } catch (Exception e) {
                System.out.println("Error adding note to pattern: " + e.getMessage());
            }
        }
        
        this.pattern = newPattern;
    }

    /**
     * Convert this song to a JSON object for storage
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("songId", songId.toString());
        json.put("title", title);
        json.put("composer", composer);
        json.put("tempo", tempo);
        json.put("genres", genres);
        json.put("lyrics", lyrics);
        json.put("rating", rating);
        json.put("keySignature", keySignature);
        
        // Create timeSignature as a JSONObject instead of a string
        JSONObject timeSignatureJson = new JSONObject();
        timeSignatureJson.put("numerator", timeSignature.getNumerator());
        timeSignatureJson.put("denominator", timeSignature.getDenominator());
        json.put("timeSignature", timeSignatureJson);
        
        // Add notes information
        JSONArray notesArray = new JSONArray();
        for (Note note : notes) {
            JSONObject noteJson = new JSONObject();
            noteJson.put("pitch", note.getPitch());
            noteJson.put("duration", note.getDuration());
            noteJson.put("volume", note.getVolume());
            noteJson.put("octave", note.getOctave());
            noteJson.put("expression", note.getExpression());
            notesArray.add(noteJson);
        }
        json.put("notes", notesArray);
        
        return json;
    }

    /**
     * Load song data from a JSON object
     */
    public void fromJSON(JSONObject json) {
        if (json.containsKey("songId")) {
            this.songId = UUID.fromString((String) json.get("songId"));
        }
        if (json.containsKey("title")) {
            this.title = (String) json.get("title");
        }
        if (json.containsKey("composer")) {
            this.composer = (String) json.get("composer");
        }
        if (json.containsKey("tempo")) {
            this.tempo = ((Long) json.get("tempo")).intValue();
        }
        if (json.containsKey("genres")) {
            this.genres = (List<String>) json.get("genres");
        }
        if (json.containsKey("lyrics")) {
            this.lyrics = (List<String>) json.get("lyrics");
        }
        if (json.containsKey("rating")) {
            this.rating = (Double) json.get("rating");
        }
        if (json.containsKey("keySignature")) {
            this.keySignature = (String) json.get("keySignature");
        }
        if (json.containsKey("timeSignature")) {
            // Handle both object and string formats for backward compatibility
            Object timeSignatureValue = json.get("timeSignature");
            if (timeSignatureValue instanceof JSONObject) {
                // New format - JSON object with numerator and denominator
                JSONObject timeSignatureJson = (JSONObject) timeSignatureValue;
                this.timeSignature = new TimeSignature(
                    ((Long) timeSignatureJson.get("numerator")).intValue(),
                    ((Long) timeSignatureJson.get("denominator")).intValue()
                );
            } else if (timeSignatureValue instanceof String) {
                // Old format - string like "4/4"
                String[] parts = ((String) timeSignatureValue).split("/");
                this.timeSignature = new TimeSignature(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
                );
            }
        }
        
        // Load notes from JSON
        if (json.containsKey("notes")) {
            JSONArray notesArray = (JSONArray) json.get("notes");
            this.notes.clear();
            
            for (Object noteObj : notesArray) {
                JSONObject noteJson = (JSONObject) noteObj;
                String pitch = (String) noteJson.get("pitch");
                double duration = ((Number) noteJson.get("duration")).doubleValue();
                int volume = ((Long) noteJson.get("volume")).intValue();
                int octave = ((Long) noteJson.get("octave")).intValue();
                String expression = (String) noteJson.get("expression");
                
                Note note = new Note(pitch);
                note.setDuration(duration);
                note.setVolume(volume);
                note.setOctave(octave);
                if (expression != null) {
                    note.setExpression(expression);
                }
                
                this.notes.add(note);
            }
            
            // Update the pattern with the loaded notes
            updatePattern();
        }
    }

    // Getters and Setters
    public UUID getSongId() { return songId; }
    public String getTitle() { return title; }
    public String getComposer() { return composer; }
    public int getTempo() { return tempo; }
    public List<String> getGenres() { return genres; }
    public List<String> getLyrics() { return lyrics; }
    public double getRating() { return rating; }
    public String getKeySignature() { return keySignature; }
    public TimeSignature getTimeSignature() { return timeSignature; }
    public Pattern getPattern() { return pattern; }

    public void setSongId(UUID songId) { this.songId = songId; }
    public void setTitle(String title) { this.title = title; }
    public void setComposer(String composer) { this.composer = composer; }
    public void setTempo(int tempo) { 
        this.tempo = tempo;
        updatePattern();
    }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public void setLyrics(List<String> lyrics) { this.lyrics = lyrics; }
    public void setRating(double rating) { this.rating = rating; }
    public void setKeySignature(String keySignature) { 
        this.keySignature = keySignature;
        updatePattern();
    }
    public void setTimeSignature(TimeSignature timeSignature) { 
        this.timeSignature = timeSignature;
        updatePattern();
    }
    public void setSignature(int numerator, int denominator) {
        this.timeSignature = new TimeSignature(numerator, denominator);
        updatePattern();
    }
}
