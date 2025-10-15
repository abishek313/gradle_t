package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelLoaderTest {

    @Test
    void testLoadLevel1() {
        // Load level 1 using integer ID
        Level level = LevelLoader.loadLevel(1);

        // Basic checks
        assertNotNull(level);
        assertEquals(1, level.getLevelId());
        assertEquals("The Awakening", level.getName());
        assertEquals(300, level.getTimeLimitSec());

        // Items
        assertNotNull(level.getItems());
        assertEquals(1, level.getItems().length);
        assertEquals("Access Card", level.getItems()[0].getName());

        // Puzzles
        assertNotNull(level.getPuzzles());
        assertEquals(1, level.getPuzzles().length);
        assertEquals("door1", level.getPuzzles()[0].getId());

        // Hints
        assertNotNull(level.getHints());
        assertEquals(1, level.getHints().length);
        assertEquals("Search under furniture.", level.getHints()[0]);
    }
}


