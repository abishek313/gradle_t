package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateLevel3Test {

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
    void testPickUpItems() {
        for (Item item : level3.items) {
            ActionResult result = gameState.pickUpItem(item);
            assertTrue(result.isSuccess());
            assertTrue(result.getMessage().contains(item.getName()));
        }

        assertEquals(level3.items.length, gameState.getInventoryNames().length);
    }

    @Test
    void testBuyHints() {
        int startingTeus = gameState.getTeus();
        ActionResult hint = gameState.buyHint(level3);

        assertNotNull(hint);
        assertTrue(hint.isSuccess());
        assertTrue(hint.getMessage().contains("Hint:"));
        assertEquals(startingTeus - 50, gameState.getTeus());
    }

    @Test
    void testTEUsNotEnoughForHint() {
        gameState.setTeus(40);
        ActionResult hint = gameState.buyHint(level3);
        assertFalse(hint.isSuccess());
        assertTrue(hint.getMessage().contains("Not enough TEUs"));
    }
}

