package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Level3LoaderTest {

    @Test
    void testLoadLevel3() {
        Level level = LevelLoader.loadLevel(3);

        // Basic checks
        assertNotNull(level, "Level should not be null");
        assertEquals(3, level.getLevelId());
        assertEquals("The Servant Machines", level.getName());
        assertEquals(400, level.getTimeLimitSec());

        // Items
        assertNotNull(level.getItems(), "Level items should not be null");
        assertEquals(2, level.getItems().length);
        assertEquals("Riddle Paper", level.getItems()[0].getName());
        assertEquals("Machine Lever", level.getItems()[1].getName());

        // Puzzles
        assertNotNull(level.getPuzzles(), "Level puzzles should not be null");
        assertEquals(3, level.getPuzzles().length);
        assertEquals("riddle_code", level.getPuzzles()[0].getId());
        assertEquals("machine_fragment", level.getPuzzles()[1].getId());
        assertEquals("exit_door", level.getPuzzles()[2].getId());

        // Hints
        assertNotNull(level.getHints(), "Level hints should not be null");
        assertEquals(2, level.getHints().length);
        assertEquals("The riddle holds a fragment.", level.getHints()[0]);
        assertEquals("The machine gives another fragment.", level.getHints()[1]);
    }
}

