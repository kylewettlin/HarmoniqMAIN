package com.harmoniqscrum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit tests for the MusicPlayer class
 */
@DisplayName("MusicPlayer Tests")
class MusicPlayerTest {
    private MusicPlayer player;
    private Song mockSong;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private boolean songPlayCalled;

    @BeforeEach
    void setUp() {
        player = new MusicPlayer();
        // Create a mock song that tracks if play() was called
        mockSong = new Song("Test Song", "Test Composer") {
            @Override
            public void play() {
                songPlayCalled = true;
                super.play();
            }
        };
        
        // Capture System.out for testing console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        songPlayCalled = false;
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Playing a valid song should output correct message and call play()")
    void testPlayValidSong() {
        player.playSong(mockSong);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Playing: Test Song by Test Composer"), 
            "Should output correct playing message");
        assertTrue(songPlayCalled, 
            "Should have called play() on the song");
    }

    @Test
    @DisplayName("Playing a null song should throw NullPointerException")
    void testPlayNullSong() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            player.playSong(null);
        }, "Should throw NullPointerException when song is null");
        
        assertEquals("Cannot play null song", exception.getMessage(),
            "Should have descriptive error message");
    }

    @Test
    @DisplayName("Playing a song with null title should throw IllegalArgumentException")
    void testPlaySongWithNullTitle() {
        Song songWithNullTitle = new Song();
        songWithNullTitle.setTitle(null);
        songWithNullTitle.setComposer("Test Composer");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.playSong(songWithNullTitle);
        }, "Should throw IllegalArgumentException when title is null");
        
        assertEquals("Song title cannot be null", exception.getMessage(),
            "Should have descriptive error message");
    }

    @Test
    @DisplayName("Playing a song with null composer should throw IllegalArgumentException")
    void testPlaySongWithNullComposer() {
        Song songWithNullComposer = new Song();
        songWithNullComposer.setTitle("Test Song");
        songWithNullComposer.setComposer(null);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.playSong(songWithNullComposer);
        }, "Should throw IllegalArgumentException when composer is null");
        
        assertEquals("Song composer cannot be null", exception.getMessage(),
            "Should have descriptive error message");
    }

    @Test
    @DisplayName("Playing a song with empty title and composer should throw IllegalArgumentException")
    void testPlaySongWithEmptyStrings() {
        Song songWithEmptyStrings = new Song("", "");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.playSong(songWithEmptyStrings);
        }, "Should throw IllegalArgumentException when strings are empty");
        
        assertEquals("Song title and composer cannot be empty", exception.getMessage(),
            "Should have descriptive error message");
    }
} 