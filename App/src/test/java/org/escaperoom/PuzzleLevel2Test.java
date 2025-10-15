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
        level2 = LevelLoader.loadLevel(2);       // Paradox Study
        gameState.resetLevelHints(level2);       // reset hints
        gameState.setTeus(200);                  // reset TEUs
    }

    @Test
    void datacorePuzzleFailsWithoutNotes() {
        ActionResult result = gameState.tryPuzzle(level2, 0); // datacore puzzle
        assertFalse(result.isSuccess(), "Puzzle should fail without notes");
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void datacorePuzzleSucceedsWithAllNotes() {
        // Pick up all 3 notes
        for (Item item : level2.getItems()) {
            gameState.pickUpItem(item);
        }

        ActionResult result = gameState.tryPuzzle(level2, 0);
        assertTrue(result.isSuccess(), "Puzzle should succeed with all notes");
        assertTrue(result.getMessage().contains("solved"));
    }
}

