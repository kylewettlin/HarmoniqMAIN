package com.harmoniqscrum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataWriterTest {
    private DataWriter dataWriter;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        dataWriter = new DataWriter();
    }

    @Test
    void testWriteJSONToFile() throws IOException, ParseException {
        // Create test data
        JSONObject testData = new JSONObject();
        testData.put("name", "Test Song");
        testData.put("composer", "Test Composer");
        testData.put("tempo", 120);

        // Create temp file path
        Path jsonFile = tempDir.resolve("test.json");
        
        // Write data
        dataWriter.writeJSONToFile(testData, jsonFile.toString());

        // Verify file exists
        assertTrue(Files.exists(jsonFile));

        // Read and verify contents
        String content = Files.readString(jsonFile);
        JSONParser parser = new JSONParser();
        JSONObject readData = (JSONObject) parser.parse(content);

        assertEquals("Test Song", readData.get("name"));
        assertEquals("Test Composer", readData.get("composer"));
        assertEquals(120L, readData.get("tempo"));
    }

    @Test
    void testWriteTextToFile() throws IOException {
        String testContent = "Test content\nLine 2\nLine 3";
        Path textFile = tempDir.resolve("test.txt");

        dataWriter.writeTextToFile(testContent, textFile.toString());

        assertTrue(Files.exists(textFile));
        List<String> lines = Files.readAllLines(textFile);
        assertEquals(3, lines.size());
        assertEquals("Test content", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    @Test
    void testWriteToInvalidPath() {
        JSONObject testData = new JSONObject();
        testData.put("test", "data");

        String invalidPath = "/invalid/path/file.json";
        
        assertThrows(IOException.class, () -> {
            dataWriter.writeJSONToFile(testData, invalidPath);
        });
    }

    @Test
    void testWriteEmptyJSON() throws IOException, ParseException {
        JSONObject emptyData = new JSONObject();
        Path jsonFile = tempDir.resolve("empty.json");

        dataWriter.writeJSONToFile(emptyData, jsonFile.toString());

        assertTrue(Files.exists(jsonFile));
        String content = Files.readString(jsonFile);
        assertEquals("{}", content.trim());
    }

    @Test
    void testWriteEmptyText() throws IOException {
        String emptyContent = "";
        Path textFile = tempDir.resolve("empty.txt");

        dataWriter.writeTextToFile(emptyContent, textFile.toString());

        assertTrue(Files.exists(textFile));
        assertEquals("", Files.readString(textFile));
    }

    @Test
    void testOverwriteExistingFile() throws IOException {
        // Create initial file
        Path file = tempDir.resolve("overwrite.txt");
        String initialContent = "Initial content";
        dataWriter.writeTextToFile(initialContent, file.toString());

        // Overwrite with new content
        String newContent = "New content";
        dataWriter.writeTextToFile(newContent, file.toString());

        assertTrue(Files.exists(file));
        assertEquals(newContent, Files.readString(file));
    }

    @Test
    void testWriteComplexJSON() throws IOException, ParseException {
        JSONObject complexData = new JSONObject();
        complexData.put("string", "value");
        complexData.put("number", 123);
        complexData.put("boolean", true);
        
        JSONObject nestedObject = new JSONObject();
        nestedObject.put("nested", "value");
        complexData.put("object", nestedObject);

        Path jsonFile = tempDir.resolve("complex.json");
        
        dataWriter.writeJSONToFile(complexData, jsonFile.toString());

        assertTrue(Files.exists(jsonFile));
        
        JSONParser parser = new JSONParser();
        JSONObject readData = (JSONObject) parser.parse(Files.readString(jsonFile));
        
        assertEquals("value", readData.get("string"));
        assertEquals(123L, readData.get("number"));
        assertEquals(true, readData.get("boolean"));
        
        JSONObject readNestedObject = (JSONObject) readData.get("object");
        assertEquals("value", readNestedObject.get("nested"));
    }

    @Test
    void testWriteSpecialCharacters() throws IOException {
        String content = "Special characters: áéíóú ñ 你好 \n\t\"'";
        Path file = tempDir.resolve("special.txt");

        dataWriter.writeTextToFile(content, file.toString());

        assertTrue(Files.exists(file));
        assertEquals(content, Files.readString(file));
    }
} 