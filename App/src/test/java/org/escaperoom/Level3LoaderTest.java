package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Level3LoaderTest {

    @Test
    void testLoadLevel3() {
        Level level = LevelLoader.loadLevel("level3.json");

        assertNotNull(level);
        assertEquals(3, level.levelId);
        assertEquals("The Forgotten Vault", level.name);
        assertEquals(500, level.timeLimitSec);

        assertNotNull(level.items);
        assertEquals("Ruby Gem", level.items[0].name);

        assertNotNull(level.puzzles);
        assertEquals("vaultDoor", level.puzzles[0].getId());

        assertNotNull(level.hints);
        assertEquals("Check the statue for hidden items", level.hints[0]);
    }
}
