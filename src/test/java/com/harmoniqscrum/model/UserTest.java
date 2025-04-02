package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 * These tests validate user construction, password verification,
 * and behavior specific to user roles (student vs teacher).
 * 
 * @author Dreyton Merck
 */
public class UserTest {

    private User studentUser;
    private User teacherUser;

    /**
     * Initializes test users before each test method.
     */
    @BeforeEach
    void setUp() {
        studentUser = new User("Amy", "Anderson", "amy123", "amy@example.com", "pass", "student", "dark", "blue");
        teacherUser = new User("Mr", "Smith", "smithT", "smith@example.com", "teachpass", "teacher", "light", "green");
    }

    /**
     * Tests that a correct password matches using checkPassword().
     */
    @Test
    void testCheckPasswordCorrect() {
        assertTrue(studentUser.checkPassword("pass"));
    }

    /**
     * Tests that an incorrect password does not match using checkPassword().
     */
    @Test
    void testCheckPasswordIncorrect() {
        assertFalse(studentUser.checkPassword("wrong"));
    }

    /**
     * Tests adding a favorite song only once, avoiding duplicates.
     */
    @Test
    void testAddFavSongWorksOnce() {
        studentUser.addFavSong("song-1");
        studentUser.addFavSong("song-1"); // should not duplicate
        assertEquals(1, studentUser.getFavSongs().size());
    }

    /**
     * Tests removing a song from the favorite songs list.
     */
    @Test
    void testRemoveFavSong() {
        studentUser.addFavSong("song-1");
        studentUser.removeFavSong("song-1");
        assertFalse(studentUser.getFavSongs().contains("song-1"));
    }

    /**
     * Tests setting and getting the user's theme preference.
     */
    @Test
    void testSetAndGetTheme() {
        studentUser.setTheme("solarized");
        assertEquals("solarized", studentUser.getTheme());
    }

    /**
     * Tests setting and getting the user's highlight color preference.
     */
    @Test
    void testSetAndGetHighlightColor() {
        studentUser.setHighlightColor("purple");
        assertEquals("purple", studentUser.getHighlightColor());
    }

    /**
     * Tests that a student can set and retrieve a grade.
     */
    @Test
    void testStudentCanSetAndGetGrade() {
        studentUser.setGrade(87);
        assertEquals(87, studentUser.getGrade());
    }

    /**
     * Tests that a teacher cannot set or retrieve a grade.
     */
    @Test
    void testTeacherCannotSetGrade() {
        teacherUser.setGrade(92);
        assertNull(teacherUser.getGrade());
    }

    /**
     * Tests that completed lessons are not duplicated for students.
     */
    @Test
    void testStudentAddCompletedLessonOnce() {
        studentUser.addCompletedLesson("lesson-101");
        studentUser.addCompletedLesson("lesson-101");
        assertEquals(1, studentUser.getCompletedLessons().size());
    }

    /**
     * Tests that teachers have no access to completed lessons.
     */
    @Test
    void testTeacherCompletedLessonsIsNull() {
        assertNull(teacherUser.getCompletedLessons());
        teacherUser.addCompletedLesson("lesson-x");
        assertNull(teacherUser.getCompletedLessons());
    }

    /**
     * Tests that assigned students are only added once for a teacher.
     */
    @Test
    void testTeacherAddAssignedStudentOnce() {
        teacherUser.addAssignedStudent("student001");
        teacherUser.addAssignedStudent("student001");
        assertEquals(1, teacherUser.getAssignedStudents().size());
    }

    /**
     * Tests that students cannot assign students.
     */
    @Test
    void testStudentAssignedStudentsIsNull() {
        assertNull(studentUser.getAssignedStudents());
        studentUser.addAssignedStudent("someone");
        assertNull(studentUser.getAssignedStudents());
    }

    /**
     * Tests removing an assigned student from a teacher’s list.
     */
    @Test
    void testRemoveAssignedStudent() {
        teacherUser.addAssignedStudent("amy123");
        teacherUser.removeAssignedStudent("amy123");
        assertFalse(teacherUser.getAssignedStudents().contains("amy123"));
    }

    /**
     * Tests the format of the toString() method.
     */
    @Test
    void testToStringFormat() {
        assertEquals("Amy Anderson (amy123)", studentUser.toString());
    }

    /**
     * Tests the full constructor for users (e.g., loading from JSON).
     */
    @Test
    void testJsonConstructorWorksCorrectly() {
        UUID userId = UUID.randomUUID();
        ArrayList<String> favSongs = new ArrayList<>();
        favSongs.add("songA");
        ArrayList<String> lessons = new ArrayList<>();
        lessons.add("lessonA");
        ArrayList<String> students = new ArrayList<>();
        students.add("studentX");

        User loadedUser = new User(userId, "Amy", "Smith", "amy123", "amy@mail.com",
                "pw", "student", favSongs, "dark", "blue", 90, lessons, students);

        assertEquals("amy123", loadedUser.getUsername());
        assertEquals(90, loadedUser.getGrade());
        assertEquals(favSongs, loadedUser.getFavSongs());
        assertEquals(lessons, loadedUser.getCompletedLessons());
    }

    /**
     * Tests updating and retrieving the email field.
     */
    @Test
    void testEmailSetterAndGetter() {
        studentUser.setEmail("new@email.com");
        assertEquals("new@email.com", studentUser.getEmail());
    }

    /**
     * Tests updating and retrieving the password field.
     */
    @Test
    void testPasswordSetterAndGetter() {
        studentUser.setPassword("newpass");
        assertEquals("newpass", studentUser.getPassword());
    }

    /**
     * Tests updating and retrieving the first and last names.
     */
    @Test
    void testNamesSettersAndGetters() {
        studentUser.setFirstName("Anna");
        studentUser.setLastName("Brown");
        assertEquals("Anna", studentUser.getFirstName());
        assertEquals("Brown", studentUser.getLastName());
    }
}
