package com.harmoniqscrum.model;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;
import java.util.List;

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
                
                // Get assigned lesson song IDs (for students)
                List<String> assignedLessonSongIds = null;
                if ("student".equals(role) && userJSON.containsKey("assignedLessonSongIds")) {
                    assignedLessonSongIds = new ArrayList<>();
                    JSONArray assignedLessonsJSON = (JSONArray) userJSON.get("assignedLessonSongIds");
                    if (assignedLessonsJSON != null) {
                        for (int j = 0; j < assignedLessonsJSON.size(); j++) {
                            assignedLessonSongIds.add((String) assignedLessonsJSON.get(j));
                        }
                    }
                } else if ("student".equals(role)) {
                     assignedLessonSongIds = new ArrayList<>(); // Initialize if key missing but role is student
                }
                
                // Create and add the user
                users.add(new User(userId, firstName, lastName, username, email, 
                                  password, role, favSongs, theme, highlightColor, 
                                  grade, completedLessons, assignedStudents, assignedLessonSongIds));
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

    /**
     * Loads songs from the JSON file
     * @return ArrayList of Song objects
     */
    public static ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<Song>();
        
        try {
            FileReader reader = new FileReader(SONG_FILE_NAME);
            JSONArray songsJSON = (JSONArray)new JSONParser().parse(reader);
            
            for(int i=0; i < songsJSON.size(); i++) {
                JSONObject songJSON = (JSONObject)songsJSON.get(i);
                
                try {
                    // Extract song data
                    UUID songId = UUID.fromString((String)songJSON.get(SONG_ID));
                    String title = (String)songJSON.get(SONG_TITLE);
                    String composer = (String)songJSON.get(SONG_COMPOSER);
                    int tempo = ((Long)songJSON.get(SONG_TEMPO)).intValue();
                    String keySignature = (String)songJSON.get(SONG_KEY_SIGNATURE);
                    
                    // Create song object
                    Song song = new Song(title, composer);
                    song.setSongId(songId);
                    song.setTempo(tempo);
                    song.setKeySignature(keySignature);
                    
                    // Load rating if present
                    if (songJSON.containsKey("rating")) {
                         // Handle potential Long or Double from JSON parser
                        Object ratingObj = songJSON.get("rating");
                        if (ratingObj instanceof Number) {
                            song.setRating(((Number) ratingObj).doubleValue());
                        } else {
                            System.out.println("Warning: Unexpected type for rating: " + ratingObj.getClass().getName());
                            song.setRating(0.0); // Default if type is wrong
                        }
                    } else {
                        song.setRating(0.0); // Default if key not present
                    }

                    // Load genres if present
                    if (songJSON.containsKey("genres")) {
                        JSONArray genresJSON = (JSONArray) songJSON.get("genres");
                        ArrayList<String> genresList = new ArrayList<>();
                        if (genresJSON != null) {
                            for(int j=0; j < genresJSON.size(); j++) {
                                genresList.add((String)genresJSON.get(j));
                            }
                        }
                        song.setGenres(genresList);
                    } else {
                        song.setGenres(new ArrayList<>()); // Default to empty list
                    }

                    // Load lyrics if present
                    if (songJSON.containsKey("lyrics")) {
                         JSONArray lyricsJSON = (JSONArray) songJSON.get("lyrics");
                        ArrayList<String> lyricsList = new ArrayList<>();
                        if (lyricsJSON != null) {
                            for (int j = 0; j < lyricsJSON.size(); j++) {
                                lyricsList.add((String) lyricsJSON.get(j));
                            }
                        }
                        song.setLyrics(lyricsList);
                    } else {
                         song.setLyrics(new ArrayList<>()); // Default to empty list
                    }
                    
                    // Handle time signature
                    JSONObject timeSignature = (JSONObject)songJSON.get(SONG_TIME_SIGNATURE);
                    int numerator = ((Long)timeSignature.get("numerator")).intValue();
                    int denominator = ((Long)timeSignature.get("denominator")).intValue();
                    song.setSignature(numerator, denominator);
                    
                    // Handle notes
                    JSONArray notesJSON = (JSONArray)songJSON.get(SONG_NOTES);
                    if (notesJSON != null) {
                        for(int j=0; j < notesJSON.size(); j++) {
                            try {
                                JSONObject noteJSON = (JSONObject)notesJSON.get(j);
                                String pitch = (String)noteJSON.get("pitch");
                                double duration = ((Number)noteJSON.get("duration")).doubleValue();
                                int volume = ((Long)noteJSON.get("volume")).intValue();
                                String expression = (String)noteJSON.get("expression");
                                
                                // Set default octave if not present in JSON
                                int octave = 5; // Default octave
                                
                                // Use octave from JSON if available
                                if (noteJSON.containsKey("octave")) {
                                    octave = ((Long)noteJSON.get("octave")).intValue();
                                } 
                                // Otherwise extract it from pitch if present (e.g., "C4" -> octave 4)
                                else if (pitch.length() > 1 && Character.isDigit(pitch.charAt(pitch.length()-1))) {
                                    octave = Character.getNumericValue(pitch.charAt(pitch.length()-1));
                                    pitch = pitch.substring(0, pitch.length()-1);
                                }
                                
                                Note note = new Note(pitch);
                                note.setOctave(octave);
                                note.setDuration(duration);
                                note.setVolume(volume);
                                note.setExpression(expression);
                                song.addNote(note);
                            } catch (Exception e) {
                                System.out.println("Error loading note: " + e.getMessage());
                            }
                        }
                    }
                    
                    songs.add(song);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping song with invalid UUID: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error loading song: " + e.getMessage());
                }
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return songs;
    }
} 