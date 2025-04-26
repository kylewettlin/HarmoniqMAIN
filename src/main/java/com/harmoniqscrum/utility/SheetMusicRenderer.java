package com.harmoniqscrum.utility; // Or a suitable package

import com.harmoniqscrum.model.Song;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles rendering a Song object to a sheet music image file using LilyPond.
 */
public class SheetMusicRenderer {

    /**
     * Renders the given song to a PNG image file using LilyPond.
     *
     * @param song The Song object to render.
     * @return The File object pointing to the generated PNG, or null if rendering failed.
     * @throws IOException If file I/O fails.
     * @throws InterruptedException If the LilyPond process is interrupted.
     * @throws RuntimeException If LilyPond executable is not found or returns an error.
     */
    public File renderSongToPng(Song song) throws IOException, InterruptedException, RuntimeException {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }

        Path tempXmlFile = null;
        Path tempLyFile = null; // Path for the intermediate .ly file
        Path tempOutputDir = null;
        File outputFile = null;

        try {
            // 1. Generate MusicXML
            String musicXml = MusicXmlGenerator.generateMusicXml(song);
            if (musicXml.isEmpty()) {
                throw new RuntimeException("Generated MusicXML was empty.");
            }

            // 2. Write MusicXML to a temporary file
            tempXmlFile = Files.createTempFile("harmoniq_score_", ".xml");
            try (FileWriter writer = new FileWriter(tempXmlFile.toFile())) {
                writer.write(musicXml);
            }
            System.out.println("MusicXML saved to: " + tempXmlFile.toAbsolutePath());

            // 3. Create path for temporary LilyPond (.ly) file
            tempLyFile = Files.createTempFile("harmoniq_score_", ".ly");
            System.out.println("Target LilyPond file: " + tempLyFile.toAbsolutePath());

            // 4. Convert MusicXML to LilyPond format using musicxml2ly
            // IMPORTANT: Assumes 'musicxml2ly' is in the system PATH
            String musicxml2lyCommand = "musicxml2ly";
            ProcessBuilder xml2lyPb = new ProcessBuilder(
                musicxml2lyCommand,
                "-o", tempLyFile.toString(), // Specify output .ly file
                tempXmlFile.toString()      // Input .xml file
            );
            xml2lyPb.inheritIO(); // Show output/errors
            
            System.out.println("Running musicxml2ly...");
            Process xml2lyProcess = xml2lyPb.start();
            int xml2lyExitCode = xml2lyProcess.waitFor();
            System.out.println("musicxml2ly finished with exit code: " + xml2lyExitCode);

            if (xml2lyExitCode != 0) {
                throw new RuntimeException("musicxml2ly conversion failed with exit code: " + xml2lyExitCode);
            }
            if (!Files.exists(tempLyFile) || Files.size(tempLyFile) == 0) {
                throw new RuntimeException("musicxml2ly ran but did not create a valid .ly file at: " + tempLyFile);
            }
            
            // 5. Create temporary output base path for LilyPond PNG
            tempOutputDir = Files.createTempDirectory("harmoniq_render_");
            Path outputBase = tempOutputDir.resolve("sheet_music"); // Base name for output file

            // 6. Prepare and run LilyPond command on the .ly file
            String lilypondCommand = "lilypond";
            ProcessBuilder lilyPb = new ProcessBuilder(
                lilypondCommand,
                "--png",
                "-dno-point-and-click",
                "-o", outputBase.toString(),
                tempLyFile.toString() // *** INPUT IS NOW THE .ly FILE ***
            );
            lilyPb.inheritIO();
            
            System.out.println("Running LilyPond on .ly file...");
            Process lilyProcess = lilyPb.start();
            int lilyExitCode = lilyProcess.waitFor();
            System.out.println("LilyPond finished with exit code: " + lilyExitCode);

            // 7. Check result and locate output file
            if (lilyExitCode != 0) {
                throw new RuntimeException("LilyPond execution failed with exit code: " + lilyExitCode);
            }

            // LilyPond usually creates outputBase.png
            outputFile = outputBase.resolveSibling(outputBase.getFileName() + ".png").toFile();
            
            if (!outputFile.exists()) {
                 // Sometimes output name might differ slightly or be in the temp dir directly
                 // Add more robust checking if needed based on LilyPond version/behavior
                 System.err.println("Expected output file not found: " + outputFile.getAbsolutePath());
                 // Try finding any PNG in the output directory as a fallback
                 try (var stream = Files.list(tempOutputDir)) {
                     outputFile = stream.filter(p -> p.toString().toLowerCase().endsWith(".png"))
                                        .findFirst()
                                        .map(Path::toFile)
                                        .orElse(null);
                 }
                 if (outputFile == null || !outputFile.exists()) { 
                     throw new RuntimeException("Could not find the generated PNG file in " + tempOutputDir.toString());
                 }
                 System.out.println("Found output file: " + outputFile.getAbsolutePath());
            }
            
             System.out.println("Sheet music rendered successfully to: " + outputFile.getAbsolutePath());
             return outputFile;

        } finally {
            // 8. Clean up temporary files
            if (tempXmlFile != null) {
                try {
                    Files.deleteIfExists(tempXmlFile);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to delete temporary XML file: " + tempXmlFile + " - " + e.getMessage());
                }
            }
            if (tempLyFile != null) { // Clean up the .ly file too
                try {
                    Files.deleteIfExists(tempLyFile);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to delete temporary LY file: " + tempLyFile + " - " + e.getMessage());
                }
            }
             // Note: We don't delete tempOutputDir or the PNG here.
             // The caller (View) should delete the PNG file when the popup closes.
             // The tempOutputDir could be cleaned up later or on exit if desired.
        }
    }
} 