package com.harmoniqscrum.model;

public abstract class DataConstants {
    // File paths
    protected static final String USER_FILE_NAME = "src/main/java/com/harmoniqscrum/data/json/Users.json";
    protected static final String USER_TEMP_FILE_NAME = "src/main/java/com/harmoniqscrum/data/json/Users_temp.json";
    
    // User JSON fields
    protected static final String USER_ID = "userId";
    protected static final String USER_FIRST_NAME = "firstName";
    protected static final String USER_LAST_NAME = "lastName";
    protected static final String USER_USERNAME = "username";
    protected static final String USER_EMAIL = "email";
    protected static final String USER_PASSWORD = "password";
    protected static final String USER_ROLE = "role";
    protected static final String USER_FAV_SONGS = "favSongs";
    protected static final String USER_SETTINGS = "userSettings";
    protected static final String USER_THEME = "theme";
    protected static final String USER_HIGHLIGHT_COLOR = "highlightColor";
    
    // Student-specific fields
    protected static final String USER_GRADE = "grade";
    protected static final String USER_COMPLETED_LESSONS = "completedLessons";
    
    // Teacher-specific fields
    protected static final String USER_ASSIGNED_STUDENTS = "assignedStudents";
} 