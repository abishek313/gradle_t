package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleLevel2Test {

    private GameState gameState;
    private Level level2;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        level2 = LevelLoader.loadLevel("level2.json");
        gameState.resetHints();
        gameState.setTeus(200); // reset TEUs
    }

    @Test
    void testDoor2PuzzleFailsWithoutItems() {
        Puzzle door = level2.puzzles[0]; // door2
        ActionResult result = gameState.tryPuzzle(door);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void testDoor2PuzzleSucceedsWithItems() {
        // Pick up required items for door2
        gameState.pickUpItem(level2.items[0]); // key1
        gameState.pickUpItem(level2.items[1]); // card2

        Puzzle door = level2.puzzles[0];
        ActionResult result = gameState.tryPuzzle(door);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void testChest1PuzzleFailsWithoutKey() {
        Puzzle chest = level2.puzzles[1]; // chest1 requires key1
        ActionResult result = gameState.tryPuzzle(chest);
        assertFalse(result.isSuccess());
    }

    @Test
    void testChest1PuzzleSucceedsWithKey() {
        // Pick up only the key needed for chest1
        gameState.pickUpItem(level2.items[0]); // key1

        Puzzle chest = level2.puzzles[1];
        ActionResult result = gameState.tryPuzzle(chest);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }
}

