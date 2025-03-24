package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String role;
    private ArrayList<String> favSongs;
    private String theme;
    private String highlightColor;
    
    // Student-specific fields
    private Integer grade;
    private ArrayList<String> completedLessons;
    
    // Teacher-specific fields
    private ArrayList<String> assignedStudents;
    
    // Constructor for creating a new user
    public User(String firstName, String lastName, String username, String email, 
               String password, String role, String theme, String highlightColor) {
        this.userId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.theme = theme;
        this.highlightColor = highlightColor;
        this.favSongs = new ArrayList<>();
        
        if ("student".equals(role)) {
            this.completedLessons = new ArrayList<>();
        } else if ("teacher".equals(role)) {
            this.assignedStudents = new ArrayList<>();
        }
    }
    
    // Constructor when loading from JSON
    public User(UUID userId, String firstName, String lastName, String username, String email,
               String password, String role, ArrayList<String> favSongs, String theme, 
               String highlightColor, Integer grade, ArrayList<String> completedLessons,
               ArrayList<String> assignedStudents) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.favSongs = favSongs;
        this.theme = theme;
        this.highlightColor = highlightColor;
        this.grade = grade;
        this.completedLessons = completedLessons;
        this.assignedStudents = assignedStudents;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Check if the provided password matches the user's password
     * 
     * @param password The password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public String getRole() {
        return role;
    }
    
    public ArrayList<String> getFavSongs() {
        return favSongs;
    }
    
    public void addFavSong(String songId) {
        if (!favSongs.contains(songId)) {
            favSongs.add(songId);
        }
    }
    
    public void removeFavSong(String songId) {
        favSongs.remove(songId);
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getHighlightColor() {
        return highlightColor;
    }
    
    public void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }
    
    public Integer getGrade() {
        return grade;
    }
    
    public void setGrade(Integer grade) {
        if ("student".equals(role)) {
            this.grade = grade;
        }
    }
    
    public ArrayList<String> getCompletedLessons() {
        return completedLessons;
    }
    
    public void addCompletedLesson(String lessonId) {
        if ("student".equals(role) && !completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
        }
    }
    
    public ArrayList<String> getAssignedStudents() {
        return assignedStudents;
    }
    
    public void addAssignedStudent(String studentId) {
        if ("teacher".equals(role) && !assignedStudents.contains(studentId)) {
            assignedStudents.add(studentId);
        }
    }
    
    public void removeAssignedStudent(String studentId) {
        if ("teacher".equals(role)) {
            assignedStudents.remove(studentId);
        }
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + username + ")";
    }
} 