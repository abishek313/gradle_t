package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    // ----------------------------
    // Inventory Tests
    // ----------------------------
    @Test
    void inventoryStartsEmpty() {
        assertNotNull(gameState.getInventoryNames(), "Inventory should not be null");
        assertEquals(0, gameState.getInventoryNames().length, "Inventory should start empty");
    }

    @Test
    void canPickUpItem() {
        Item card = new Item("card1", "Access Card");
        ActionResult result = gameState.pickUpItem(card);

        assertTrue(result.isSuccess());
        assertEquals("Access Card added to inventory!", result.getMessage());
        assertEquals(1, gameState.getInventoryNames().length);
        assertEquals("Access Card", gameState.getInventoryNames()[0]);
    }

    @Test
    void cannotPickUpNullItem() {
        ActionResult result = gameState.pickUpItem(null);
        assertFalse(result.isSuccess());
        assertEquals("Cannot pick up a null item.", result.getMessage());
    }

    // ----------------------------
    // Puzzle Tests
    // ----------------------------
    @Test
    void puzzleFailsWithoutRequiredItem() {
        Level level = createTestLevelWithPuzzles(new Puzzle("door1", "Locked Door", new String[]{"card1"}));

        ActionResult result = gameState.tryPuzzle(level, 0);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("cannot be solved"));
    }

    @Test
    void puzzleSucceedsWithRequiredItem() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Level level = createTestLevelWithPuzzles(new Puzzle("door1", "Locked Door", new String[]{"card1"}));

        ActionResult result = gameState.tryPuzzle(level, 0);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("solved"));
    }

    @Test
    void puzzleSucceedsWithMultipleRequiredItems() {
        Item card = new Item("card1", "Access Card");
        Item key = new Item("key1", "Golden Key");
        gameState.pickUpItem(card);
        gameState.pickUpItem(key);

        Level level = createTestLevelWithPuzzles(
                new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"})
        );

        ActionResult result = gameState.tryPuzzle(level, 0);

        assertTrue(result.isSuccess());
    }

    // ----------------------------
    // Hint / TEU Tests
    // ----------------------------
    @Test
    void canBuyHintsUsingTeus() {
        Level level = createTestLevelWithHints("Look under the bed", "Check the desk drawer");

        int startingTeus = gameState.getTeus();
        ActionResult hint = gameState.buyHint(level);

        assertTrue(hint.isSuccess());
        assertTrue(hint.getMessage().contains("Hint:"));
        assertEquals(startingTeus - 50, gameState.getTeus());
    }

    @Test
    void cannotBuyHintIfNotEnoughTeus() {
        gameState.setTeus(0);
        Level level = createTestLevelWithHints("Look under the bed");

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals("Not enough TEUs for a hint.", result.getMessage());
    }

    @Test
    void cannotBuyHintIfAllHintsUsed() {
        Level level = createTestLevelWithHints("Hint1");
        gameState.buyHint(level); // use first hint

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals("No more hints available.", result.getMessage());
    }

    @Test
    void resetHintsAllowsBuyingAgain() {
        Level level = createTestLevelWithHints("Hint1");
        gameState.buyHint(level); // use first hint
        gameState.resetLevelHints(level);   // reset hints

        ActionResult result = gameState.buyHint(level);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Hint:"));
    }

    // ----------------------------
    // Helper Methods to create test Levels
    // ----------------------------
    // ----------------------------
// Helper Methods to create test Levels
// ----------------------------
private Level createTestLevelWithPuzzles(Puzzle... puzzles) {
    LevelData data = new LevelData();

    // Convert runtime Puzzle to LevelData.Puzzle
    LevelData.Puzzle[] jsonPuzzles = new LevelData.Puzzle[puzzles.length];
    for (int i = 0; i < puzzles.length; i++) {
        Puzzle p = puzzles[i];
        jsonPuzzles[i] = new LevelData.Puzzle();
        jsonPuzzles[i].setId(p.getId());
        jsonPuzzles[i].setDescription(p.getDescription());
        jsonPuzzles[i].setRequiredItems(p.getRequiredItems());

    }

    data.puzzles = jsonPuzzles;
    data.items = new LevelData.Item[0];
    data.hints = new String[0];

    return new Level(data);
}

private Level createTestLevelWithHints(String... hints) {
    LevelData data = new LevelData();
    data.hints = hints;
    data.items = new LevelData.Item[0];
    data.puzzles = new LevelData.Puzzle[0]; // empty LevelData.Puzzle array
    return new Level(data);
}
}


