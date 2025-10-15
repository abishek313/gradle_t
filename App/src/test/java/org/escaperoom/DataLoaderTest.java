package org.escaperoom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {

    private DataLoader loader = new DataLoader();

    @Test
    void testLoadValidJson() {
        LevelData data = loader.loadData("levels/level1.json", LevelData.class);
        assertNotNull(data);
        assertEquals(1, data.levelId);
        assertNotNull(data.items);
        assertNotNull(data.puzzles);
        assertNotNull(data.hints);
    }

    @Test
    void testMissingFileThrowsException() {
        assertThrows(DataLoader.CustomFileNotFoundException.class,
                () -> loader.loadData("levels/nonexistent.json", LevelData.class));
    }

    @Test
    void testInvalidJsonThrowsException() {
        assertThrows(DataLoader.CustomParsingException.class,
                () -> loader.loadData("levels/invalid.json", LevelData.class)); // assume invalid.json is malformed
    }
}
