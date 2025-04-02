package com.harmoniqscrum.model;

import com.harmoniqscrum.model.Student;
import com.harmoniqscrum.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Student class.
 * 
 * @author Dreyton Merck
 */
public class StudentTest {

    private Student student;
    private Lesson lesson;

    @BeforeEach
    public void setup() {
        student = new Student("Test", "Student", "teststudent", "test@student.com", "password", "dark", "blue");
        lesson = new Lesson("L01", "Intro", "Basic lesson", new ArrayList<>(), "Easy", new ArrayList<>(), new ArrayList<>(), 10, "Jazz");
    }

    @Test
    public void testCompleteLesson_notAssigned() {
        student.completeLesson(lesson);
        assertEquals(0, student.getLessonObjects().size());
    }

    @Test
    public void testCompleteLesson_nullAssignedLessons() {
        student.setAssignedLessons(null);
        assertThrows(NullPointerException.class, () -> {
            student.completeLesson(lesson);
        });
    }

    @Test
    public void testCompleteLesson_nullCompletedLessons() {
        student.getAssignedLessons().add(lesson);
        student.setCompletedLessons(null);
        assertThrows(NullPointerException.class, () -> {
            student.completeLesson(lesson);
        });
    }

    @Test
    public void testCompleteLesson_nullLesson() {
        student.completeLesson(null);
        assertEquals(0, student.getLessonObjects().size());
    }

    @Test
    public void testAssignInvalidLessonObject() {
        ArrayList rawList = new ArrayList();
        rawList.add("not a lesson");
        student.setAssignedLessons(rawList);
        assertTrue(student.getAssignedLessons().contains("not a lesson"));
    }

    @Test
    public void testDuplicateLessonCompletion() {
        student.getAssignedLessons().add(lesson);
        student.completeLesson(lesson);
        student.completeLesson(lesson); // attempt again
        assertEquals(1, student.getLessonObjects().size());
    }

    @Test
    public void testGradeDefaultsToNull() {
        assertNull(student.getStudentGrade());
    }

    @Test
    public void testSetGradeToZero() {
        student.setGrade(0);
        assertEquals(0, student.getStudentGrade());
    }

    @Test
    public void testAllLessonsWithNullLists() {
        student.setAssignedLessons(null);
        student.setCompletedLessons(null);
        assertThrows(NullPointerException.class, () -> {
            student.getAllLessons();
        });
    }
}
