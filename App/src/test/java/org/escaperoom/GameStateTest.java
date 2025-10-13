package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    // ---------- Inventory Tests ----------

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

    // ---------- Puzzle Tests ----------

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
    }

    @Test
    void puzzleFailsIfSomeRequiredItemsMissing() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Puzzle treasureDoor = new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"});
        ActionResult result = gameState.tryPuzzle(treasureDoor);

        assertFalse(result.isSuccess());
    }

    @Test
    void puzzleWithNullOrEmptyRequiredItemsSucceeds() {
        Puzzle freePuzzle1 = new Puzzle("door3", "Open Door", new String[]{});
        Puzzle freePuzzle2 = new Puzzle("door4", "Mystery Door", null);

        assertTrue(gameState.tryPuzzle(freePuzzle1).isSuccess());
        assertTrue(gameState.tryPuzzle(freePuzzle2).isSuccess());
    }

    @Test
    void puzzleFailsWhenNullPuzzleProvided() {
        ActionResult result = gameState.tryPuzzle(null);
        assertFalse(result.isSuccess());
        assertEquals("Puzzle is null!", result.getMessage());
    }

    // ---------- Hint / TEUs Tests ----------

    @Test
    void canBuyHint() {
        Level level = new Level();
        level.hints = new String[]{"Look under the bed"};

        int startingTeus = gameState.getTeus();
        ActionResult result = gameState.buyHint(level);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Hint:"));
        assertEquals(startingTeus - 50, gameState.getTeus());
    }

    @Test
    void cannotBuyHintIfNotEnoughTeus() {
        gameState.setTeus(0);

        Level level = new Level();
        level.hints = new String[]{"Look under the bed"};

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals("Not enough TEUs for a hint.", result.getMessage());
    }

    @Test
    void cannotBuyHintIfAllHintsUsed() {
        Level level = new Level();
        level.hints = new String[]{"Hint1"};
        gameState.buyHint(level); // use the only hint

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals("No more hints available.", result.getMessage());
    }

    @Test
    void resetHintsAllowsBuyingAgain() {
        Level level = new Level();
        level.hints = new String[]{"Hint1"};
        gameState.buyHint(level); // use hint
        gameState.resetHints();

        ActionResult result = gameState.buyHint(level);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Hint:"));
    }

    @Test
    void buyingMultipleHintsDecreasesTeus() {
        Level level = new Level();
        level.hints = new String[]{"Hint1", "Hint2", "Hint3"};

        int startTeus = gameState.getTeus();

        gameState.buyHint(level);
        gameState.buyHint(level);

        assertEquals(startTeus - 100, gameState.getTeus());
    }

    @Test
    void buyHintDoesNotChangeInventory() {
        Item card = new Item("card1", "Access Card");
        gameState.pickUpItem(card);

        Level level = new Level();
        level.hints = new String[]{"Hint1"};

        gameState.buyHint(level);
        assertEquals(1, gameState.getInventoryNames().length);
        assertEquals("Access Card", gameState.getInventoryNames()[0]);
    }

    // ---------- TEUs Tests ----------

    @Test
    void initialTeusIsCorrect() {
        assertEquals(200, gameState.getTeus());
    }

    @Test
    void setTeusWorks() {
        gameState.setTeus(500);
        assertEquals(500, gameState.getTeus());
    }

    @Test
    void teusCannotBeNegativeWhenBuyingHints() {
        gameState.setTeus(40);

        Level level = new Level();
        level.hints = new String[]{"Hint1"};

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals(40, gameState.getTeus(), "TEUs should remain unchanged");
    }
}

