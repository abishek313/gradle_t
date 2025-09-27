package org.escaperoom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelLoaderTest {

    @Test
    void testLoadLevel1() {
        Level level = LevelLoader.loadLevel("level1.json"); // must be in resources

        assertNotNull(level);
        assertEquals(1, level.levelId);
        assertEquals("The Awakening", level.name);
        assertEquals(300, level.timeLimitSec);

        assertNotNull(level.items);
        assertEquals("Access Card", level.items[0].name);

        assertNotNull(level.puzzles);
        assertEquals("door1", level.puzzles[0].getId());

        assertNotNull(level.hints);
        assertEquals("Search under furniture.", level.hints[0]);
    }
}


