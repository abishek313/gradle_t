package org.escaperoom;  // Ensure this is at the top, as in your code
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ProgressData {
    @JsonProperty("teus")
    private int teus;
    
    @JsonProperty("unlockedLevels")
    private int[] unlockedLevels;
    
    @JsonProperty("settings")
    private Settings settings;

    // No-arg constructor for Jackson deserialization
    public ProgressData() {}
    
    // Getters and setters with basic validation
    public int getTeus() { return teus; }
    
    public void setTeus(int teus) {
        if (teus < 0) {
            throw new IllegalArgumentException("TEUs cannot be negative.");
        }
        this.teus = teus;
    }
    
    public int[] getUnlockedLevels() { return unlockedLevels; }
    
    public void setUnlockedLevels(int[] unlockedLevels) {
        this.unlockedLevels = unlockedLevels;  // You could add array validation if needed
    }
    
    public Settings getSettings() { return settings; }
    
    public void setSettings(Settings settings) { this.settings = settings; }
    
    // Nested Settings class with enhancements
    public static class Settings {
        @JsonProperty("soundEnabled")
        private boolean soundEnabled;
        
        @JsonProperty("hintsEnabled")
        private boolean hintsEnabled;
        
        public Settings() {}  // No-arg constructor
        
        public Settings(boolean soundEnabled, boolean hintsEnabled) {
            this.soundEnabled = soundEnabled;
            this.hintsEnabled = hintsEnabled;
        }
        
        public boolean isSoundEnabled() { return soundEnabled; }
        
        public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
        
        public boolean isHintsEnabled() { return hintsEnabled; }
        
        public void setHintsEnabled(boolean hintsEnabled) { this.hintsEnabled = hintsEnabled; }
        
        // Optional: Override toString for easier debugging
        @Override
        public String toString() {
            return "Settings{soundEnabled=" + soundEnabled + ", hintsEnabled=" + hintsEnabled + "}";
        }
    }
    
    // Optional: Override toString for the main class
    @Override
    public String toString() {
        return "ProgressData{teus=" + teus + ", unlockedLevels=" + Arrays.toString(unlockedLevels) + ", settings=" + settings + "}";
    }
}
