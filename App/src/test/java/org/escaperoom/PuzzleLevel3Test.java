package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleLevel3Test {

    private GameState gameState;
    private Level level3;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        level3 = LevelLoader.loadLevel("level3.json");
        gameState.resetHints();
        gameState.setTeus(200);
    }

    @Test
    void testVaultDoorFailsWithoutItems() {
        Puzzle vaultDoor = level3.puzzles[0]; // vaultDoor
        ActionResult result = gameState.tryPuzzle(vaultDoor);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void testVaultDoorSucceedsWithItems() {
        gameState.pickUpItem(level3.items[1]); // key3
        gameState.pickUpItem(level3.items[2]); // card3

        Puzzle vaultDoor = level3.puzzles[0];
        ActionResult result = gameState.tryPuzzle(vaultDoor);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void testTreasureBoxFailsWithoutGem() {
        Puzzle treasureBox = level3.puzzles[1]; // treasureBox
        ActionResult result = gameState.tryPuzzle(treasureBox);
        assertFalse(result.isSuccess());
    }

    @Test
    void testTreasureBoxSucceedsWithGem() {
        gameState.pickUpItem(level3.items[0]); // gem1

        Puzzle treasureBox = level3.puzzles[1];
        ActionResult result = gameState.tryPuzzle(treasureBox);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }
}
