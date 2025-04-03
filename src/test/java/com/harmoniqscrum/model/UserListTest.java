package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Unit tests for the UserList class
 * 
 * @author Dreyton Merck
 */
public class UserListTest {
    
    private UserList userList;
    
    @BeforeEach
    public void setUp() {
        userList = UserList.getInstance();
        userList.clearUsers();
    }
    
    @Test
    public void testSingletonAndBasicOperations() {
        // Test singleton pattern
        UserList instance2 = UserList.getInstance();
        assertSame(userList, instance2, "getInstance() should return same instance");
        
        // Test adding and retrieving users
        userList.addUser("John", "Doe", "jdoe", "jdoe@example.com", 
                        "password", "student", "dark", "blue");
        
        assertTrue(userList.haveUser("jdoe"), "User should exist");
        User user = userList.getUser("jdoe");
        assertNotNull(user, "Should retrieve added user");
        assertEquals("jdoe", user.getUsername(), "Username should match");
        
        // Test adding duplicate username
        boolean result = userList.addUser("Jane", "Smith", "jdoe", 
                                         "different@example.com", "password2", 
                                         "teacher", "light", "red");
        assertFalse(result, "Should reject duplicate username");
    }
    
    @Test
    public void testAuthentication() {
        userList.addUser("John", "Doe", "jdoe", "jdoe@example.com", 
                        "password", "student", "dark", "blue");
        
        // Test successful authentication
        User authenticated = userList.authenticateUser("jdoe", "password");
        assertNotNull(authenticated, "Authentication should succeed with correct credentials");
        assertEquals(authenticated, userList.getCurrentUser(), "Current user should be set");
        
        // Test failed authentication
        User failedAuth = userList.authenticateUser("jdoe", "wrongpassword");
        assertNull(failedAuth, "Authentication should fail with wrong password");
        
        // Test case sensitivity
        User caseSensitiveAuth = userList.authenticateUser("JDOE", "password");
        assertNull(caseSensitiveAuth, "Username should be case-sensitive");
    }
    
    @Test
    public void testUserManagement() {
        User user = new User("John", "Doe", "jdoe", "jdoe@example.com", 
                            "password", "student", "dark", "blue");
        userList.addUser(user);
        
        // Test user removal
        userList.removeUser(user);
        assertFalse(userList.haveUser("jdoe"), "User should be removed");
        
        // Test clear users
        userList.addUser(user);
        userList.clearUsers();
        assertEquals(0, userList.getUsers().size(), "User list should be empty after clearing");
        assertNull(userList.getCurrentUser(), "Current user should be null after clearing");
    }
    
    @Test
    public void testEncapsulationAndEdgeCases() {
        userList.addUser("John", "Doe", "jdoe", "jdoe@example.com", 
                        "password", "student", "dark", "blue");
        
        // Test encapsulation of users list
        ArrayList<User> returnedList = userList.getUsers();
        int originalSize = returnedList.size();
        
        returnedList.add(new User("Jane", "Smith", "jsmith", "jsmith@example.com", 
                                 "password", "teacher", "light", "red"));
        
        ArrayList<User> newList = userList.getUsers();
        assertEquals(originalSize + 1, newList.size(), 
                    "Internal list should be affected by modifying returned list");
        
        // Test failed authentication doesn't change current user
        userList.authenticateUser("jdoe", "password");
        User currentUser = userList.getCurrentUser();
        
        userList.authenticateUser("nonexistent", "password");
        assertSame(currentUser, userList.getCurrentUser(), 
                  "Current user shouldn't change after failed authentication");
    }
}