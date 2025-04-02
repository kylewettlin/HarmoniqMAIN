package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.ArrayList;

/**
 * Unit tests for the PlaybackEngine class
 */
@DisplayName("PlaybackEngine Tests")
class PlaybackEngineTest {
    private PlaybackEngine playbackEngine;
    private Song mockSong;

    @BeforeEach
    void setUp() {
        // Create a mock song for testing
        mockSong = new Song("Test Song", "Test Composer");
        playbackEngine = new PlaybackEngine();
    }

    @Test
    @DisplayName("Default constructor should initialize with correct values")
    void testDefaultConstructor() {
        PlaybackEngine engine = new PlaybackEngine();
        assertEquals(100, engine.getSpeed(), "Default speed should be 100");
        assertEquals(120, engine.getMetronomeSpeed(), "Default metronome speed should be 120");
        assertFalse(engine.isLooping(), "Looping should be false by default");
        assertNull(engine.getSong(), "Song should be null by default");
    }

    @Test
    @DisplayName("Constructor with song should initialize with correct values")
    void testConstructorWithSong() {
        PlaybackEngine engine = new PlaybackEngine(mockSong);
        assertEquals(100, engine.getSpeed(), "Default speed should be 100");
        assertEquals(120, engine.getMetronomeSpeed(), "Default metronome speed should be 120");
        assertFalse(engine.isLooping(), "Looping should be false by default");
        assertEquals(mockSong, engine.getSong(), "Song should be set to mockSong");
    }

    @Test
    @DisplayName("Setting and getting song should work correctly")
    void testSetAndGetSong() {
        assertNull(playbackEngine.getSong(), "Song should initially be null");
        playbackEngine.setSong(mockSong);
        assertEquals(mockSong, playbackEngine.getSong(), "Song should be set to mockSong");
    }

    @Test
    @DisplayName("Speed adjustment should work within valid ranges")
    void testAdjustSpeed() {
        // Test normal speed adjustment
        playbackEngine.adjustSpeed(150);
        assertEquals(150, playbackEngine.getSpeed(), "Speed should be adjusted to 150");

        // Test minimum speed
        playbackEngine.adjustSpeed(0);
        assertEquals(0, playbackEngine.getSpeed(), "Speed should be adjusted to 0");

        // Test maximum speed
        playbackEngine.adjustSpeed(200);
        assertEquals(200, playbackEngine.getSpeed(), "Speed should be adjusted to 200");
    }

    @Test
    @DisplayName("Metronome speed should be adjustable")
    void testSetAndGetMetronomeSpeed() {
        // Test normal metronome speed
        playbackEngine.setMetronomeSpeed(60);
        assertEquals(60, playbackEngine.getMetronomeSpeed(), "Metronome speed should be 60");

        // Test higher metronome speed
        playbackEngine.setMetronomeSpeed(180);
        assertEquals(180, playbackEngine.getMetronomeSpeed(), "Metronome speed should be 180");
    }

    @Test
    @DisplayName("Looping state should be toggleable")
    void testSetAndGetLooping() {
        assertFalse(playbackEngine.isLooping(), "Looping should initially be false");
        
        playbackEngine.setLooping(true);
        assertTrue(playbackEngine.isLooping(), "Looping should be set to true");
        
        playbackEngine.setLooping(false);
        assertFalse(playbackEngine.isLooping(), "Looping should be set to false");
    }

    @Test
    @DisplayName("Loop playback should enable looping state")
    void testLoopPlayback() {
        playbackEngine.loopPlayback(0, 10);
        assertTrue(playbackEngine.isLooping(), "Looping should be enabled after calling loopPlayback");
    }

    @Test
    @DisplayName("Playing song by title should work for existing songs")
    void testPlaySongByTitle_SongFound() {
        // Create a song in the database with a known title
        Song testSong = new Song("Known Song", "Test Composer");
        SongDatabase.getInstance().addSong(testSong);

        boolean result = playbackEngine.playSongByTitle("Known Song");
        assertTrue(result, "playSongByTitle should return true for existing song");
        assertEquals(testSong.getTitle(), playbackEngine.getSong().getTitle(), 
            "PlaybackEngine should be set to play the found song");
    }

    @Test
    @DisplayName("Playing song by title should fail for non-existent songs")
    void testPlaySongByTitle_SongNotFound() {
        boolean result = playbackEngine.playSongByTitle("Nonexistent Song");
        assertFalse(result, "playSongByTitle should return false for nonexistent song");
    }

