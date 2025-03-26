package com.harmoniqscrum.model;

import java.util.ArrayList;

/**
 * The UserList class manages the list of users in the application.
 * It uses the singleton pattern to ensure only one instance is used.
 * 
 * It supports adding, removing, retrieving, and authenticating users,
 * and it also tracks the currently logged-in user.
 * 
 * User data is loaded using DataLoader and can be saved with DataWriter.
 */
public class UserList {
    private static UserList Instance;
    private ArrayList<User> users;
    private User currentUser;

    /**
     * Private constructor that initializes the list of users
     * by loading them from persistent storage.
     */
    private UserList() {
        this.users = DataLoader.getUsers();
    }

    /**
     * Returns the singleton instance of UserList.
     *
     * @return the single shared instance of UserList
     */
    public static UserList getInstance() {
        if (Instance == null) {
            Instance = new UserList();
        }
        return Instance;
    }

    /**
     * Adds a User object to the list.
     *
     * @param user the user to be added
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a User object from the list.
     *
     * @param user the user to be removed
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /**
     * Returns the list of all users.
     *
     * @return an ArrayList containing all users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    public boolean haveUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return the User with the matching username, or null if not found
     */
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Creates and adds a new user with the given information.
     * If a user with the same username already exists, the user will not be added.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param username the desired username
     * @param email the user's email
     * @param password the user's password
     * @param role the user's role (e.g., "student" or "teacher")
     * @param theme the user's chosen theme
     * @param highlightColor the user's highlight color
     * @return true if the user was successfully added, false if the username already exists
     */
    public boolean addUser(String firstName, String lastName, String username,
                           String email, String password, String role,
                           String theme, String highlightColor) {
        if (haveUser(username)) return false;

        users.add(new User(firstName, lastName, username, email,
                           password, role, theme, highlightColor));
        return true;
    }

    /**
     * Authenticates a user by username and password.
     * If the credentials match, the user is stored as the current user.
     *
     * @param username the username to check
     * @param password the password to validate
     * @return the authenticated User object, or null if authentication fails
     */
    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    /**
     * Returns the currently authenticated user.
     *
     * @return the current logged-in user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Clears all users from the list and resets the current user.
     * Useful for testing or resetting the system state.
     */
    public void clearUsers() {
        users.clear();
        currentUser = null;
    }

    /**
     * Saves the current list of users to persistent storage using DataWriter.
     */
    public void saveUsers() {
        DataWriter.saveUsers();
    }
}