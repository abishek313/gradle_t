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
        level2 = LevelLoader.loadLevel(2);
        gameState.resetLevelHints(level2);
        gameState.setTeus(200); // reset TEUs for tests
    }

    @Test
    void inventoryStartsEmpty() {
        assertNotNull(gameState.getInventoryNames());
        assertEquals(0, gameState.getInventoryNames().length);
    }

    @Test
    void canPickUpLevel2Items() {
        for (Item item : level2.getItems()) {  // use getter
            ActionResult result = gameState.pickUpItem(item);
            assertTrue(result.isSuccess(), "Should pick up " + item.getName());
        }

        String[] inventoryNames = gameState.getInventoryNames();
        assertEquals(level2.getItems().length, inventoryNames.length);  // use getter
    }

    @Test
void puzzleFailsWithoutRequiredItems() {
    ActionResult result = gameState.tryPuzzle(level2, 0); // datacore puzzle
    assertFalse(result.isSuccess(), "Puzzle should fail without notes");
}

@Test
void puzzleSucceedsWithRequiredItems() {
    // Pick up all required notes
    for (Item item : level2.getItems()) {
        gameState.pickUpItem(item);
    }

    ActionResult result = gameState.tryPuzzle(level2, 0);
    assertTrue(result.isSuccess(), "Puzzle should succeed with all notes");
}

    @Test
    void canBuyAllHints() {
    int startingTeus = gameState.getTeus();

    for (int i = 0; i < level2.getHints().length; i++) {
        ActionResult hint = gameState.buyHint(level2);
        assertTrue(hint.isSuccess());
        assertTrue(hint.getMessage().contains("Hint:"));
    }


        // All hints used
        ActionResult noHint = gameState.buyHint(level2);
        assertFalse(noHint.isSuccess());
        assertEquals("No more hints available.", noHint.getMessage());

        // TEUs deducted correctly
        assertEquals(startingTeus - (level2.getHints().length * 50), gameState.getTeus());
    }

    @Test
    void cannotBuyHintWithoutEnoughTeus() {
        gameState.setTeus(40); // less than hint cost
        ActionResult result = gameState.buyHint(level2);
        assertFalse(result.isSuccess());
        assertEquals("Not enough TEUs for a hint.", result.getMessage());
    }
}

