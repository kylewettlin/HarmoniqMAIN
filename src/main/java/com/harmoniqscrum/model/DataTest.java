package com.harmoniqscrum.model;

public class DataTest {
    public static void main(String[] args) {
        // Test loading users
        System.out.println("Loading users from JSON...");
        UserList userList = UserList.getInstance();
        
        // Display all loaded users
        System.out.println("\nLoaded Users:");
        for (User user : userList.getUsers()) {
            System.out.println(user);
            System.out.println("  Username: " + user.getUsername());
            System.out.println("  Role: " + user.getRole());
            
            // Display user-specific details based on role
            if ("student".equals(user.getRole())) {
                System.out.println("  Grade: " + user.getGrade());
                System.out.println("  Completed Lessons: " + user.getCompletedLessons());
            } else if ("teacher".equals(user.getRole())) {
                System.out.println("  Assigned Students: " + user.getAssignedStudents());
            }
            
            System.out.println("  Favorite Songs: " + user.getFavSongs());
            System.out.println();
        }
        
        // Adding a new test user
        System.out.println("Adding a new user...");
        boolean success = userList.addUser(
            "Test", 
            "User", 
            "testuser", 
            "test@example.com", 
            "password123", 
            "student", 
            "dark", 
            "#FF5733"
        );
        
        if (success) {
            System.out.println("Successfully added new user.");
            
            // Save to file
            System.out.println("Saving all users...");
            userList.saveUsers();
            System.out.println("Users saved successfully!");
        } else {
            System.out.println("Failed to add user. Username may already exist.");
        }
    }
} 