package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

 /**
 * tests for the Staff class
 * Validates clef initialization, measure
 * management, and edge cases.
 * 
 * @author Chase Tegtmeier McCracken
 */
public class StaffTest {

    private Staff staff;

    @BeforeEach
    void setUp() {
        staff = new Staff();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(staff.getMeasures());
        assertTrue(staff.getMeasures().isEmpty());
        assertEquals("treble", staff.getClef());
        assertEquals("4/4", staff.getTimeSignature());
        assertEquals("C", staff.getKeySignature());
    }

    @Test
    void testCustomConstructor() {
        staff = new Staff("bass", "3/4", "F#");
        assertEquals("bass", staff.getClef());
        assertEquals("3/4", staff.getTimeSignature());
        assertEquals("F#", staff.getKeySignature());
        assertNotNull(staff.getMeasures());
        assertTrue(staff.getMeasures().isEmpty());
    }

    @Test
    void testAddMeasure() {
        Measure measure = new Measure("4/4");
        staff.addMeasure(measure);
        
        assertEquals(1, staff.getMeasures().size());
        assertEquals(measure, staff.getMeasures().get(0));
    }

    @Test
    void testAddMultipleMeasures() {
        Measure measure1 = new Measure("4/4");
        Measure measure2 = new Measure("4/4");
        Measure measure3 = new Measure("4/4");

        staff.addMeasure(measure1);
        staff.addMeasure(measure2);
        staff.addMeasure(measure3);

        assertEquals(3, staff.getMeasures().size());
        assertEquals(measure1, staff.getMeasures().get(0));
        assertEquals(measure2, staff.getMeasures().get(1));
        assertEquals(measure3, staff.getMeasures().get(2));
    }

    @Test
    void testRemoveMeasure() {
        Measure measure1 = new Measure("4/4");
        Measure measure2 = new Measure("4/4");
        
        staff.addMeasure(measure1);
        staff.addMeasure(measure2);
        
        staff.removeMeasure(0);
        assertEquals(1, staff.getMeasures().size());
        assertEquals(measure2, staff.getMeasures().get(0));
    }

    @Test
    void testRemoveMeasureInvalidIndex() {
        Measure measure = new Measure("4/4");
        staff.addMeasure(measure);

        staff.removeMeasure(-1); // Should not throw exception
        staff.removeMeasure(1); // Should not throw exception
        assertEquals(1, staff.getMeasures().size()); // Measure should still be there
    }

    @Test
    void testSetMeasures() {
        List<Measure> measures = new ArrayList<>();
        measures.add(new Measure("4/4"));
        measures.add(new Measure("4/4"));
        measures.add(new Measure("4/4"));

        staff.setMeasures(measures);
        assertEquals(3, staff.getMeasures().size());
        assertEquals(measures, staff.getMeasures());
    }

    @Test
    void testSetInvalidClef() {
        assertThrows(IllegalArgumentException.class, () -> {
            staff.setClef("invalid");
        });
    }

    @Test
    void testSetInvalidTimeSignature() {
        assertThrows(IllegalArgumentException.class, () -> {
            staff.setTimeSignature("5/3");
        });
    }

    @Test
    void testSetInvalidKeySignature() {
        assertThrows(IllegalArgumentException.class, () -> {
            staff.setKeySignature("H#");
        });
    }

    @Test
    void testValidClefs() {
        String[] validClefs = {"treble", "bass", "alto", "tenor"};
        for (String clef : validClefs) {
            staff.setClef(clef);
            assertEquals(clef, staff.getClef());
        }
    }

    @Test
    void testValidTimeSignatures() {
        String[] validTimeSignatures = {"2/4", "3/4", "4/4", "6/8", "9/8", "12/8"};
        for (String timeSignature : validTimeSignatures) {
            staff.setTimeSignature(timeSignature);
            assertEquals(timeSignature, staff.getTimeSignature());
        }
    }

    @Test
    void testValidKeySignatures() {
        String[] validKeySignatures = {"C", "G", "D", "A", "E", "B", "F#", "C#",
                                     "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb"};
        for (String keySignature : validKeySignatures) {
            staff.setKeySignature(keySignature);
            assertEquals(keySignature, staff.getKeySignature());
        }
    }

    @Test
    void testMeasureTimeSignatureValidation() {
        staff.setTimeSignature("3/4");
        
        // Adding measure with different time signature should throw exception
        Measure measure = new Measure("4/4");
        assertThrows(IllegalArgumentException.class, () -> {
            staff.addMeasure(measure);
        });
    }

    @Test
    void testClearMeasures() {
        staff.addMeasure(new Measure("4/4"));
        staff.addMeasure(new Measure("4/4"));
        staff.addMeasure(new Measure("4/4"));

        staff.clearMeasures();
        assertTrue(staff.getMeasures().isEmpty());
    }
}