package com.harmoniqscrum.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.jfugue.pattern.Pattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DataLoaderTest {
    private static final String TEST_USER_FILE = "src/test/resources/TestUsers.json";
    private static final String TEST_SONG_FILE = "src/test/resources/TestSongs.json";
    private static final String INVALID_JSON_FILE = "src/test/resources/InvalidJson.json";
    private static final String NONEXISTENT_FILE = "src/test/resources/NonexistentFile.json";

    @BeforeEach
    void setUp() {
        // Create test directories if they don't exist
        new File("src/test/resources").mkdirs();
    }

    @AfterEach
    void tearDown() {
        // Clean up test files
        new File(TEST_USER_FILE).delete();
        new File(TEST_SONG_FILE).delete();
        new File(INVALID_JSON_FILE).delete();
    }

    @Test
    void testGetUsers_ValidData() throws IOException {
        // Create a test JSON file with valid user data
        String validUserJson = """
            [
                {
                    "userId": "550e8400-e29b-41d4-a716-446655440000",
                    "firstName": "John",
                    "lastName": "Doe",
                    "username": "johndoe",
                    "email": "john@example.com",
                    "password": "password123",
                    "role": "student",
                    "theme": "light",
                    "highlightColor": "#FF0000",
                    "grade": 10,
                    "completedLessons": ["lesson1", "lesson2"],
                    "favoriteSongs": ["song1", "song2"]
                }
            ]
            """;
        createTestFile(TEST_USER_FILE, validUserJson);

        // Test loading users
        ArrayList<User> users = DataLoader.getUsers();
        
        assertNotNull(users);
        assertEquals(1, users.size());
        
        User user = users.get(0);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("student", user.getRole());
        assertEquals("light", user.getTheme());
        assertEquals("#FF0000", user.getHighlightColor());
        assertEquals(10, user.getGrade());
        assertEquals(2, user.getCompletedLessons().size());
        assertEquals(2, user.getFavSongs().size());
    }

    @Test
    void testGetUsers_EmptyFile() throws IOException {
        // Create empty JSON array
        createTestFile(TEST_USER_FILE, "[]");

        ArrayList<User> users = DataLoader.getUsers();
        
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetUsers_InvalidJson() throws IOException {
        // Create invalid JSON
        createTestFile(INVALID_JSON_FILE, "{ invalid json }");

        ArrayList<User> users = DataLoader.getUsers();
        
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetUsers_NonexistentFile() {
        ArrayList<User> users = DataLoader.getUsers();
        
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetSongs_ValidData() throws IOException {
        // Create a test JSON file with valid song data
        String validSongJson = """
            [
                {
                    "songId": "550e8400-e29b-41d4-a716-446655440000",
                    "title": "Test Song",
                    "composer": "Test Composer",
                    "tempo": 120,
                    "keySignature": "C",
                    "timeSignature": {
                        "numerator": 4,
                        "denominator": 4
                    },
                    "notes": [
                        {
                            "pitch": "C",
                            "duration": 1.0,
                            "volume": 100,
                            "expression": "normal",
                            "octave": 4
                        }
                    ]
                }
            ]
            """;
        createTestFile(TEST_SONG_FILE, validSongJson);

        // Test loading songs
        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertEquals(1, songs.size());
        
        Song song = songs.get(0);
        assertEquals("Test Song", song.getTitle());
        assertEquals("Test Composer", song.getComposer());
        assertEquals(120, song.getTempo());
        assertEquals("C", song.getKeySignature());
        assertEquals(4, song.getTimeSignature().getNumerator());
        assertEquals(4, song.getTimeSignature().getDenominator());
        
        // Test the pattern which contains the notes
        Pattern pattern = song.getPattern();
        assertNotNull(pattern);
        String patternStr = pattern.toString();
        assertTrue(patternStr.contains("C4"));
    }

    @Test
    void testGetSongs_EmptyFile() throws IOException {
        // Create empty JSON array
        createTestFile(TEST_SONG_FILE, "[]");

        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongs_InvalidJson() throws IOException {
        // Create invalid JSON
        createTestFile(INVALID_JSON_FILE, "{ invalid json }");

        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongs_NonexistentFile() {
        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongs_InvalidUUID() throws IOException {
        // Create a test JSON file with invalid UUID
        String invalidUuidJson = """
            [
                {
                    "songId": "invalid-uuid",
                    "title": "Test Song",
                    "composer": "Test Composer",
                    "tempo": 120,
                    "keySignature": "C",
                    "timeSignature": {
                        "numerator": 4,
                        "denominator": 4
                    }
                }
            ]
            """;
        createTestFile(TEST_SONG_FILE, invalidUuidJson);

        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongs_MissingRequiredFields() throws IOException {
        // Create a test JSON file with missing required fields
        String missingFieldsJson = """
            [
                {
                    "songId": "550e8400-e29b-41d4-a716-446655440000",
                    "title": "Test Song"
                }
            ]
            """;
        createTestFile(TEST_SONG_FILE, missingFieldsJson);

        ArrayList<Song> songs = DataLoader.getSongs();
        
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    private void createTestFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }
} 