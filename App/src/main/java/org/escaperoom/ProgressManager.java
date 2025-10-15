package org.escaperoom; 

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;  


public class ProgressManager {
    private static final Logger LOGGER = Logger.getLogger(ProgressManager.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String PROGRESS_FILE_PATH = "progress.json";

    public ProgressData loadProgress() {
        File progressFile = new File(PROGRESS_FILE_PATH);
        
        if (!progressFile.exists()) {
            LOGGER.warning("Progress file not found; creating a default one.");  // Logger is used here
            return createDefaultProgress();
        }
        
        try {
            ProgressData progress = objectMapper.readValue(progressFile, ProgressData.class);
            validateProgress(progress);  // Validate loaded data
            return progress;
        } catch (IOException e) {
            LOGGER.severe("Error loading progress file: " + e.getMessage());  // Logger is used here
            throw new CustomParsingException("Failed to load progress.json. Details: " + e.getMessage(), e);
        }
    }

    public void saveProgress(ProgressData progress) {
        try {
            objectMapper.writeValue(new File(PROGRESS_FILE_PATH), progress);
        } catch (IOException e) {
            LOGGER.severe("Error saving progress file: " + e.getMessage());  // Logger is used here
            throw new CustomParsingException("Failed to save progress.json. Details: " + e.getMessage(), e);
        }
    }

    private ProgressData createDefaultProgress() {
        ProgressData defaultProgress = new ProgressData();
        defaultProgress.setTeus(200);  // Starting TEUs
        defaultProgress.setUnlockedLevels(new int[]{1});  // Level 1 unlocked
        defaultProgress.setSettings(new ProgressData.Settings(true, true));  // Default settings
        saveProgress(defaultProgress);  // Save the default file
        return defaultProgress;
    }

    private void validateProgress(ProgressData progress) {
        if (progress.getUnlockedLevels() == null || progress.getTeus() < 0) {
            LOGGER.warning("Invalid progress data detected; resetting to default.");  // Logger is used here
            throw new CustomValidationException("Progress data is corrupted. Resetting to default.");
        }
    }

    // Additional custom exceptions
    public static class CustomValidationException extends RuntimeException {
        public CustomValidationException(String message) {
            super(message);
        }
    }

    public static class CustomParsingException extends RuntimeException {
        public CustomParsingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

