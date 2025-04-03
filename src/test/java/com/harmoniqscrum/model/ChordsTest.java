package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.jfugue.pattern.Pattern;

public class ChordsTest {
    private Chords chord;

    @BeforeEach
    void setUp() {
        chord = new Chords();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("C", chord.getRoot());
        assertEquals("MAJ", chord.getQuality());
        assertEquals(1.0, chord.getDuration());
        assertEquals(100, chord.getVolume());
        assertNotNull(chord.getNotes());
        assertTrue(chord.getNotes().isEmpty());
    }

    @Test
    void testRootQualityConstructor() {
        chord = new Chords("D", "MIN");
        assertEquals("D", chord.getRoot());
        assertEquals("MIN", chord.getQuality());
        assertEquals(1.0, chord.getDuration()); // Default duration
        assertEquals(100, chord.getVolume()); // Default volume
    }

    @Test
    void testFullConstructor() {
        chord = new Chords("E", "DIM", 2.0, 80);
        assertEquals("E", chord.getRoot());
        assertEquals("DIM", chord.getQuality());
        assertEquals(2.0, chord.getDuration());
        assertEquals(80, chord.getVolume());
    }

    @Test
    void testNotesConstructor() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("C", 1.0, 100, 4));
        notes.add(new Note("E", 1.0, 100, 4));
        notes.add(new Note("G", 1.0, 100, 4));

        chord = new Chords(notes);
        assertEquals(notes, chord.getNotes());
        assertEquals(1.0, chord.getDuration()); // Should use default duration
        assertEquals(100, chord.getVolume()); // Should use default volume
    }

    @Test
    void testNotesWithDurationVolumeConstructor() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("C", 1.0, 100, 4));
        notes.add(new Note("E", 1.0, 100, 4));
        notes.add(new Note("G", 1.0, 100, 4));

        chord = new Chords(notes, 2.0, 80);
        assertEquals(notes, chord.getNotes());
        assertEquals(2.0, chord.getDuration());
        assertEquals(80, chord.getVolume());
    }

    @Test
    void testToPattern() {
        chord = new Chords("C", "MAJ", 1.0, 100);
        Pattern pattern = chord.toPattern();
        assertEquals("CMAJ", pattern.toString().trim());

        chord = new Chords("D", "MIN", 2.0, 80);
        pattern = chord.toPattern();
        assertEquals("DMIN/2.0 a80", pattern.toString().trim());
    }

    @Test
    void testToPatternNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("C", 1.0, 100, 4));
        notes.add(new Note("E", 1.0, 100, 4));
        notes.add(new Note("G", 1.0, 100, 4));

        chord = new Chords(notes);
        Pattern pattern = chord.toPatternNotes();
        assertEquals("C4q+E4q+G4q", pattern.toString().trim());
    }

    @Test
    void testCreateProgression() {
        List<Chords> progression = new ArrayList<>();
        progression.add(new Chords("I", "MAJ"));
        progression.add(new Chords("IV", "MAJ"));
        progression.add(new Chords("V", "MAJ"));

        Pattern pattern = Chords.createProgression(progression, "C");
        assertNotNull(pattern);
        // The actual pattern string will depend on JFugue's implementation
        assertTrue(pattern.toString().contains("I") && pattern.toString().contains("IV") && pattern.toString().contains("V"));
    }

    @Test
    void testAddNote() {
        Note note = new Note("C", 1.0, 100, 4);
        chord.addNote(note);
        
        assertEquals(1, chord.getNotes().size());
        assertEquals(note, chord.getNotes().get(0));
    }

    @Test
    void testRemoveNote() {
        Note note1 = new Note("C", 1.0, 100, 4);
        Note note2 = new Note("E", 1.0, 100, 4);
        chord.addNote(note1);
        chord.addNote(note2);
        
        assertEquals(2, chord.getNotes().size());
        chord.removeNote(0);
        assertEquals(1, chord.getNotes().size());
        assertEquals(note2, chord.getNotes().get(0));
    }

    @Test
    void testRemoveNoteInvalidIndex() {
        chord.addNote(new Note("C", 1.0, 100, 4));
        chord.removeNote(-1); // Should not throw exception
        chord.removeNote(1); // Should not throw exception
        assertEquals(1, chord.getNotes().size()); // Note should still be there
    }

    @Test
    void testInvalidChordQuality() {
        chord = new Chords("C", "INVALID");
        Pattern pattern = chord.toPattern();
        // Should still create a pattern, but might not be musically correct
        assertEquals("CINVALID", pattern.toString().trim());
    }
} 