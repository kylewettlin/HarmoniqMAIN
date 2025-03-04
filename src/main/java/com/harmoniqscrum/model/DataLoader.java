package com.harmoniqscrum.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLoader extends DataConstants {
    
    /**
     * Loads users from the JSON file
     * @return ArrayList of User objects
     */
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        
        try {
            FileReader reader = new FileReader(USER_FILE_NAME);
            JSONArray usersJSON = (JSONArray)new JSONParser().parse(reader);
            
            for(int i=0; i < usersJSON.size(); i++) {
                JSONObject userJSON = (JSONObject)usersJSON.get(i);
                
                // Extract basic user data
                UUID userId = UUID.fromString((String)userJSON.get(USER_ID));
                String firstName = (String)userJSON.get(USER_FIRST_NAME);
                String lastName = (String)userJSON.get(USER_LAST_NAME);
                String username = (String)userJSON.get(USER_USERNAME);
                String email = (String)userJSON.get(USER_EMAIL);
                String password = (String)userJSON.get(USER_PASSWORD);
                String role = (String)userJSON.get(USER_ROLE);
                String theme = (String)userJSON.get(USER_THEME);
                String highlightColor = (String)userJSON.get(USER_HIGHLIGHT_COLOR);
                
                // Get favorite songs list
                ArrayList<String> favSongs = new ArrayList<>();
                JSONArray favSongsJSON = (JSONArray)userJSON.get(USER_FAV_SONGS);
                if (favSongsJSON != null) {
                    for (int j = 0; j < favSongsJSON.size(); j++) {
                        favSongs.add((String)favSongsJSON.get(j));
                    }
                }
                
                // Get student-specific data
                Integer grade = null;
                ArrayList<String> completedLessons = null;
                if ("student".equals(role)) {
                    if (userJSON.get(USER_GRADE) != null) {
                        grade = ((Long)userJSON.get(USER_GRADE)).intValue();
                    }
                    
                    completedLessons = new ArrayList<>();
                    JSONArray lessonsJSON = (JSONArray)userJSON.get(USER_COMPLETED_LESSONS);
                    if (lessonsJSON != null) {
                        for (int j = 0; j < lessonsJSON.size(); j++) {
                            completedLessons.add((String)lessonsJSON.get(j));
                        }
                    }
                }
                
                // Get teacher-specific data
                ArrayList<String> assignedStudents = null;
                if ("teacher".equals(role)) {
                    assignedStudents = new ArrayList<>();
                    JSONArray studentsJSON = (JSONArray)userJSON.get(USER_ASSIGNED_STUDENTS);
                    if (studentsJSON != null) {
                        for (int j = 0; j < studentsJSON.size(); j++) {
                            assignedStudents.add((String)studentsJSON.get(j));
                        }
                    }
                }
                
                // Create and add the user
                users.add(new User(userId, firstName, lastName, username, email, 
                                  password, role, favSongs, theme, highlightColor, 
                                  grade, completedLessons, assignedStudents));
            }
            
            reader.close();
            return users;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     * Test method to load and display users
     */
    public static void main(String[] args) {
        ArrayList<User> users = DataLoader.getUsers();

        for(User user : users) {
            System.out.println(user);
        }
    }
} 