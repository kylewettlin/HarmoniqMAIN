package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * tests for the Lesson class and JSON-aligned lesson content and student interactions.
 * 
 * @author Chase Tegtmeier McCracken
 */
 
public class LessonTest {

    private Lesson rockGuitarLesson;
    private Student student;

    @BeforeEach
    void setUp() {
        // Initialize with "Rock Guitar Soloing" lesson from JSON
        rockGuitarLesson = new Lesson(
            "l1",
            "Rock Guitar Soloing with 'Sweet Child O' Mine'",
            "Learn Slash's iconic guitar solo techniques",
            List.of("a79067ee-506f-4b65-9c9b-6a4a09eacb8e"),
            "Intermediate",
            List.of(
                "Master bending and vibrato techniques",
                "Understand pentatonic scale usage",
                "Develop solo phrasing"
            ),
            List.of(
                "Practice the solo at 50% tempo",
                "Loop the chord progression for improvisation"
            ),
            45,
            "ROCK"
        );
        
        student = new Student(null, null, null, null, null, null, null);
    }

    
    @Test
    void testRockLessonConstructorMatchesJsonData() {
        assertEquals("l1", rockGuitarLesson.getLessonId());
        assertEquals("ROCK", rockGuitarLesson.getGenre());
        assertEquals(2, rockGuitarLesson.getExercises().size());
        assertTrue(rockGuitarLesson.getRelatedSongIds().contains("a79067ee-506f-4b65-9c9b-6a4a09eacb8e"));
    }

    @Test
    void testViewLessonShowsRockContent() {
        String content = rockGuitarLesson.viewLesson();
        
        // Verify key sections from JSON data
        assertTrue(content.contains("Rock Guitar Soloing"));
        assertTrue(content.contains("Intermediate"));
        assertTrue(content.contains("pentatonic scale usage"));
        assertTrue(content.contains("Practice the solo at 50% tempo")); 
        assertTrue(content.contains("a79067ee-506f-4b65-9c9b-6a4a09eacb8e")); 
    }


    @Test
    void testAssignRockLessonToStudent() {
        assertTrue(rockGuitarLesson.assignLesson(student));
        assertTrue(student.getAssignedLessons().contains(rockGuitarLesson));
    }

    @Test
    void testCompleteRockLessonUpdatesStudentProgress() {
        student.getAssignedLessons().add(rockGuitarLesson);
        assertTrue(rockGuitarLesson.completeLesson(student));
        assertTrue(student.getCompletedLessons().contains(rockGuitarLesson));
    }

    @Test
    void testCannotCompleteUnassignedHipHopLesson() {
        Lesson hipHopLesson = new Lesson(
            "l3",
            "Hip-Hop Flow and Rhyme Schemes with 'Lose Yourself'",
            "Analyze Eminem's lyrical delivery",
            List.of("d34567ef-8901-4g23-b456-78ef901g2345"),
            "Advanced",
            List.of("Internalize complex rhyme schemes"),
            List.of("Practice verse 1 at 75% speed"),
            60,
            "HIP-HOP"
        );
        
        assertFalse(hipHopLesson.completeLesson(student));
    }


    @Test
    void testPopLessonWithEmptySections() {
        Lesson popLesson = new Lesson(
            "l2",
            "Pop Vocal Dynamics with 'Rolling in the Deep'",
            "Emulate Adele's powerful delivery",
            new ArrayList<>(),
            "Beginner",
            new ArrayList<>(), 
            new ArrayList<>(), 
            30,
            "POP"
        );
        
        String content = popLesson.viewLesson();
        assertFalse(content.contains("Related Songs:"));
        assertFalse(content.contains("Learning Objectives:")); 
    }

    @Test
    void testCountryLessonDurationValidation() {
        Lesson countryLesson = new Lesson(
            "l4",
            "Country Fingerpicking with 'Jolene'",
            "Master Dolly Parton's acoustic style",
            List.of("e45678fg-9012-4h34-c567-89fg012h3456"),
            "Intermediate",
            List.of("Alternating bass technique"),
            List.of("Practice the intro riff with metronome"),
            40,
            "COUNTRY"
        );
        
        assertEquals(40, countryLesson.getDurationMinutes());
    }
}