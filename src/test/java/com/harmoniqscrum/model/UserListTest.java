package com.harmoniqscrum.model;

import com.harmoniqscrum.model.User;
import com.harmoniqscrum.model.UserList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserList singleton class.
 */
public class UserListTest {

    private UserList userList;

    /**
     * Sets up a fresh UserList before each test.
     */
    @BeforeEach
    public void setup() {
        userList = UserList.getInstance();
        userList.clearUsers();
    }

    /**
     * Tests adding a user successfully.
     */
    @Test
    public void testAddUser_Success() {
        boolean result = userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        assertTrue(result);
        assertEquals(1, userList.getUsers().size());
    }

    /**
     * Tests adding a user with a duplicate username.
     */
    @Test
    public void testAddUser_Duplicate() {
        userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        boolean result = userList.addUser("John", "Doe", "janedoe", "john@test.com", "pass", "student", "dark", "blue");
        assertFalse(result);
    }

    /**
     * Tests user authentication success.
     */
    @Test
    public void testAuthenticateUser_Success() {
        userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        User user = userList.authenticateUser("janedoe", "pass");
        assertNotNull(user);
    }

    /**
     * Tests failed authentication with wrong password.
     */
    @Test
    public void testAuthenticateUser_WrongPassword() {
        userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        User user = userList.authenticateUser("janedoe", "wrong");
        assertNull(user);
    }

    /**
     * Tests removing a user.
     */
    @Test
    public void testRemoveUser() {
        userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        User user = userList.getUser("janedoe");
        userList.removeUser(user);
        assertEquals(0, userList.getUsers().size());
    }

    /**
     * Tests currentUser is stored after login.
     */
    @Test
    public void testCurrentUser() {
        userList.addUser("Jane", "Doe", "janedoe", "jane@test.com", "pass", "student", "dark", "blue");
        userList.authenticateUser("janedoe", "pass");
        assertNotNull(userList.getCurrentUser());
    }

    /**
     * Tests clearing the user list.
     */
    @Test
    public void testClearUsers() {
        userList.addUser("Test", "User", "testuser", "test@test.com", "123", "student", "dark", "red");
        userList.clearUsers();
        assertEquals(0, userList.getUsers().size());
        assertNull(userList.getCurrentUser());
    }
}
