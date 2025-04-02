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
} 