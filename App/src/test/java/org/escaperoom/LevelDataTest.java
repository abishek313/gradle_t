package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelDataTest {

    @Test
    void testLevelDataFields() {
        LevelData data = new LevelData();
        data.levelId = 1;
        data.name = "Test Level";
        data.timeLimitSec = 100;

        LevelData.Item item = new LevelData.Item();
        item.id = "i1"; item.name = "Key";
        data.items = new LevelData.Item[]{item};

        LevelData.Puzzle puzzle = new LevelData.Puzzle();
        puzzle.setId("p1");
        puzzle.setDescription("Open Door");
        puzzle.setRequiredItems(new String[]{"i1"});
        data.puzzles = new LevelData.Puzzle[]{puzzle};

        data.hints = new String[]{"Try the key"};

        assertEquals(1, data.levelId);
        assertEquals("Test Level", data.name);
        assertEquals(100, data.timeLimitSec);
        assertEquals(1, data.items.length);
        assertEquals("Key", data.items[0].name);
        assertEquals("p1", data.puzzles[0].getId());
        assertArrayEquals(new String[]{"i1"}, data.puzzles[0].getRequiredItems());
        assertEquals("Try the key", data.hints[0]);
    }
}
