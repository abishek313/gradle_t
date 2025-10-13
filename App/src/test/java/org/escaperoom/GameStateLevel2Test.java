package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateLevel2Test {

    private GameState gameState;
    private Level level2;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        level2 = LevelLoader.loadLevel("level2.json");
        gameState.resetHints();
        gameState.setTeus(200); // reset TEUs for tests
    }

    @Test
    void inventoryStartsEmpty() {
        assertNotNull(gameState.getInventoryNames());
        assertEquals(0, gameState.getInventoryNames().length);
    }

    @Test
    void canPickUpLevel2Items() {
        for (Item item : level2.items) {
            ActionResult result = gameState.pickUpItem(item);
            assertTrue(result.isSuccess(), "Should pick up " + item.getName());
        }

        String[] inventoryNames = gameState.getInventoryNames();
        assertEquals(level2.items.length, inventoryNames.length);
    }

    @Test
    void puzzleFailsWithoutRequiredItems() {
        Puzzle door = level2.puzzles[0]; // requires key1 + card2
        ActionResult result = gameState.tryPuzzle(door);
        assertFalse(result.isSuccess(), "Puzzle should fail without items");
    }

    @Test
    void puzzleSucceedsWithRequiredItems() {
        // Pick up all required items
        for (Item item : level2.items) {
            gameState.pickUpItem(item);
        }

        // Test all puzzles
        for (Puzzle puzzle : level2.puzzles) {
            ActionResult result = gameState.tryPuzzle(puzzle);
            assertTrue(result.isSuccess(), "Puzzle " + puzzle.getDescription() + " should succeed");
        }
    }

    @Test
    void canBuyAllHints() {
        int startingTeus = gameState.getTeus();

        for (int i = 0; i < level2.hints.length; i++) {
            ActionResult hint = gameState.buyHint(level2);
            assertTrue(hint.isSuccess());
            assertTrue(hint.getMessage().contains("Hint:"));
        }

        // All hints used
        ActionResult noHint = gameState.buyHint(level2);
        assertFalse(noHint.isSuccess());
        assertEquals("No more hints available.", noHint.getMessage());

        // TEUs deducted correctly
        assertEquals(startingTeus - (level2.hints.length * 50), gameState.getTeus());
    }

    @Test
    void cannotBuyHintWithoutEnoughTeus() {
        gameState.setTeus(40); // less than hint cost
        ActionResult result = gameState.buyHint(level2);
        assertFalse(result.isSuccess());
        assertEquals("Not enough TEUs for a hint.", result.getMessage());
    }
}

