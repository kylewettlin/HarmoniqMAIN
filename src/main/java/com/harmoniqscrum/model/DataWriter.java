package com.harmoniqscrum.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class DataWriter extends DataConstants {
    
    /**
     * Saves all users to the JSON file
     */
    public static void saveUsers() {
        UserList userList = UserList.getInstance();
        ArrayList<User> users = userList.getUsers();
        
        JSONArray jsonUsers = new JSONArray();
        
        // Creating all the JSON objects
        for(int i=0; i < users.size(); i++) {
            jsonUsers.add(getUserJSON(users.get(i)));
        }
        
        // Write JSON file with pretty formatting
        try (FileWriter file = new FileWriter(USER_FILE_NAME)) {
            String prettyJson = formatJsonString(jsonUsers.toJSONString());
            file.write(prettyJson);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Converts a User object to a JSONObject
     * @param user The user to convert
     * @return JSONObject representing the user
     */
    public static JSONObject getUserJSON(User user) {
        JSONObject userDetails = new JSONObject();
        
        // Add basic user properties
        userDetails.put(USER_ID, user.getUserId().toString());
        userDetails.put(USER_FIRST_NAME, user.getFirstName());
        userDetails.put(USER_LAST_NAME, user.getLastName());
        userDetails.put(USER_USERNAME, user.getUsername());
        userDetails.put(USER_EMAIL, user.getEmail());
        userDetails.put(USER_PASSWORD, user.getPassword());
        userDetails.put(USER_ROLE, user.getRole());
        userDetails.put(USER_THEME, user.getTheme());
        userDetails.put(USER_HIGHLIGHT_COLOR, user.getHighlightColor());
        
        // Add favorite songs
        JSONArray favSongsJSON = new JSONArray();
        ArrayList<String> favSongs = user.getFavSongs();
        if (favSongs != null) {
            for (String songId : favSongs) {
                favSongsJSON.add(songId);
            }
        }
        userDetails.put(USER_FAV_SONGS, favSongsJSON);
        
        // Add student-specific properties
        if ("student".equals(user.getRole())) {
            if (user.getGrade() != null) {
                userDetails.put(USER_GRADE, user.getGrade());
            }
            
            JSONArray completedLessonsJSON = new JSONArray();
            ArrayList<String> completedLessons = user.getCompletedLessons();
            if (completedLessons != null) {
                for (String lessonId : completedLessons) {
                    completedLessonsJSON.add(lessonId);
                }
            }
            userDetails.put(USER_COMPLETED_LESSONS, completedLessonsJSON);
        }
        
        // Add teacher-specific properties
        if ("teacher".equals(user.getRole())) {
            JSONArray assignedStudentsJSON = new JSONArray();
            ArrayList<String> assignedStudents = user.getAssignedStudents();
            if (assignedStudents != null) {
                for (String studentId : assignedStudents) {
                    assignedStudentsJSON.add(studentId);
                }
            }
            userDetails.put(USER_ASSIGNED_STUDENTS, assignedStudentsJSON);
        }
        
        return userDetails;
    }
    
    /**
     * Saves all songs to the JSON file
     * 
     * @param songs List of songs to save
     */
    public static void saveSongs(List<Song> songs) {
        JSONArray jsonSongs = new JSONArray();
        
        // Creating all the JSON objects
        for (Song song : songs) {
            jsonSongs.add(song.toJSON());
        }
        
        // Write JSON file with pretty formatting
        try (FileWriter file = new FileWriter(SONG_FILE_NAME)) {
            String prettyJson = formatJsonString(jsonSongs.toJSONString());
            file.write(prettyJson);
            file.flush();
            System.out.println("Saved " + songs.size() + " songs to JSON file");
        } catch (IOException e) {
            System.err.println("Error saving songs to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Format a JSON string with proper indentation and line breaks
     * 
     * @param jsonString The compact JSON string
     * @return A formatted, pretty-printed JSON string
     */
    private static String formatJsonString(String jsonString) {
        StringBuilder sb = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;
        char previousChar = 0;
        
        // Loop through the string character by character
        for (char c : jsonString.toCharArray()) {
            if (c == '"' && previousChar != '\\') {
                inQuotes = !inQuotes;
            }
            
            if (!inQuotes) {
                // Format based on the current character if not in quotes
                switch (c) {
                    case '{':
                    case '[':
                        sb.append(c);
                        sb.append('\n');
                        indentLevel++;
                        addIndentation(sb, indentLevel);
                        break;
                    case ',':
                        sb.append(c);
                        sb.append('\n');
                        addIndentation(sb, indentLevel);
                        break;
                    case '}':
                    case ']':
                        sb.append('\n');
                        indentLevel--;
                        addIndentation(sb, indentLevel);
                        sb.append(c);
                        break;
                    case ':':
                        sb.append(c).append(' ');
                        break;
                    default:
                        sb.append(c);
                }
            } else {
                // If in quotes, just append the character
                sb.append(c);
            }
            
            previousChar = c;
        }
        
        return sb.toString();
    }
    
    /**
     * Add indentation spaces to a StringBuilder
     * 
     * @param sb StringBuilder to append to
     * @param indentLevel Number of indentation levels
     */
    private static void addIndentation(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    "); // 4 spaces per indent level
        }
    }

    /**
     * Test method to save users
     */
    public static void main(String[] args) {
        // First load users
        ArrayList<User> users = DataLoader.getUsers();
        
        // Initialize the UserList singleton with loaded data
        UserList.getInstance();
        
        // Save users back to file
        DataWriter.saveUsers();
        
        System.out.println("Users saved successfully!");
    }
} 