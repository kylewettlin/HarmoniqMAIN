package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
    private Note note;

    @BeforeEach
    void setUp() {
        note = new Note();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("C", note.getPitch());
        assertEquals(0.25, note.getDuration()); // Default duration is 0.25 (sixteenth note)
        assertEquals(100, note.getVolume()); // Default volume is 100
        assertEquals(5, note.getOctave()); // Default octave is 5
    }

    @Test
    void testPitchConstructor() {
        note = new Note("D#");
        assertEquals("D#", note.getPitch());
        assertEquals(0.25, note.getDuration());
        assertEquals(100, note.getVolume());
        assertEquals(5, note.getOctave());
    }

    @Test
    void testFullConstructor() {
        note = new Note("A", 2.0, 80, 4);
        assertEquals("A", note.getPitch());
        assertEquals(2.0, note.getDuration());
        assertEquals(80, note.getVolume());
        assertEquals(4, note.getOctave());
    }

    @Test
    void testGuitarConstructor() {
        // Test open high E string (string 0, fret 0)
        note = new Note(0, 0);
        assertEquals("E", note.getPitch());
        assertEquals(2, note.getOctave());

        // Test 5th fret on A string (string 4, fret 5)
        note = new Note(4, 5);
        assertEquals("D", note.getPitch());
        assertEquals(2, note.getOctave());
    }

    @Test
    void testVolumeValidation() {
        note.setVolume(-10);
        assertEquals(0, note.getVolume(), "Volume should be clamped to minimum 0");

        note.setVolume(150);
        assertEquals(127, note.getVolume(), "Volume should be clamped to maximum 127");

        note.setVolume(64);
        assertEquals(64, note.getVolume(), "Volume should remain unchanged within valid range");
    }

    @Test
    void testPitchCaseInsensitive() {
        note = new Note("c#");
        assertEquals("C#", note.getPitch(), "Pitch should be converted to uppercase");

        note.setPitch("eb");
        assertEquals("EB", note.getPitch(), "Pitch should be converted to uppercase when set");
    }

    @Test
    void testExpressionHandling() {
        note.setExpression("staccato");
        assertEquals("staccato", note.getExpression());

        note.setExpression("legato");
        assertEquals("legato", note.getExpression());
    }

    @Test
    void testToPattern() {
        // Test quarter note pattern
        note = new Note("C", 1.0, 100, 5);
        assertEquals("C5q", note.toPattern().toString().trim());

        // Test half note pattern with different volume
        note = new Note("D#", 2.0, 80, 4);
        assertEquals("D#4h a80", note.toPattern().toString().trim());

        // Test rest note
        note = new Note("Rest", 0.25, 100, 5);
        assertEquals("Rs", note.toPattern().toString().trim());
    }

    @Test
    void testCalculatePitchFromFret() {
        // Test high E string frets
        note = new Note(0, 1); // First fret on high E string
        assertEquals("F", note.getPitch());
        assertEquals(2, note.getOctave());

        note = new Note(0, 12); // 12th fret on high E string
        assertEquals("E", note.getPitch());
        assertEquals(3, note.getOctave());

        // Test low E string frets
        note = new Note(5, 0); // Open low E string
        assertEquals("E", note.getPitch());
        assertEquals(0, note.getOctave());

        note = new Note(5, 5); // 5th fret on low E string
        assertEquals("A", note.getPitch());
        assertEquals(0, note.getOctave());
    }

    @Test
    void testInvalidGuitarString() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            new Note(6, 0); // Invalid string number (valid range is 0-5)
        });
    }
} 