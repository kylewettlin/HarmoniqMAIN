package com.harmoniqscrum.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Unit tests for the UserList class
 * 
 * @author Dreyton Merck
 */
public class UserListTest {
    
    private UserList userList;
    
    @Before
    public void setUp() {
        userList = UserList.getInstance();
        userList.clearUsers();
    }
    
    @Test
    public void testSingletonAndBasicOperations() {
        // Test singleton pattern
        UserList instance2 = UserList.getInstance();
        assertSame("getInstance() should return same instance", userList, instance2);
        
        // Test adding and retrieving users
        userList.addUser("John", "Doe", "jdoe", "jdoe@example.com", 
                        "password", "student", "dark", "blue");
        
        assertTrue("User should exist", userList.haveUser("jdoe"));
        User user = userList.getUser("jdoe");
        assertNotNull("Should retrieve added user", user);
        assertEquals("Username should match", "jdoe", user.getUsername());
        
        // Test adding duplicate username
        boolean result = userList.addUser("Jane", "Smith", "jdoe", 
                                         "different@example.com", "password2", 
                                         "teacher", "light", "red");
        assertFalse("Should reject duplicate username", result);
    }
    
    @Test
    public void testAuthentication() {
        userList.addUser("John", "Doe", "jdoe", "jdoe@example.com", 
                        "password", "student", "dark", "blue");
        
        // Test successful authentication
        User authenticated = userList.authenticateUser("jdoe", "password");
        assertNotNull("Authentication should succeed with correct credentials", authenticated);
        assertEquals("Current user should be set", authenticated, userList.getCurrentUser());
        
        // Test failed authentication
        User failedAuth = userList.authenticateUser("jdoe", "wrongpassword");
        assertNull("Authentication should fail with wrong password", failedAuth);
        
        // Test case sensitivity
        User caseSensitiveAuth = userList.authenticateUser("JDOE", "password");
        assertNull("Username should be case-sensitive", caseSensitiveAuth);
    }
    
    @Test
    public void testUserManagement() {
        User user = new User("John", "Doe", "jdoe", "jdoe@example.com", 
                            "password", "student", "dark", "blue");
        userList.addUser(user);
        
        // Test user removal
        userList.removeUser(user);
        assertFalse("User should be removed", userList.haveUser("jdoe"));
        
        // Test clear users
        userList.addUser(user);
        userList.clearUsers();
        assertEquals("User list should be empty after clearing", 0, userList.getUsers().size());
        assertNull("Current user should be null after clearing", userList.getCurrentUser());
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
        assertEquals("Internal list should be affected by modifying returned list", 
                    originalSize + 1, newList.size());
        
        // Test failed authentication doesn't change current user
        userList.authenticateUser("jdoe", "password");
        User currentUser = userList.getCurrentUser();
        
        userList.authenticateUser("nonexistent", "password");
        assertSame("Current user shouldn't change after failed authentication", 
                  currentUser, userList.getCurrentUser());
    }
}