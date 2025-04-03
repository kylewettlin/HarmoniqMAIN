package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.jfugue.pattern.Pattern;

public class SongTest {
    private Song song;
    private final String TEST_TITLE = "Test Song";
    private final String TEST_COMPOSER = "Test Composer";

    @BeforeEach
    void setUp() {
        song = new Song();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(song.getSongId());
        assertEquals("Untitled", song.getTitle());
        assertEquals("Unknown", song.getComposer());
        assertEquals(120, song.getTempo());
        assertNotNull(song.getGenres());
        assertTrue(song.getGenres().isEmpty());
        assertNotNull(song.getLyrics());
        assertTrue(song.getLyrics().isEmpty());
        assertEquals(0.0, song.getRating());
        assertEquals("C", song.getKeySignature());
        assertNotNull(song.getTimeSignature());
        assertEquals(4, song.getTimeSignature().getNumerator());
        assertEquals(4, song.getTimeSignature().getDenominator());
        assertNotNull(song.getPattern());
    }

    @Test
    void testBasicConstructor() {
        song = new Song(TEST_TITLE, TEST_COMPOSER);
        assertEquals(TEST_TITLE, song.getTitle());
        assertEquals(TEST_COMPOSER, song.getComposer());
    }

    @Test
    void testFullConstructor() {
        UUID id = UUID.randomUUID();
        List<String> genres = new ArrayList<>();
        genres.add("Rock");
        List<String> lyrics = new ArrayList<>();
        lyrics.add("Test lyrics");
        TimeSignature timeSignature = new TimeSignature(3, 4);

        song = new Song(id, TEST_TITLE, TEST_COMPOSER, 140, genres, 4.5, "Am", timeSignature, lyrics);

        assertEquals(id, song.getSongId());
        assertEquals(TEST_TITLE, song.getTitle());
        assertEquals(TEST_COMPOSER, song.getComposer());
        assertEquals(140, song.getTempo());
        assertEquals(genres, song.getGenres());
        assertEquals(4.5, song.getRating());
        assertEquals("Am", song.getKeySignature());
        assertEquals(timeSignature, song.getTimeSignature());
        assertEquals(lyrics, song.getLyrics());
    }

    @Test
    void testAddNote() {
        Note note = new Note("C", 1.0, 100, 4);
        song.addNote(note);
        
        Pattern pattern = song.getPattern();
        assertNotNull(pattern);
        assertTrue(pattern.toString().contains("C4q")); // Quarter note C in octave 4
    }

    @Test
    void testRemoveNote() {
        Note note1 = new Note("C", 1.0, 100, 4);
        Note note2 = new Note("E", 1.0, 100, 4);
        song.addNote(note1);
        song.addNote(note2);
        
        song.removeNote(0);
        Pattern pattern = song.getPattern();
        assertFalse(pattern.toString().contains("C4q")); // First note should be removed
        assertTrue(pattern.toString().contains("E4q")); // Second note should remain
    }

    @Test
    void testRemoveNoteInvalidIndex() {
        Note note = new Note("C", 1.0, 100, 4);
        song.addNote(note);
        
        song.removeNote(-1); // Should not throw exception
        song.removeNote(1); // Should not throw exception
        
        Pattern pattern = song.getPattern();
        assertTrue(pattern.toString().contains("C4q")); // Note should still be there
    }

    @Test
    void testToJSON() {
        song = new Song(TEST_TITLE, TEST_COMPOSER);
        song.setTempo(140);
        song.getGenres().add("Jazz");
        song.getLyrics().add("Test lyrics");
        song.setRating(4.5);
        song.setKeySignature("Dm");
        song.setTimeSignature(new TimeSignature(6, 8));
        
        Note note = new Note("C", 1.0, 100, 4);
        note.setExpression("staccato");
        song.addNote(note);

        JSONObject json = song.toJSON();
        
        assertNotNull(json.get("songId"));
        assertEquals(TEST_TITLE, json.get("title"));
        assertEquals(TEST_COMPOSER, json.get("composer"));
        assertEquals(140L, json.get("tempo"));
        assertTrue(((List<?>)json.get("genres")).contains("Jazz"));
        assertTrue(((List<?>)json.get("lyrics")).contains("Test lyrics"));
        assertEquals(4.5, (Double)json.get("rating"), 0.001);
        assertEquals("Dm", json.get("keySignature"));
        
        JSONObject timeSignature = (JSONObject)json.get("timeSignature");
        assertEquals(6L, timeSignature.get("numerator"));
        assertEquals(8L, timeSignature.get("denominator"));
    }

    @Test
    void testFromJSON() {
        // Create a song and convert to JSON
        Song originalSong = new Song(TEST_TITLE, TEST_COMPOSER);
        originalSong.setTempo(140);
        originalSong.getGenres().add("Jazz");
        originalSong.getLyrics().add("Test lyrics");
        originalSong.setRating(4.5);
        originalSong.setKeySignature("Dm");
        originalSong.setTimeSignature(new TimeSignature(6, 8));
        
        Note note = new Note("C", 1.0, 100, 4);
        note.setExpression("staccato");
        originalSong.addNote(note);

        JSONObject json = originalSong.toJSON();

        // Create new song from JSON
        Song newSong = new Song();
        newSong.fromJSON(json);

        // Verify all properties match
        assertEquals(originalSong.getSongId().toString(), newSong.getSongId().toString());
        assertEquals(originalSong.getTitle(), newSong.getTitle());
        assertEquals(originalSong.getComposer(), newSong.getComposer());
        assertEquals(originalSong.getTempo(), newSong.getTempo());
        assertEquals(originalSong.getGenres(), newSong.getGenres());
        assertEquals(originalSong.getLyrics(), newSong.getLyrics());
        assertEquals(originalSong.getRating(), newSong.getRating());
        assertEquals(originalSong.getKeySignature(), newSong.getKeySignature());
        assertEquals(originalSong.getTimeSignature().getNumerator(), 
                    newSong.getTimeSignature().getNumerator());
        assertEquals(originalSong.getTimeSignature().getDenominator(), 
                    newSong.getTimeSignature().getDenominator());
    }

    @Test
    void testSetTempo() {
        song.setTempo(160);
        assertEquals(160, song.getTempo());
        
        // Test invalid tempos
        song.setTempo(0);
        assertTrue(song.getTempo() > 0, "Tempo should not be zero or negative");
        
        song.setTempo(-20);
        assertTrue(song.getTempo() > 0, "Tempo should not be zero or negative");
    }

    @Test
    void testSetTimeSignature() {
        TimeSignature ts = new TimeSignature(5, 4);
        song.setTimeSignature(ts);
        assertEquals(ts, song.getTimeSignature());
        assertEquals(5, song.getTimeSignature().getNumerator());
        assertEquals(4, song.getTimeSignature().getDenominator());
    }
} 