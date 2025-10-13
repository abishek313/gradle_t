package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PuzzleTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void puzzleFailsWithoutRequiredItem() {
        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});

        ActionResult result = gameState.tryPuzzle(door);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void puzzleSucceedsWithRequiredItem() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});

        ActionResult result = gameState.tryPuzzle(door);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleSucceedsWithMultipleRequiredItems() {
        Item card = new Item("card1", "Access Card");
        Item key = new Item("key1", "Golden Key");
        gameState.pickUpItem(card);
        gameState.pickUpItem(key);

        Puzzle treasureDoor = new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"});

        ActionResult result = gameState.tryPuzzle(treasureDoor);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleFailsIfSomeRequiredItemsMissing() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Puzzle treasureDoor = new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"});

        ActionResult result = gameState.tryPuzzle(treasureDoor);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void puzzleWithNoRequiredItemsSucceeds() {
        Puzzle freePuzzle = new Puzzle("door3", "Open Door", new String[]{});

        ActionResult result = gameState.tryPuzzle(freePuzzle);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleWithNullRequiredItemsSucceeds() {
        Puzzle freePuzzle = new Puzzle("door4", "Mystery Door", null);

        ActionResult result = gameState.tryPuzzle(freePuzzle);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleFailsWhenNullPuzzleProvided() {
        ActionResult result = gameState.tryPuzzle(null);

        assertFalse(result.isSuccess());
        assertEquals("Puzzle is null!", result.getMessage());
    }
}