    @Test
    @DisplayName("Playing a song should set it as the current song")
    void testPlay() {
        // Test playing a valid song
        playbackEngine.play(mockSong);
        assertEquals(mockSong, playbackEngine.getSong(), "Current song should be set to mockSong");
    }

    @Test
    @DisplayName("Speed adjustment should handle invalid values")
    void testAdjustSpeedWithInvalidValues() {
        // Test negative speed
        playbackEngine.adjustSpeed(-50);
        assertEquals(0, playbackEngine.getSpeed(), "Speed should be clamped to 0 for negative values");

        // Test extremely high speed
        playbackEngine.adjustSpeed(1000);
        assertEquals(200, playbackEngine.getSpeed(), "Speed should be clamped to 200 for values above maximum");
    }

    @Test
    @DisplayName("Metronome speed should handle invalid values")
    void testMetronomeSpeedWithInvalidValues() {
        // Test extremely low BPM
        playbackEngine.setMetronomeSpeed(0);
        assertEquals(1, playbackEngine.getMetronomeSpeed(), "Metronome speed should be clamped to minimum of 1 BPM");

        // Test extremely high BPM
        playbackEngine.setMetronomeSpeed(1000);
        assertEquals(300, playbackEngine.getMetronomeSpeed(), "Metronome speed should be clamped to maximum of 300 BPM");
    }

    @Test
    @DisplayName("Loop playback should handle invalid indices")
    void testLoopPlaybackWithInvalidIndices() {
        // Test negative start index
        playbackEngine.loopPlayback(-1, 10);
        assertTrue(playbackEngine.isLooping(), "Looping should still be enabled");
        
        // Test end index less than start index
        playbackEngine.loopPlayback(10, 5);
        assertTrue(playbackEngine.isLooping(), "Looping should still be enabled");
        
        // Test negative end index
        playbackEngine.loopPlayback(0, -1);
        assertTrue(playbackEngine.isLooping(), "Looping should still be enabled");
    }

    @Test
    @DisplayName("Playing null song should handle gracefully")
    void testPlayNullSong() {
        playbackEngine.play(null);
        assertNull(playbackEngine.getSong(), "Song should remain null after attempting to play null");
    }

    @Test
    @DisplayName("Playing song with null title should handle gracefully")
    void testPlaySongWithNullTitle() {
        Song songWithNullTitle = new Song(null, "Test Composer");
        playbackEngine.play(songWithNullTitle);
        assertEquals(songWithNullTitle, playbackEngine.getSong(), "Song should be set even with null title");
    }

    @Test
    @DisplayName("Playing song by empty or null title should fail gracefully")
    void testPlaySongByInvalidTitle() {
        // Test empty title
        boolean resultEmpty = playbackEngine.playSongByTitle("");
        assertFalse(resultEmpty, "playSongByTitle should return false for empty title");

        // Test null title
        boolean resultNull = playbackEngine.playSongByTitle(null);
        assertFalse(resultNull, "playSongByTitle should return false for null title");
    }

    @Test
    @DisplayName("Multiple rapid song changes should work correctly")
    void testRapidSongChanges() {
        Song song1 = new Song("Song 1", "Composer 1");
        Song song2 = new Song("Song 2", "Composer 2");
        Song song3 = new Song("Song 3", "Composer 3");

        playbackEngine.play(song1);
        assertEquals(song1, playbackEngine.getSong(), "First song should be set");

        playbackEngine.play(song2);
        assertEquals(song2, playbackEngine.getSong(), "Second song should be set");

        playbackEngine.play(song3);
        assertEquals(song3, playbackEngine.getSong(), "Third song should be set");
    }

    @Test
    @DisplayName("Concurrent speed and song changes should work correctly")
    void testConcurrentSpeedAndSongChanges() {
        Song testSong = new Song("Test Song", "Test Composer");
        
        // Change speed while playing song
        playbackEngine.play(testSong);
        playbackEngine.adjustSpeed(150);
        assertEquals(testSong, playbackEngine.getSong(), "Song should remain unchanged");
        assertEquals(150, playbackEngine.getSpeed(), "Speed should be updated");

        // Change song while at different speed
        Song newSong = new Song("New Song", "New Composer");
        playbackEngine.play(newSong);
        assertEquals(newSong, playbackEngine.getSong(), "New song should be set");
        assertEquals(150, playbackEngine.getSpeed(), "Speed should remain unchanged");
    }
} 