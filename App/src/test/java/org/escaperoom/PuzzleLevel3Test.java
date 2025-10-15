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
        level3 = LevelLoader.loadLevel(3);
        gameState.resetLevelHints(level3);
        gameState.setTeus(200);
    }

    @Test
    void testRiddleCodePuzzle() {
        // Without item
        ActionResult fail = gameState.tryPuzzle(level3, 0);
        assertFalse(fail.isSuccess());

        // With item
        gameState.pickUpItem(level3.getItems()[0]); // riddle_paper
        ActionResult pass = gameState.tryPuzzle(level3, 0);
        assertTrue(pass.isSuccess());
    }

    @Test
    void testMachineFragmentPuzzle() {
        // Without item
        ActionResult fail = gameState.tryPuzzle(level3, 1);
        assertFalse(fail.isSuccess());

        // With item
        gameState.pickUpItem(level3.getItems()[1]); // lever
        ActionResult pass = gameState.tryPuzzle(level3, 1);
        assertTrue(pass.isSuccess());
    }

    @Test
    void testExitDoorPuzzle() {
        // Fail if any item missing
        ActionResult fail1 = gameState.tryPuzzle(level3, 2);
        assertFalse(fail1.isSuccess());

        // Fail if only one item
        gameState.pickUpItem(level3.getItems()[0]);
        ActionResult fail2 = gameState.tryPuzzle(level3, 2);
        assertFalse(fail2.isSuccess());

        gameState.pickUpItem(level3.getItems()[1]);
        // Pass if both items
        ActionResult pass = gameState.tryPuzzle(level3, 2);
        assertTrue(pass.isSuccess());
    }
}
