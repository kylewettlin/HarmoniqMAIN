package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Student class
 * 
 * @author Dreyton Merck
 */
public class StudentTest {
    
    private Student student;
    private Lesson lesson1;
    private Lesson lesson2;
    private Lesson lesson3;
    
    @BeforeEach
    public void setUp() {
        student = new Student("John", "Doe", "jdoe", "jdoe@example.com", 
                             "password", "dark", "blue");
        
        lesson1 = new Lesson("Lesson 1", "Description 1", null, null, null, null, null, 0, null);
        lesson2 = new Lesson("Lesson 2", "Description 2", null, null, null, null, null, 0, null);
        lesson3 = new Lesson("Lesson 3", "Description 3", null, null, null, null, null, 0, null);
    }
    
    @Test
    public void testConstructorAndBasicProperties() {
        assertEquals("student", student.getRole(), "Role should be student");
        assertTrue(student.getLessonObjects().isEmpty(), "Completed lessons should start empty");
        assertTrue(student.getAssignedLessons().isEmpty(), "Assigned lessons should start empty");
        
        // Test grade functions
        student.setGrade(10);
        assertEquals(Integer.valueOf(10), student.getStudentGrade(), "Grade should be set correctly");
    }
    
    @Test
    public void testLessonManagement() {
        // Test assigned lessons
        ArrayList<Lesson> assigned = new ArrayList<>();
        assigned.add(lesson1);
        assigned.add(lesson2);
        student.setAssignedLessons(assigned);
        
        assertEquals(2, student.getAssignedLessons().size(), "Should have 2 assigned lessons");
        
        // Test completing a lesson
        student.completeLesson(lesson1);
        assertTrue(student.getLessonObjects().contains(lesson1), 
                  "Lesson should be marked completed");
        
        // Test parent class tracking
        List<String> parentCompleted = student.getCompletedLessons();
        assertTrue(parentCompleted.contains(lesson1.toString()), 
                  "Parent should track completed lesson");
        
        // Test completing non-assigned lesson
        student.completeLesson(lesson3);
        assertFalse(student.getLessonObjects().contains(lesson3), 
                   "Non-assigned lesson should not be completed");
        
        // Test getting all lessons
        List<Lesson> allLessons = student.getAllLessons();
        assertEquals(assigned.size() + 1, allLessons.size(), 
                    "All lessons should include both lists");
    }
    
    @Test
    public void testEncapsulationAndEdgeCases() {
        // Test encapsulation issues
        ArrayList<Lesson> assigned = new ArrayList<>();
        assigned.add(lesson1);
        student.setAssignedLessons(assigned);
        
        ArrayList<Lesson> returned = student.getAssignedLessons();
        returned.add(lesson2);
        
        assertTrue(student.getAssignedLessons().contains(lesson2), 
                  "Modifying returned list affects internal state");
        
        // Test null handling
        assertDoesNotThrow(() -> {
            student.setAssignedLessons(null);
            student.getAssignedLessons(); // This will throw NPE if no null check in setter
            student.completeLesson(null); // This should not throw exception
        }, "Class should handle null values gracefully");
        
        // Test method naming confusion
        ArrayList<Lesson> completed = new ArrayList<>();
        completed.add(lesson3);
        student.setCompletedLessons(completed);
        
        assertEquals(completed, student.getLessonObjects(), 
                    "getLessonObjects returns completed lessons");
        
        // Test duplicate entries in getAllLessons
        ArrayList<Lesson> newAssigned = new ArrayList<>();
        newAssigned.add(lesson1);
        student.setAssignedLessons(newAssigned);
        student.completeLesson(lesson1);
        
        List<Lesson> allLessons = student.getAllLessons();
        int lesson1Count = 0;
        for (Lesson lesson : allLessons) {
            if (lesson.equals(lesson1)) lesson1Count++;
        }
        assertEquals(2, lesson1Count, "Lesson appears twice in getAllLessons");
    }
}