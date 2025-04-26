package com.harmoniqscrum.utility; // Or a suitable package

import com.harmoniqscrum.model.Song;
import com.harmoniqscrum.model.Note;
import java.util.List;
import java.util.Map;
import com.harmoniqscrum.model.Song.TimeSignature;

/**
 * Generates a basic MusicXML string from a Song object.
 * Handles basic metadata, structure, notes, and rests.
 * Limitations: Assumes single part/instrument, simple rhythms, no chords, beams, etc.
 */
public class MusicXmlGenerator {

    // MusicXML uses divisions per quarter note to represent duration precisely.
    // A common value allows representing various durations as integers.
    private static final int DIVISIONS = 4; // Allows quarter=4, eighth=2, sixteenth=1

    // Map simple duration values to MusicXML type strings
    private static final Map<Double, String> DURATION_TO_TYPE = Map.of(
        4.0, "whole",
        2.0, "half",
        1.0, "quarter",
        0.5, "eighth",
        0.125, "16th", // MusicXML uses numbers for 16th and smaller
        0.0625, "32nd"
    );

    // Map key signatures (simplified) to fifths count
    private static final Map<String, Integer> KEY_TO_FIFTHS = Map.ofEntries(
        Map.entry("C Major", 0), Map.entry("A Minor", 0),
        Map.entry("G Major", 1), Map.entry("E Minor", 1),
        Map.entry("D Major", 2), Map.entry("B Minor", 2),
        Map.entry("A Major", 3), Map.entry("F# Minor", 3),
        Map.entry("E Major", 4), Map.entry("C# Minor", 4),
        Map.entry("B Major", 5), Map.entry("G# Minor", 5),
        Map.entry("F# Major", 6), Map.entry("D# Minor", 6),
        Map.entry("C# Major", 7), Map.entry("A# Minor", 7),
        Map.entry("F Major", -1), Map.entry("D Minor", -1),
        Map.entry("Bb Major", -2), Map.entry("G Minor", -2),
        Map.entry("Eb Major", -3), Map.entry("C Minor", -3),
        Map.entry("Ab Major", -4), Map.entry("F Minor", -4),
        Map.entry("Db Major", -5), Map.entry("Bb Minor", -5),
        Map.entry("Gb Major", -6), Map.entry("Eb Minor", -6),
        Map.entry("Cb Major", -7), Map.entry("Ab Minor", -7)
    );

