package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private GameState gameState;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        inventory = new Inventory();
    }

    @Test
    void inventoryStartsEmpty() {
        assertTrue(inventory.isEmpty(), "Inventory should start empty");
    }

    @Test
    void canPickUpAccessCard() {
        Item card = new Item("card1", "Access Card");
        String result = gameState.pickUpItem(card);

        assertEquals("Access Card added to inventory!", result);
        assertTrue(gameState.getInventoryNames().length > 0);
    }

    @Test
    void puzzleFailsWithoutRequiredItem() {
        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});

        String result = gameState.tryPuzzle(door);

        assertTrue(result.contains("cannot be solved"), "Puzzle should fail if Access Card is missing");
    }

    @Test
    void puzzleSucceedsWithRequiredItem() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});
        String result = gameState.tryPuzzle(door);

        assertTrue(result.contains("solved"), "Puzzle should succeed once Access Card is in inventory");
    }

    @Test
    void canBuyHintsUsingTeus() {
        Level level = new Level();
        level.hints = new String[]{"Look under the bed", "Check the desk drawer"};

        int startingTeus = gameState.getTeus();
        String hint = gameState.buyHint(level);

        assertNotNull(hint, "Hint should be returned");
        assertTrue(hint.contains("Hint:"), "Hint text should include prefix");
        assertEquals(startingTeus - 50, gameState.getTeus(), "TEUs should decrease after buying a hint");
    }
}