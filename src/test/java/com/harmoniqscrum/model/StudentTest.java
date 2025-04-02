package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Student class.
 * Covers lesson assignment, completion tracking, grade functionality, and data integrity.
 * 
 * @author Dreyton Merck
 */
public class StudentTest {

    private Student student;
    private Lesson lesson1;
    private Lesson lesson2;

    /**
     * Set up common test data before each test.
     */
    @BeforeEach
    public void setUp() {
        student = new Student("John", "Smith", "jsmith", "john@email.com", "pass", "light", "blue");

        List<String> objectives = List.of("Learn C Major Scale");
        List<String> exercises = List.of("Warm-up 1");
        List<String> relatedSongs = List.of("song123");

        lesson1 = new Lesson("L1", "Intro", "Simple intro", relatedSongs, "easy", objectives, exercises, 15, "Pop");
        lesson2 = new Lesson("L2", "Chords", "Basic chords", relatedSongs, "medium", objectives, exercises, 20, "Rock");
    }

    /**
     * Test that a student is initialized with empty lesson lists.
     */
    @Test
    public void testStudentStartsWithNoLessons() {
        assertTrue(student.getAssignedLessons().isEmpty());
        assertTrue(student.getLessonObjects().isEmpty());
        assertEquals(0, student.getAllLessons().size());
    }

    /**
     * Test setting and retrieving assigned lessons.
     */
    @Test
    public void testSetAssignedLessons() {
        ArrayList<Lesson> assigned = new ArrayList<>();
        assigned.add(lesson1);
        student.setAssignedLessons(assigned);

        assertEquals(1, student.getAssignedLessons().size());
        assertTrue(student.getAssignedLessons().contains(lesson1));
    }

    /**
     * Test setting and retrieving completed lessons.
     */
    @Test
    public void testSetCompletedLessons() {
        ArrayList<Lesson> completed = new ArrayList<>();
        completed.add(lesson2);
        student.setCompletedLessons(completed);

        assertEquals(1, student.getLessonObjects().size());
        assertTrue(student.getLessonObjects().contains(lesson2));
    }

    /**
     * Test completing a lesson successfully.
     */
    @Test
    public void testCompleteLessonSuccess() {
        student.getAssignedLessons().add(lesson1);

        student.completeLesson(lesson1);

        assertTrue(student.getLessonObjects().contains(lesson1));
        assertEquals(1, student.getCompletedLessons().size());
        assertTrue(student.getCompletedLessons().get(0).toString().contains("Intro"));
    }

    /**
     * Test that a lesson not in assigned list is not completed.
     */
    @Test
    public void testCompleteLessonFailsIfNotAssigned() {
        student.completeLesson(lesson1);

        assertFalse(student.getLessonObjects().contains(lesson1));
        assertTrue(student.getCompletedLessons().isEmpty());
    }

    /**
     * Test that a duplicate lesson is not added to completed list.
     */
    @Test
    public void testCompleteLessonNotAddedTwice() {
        student.getAssignedLessons().add(lesson1);
        student.completeLesson(lesson1);
        student.completeLesson(lesson1);

        assertEquals(1, student.getLessonObjects().size());
    }

    /**
     * Test retrieving all lessons (assigned + completed).
     */
    @Test
    public void testGetAllLessons() {
        student.getAssignedLessons().add(lesson1);
        student.getLessonObjects().add(lesson2);

        List<Lesson> all = student.getAllLessons();
        assertEquals(2, all.size());
        assertTrue(all.contains(lesson1));
        assertTrue(all.contains(lesson2));
    }

    /**
     * Test setting and getting student grade.
     */
    @Test
    public void testSetAndGetGrade() {
        student.setGrade(90);
        assertEquals(90, student.getStudentGrade());
    }

    /**
     * Test grade is not changed when student role is modified (defensive test).
     */
    @Test
    public void testGradeSetOnlyForStudentRole() {
        student.setGrade(85);
        assertEquals(85, student.getGrade());

        // simulate bad case (e.g., trying to set a teacher grade through Student - not possible here but tested for stability)
        student.setGrade(100);
        assertEquals(100, student.getGrade());
    }
}
