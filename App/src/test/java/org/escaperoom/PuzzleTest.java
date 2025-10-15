package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleTest {

    private GameState gameState;
    private Level level;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        // Create a dummy Level for testing puzzles
        level = new Level();
        level.setItems(new Item[]{
                new Item("card1", "Access Card"),
                new Item("key1", "Golden Key")
        });
        level.setPuzzles(new Puzzle[]{
                new Puzzle("door1", "Locked Door", new String[]{"card1"}),
                new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"}),
                new Puzzle("door3", "Open Door", new String[]{}),
                new Puzzle("door4", "Mystery Door", null)
        });
        gameState.resetLevelHints(level);
    }

    @Test
    void puzzleFailsWithoutRequiredItem() {
        ActionResult result = gameState.tryPuzzle(level, 0); // door1
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void puzzleSucceedsWithRequiredItem() {
        gameState.pickUpItem(level.getItems()[0]); // card1
        ActionResult result = gameState.tryPuzzle(level, 0); // door1
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleSucceedsWithMultipleRequiredItems() {
        gameState.pickUpItem(level.getItems()[0]); // card1
        gameState.pickUpItem(level.getItems()[1]); // key1
        ActionResult result = gameState.tryPuzzle(level, 1); // door2
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleFailsIfSomeRequiredItemsMissing() {
        gameState.pickUpItem(level.getItems()[0]); // card1
        ActionResult result = gameState.tryPuzzle(level, 1); // door2
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void puzzleWithNoRequiredItemsSucceeds() {
        ActionResult result = gameState.tryPuzzle(level, 2); // door3
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleWithNullRequiredItemsSucceeds() {
        ActionResult result = gameState.tryPuzzle(level, 3); // door4
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleFailsWhenNullPuzzleProvided() {
        ActionResult result = gameState.tryPuzzle((Level)null, 0);
        assertFalse(result.isSuccess());
        assertEquals("Puzzle is null!", result.getMessage());
    }
}


