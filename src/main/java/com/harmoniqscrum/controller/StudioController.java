package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
import com.harmoniqscrum.model.Song;
import com.harmoniqscrum.model.Note; // Assuming Note class exists
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StudioController {

    private HarmoniqFACADE facade;

    public StudioController(HarmoniqFACADE facade) {
        this.facade = facade;
    }

    /**
     * Gathers data from UI fields (passed as arguments for now)
     * creates a Song object, and tells the facade to save it.
     * 
     * In a real scenario, this method would likely take UI controls 
     * as arguments or access them via the view to get current values.
     */
    public void saveSong(String title, String composer, String tempoStr, 
                         String keySignature, String timeSigNumStr, String timeSigDenStr,
                         String genresStr, String lyricsStr, String notesStr) {
        
        System.out.println("StudioController: Attempting to save song...");

        // --- Data Validation and Parsing --- 
        // Use defaults or show errors for invalid input

        String finalTitle = (title == null || title.trim().isEmpty()) ? "Untitled" : title.trim();
        String finalComposer = (composer == null || composer.trim().isEmpty()) ? "Unknown Composer" : composer.trim();
        String finalKeySignature = (keySignature == null || keySignature.trim().isEmpty()) ? "C Major" : keySignature.trim();

        int tempo = 120;
        try { tempo = Integer.parseInt(tempoStr.trim()); } 
        catch (NumberFormatException e) { System.err.println("Invalid Tempo: Using default 120"); }

        int timeSigNum = 4;
        try { timeSigNum = Integer.parseInt(timeSigNumStr.trim()); } 
        catch (NumberFormatException e) { System.err.println("Invalid Time Signature Numerator: Using default 4"); }

        int timeSigDen = 4;
        try { timeSigDen = Integer.parseInt(timeSigDenStr.trim()); } 
        catch (NumberFormatException e) { System.err.println("Invalid Time Signature Denominator: Using default 4"); }

        // Parse genres (simple comma separation, trim whitespace)
        List<String> genres = new ArrayList<>();
        if (genresStr != null && !genresStr.trim().isEmpty()) {
            genres = Arrays.stream(genresStr.split("\\s*,\\s*"))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty())
                         .collect(Collectors.toList());
        }
        
        // Parse lyrics (split by newline)
        List<String> lyrics = new ArrayList<>();
         if (lyricsStr != null) { // Allow empty lyrics string
            lyrics = Arrays.stream(lyricsStr.split("\\r?\\n"))
                         .collect(Collectors.toList());
        }
        
        // --- Create Song Object --- 
        Song newSong = new Song(finalTitle, finalComposer);
        newSong.setTempo(tempo);
        newSong.setKeySignature(finalKeySignature);
        newSong.setSignature(timeSigNum, timeSigDen);
        newSong.setGenres(genres);
        newSong.setLyrics(lyrics);
        // Rating is not set here, defaults to 0.0
        
        // --- Note Parsing --- 
        System.out.println("Raw Notes Input: " + notesStr);
        if (notesStr != null && !notesStr.trim().isEmpty()) {
            String[] noteTokens = notesStr.trim().split("\\s+"); // Split by whitespace
            for (String token : noteTokens) {
                if (token.isEmpty()) continue;
                
                try {
                    Note note = parseNoteToken(token);
                    if (note != null) {
                        newSong.addNote(note);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse note token: '" + token + "' - " + e.getMessage());
                }
            }
        }
        
        // --- Save Song --- 
        try {
            facade.saveSong(newSong); // Assumes facade handles persistence (e.g., saving to JSON)
            System.out.println("Song save requested for: " + newSong.getTitle());
             // TODO: Provide success feedback to the user (e.g., show alert)
        } catch (Exception e) {
             System.err.println("Error saving song via facade: " + e.getMessage());
             e.printStackTrace();
             // TODO: Provide error feedback to the user
        }
    }

    /**
     * Parses a single note token (e.g., "C5q", "G#", "Fh") into a Note object.
     * Very basic implementation.
     * @param token The string token representing the note.
     * @return A Note object, or null if parsing fails.
     */
    private Note parseNoteToken(String token) {
        String pitch = null;
        int octave = 5; // JFugue default middle octave
        double duration = 1.0; // Default to quarter note duration (JFugue 'q')

        // Regex to capture Pitch (group 1), Octave (group 3), Duration char (group 4)
        // Pitch: [A-G] followed by optional # or b
        // Octave: Optional digit
        // Duration: Optional single char w, h, q, i, s, t
        java.util.regex.Pattern notePattern = java.util.regex.Pattern.compile("([A-G][#b]?)((\\d)?)([whqist]?)");
        java.util.regex.Matcher matcher = notePattern.matcher(token.toUpperCase());

        if (matcher.matches()) {
            pitch = matcher.group(1);

            // Parse Octave if present
            if (matcher.group(3) != null) {
                try {
                    octave = Integer.parseInt(matcher.group(3));
                } catch (NumberFormatException e) { /* Keep default octave */ }
            }

            // Parse Duration char if present
            if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                char durationChar = matcher.group(4).charAt(0);
                switch (durationChar) {
                    case 'W': duration = 4.0; break;
                    case 'H': duration = 2.0; break;
                    case 'Q': duration = 1.0; break;
                    case 'I': duration = 0.5; break;
                    case 'S': duration = 0.125; break; // Corrected sixteenth duration
                    case 'T': duration = 0.0625; break; // Corrected thirty-second duration
                    // Default duration (1.0) is already set
                }
            }
            
             // Create Note object (using basic constructor and setters for clarity)
             Note note = new Note();
             note.setPitch(pitch);
             note.setOctave(octave);
             note.setDuration(duration);
             // Keep default volume for now
             System.out.println("Parsed Note: Pitch=" + pitch + ", Octave=" + octave + ", Duration=" + duration);
             return note;

        } else if (token.equalsIgnoreCase("R")) { // Handle Rest
             Note rest = new Note();
             rest.setPitch("Rest"); // Use a special pitch string for rests
             // TODO: Need to handle rest duration similar to notes
             rest.setDuration(1.0); // Default rest to quarter duration for now
             System.out.println("Parsed Rest");
             return rest;
        } else {
            System.err.println("Could not parse note token: " + token);
            return null;
        }
    }
} 