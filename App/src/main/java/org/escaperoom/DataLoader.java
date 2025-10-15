package org.escaperoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class DataLoader {
    private static final Logger LOGGER = Logger.getLogger(DataLoader.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> T loadData(String fileName, Class<T> clazz) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
        if (in == null) {
            LOGGER.severe("JSON file not found on classpath: " + fileName);
            throw new CustomFileNotFoundException("File is missing: " + fileName);
        }

        try {
            return objectMapper.readValue(in, clazz);
        } catch (IOException e) {
            LOGGER.severe("Error parsing JSON file: " + e.getMessage());
            throw new CustomParsingException("Failed to parse JSON: " + fileName, e);
        }
    }

    // Custom exceptions
    public static class CustomFileNotFoundException extends RuntimeException {
        public CustomFileNotFoundException(String message) { super(message); }
    }

    public static class CustomParsingException extends RuntimeException {
        public CustomParsingException(String message, Throwable cause) { super(message, cause); }
    }
}

