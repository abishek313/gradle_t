package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Level2LoaderTest {

    @Test
    void testLoadLevel2() {
        // Load the level2.json file
        Level level = LevelLoader.loadLevel("level2.json");

        // Basic checks
        assertNotNull(level, "Level should not be null");
        assertEquals(2, level.levelId);
        assertEquals("The Hidden Chamber", level.name);
        assertEquals(400, level.timeLimitSec);

        // Items
        assertNotNull(level.items, "Level items should not be null");
        assertEquals(2, level.items.length);
        assertEquals("Golden Key", level.items[0].name);
        assertEquals("Security Card", level.items[1].name);

        // Puzzles
        assertNotNull(level.puzzles, "Level puzzles should not be null");
        assertEquals(2, level.puzzles.length);
        assertEquals("door2", level.puzzles[0].getId());
        assertEquals("chest1", level.puzzles[1].getId());

        // Hints
        assertNotNull(level.hints, "Level hints should not be null");
        assertEquals(3, level.hints.length);
        assertEquals("Check behind the bookshelf", level.hints[0]);
        assertEquals("The drawer might have a key", level.hints[1]);
        assertEquals("Use the golden key on the chest", level.hints[2]);
    }
}

