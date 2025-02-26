package com.harmoniqscrum;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

public class ToneGenerator {
    
    private static final int SAMPLE_RATE = 44100; 
    private static final Map<String, Double> NOTE_FREQUENCIES = new HashMap<>();
    
    static {
        NOTE_FREQUENCIES.put("C", 261.63);
        NOTE_FREQUENCIES.put("D", 293.66);
        NOTE_FREQUENCIES.put("E", 329.63);
        NOTE_FREQUENCIES.put("F", 349.23);
        NOTE_FREQUENCIES.put("G", 392.00);
        NOTE_FREQUENCIES.put("A", 440.00);
        NOTE_FREQUENCIES.put("B", 493.88);
    }
    
    /**
     * Play a note with the given duration
     *
     * @param note The note to play (C, D, E, etc.)
     * @param milliseconds Duration in milliseconds
     */
    public static void play(String note, int milliseconds) {
        try {
            double frequency = NOTE_FREQUENCIES.getOrDefault(note, 440.0); 
            
            byte[] data = generateSineWaveData(frequency, milliseconds);
            
            AudioFormat format = new AudioFormat(
                    SAMPLE_RATE,  
                    8,            
                    1,            
                    true,         
                    false         
            );
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(
                    bais,
                    format,
                    data.length / format.getFrameSize()
            );
            
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            
            Thread.sleep(milliseconds);
            clip.stop();
            clip.close();
            
        } catch (Exception e) {
            System.err.println("Error playing note " + note + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate sine wave data
     *
     * @param frequency Frequency in Hz
     * @param milliseconds Duration in milliseconds
     * @return Byte array containing audio data
     */
    private static byte[] generateSineWaveData(double frequency, int milliseconds) {
        int samples = (int) (milliseconds * (SAMPLE_RATE / 1000.0));
        byte[] data = new byte[samples];
        double period = (double) SAMPLE_RATE / frequency;
        
        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * i / period;
            data[i] = (byte) (Math.sin(angle) * 127.0);
        }
        
        return data;
    }
    
    /**
     * Generate a WAV file for a note
     *
     * @param note The note to generate
     * @param filePath Path to save the file
     * @param milliseconds Duration in milliseconds
     * @return True if file was created successfully
     */
    public static boolean generateWavFile(String note, String filePath, int milliseconds) {
        try {
            double frequency = NOTE_FREQUENCIES.getOrDefault(note, 440.0); 
            
            byte[] data = generateSineWaveData(frequency, milliseconds);
            
            AudioFormat format = new AudioFormat(
                    SAMPLE_RATE,  
                    8,            
                    1,            
                    true,         
                    false         
            );
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(
                    bais,
                    format,
                    data.length / format.getFrameSize()
            );
            
            File outFile = new File(filePath);
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error generating WAV file for note " + note + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 