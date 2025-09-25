package org.escaperoom;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LevelLoader {

    public static Level loadLevel(String resourceName) {
        InputStream is = LevelLoader.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            throw new RuntimeException("Resource not found: " + resourceName);
        }

        try (Reader reader = new InputStreamReader(is)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Level.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




