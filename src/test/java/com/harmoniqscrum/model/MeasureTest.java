package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MeasureTest {
    private Measure measure;

    @BeforeEach
    void setUp() {
        measure = new Measure();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("4/4", measure.getTimeSignature());
        assertNotNull(measure.getNotes());
        assertTrue(measure.getNotes().isEmpty());
    }

    @Test
    void testTimeSignatureConstructor() {
        measure = new Measure("3/4");
        assertEquals("3/4", measure.getTimeSignature());
        assertNotNull(measure.getNotes());
        assertTrue(measure.getNotes().isEmpty());

        measure = new Measure("6/8");
        assertEquals("6/8", measure.getTimeSignature());
    }

    @Test
    void testAddNote() {
        Note note = new Note("C", 1.0, 100, 4);
        measure.addNote(note);
        
        assertEquals(1, measure.getNotes().size());
        assertEquals(note, measure.getNotes().get(0));
    }

    @Test
    void testSetNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note("C", 1.0, 100, 4));
        notes.add(new Note("E", 1.0, 100, 4));
        notes.add(new Note("G", 1.0, 100, 4));

        measure.setNotes(notes);
        assertEquals(3, measure.getNotes().size());
        assertEquals(notes, measure.getNotes());
    }

    @Test
    void testMeasureDurationValidation() {
        measure = new Measure("4/4");
        
        // Add notes that exceed measure duration (4 beats in 4/4)
        measure.addNote(new Note("C", 2.0, 100, 4)); // 2 beats
        measure.addNote(new Note("E", 2.0, 100, 4)); // 2 beats
        measure.addNote(new Note("G", 1.0, 100, 4)); // 1 beat - should make measure invalid

        assertFalse(measure.validate(), "Measure should be invalid when total duration exceeds time signature");
    }

    @Test
    void testInvalidTimeSignature() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Measure("5/3"); // Uncommon time signature
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Measure("0/4"); // Invalid numerator
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Measure("4/3"); // Invalid denominator (should be power of 2)
        });
    }

    @Test
    void testEmptyMeasureValidation() {
        assertTrue(measure.validate(), "Empty measure should be valid");
    }

    @Test
    void testExactDurationMeasure() {
        measure = new Measure("3/4");
        
        measure.addNote(new Note("C", 1.0, 100, 4)); // 1 beat
        measure.addNote(new Note("E", 1.0, 100, 4)); // 1 beat
        measure.addNote(new Note("G", 1.0, 100, 4)); // 1 beat

        assertTrue(measure.validate(), "Measure should be valid when total duration equals time signature");
    }

    @Test
    void testIncompleteMeasure() {
        measure = new Measure("4/4");
        
        measure.addNote(new Note("C", 1.0, 100, 4)); // 1 beat
        measure.addNote(new Note("E", 1.0, 100, 4)); // 1 beat

        assertFalse(measure.validate(), "Measure should be invalid when not completely filled");
    }
} 