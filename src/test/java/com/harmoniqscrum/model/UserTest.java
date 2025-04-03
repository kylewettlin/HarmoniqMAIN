package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * tests for the User class using JSON data
 * integrity and role-specific behaviors.
 * 
 * @author Chase Tegtmeier McCracken
 */

public class UserTest {

    private User aliceStudent;
    private User bobTeacher;

    @BeforeEach
    void setUp() {
        // Initialize with Alice (student) and Bob (teacher) from JSON
        aliceStudent = new User(
            UUID.fromString("9bba704b-a86b-4795-a513-afc08baf9d46"),
            "Alice",
            "Johnson",
            "alicej",
            "alice@example.com",
            "secure123",
            "student",
            new ArrayList<>(),
            "dark",
            "#3498db",
            10,
            new ArrayList<>(List.of("l1")),
            null
        );
        
        bobTeacher = new User(
            UUID.fromString("9fbf9aeb-0a8d-4fc2-b632-725a4b3fedd6"),
            "Bob",
            "Smith",
            "bobtheteacher",
            "bob@example.com",
            "teach456",
            "teacher",
            new ArrayList<>(),
            "light",
            "#e74c3c",
            null,
            null,
            new ArrayList<>(List.of("u1"))
        );
    }

    @Test
    void testAliceStudentMatchesJsonData() {
        assertEquals("Alice Johnson (alicej)", aliceStudent.toString());
        assertEquals(10, aliceStudent.getGrade());
        assertTrue(aliceStudent.getCompletedLessons().contains("l1"));
        assertEquals("#3498db", aliceStudent.getHighlightColor());
        assertTrue(aliceStudent.getFavSongs().isEmpty());
    }

    @Test
    void testBobTeacherMatchesJsonData() {
        assertEquals("Bob Smith (bobtheteacher)", bobTeacher.toString());
        assertEquals(1, bobTeacher.getAssignedStudents().size());
        assertTrue(bobTeacher.getAssignedStudents().contains("u1"));
        assertEquals("light", bobTeacher.getTheme());
    }


    @Test
    void testAddDuplicateCompletedLesson() {
        aliceStudent.addCompletedLesson("l1"); // Already exists in JSON
        assertEquals(1, aliceStudent.getCompletedLessons().size());
    }

    @Test
    void testUpdateAliceGrade() {
        aliceStudent.setGrade(12);
        assertEquals(12, aliceStudent.getGrade());
    }
    
    @Test
    void testAddNewAssignedStudent() {
        bobTeacher.addAssignedStudent("u2");
        assertEquals(2, bobTeacher.getAssignedStudents().size());
    }

    @Test
    void testRemoveAssignedStudentU1() {
        bobTeacher.removeAssignedStudent("u1");
        assertFalse(bobTeacher.getAssignedStudents().contains("u1"));
    }

    @Test
    void testDefaultStudentWithEmptyLists() {
        User testUser = new User(
            UUID.fromString("89cd05e4-566e-4afd-9802-b8728a00bce9"),
            "Test",
            "User",
            "testuser",
            "test@example.com",
            "password123",
            "student",
            new ArrayList<>(),
            "dark",
            "#FF5733",
            null,
            new ArrayList<>(),
            null
        );
        
        assertTrue(testUser.getCompletedLessons().isEmpty());
        assertTrue(testUser.getFavSongs().isEmpty());
    }
    
    @Test
    void testAlicePasswordCheck() {
        assertTrue(aliceStudent.checkPassword("secure123"));
        assertFalse(aliceStudent.checkPassword("wrongpass"));
    }

    @Test
    void testThemeUpdateForBob() {
        bobTeacher.setTheme("solarized");
        assertEquals("solarized", bobTeacher.getTheme());
    }
}
