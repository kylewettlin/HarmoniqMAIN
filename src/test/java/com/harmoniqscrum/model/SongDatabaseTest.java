package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for the SongDatabase class
 */
@DisplayName("SongDatabase Tests")
class SongDatabaseTest {
    private SongDatabase songDb;
    private Song testSong1;
    private Song testSong2;
    private Song testSong3;

    @BeforeEach
    void setUp() {
        // Get the singleton instance
        songDb = SongDatabase.getInstance();
        
        // Clear any existing songs
        List<Song> existingSongs = new ArrayList<>(songDb.getSongs());
        for (Song song : existingSongs) {
            songDb.removeSong(song);
        }
        
        // Create test songs
        testSong1 = new Song("Test Song 1", "Test Composer 1");
        testSong2 = new Song("Test Song 2", "Test Composer 2");
        testSong3 = new Song("Another Song", "Test Composer 1");
    }

    @Test
    @DisplayName("Adding a song should work correctly")
    void testAddSong() {
        songDb.addSong(testSong1);
        List<Song> songs = songDb.getSongs();
        
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(testSong1, songs.get(0), "The added song should be in the database");
    }

    @Test
    @DisplayName("Adding multiple songs should work correctly")
    void testAddMultipleSongs() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        List<Song> songs = songDb.getSongs();
        
        assertEquals(2, songs.size(), "Database should contain two songs");
        assertTrue(songs.contains(testSong1), "First song should be in the database");
        assertTrue(songs.contains(testSong2), "Second song should be in the database");
    }

    @Test
    @DisplayName("Removing a song should work correctly")
    void testRemoveSong() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        songDb.removeSong(testSong1);
        List<Song> songs = songDb.getSongs();
        
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(testSong2, songs.get(0), "The remaining song should be testSong2");
    }

    @Test
    @DisplayName("Removing a non-existent song should not affect the database")
    void testRemoveNonExistentSong() {
        songDb.addSong(testSong1);
        songDb.removeSong(testSong2);
        List<Song> songs = songDb.getSongs();
        
        assertEquals(1, songs.size(), "Database should still contain one song");
        assertEquals(testSong1, songs.get(0), "The original song should still be in the database");
    }

    @Test
    @DisplayName("Searching songs should find matches in title")
    void testSearchSongsByTitle() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        songDb.addSong(testSong3);
        
        List<Song> results = songDb.searchSongs("Test Song");
        assertEquals(2, results.size(), "Should find two songs with 'Test Song' in the title");
        assertTrue(results.contains(testSong1), "Should find Test Song 1");
        assertTrue(results.contains(testSong2), "Should find Test Song 2");
    }

    @Test
    @DisplayName("Searching songs should find matches in composer")
    void testSearchSongsByComposer() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        songDb.addSong(testSong3);
        
        List<Song> results = songDb.searchSongs("Test Composer 1");
        assertEquals(2, results.size(), "Should find two songs by Test Composer 1");
        assertTrue(results.contains(testSong1), "Should find first song by composer");
        assertTrue(results.contains(testSong3), "Should find second song by composer");
    }

    @Test
    @DisplayName("Search should be case insensitive")
    void testSearchCaseInsensitive() {
        songDb.addSong(testSong1);
        
        List<Song> results1 = songDb.searchSongs("TEST SONG");
        List<Song> results2 = songDb.searchSongs("test song");
        
        assertEquals(1, results1.size(), "Should find song with uppercase search");
        assertEquals(1, results2.size(), "Should find song with lowercase search");
    }

    @Test
    @DisplayName("Search with empty string should return empty list")
    void testSearchEmptyString() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        
        List<Song> results = songDb.searchSongs("");
        assertEquals(0, results.size(), "Empty search string should return no results");
    }

    @Test
    @DisplayName("Search with null should return empty list")
    void testSearchNull() {
        songDb.addSong(testSong1);
        songDb.addSong(testSong2);
        
        List<Song> results = songDb.searchSongs(null);
        assertEquals(0, results.size(), "Null search string should return no results");
    }

    @Test
    @DisplayName("Saving a new song should add it to the database")
    void testSaveNewSong() {
        songDb.saveSong(testSong1);
        List<Song> songs = songDb.getSongs();
        
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(testSong1, songs.get(0), "The saved song should be in the database");
    }

    @Test
    @DisplayName("Saving an existing song should update it")
    void testSaveExistingSong() {
        songDb.saveSong(testSong1);
        
        // Modify the song and save again
        testSong1.setComposer("New Composer");
        songDb.saveSong(testSong1);
        
        List<Song> songs = songDb.getSongs();
        assertEquals(1, songs.size(), "Database should still contain one song");
        assertEquals("New Composer", songs.get(0).getComposer(), "The composer should be updated");
    }

    @Test
    @DisplayName("Creating a song should add it to the database")
    void testCreateSong() {
        Song newSong = songDb.createSong("New Song", "New Composer", 120, "C Major", 4, 4);
        
        List<Song> songs = songDb.getSongs();
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(newSong, songs.get(0), "The created song should be in the database");
        assertEquals("New Song", newSong.getTitle(), "Title should be set correctly");
        assertEquals("New Composer", newSong.getComposer(), "Composer should be set correctly");
        assertEquals(120, newSong.getTempo(), "Tempo should be set correctly");
        assertEquals("C Major", newSong.getKeySignature(), "Key signature should be set correctly");
        assertEquals(4, newSong.getTimeSignature().getNumerator(), "Time signature numerator should be set correctly");
        assertEquals(4, newSong.getTimeSignature().getDenominator(), "Time signature denominator should be set correctly");
    }

    @Test
    @DisplayName("Creating a song with genre should set all properties correctly")
    void testCreateSongWithGenre() {
        Song newSong = songDb.createSong("New Song", "New Composer", "Classical", 120, "C Major", 4, 4);
        
        List<Song> songs = songDb.getSongs();
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(newSong, songs.get(0), "The created song should be in the database");
        assertEquals("Classical", newSong.getGenres().get(0), "Genre should be set correctly");
    }

    @Test
    @DisplayName("Deleting a song should remove it from the database")
    void testDeleteSong() {
        songDb.saveSong(testSong1);
        songDb.saveSong(testSong2);
        
        songDb.deleteSong(testSong1);
        
        List<Song> songs = songDb.getSongs();
        assertEquals(1, songs.size(), "Database should contain one song");
        assertEquals(testSong2, songs.get(0), "The remaining song should be testSong2");
    }

    @Test
    @DisplayName("Deleting a non-existent song should not affect the database")
    void testDeleteNonExistentSong() {
        songDb.saveSong(testSong1);
        
        Song nonExistentSong = new Song("Non-existent", "Unknown");
        songDb.deleteSong(nonExistentSong);
        
        List<Song> songs = songDb.getSongs();
        assertEquals(1, songs.size(), "Database should still contain one song");
        assertEquals(testSong1, songs.get(0), "The original song should still be in the database");
    }
} 