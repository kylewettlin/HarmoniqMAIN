package com.harmoniqscrum.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    
    @Before
    public void setUp() {
        student = new Student("John", "Doe", "jdoe", "jdoe@example.com", 
                             "password", "dark", "blue");
        
        lesson1 = new Lesson("Lesson 1", "Description 1");
        lesson2 = new Lesson("Lesson 2", "Description 2");
        lesson3 = new Lesson("Lesson 3", "Description 3");
    }
    
    @Test
    public void testConstructorAndBasicProperties() {
        assertEquals("Role should be student", "student", student.getRole());
        assertTrue("Completed lessons should start empty", student.getLessonObjects().isEmpty());
        assertTrue("Assigned lessons should start empty", student.getAssignedLessons().isEmpty());
        
        // Test grade functions
        student.setGrade(10);
        assertEquals("Grade should be set correctly", Integer.valueOf(10), student.getStudentGrade());
    }
    
    @Test
    public void testLessonManagement() {
        // Test assigned lessons
        ArrayList<Lesson> assigned = new ArrayList<>();
        assigned.add(lesson1);
        assigned.add(lesson2);
        student.setAssignedLessons(assigned);
        
        assertEquals("Should have 2 assigned lessons", 2, student.getAssignedLessons().size());
        
        // Test completing a lesson
        student.completeLesson(lesson1);
        assertTrue("Lesson should be marked completed", 
                  student.getLessonObjects().contains(lesson1));
        
        // Test parent class tracking
        List<String> parentCompleted = student.getCompletedLessons();
        assertTrue("Parent should track completed lesson", 
                  parentCompleted.contains(lesson1.toString()));
        
        // Test completing non-assigned lesson
        student.completeLesson(lesson3);
        assertFalse("Non-assigned lesson should not be completed", 
                   student.getLessonObjects().contains(lesson3));
        
        // Test getting all lessons
        List<Lesson> allLessons = student.getAllLessons();
        assertEquals("All lessons should include both lists", 
                    assigned.size() + 1, allLessons.size());
    }
    
    @Test
    public void testEncapsulationAndEdgeCases() {
        // Test encapsulation issues
        ArrayList<Lesson> assigned = new ArrayList<>();
        assigned.add(lesson1);
        student.setAssignedLessons(assigned);
        
        ArrayList<Lesson> returned = student.getAssignedLessons();
        returned.add(lesson2);
        
        assertTrue("Modifying returned list affects internal state", 
                  student.getAssignedLessons().contains(lesson2));
        
        // Test null handling
        try {
            student.setAssignedLessons(null);
            student.getAssignedLessons(); // This will throw NPE if no null check in setter
            student.completeLesson(null); // This should not throw exception
        } catch (NullPointerException e) {
            fail("Class should handle null values gracefully");
        }
        
        // Test method naming confusion
        ArrayList<Lesson> completed = new ArrayList<>();
        completed.add(lesson3);
        student.setCompletedLessons(completed);
        
        assertEquals("getLessonObjects returns completed lessons", 
                    completed, student.getLessonObjects());
        
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
        assertEquals("Lesson appears twice in getAllLessons", 2, lesson1Count);
    }
}