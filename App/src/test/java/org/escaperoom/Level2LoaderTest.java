package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Level2LoaderTest {

    @Test
    void testLoadLevel2() {
        // Load level 2
        Level level = LevelLoader.loadLevel(2);

        // Basic checks
        assertNotNull(level, "Level should not be null");
        assertEquals(2, level.getLevelId());
        assertEquals("The Paradox Study", level.getName());
        assertEquals(300, level.getTimeLimitSec());

        // Items
        assertNotNull(level.getItems(), "Level items should not be null");
        assertEquals(3, level.getItems().length);
        assertEquals("Note 1", level.getItems()[0].getName());
        assertEquals("Note 2", level.getItems()[1].getName());
        assertEquals("Note 3", level.getItems()[2].getName());

        // Puzzles
        assertNotNull(level.getPuzzles(), "Level puzzles should not be null");
        assertEquals(1, level.getPuzzles().length);
        assertEquals("datacore", level.getPuzzles()[0].getId());
        assertEquals("Collect 3 notes → code 3-7-2 → unlock Data Core",
                     level.getPuzzles()[0].getDescription());

        // Hints
        assertNotNull(level.getHints(), "Level hints should not be null");
        assertEquals(2, level.getHints().length);
        assertEquals("Notes contain numbers.", level.getHints()[0]);
        assertEquals("Combine into sequence.", level.getHints()[1]);
    }
}



