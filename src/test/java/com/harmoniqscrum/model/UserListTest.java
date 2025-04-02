package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserList class.
 * These tests validate the singleton behavior, user addition,
 * authentication, and retrieval.
 * 
 * @author Dreyton Merck
 */
class UserListTest {

    private UserList userList;
    private User student;
    private User teacher;

    /**
     * Setup before each test by clearing users and adding new sample users.
     */
    @BeforeEach
    void setUp() {
        userList = UserList.getInstance();
        userList.clearUsers();

        student = new User("Sam", "Student", "sam123", "sam@example.com", "pass123", "student", "dark", "blue");
        teacher = new User("Tom", "Teacher", "tom456", "tom@example.com", "teach456", "teacher", "light", "green");

        userList.addUser(student);
        userList.addUser(teacher);
    }

    /**
     * Ensure that existing usernames are correctly identified.
     */
    @Test
    void testHaveUserReturnsTrueIfExists() {
        assertTrue(userList.haveUser("sam123"));
        assertTrue(userList.haveUser("tom456"));
    }

    /**
     * Ensure that unknown usernames return false.
     */
    @Test
    void testHaveUserReturnsFalseIfNotExists() {
        assertFalse(userList.haveUser("ghost"));
    }

    /**
     * Ensure duplicate usernames are not allowed.
     */
    @Test
    void testAddUserRejectsDuplicateUsername() {
        boolean added = userList.addUser("Fake", "Duplicate", "sam123", "new@example.com", "newpass", "student", "light", "red");
        assertFalse(added);
        assertEquals(2, userList.getUsers().size());
    }

    /**
     * Test successful user authentication.
     */
    @Test
    void testAuthenticateUserSuccess() {
        User result = userList.authenticateUser("sam123", "pass123");
        assertNotNull(result);
        assertEquals("Sam", result.getFirstName());
        assertEquals(result, userList.getCurrentUser());
    }

    /**
     * Test authentication with an incorrect password.
     */
    @Test
    void testAuthenticateUserFailsOnWrongPassword() {
        User result = userList.authenticateUser("sam123", "wrongpass");
        assertNull(result);
        assertNull(userList.getCurrentUser());
    }

    /**
     * Test authentication with a non-existent user.
     */
    @Test
    void testAuthenticateUserFailsOnNonexistentUser() {
        User result = userList.authenticateUser("ghost", "pass123");
        assertNull(result);
        assertNull(userList.getCurrentUser());
    }

    /**
     * Test that the correct user is retrieved by username.
     */
    @Test
    void testGetUserReturnsCorrectUser() {
        User found = userList.getUser("tom456");
        assertNotNull(found);
        assertEquals("Tom", found.getFirstName());
    }

    /**
     * Test that getUser returns null for unknown usernames.
     */
    @Test
    void testGetUserReturnsNullForUnknownUsername() {
        assertNull(userList.getUser("nonexistent"));
    }

    /**
     * Test that clearUsers empties both the user list and the session.
     */
    @Test
    void testClearUsersEmptiesListAndSession() {
        userList.authenticateUser("sam123", "pass123");
        userList.clearUsers();

        assertTrue(userList.getUsers().isEmpty());
        assertNull(userList.getCurrentUser());
    }

    /**
     * Test adding multiple distinct users works as expected.
     */
    @Test
    void testMultipleUsersAddedSuccessfully() {
        userList.addUser(new User("Jane", "Doe", "jane1", "jane@example.com", "abc123", "student", "light", "yellow"));
        userList.addUser(new User("Rick", "Astley", "rickroll", "rick@example.com", "never", "teacher", "retro", "pink"));

        assertEquals(4, userList.getUsers().size());
        assertTrue(userList.haveUser("jane1"));
        assertTrue(userList.haveUser("rickroll"));
    }
}