    public static String generateMusicXml(Song song) {
        if (song == null) return "";

        StringBuilder xml = new StringBuilder();

        // Header
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        xml.append("<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">\n");
        xml.append("<score-partwise version=\"3.0\">\n");

        // Metadata
        xml.append("  <work>\n");
        xml.append("    <work-title>").append(escapeXml(song.getTitle())).append("</work-title>\n");
        xml.append("  </work>\n");
        xml.append("  <identification>\n");
        xml.append("    <creator type=\"composer\">").append(escapeXml(song.getComposer())).append("</creator>\n");
        // Add encoding information if needed
        xml.append("  </identification>\n");

        // Part List (assuming single part)
        xml.append("  <part-list>\n");
        xml.append("    <score-part id=\"P1\">\n");
        xml.append("      <part-name>Music</part-name>\n");
        xml.append("    </score-part>\n");
        xml.append("  </part-list>\n");

        // Part Data
        xml.append("  <part id=\"P1\">\n");

        List<Note> notes = song.getNotes();
        if (notes == null || notes.isEmpty()) {
            // Add an empty measure if there are no notes
            xml.append("    <measure number=\"1\">\n");
            appendAttributes(xml, song); // Still add attributes
            xml.append("    </measure>\n");
        } else {
            TimeSignature timeSig = song.getTimeSignature() != null ? song.getTimeSignature() : new TimeSignature(4, 4);
            // Calculate measure capacity in divisions
            // Capacity = (numerator * divisions_per_beat)
            // divisions_per_beat = DIVISIONS * (4 / denominator)
            int measureCapacity = timeSig.getNumerator() * DIVISIONS * (4 / timeSig.getDenominator());
            if (measureCapacity <= 0) {
                System.err.println("Warning: Invalid time signature resulted in zero/negative measure capacity. Defaulting to 4/4.");
                measureCapacity = 4 * DIVISIONS; // Default capacity for 4/4
            }
            
            int currentMeasureDuration = 0;
            int measureNumber = 1;

            // Start first measure
            xml.append("    <measure number=\"").append(measureNumber).append("\">\n");
            appendAttributes(xml, song); // Add attributes to the first measure
            appendTempo(xml, song);      // Add tempo direction to the first measure

            for (Note note : notes) {
                int noteDuration = getMusicXmlDuration(note.getDuration());

                // Check if note fits in the current measure
                if (currentMeasureDuration + noteDuration > measureCapacity) {
                    // Note doesn't fit, close current measure and start a new one
                    xml.append("    </measure>\n");
                    measureNumber++;
                    xml.append("    <measure number=\"").append(measureNumber).append("\">\n");
                    // Reset duration for the new measure
                    currentMeasureDuration = 0;
                    // TODO: Add attribute changes here if needed (e.g., key/time signature changes mid-song)
                }

                // Add the note/rest XML
                appendNoteXml(xml, note, noteDuration);

                // Update current measure duration
                currentMeasureDuration += noteDuration;

                // Check if measure is exactly full after adding the note
                if (currentMeasureDuration == measureCapacity && notes.indexOf(note) < notes.size() - 1) {
                     // Close current measure and start a new one for the next note
                     xml.append("    </measure>\n");
                     measureNumber++;
                     xml.append("    <measure number=\"").append(measureNumber).append("\">\n");
                     currentMeasureDuration = 0;
                     // TODO: Add attribute changes here if needed
                }
            }

            // Close the last measure
            xml.append("    </measure>\n");
        }

        xml.append("  </part>\n");
        xml.append("</score-partwise>\n");

        return xml.toString();
    }

    // Helper to append the <attributes> block
    private static void appendAttributes(StringBuilder xml, Song song) {
        xml.append("      <attributes>\n");
        xml.append("        <divisions>").append(DIVISIONS).append("</divisions>\n");

        int fifths = KEY_TO_FIFTHS.getOrDefault(song.getKeySignature(), 0);
        xml.append("        <key>\n");
        xml.append("          <fifths>").append(fifths).append("</fifths>\n");
        xml.append("        </key>\n");

        TimeSignature timeSig = song.getTimeSignature() != null ? song.getTimeSignature() : new TimeSignature(4, 4);
        xml.append("        <time>\n");
        xml.append("          <beats>").append(timeSig.getNumerator()).append("</beats>\n");
        xml.append("          <beat-type>").append(timeSig.getDenominator()).append("</beat-type>\n");
        xml.append("        </time>\n");

        // Assuming treble clef for now
        xml.append("        <clef>\n");
        xml.append("          <sign>G</sign>\n");
        xml.append("          <line>2</line>\n");
        xml.append("        </clef>\n");
        xml.append("      </attributes>\n");
    }

    // Helper to append the tempo <direction> block
    private static void appendTempo(StringBuilder xml, Song song) {
         if (song.getTempo() > 0) {
            xml.append("      <direction placement=\"above\">\n");
            xml.append("        <direction-type>\n");
            xml.append("          <metronome>\n");
            xml.append("            <beat-unit>quarter</beat-unit>\n"); // Assumes tempo is quarter note based
            xml.append("            <per-minute>").append(song.getTempo()).append("</per-minute>\n");
            xml.append("          </metronome>\n");
            xml.append("        </direction-type>\n");
            xml.append("        <sound tempo=\"").append(song.getTempo()).append("\"/>\n");
            xml.append("      </direction>\n");
        }
    }

    // Helper to append the XML for a single note or rest
    private static void appendNoteXml(StringBuilder xml, Note note, int musicXmlDuration) {
        if ("Rest".equalsIgnoreCase(note.getPitch())) {
            xml.append("        <note>\n");
            xml.append("          <rest/>\n");
            xml.append("          <duration>").append(musicXmlDuration).append("</duration>\n");
            String restType = DURATION_TO_TYPE.getOrDefault(note.getDuration(), "quarter");
            xml.append("          <type>").append(restType).append("</type>\n");
            xml.append("        </note>\n");
        } else {
            xml.append("        <note>\n");
            xml.append("          <pitch>\n");
            xml.append("            <step>").append(note.getPitch().substring(0, 1)).append("</step>\n");
            int alter = 0;
            boolean hasSharp = note.getPitch().contains("#");
            boolean hasFlat = note.getPitch().contains("b");
            if (hasSharp) alter = 1;
            if (hasFlat) alter = -1; // Flat overrides sharp if both present (unlikely)
            if (alter != 0) {
                xml.append("            <alter>").append(alter).append("</alter>\n");
            }
            xml.append("            <octave>").append(note.getOctave()).append("</octave>\n");
            xml.append("          </pitch>\n");
            xml.append("          <duration>").append(musicXmlDuration).append("</duration>\n");
            String noteType = DURATION_TO_TYPE.getOrDefault(note.getDuration(), "quarter");
            xml.append("          <type>").append(noteType).append("</type>\n");
            // Add accidental display if needed (can be complex, requires context)
            // For simplicity, let's omit explicit <accidental> for now.
            // Lilypond usually infers it from key signature and <alter>.
            // if (alter != 0) {
            //     xml.append("          <accidental>").append(hasSharp ? "sharp" : "flat").append("</accidental>\n");
            // }
            xml.append("        </note>\n");
        }
    }

    // Helper to convert duration (beats, where 1.0 = quarter note) to MusicXML duration
    private static int getMusicXmlDuration(double beatDuration) {
        return (int) Math.round(beatDuration * DIVISIONS); // Use Math.round for potentially fractional JFugue durations
    }
    
    // Basic XML escaping for content
    private static String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
} 