package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void inventoryStartsEmpty() {
        assertNotNull(gameState.getInventoryNames(), "Inventory array should not be null");
        assertEquals(0, gameState.getInventoryNames().length, "Inventory should start empty");
    }

    @Test
    void canPickUpAccessCard() {
        Item card = new Item("card1", "Access Card");
        assertNotNull(card, "Card should not be null");

        ActionResult result = gameState.pickUpItem(card);

        assertNotNull(result);
        assertEquals("Access Card added to inventory!", result.getMessage());
        assertTrue(result.isSuccess());
        assertTrue(gameState.getInventoryNames().length > 0);
    }

    @Test
    void puzzleFailsWithoutRequiredItem() {
        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});
        assertNotNull(door);

        ActionResult result = gameState.tryPuzzle(door);

        assertNotNull(result);
        assertTrue(result.getMessage().contains("cannot be solved"));
        assertFalse(result.isSuccess());
    }

    @Test
    void puzzleSucceedsWithRequiredItem() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});

        ActionResult result = gameState.tryPuzzle(door);

        assertNotNull(result);
        assertTrue(result.getMessage().contains("solved"));
        assertTrue(result.isSuccess());
    }

    @Test
    void canBuyHintsUsingTeus() {
        Level level = new Level();
        level.hints = new String[]{"Look under the bed", "Check the desk drawer"};

        int startingTeus = gameState.getTeus();
        ActionResult hint = gameState.buyHint(level);

        assertNotNull(hint);
        assertTrue(hint.getMessage().contains("Hint:"));
        assertEquals(startingTeus - 50, gameState.getTeus());
        assertTrue(hint.isSuccess());
    }
}

