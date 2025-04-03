package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Default constructor initializes with treble clef and empty measures")
    void defaultConstructor_SetsTrebleClefAndEmptyMeasures() {
        assertEquals("treble", staff.getClef());
        assertTrue(staff.getMeasures().isEmpty());
    }

    @Test
    @DisplayName("Constructor with clef parameter sets clef correctly")
    void constructorWithClef_SetsSpecifiedClef() {
        Staff bassStaff = new Staff("bass");
        assertEquals("bass", bassStaff.getClef());
    }

    
    @Test
    @DisplayName("Adding a measure increases measures list size")
    void addMeasure_AddsToMeasuresList() {
        staff.addMeasure(new Measure());
        assertEquals(1, staff.getMeasures().size());
    }

    @Test
    @DisplayName("Setting measures replaces existing list")
    void setMeasures_ReplacesCurrentMeasures() {
        ArrayList<Measure> newMeasures = new ArrayList<>();
        newMeasures.add(new Measure());
        newMeasures.add(new Measure());
        
        staff.setMeasures(newMeasures);
        assertEquals(2, staff.getMeasures().size());
    }

    
    @Test
    @DisplayName("Setting clef updates the staff's clef")
    void setClef_UpdatesClefValue() {
        staff.setClef("alto");
        assertEquals("alto", staff.getClef());
    }
    
    @Test
    @DisplayName("Adding measure to null measures list throws exception")
    void addMeasure_WithNullMeasuresList_ThrowsException() {
        staff.setMeasures(null); // Force null measures list
        assertThrows(NullPointerException.class, () -> staff.addMeasure(new Measure()));
    }

    @Test
    @DisplayName("Render method does not throw errors")
    void render_DoesNotThrowErrors() {
        assertDoesNotThrow(() -> staff.render());
    }
}