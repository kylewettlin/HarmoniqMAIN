package com.harmoniqscrum.model;

import com.harmoniqscrum.model.User;
import com.harmoniqscrum.model.UserList;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {

    private UserList userList;

    @BeforeEach
    void setUp() {
        userList = UserList.getInstance();
        userList.clearUsers();
    }

    @Test
    void testAddUserObject() {
        User user = new User("John", "Doe", "jdoe", "jdoe@email.com", "pass123", "student", "dark", "blue");
        userList.addUser(user);
        assertTrue(userList.getUsers().contains(user));
    }

    @Test
    void testRemoveUser() {
        User user = new User("Jane", "Smith", "jsmith", "jsmith@email.com", "password", "teacher", "light", "red");
        userList.addUser(user);
        userList.removeUser(user);
        assertFalse(userList.getUsers().contains(user));
    }

    @Test
    void testHaveUserWhenExists() {
        userList.addUser("Test", "User", "testuser", "test@email.com", "1234", "student", "dark", "blue");
        assertTrue(userList.haveUser("testuser"));
    }

    @Test
    void testHaveUserWhenNotExists() {
        assertFalse(userList.haveUser("nonexistent"));
    }

    @Test
    void testGetUserWhenExists() {
        userList.addUser("Alex", "King", "aking", "alex@email.com", "pass", "teacher", "light", "green");
        User retrieved = userList.getUser("aking");
        assertNotNull(retrieved);
        assertEquals("aking", retrieved.getUsername());
    }

    @Test
    void testGetUserWhenNotExists() {
        assertNull(userList.getUser("ghost"));
    }

    @Test
    void testAddUserWithExistingUsername() {
        userList.addUser("Tom", "Brady", "tbrady", "tom@email.com", "football", "student", "dark", "blue");
        boolean result = userList.addUser("Tom", "Brady", "tbrady", "duplicate@email.com", "newpass", "teacher", "light", "red");
        assertFalse(result);
    }

    @Test
    void testAuthenticateUserSuccess() {
        userList.addUser("Erin", "Lee", "elee", "erin@email.com", "mypassword", "student", "light", "yellow");
        User loggedIn = userList.authenticateUser("elee", "mypassword");
        assertNotNull(loggedIn);
        assertEquals("elee", loggedIn.getUsername());
    }

    @Test
    void testAuthenticateUserFailure() {
        userList.addUser("Erin", "Lee", "elee", "erin@email.com", "mypassword", "student", "light", "yellow");
        User failedLogin = userList.authenticateUser("elee", "wrongpass");
        assertNull(failedLogin);
    }

    @Test
    void testGetCurrentUserAfterLogin() {
        userList.addUser("Bob", "Dylan", "bdylan", "bob@email.com", "guitar", "teacher", "dark", "gray");
        userList.authenticateUser("bdylan", "guitar");
        User current = userList.getCurrentUser();
        assertNotNull(current);
        assertEquals("bdylan", current.getUsername());
    }

    @Test
    void testClearUsers() {
        userList.addUser("Clear", "Test", "clearuser", "clear@email.com", "clearpass", "student", "dark", "black");
        userList.clearUsers();
        assertEquals(0, userList.getUsers().size());
        assertNull(userList.getCurrentUser());
    }
}

