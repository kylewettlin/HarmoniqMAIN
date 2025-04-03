package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.harmoniqscrum.model.Chords;
import com.harmoniqscrum.model.Composition;
import com.harmoniqscrum.model.Measure;
import com.harmoniqscrum.model.Staff;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


 /**
 * tests for the Composition class and JSON-aligned composition structure, 
 * annotations, and linked lesson/song relationships
 *
 * @author Chase Tegtmeier McCracken
 */
 
public class CompositionTest {

    private Composition rockComposition;
    private Composition popComposition;

    @BeforeEach
    void setUp() {
        // Initialize with "Sweet Child O' Mine" (Rock) and "Rolling in the Deep" (Pop) examples
        rockComposition = new Composition();
        rockComposition.addAnnotations("Iconic rock ballad with Slash's legendary guitar solo.");
        rockComposition.getChords().add(new Chords("E", "MIN"));
        
        popComposition = new Composition();
        popComposition.addAnnotations("Soulful breakup anthem with powerful vocals.");
        popComposition.setCompositionPrivacy(true);
    }

    
    @Test
    void testRockCompositionAnnotationsMatchJson() {
        assertTrue(rockComposition.getAnnotations().contains("Iconic rock ballad with Slash's legendary guitar solo."));
        assertEquals(1, rockComposition.getChords().size());
    }

    @Test
    void testPopCompositionPrivacyAndAnnotations() {
        assertTrue(popComposition.isCompositionPrivacy());
        assertTrue(popComposition.getAnnotations().contains("Soulful breakup anthem with powerful vocals."));
    }

    
    @Test
    void testAddAndRemoveAnnotationForHipHopTrack() {
        Composition hipHopComp = new Composition();
        hipHopComp.addAnnotations("Grammy-winning rap anthem about seizing opportunities.");
        hipHopComp.removeAnnotation("Grammy-winning rap anthem about seizing opportunities.");
        
        assertTrue(hipHopComp.getAnnotations().isEmpty());
    }

    @Test
    void testCountryCompositionAnnotationPersistence() {
        Composition countryComp = new Composition();
        countryComp.addAnnotations("Haunting country classic about romantic insecurity.");
        assertEquals(1, countryComp.getAnnotations().size());
    }

    
    @Test
    void testJazzCompositionChordManagement() {
        Composition jazzComp = new Composition();
        jazzComp.getChords().add(new Chords("Cmaj7", "JAZZ"));
        jazzComp.getChords().add(new Chords("Dm7", "JAZZ"));
        
        assertEquals(2, jazzComp.getChords().size());
        jazzComp.getChords().clear();
        assertTrue(jazzComp.getChords().isEmpty());
    }

    @Test
    void testClassicalCompositionStaffAndMeasures() {
        Composition classicalComp = new Composition();
        classicalComp.getMeasures().add(new Measure());
        classicalComp.getStaves().add(new Staff());
        
        assertEquals(1, classicalComp.getMeasures().size());
        assertEquals(1, classicalComp.getStaves().size());
    }

   
    @Test
    void testEmptyRBComposition() {
        Composition rbComp = new Composition();
        assertTrue(rbComp.getAnnotations().isEmpty());
        assertFalse(rbComp.isCompositionPrivacy());
    }

    @Test
    void testRemoveNonExistentAnnotation() {
        Composition testComp = new Composition();
        testComp.removeAnnotation("Ghost annotation");
        assertTrue(testComp.getAnnotations().isEmpty());
    }
}