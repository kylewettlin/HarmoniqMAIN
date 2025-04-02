package com.harmoniqscrum.model;

import com.harmoniqscrum.model.Teacher;
import com.harmoniqscrum.model.Student;
import com.harmoniqscrum.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Teacher class.
 * 
 * @author Dreyton Merck
 */
public class TeacherTest {

    private Teacher teacher;
    private Student student;
    private Lesson lesson;

    /**
     * Initializes a teacher and student for testing.
     */
    @BeforeEach
    public void setup() {
        teacher = new Teacher("Teach", "Er", "teach1", "teach@test.com", "pass", "light", "green");
        student = new Student("Stu", "Dent", "student1", "stu@test.com", "word", "dark", "blue");
        lesson = new Lesson("L01", "Basics", "Intro", new ArrayList<>(), "Easy", new ArrayList<>(), new ArrayList<>(), 15, "Rock");
    }

    /**
     * Tests adding a student to the teacher.
     */
    @Test
    public void testAddStudent() {
        teacher.addStudent(student);
        assertTrue(teacher.getStudents().contains(student));
    }

    /**
     * Tests removing a student from the teacher.
     */
    @Test
    public void testRemoveStudent() {
        teacher.addStudent(student);
        teacher.removeStudent(student);
        assertFalse(teacher.getStudents().contains(student));
    }

    /**
     * Tests assigning a lesson to a student.
     */
    @Test
    public void testCreateAndAssignLesson() {
        teacher.addStudent(student);
        teacher.createAndAssignLesson(lesson, student);
        assertTrue(student.getAssignedLessons().contains(lesson));
    }

    /**
     * Tests assigning a lesson to a student not in the list.
     */
    @Test
    public void testCreateAndAssignLesson_NotInList() {
        teacher.createAndAssignLesson(lesson, student);
        assertFalse(student.getAssignedLessons().contains(lesson));
    }

    /**
     * Tests assigning null student.
     */
    @Test
    public void testAddNullStudent() {
        teacher.addStudent(null);
        assertTrue(teacher.getStudents().contains(null));
    }
}
