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
    Level level = new Level();
    level.setPuzzles(new Puzzle[]{ new Puzzle("door1", "Locked Door", new String[]{"card1"}) });
    gameState.resetLevelHints(level);

    ActionResult result = gameState.tryPuzzle(level, 0); // <-- index 0
    assertFalse(result.isSuccess());
}

@Test
void puzzleSucceedsWithRequiredItem() {
    // Pick up the required item
    Item card = new Item("card1", "Access Card");
    gameState.pickUpItem(card);

    // Create a level containing the puzzle
    Puzzle door = new Puzzle("door1", "Locked Door", new String[]{"card1"});
    LevelData data = new LevelData();
    data.puzzles = new LevelData.Puzzle[1];
    data.puzzles[0] = new LevelData.Puzzle();
    data.puzzles[0].setId(door.getId());
    data.puzzles[0].setDescription(door.getDescription());
    data.puzzles[0].setRequiredItems(door.getRequiredItems());
    data.items = new LevelData.Item[0];
    data.hints = new String[0];

    Level level = new Level(data);

    // Try the puzzle using the level and index 0
    ActionResult result = gameState.tryPuzzle(level, 0);

    assertTrue(result.isSuccess());
    assertTrue(result.getMessage().contains("solved"));
}

    @Test
void puzzleSucceedsWithMultipleRequiredItems() {
    // Pick up required items
    Item card = new Item("card1", "Access Card");
    Item key = new Item("key1", "Golden Key");
    gameState.pickUpItem(card);
    gameState.pickUpItem(key);

    // Create the puzzle
    Puzzle treasureDoor = new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"});

    // Wrap it inside LevelData
    LevelData data = new LevelData();
    data.puzzles = new LevelData.Puzzle[1];
    data.puzzles[0] = new LevelData.Puzzle();
    data.puzzles[0].setId(treasureDoor.getId());
    data.puzzles[0].setDescription(treasureDoor.getDescription());
    data.puzzles[0].setRequiredItems(treasureDoor.getRequiredItems());
    data.items = new LevelData.Item[0];
    data.hints = new String[0];

    Level level = new Level(data);

    // Try the puzzle using the level and index 0
    ActionResult result = gameState.tryPuzzle(level,0);

    assertTrue(result.isSuccess());
}


    @Test
void puzzleFailsIfSomeRequiredItemsMissing() {
    Item card = new Item("card1", "Access Card");
    gameState.pickUpItem(card);

    // Create the puzzle
    Puzzle treasureDoor = new Puzzle("door2", "Treasure Door", new String[]{"card1", "key1"});

    // Wrap in LevelData -> Level
    LevelData data = new LevelData();
    data.puzzles = new LevelData.Puzzle[1];
    data.puzzles[0] = new LevelData.Puzzle();
    data.puzzles[0].setId(treasureDoor.getId());
    data.puzzles[0].setDescription(treasureDoor.getDescription());
    data.puzzles[0].setRequiredItems(treasureDoor.getRequiredItems());
    data.items = new LevelData.Item[0];
    data.hints = new String[0];
    Level level = new Level(data);

    ActionResult result = gameState.tryPuzzle(level, 0);
    assertFalse(result.isSuccess());
}

    @Test
void puzzleWithNullOrEmptyRequiredItemsSucceeds() {
    // Create free puzzles
    Puzzle freePuzzle1 = new Puzzle("door3", "Open Door", new String[]{});
    Puzzle freePuzzle2 = new Puzzle("door4", "Mystery Door", null);

    // Wrap in LevelData -> Level
    LevelData data = new LevelData();
    data.puzzles = new LevelData.Puzzle[2];
    data.puzzles[0] = new LevelData.Puzzle();
    data.puzzles[0].setId(freePuzzle1.getId());
    data.puzzles[0].setDescription(freePuzzle1.getDescription());
    data.puzzles[0].setRequiredItems(freePuzzle1.getRequiredItems());

    data.puzzles[1] = new LevelData.Puzzle();
    data.puzzles[1].setId(freePuzzle2.getId());
    data.puzzles[1].setDescription(freePuzzle2.getDescription());
    data.puzzles[1].setRequiredItems(freePuzzle2.getRequiredItems());

    data.items = new LevelData.Item[0];
    data.hints = new String[0];
    Level level = new Level(data);

    assertTrue(gameState.tryPuzzle(level, 0).isSuccess());
    assertTrue(gameState.tryPuzzle(level, 1).isSuccess());
}

@Test
void puzzleFailsWhenNullPuzzleProvided() {
    LevelData data = new LevelData();
    data.puzzles = new LevelData.Puzzle[0];
    data.items = new LevelData.Item[0];
    data.hints = new String[0];
    Level level = new Level(data);

    ActionResult result = gameState.tryPuzzle(level, -1);
    assertFalse(result.isSuccess());
    assertEquals("Invalid puzzle index!", result.getMessage());
}


    // ---------- Hint / TEUs Tests ----------

    @Test
    void canBuyHint() {
        Level level = new Level();
        level.setHints(new String[]{"Look under the bed"}); // use setter
        gameState.resetLevelHints(level);

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
        level.setHints(new String[]{"Look under the bed"});
        gameState.resetLevelHints(level);

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals("Not enough TEUs for a hint.", result.getMessage());
    }

    @Test
    void cannotBuyHintIfAllHintsUsed() {
        Level level = new Level();
        level.setHints(new String[]{"Hint1"});
        gameState.resetLevelHints(level);

        gameState.buyHint(level); // use the only hint
        ActionResult result = gameState.buyHint(level);

        assertFalse(result.isSuccess());
        assertEquals("No more hints available.", result.getMessage());
    }

    @Test
    void resetHintsAllowsBuyingAgain() {
        Level level = new Level();
        level.setHints(new String[]{"Hint1"});
        gameState.resetLevelHints(level);

        gameState.buyHint(level); // use hint
        gameState.resetLevelHints(level);

        ActionResult result = gameState.buyHint(level);
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Hint:"));
    }

    @Test
    void buyingMultipleHintsDecreasesTeus() {
        Level level = new Level();
        level.setHints(new String[]{"Hint1", "Hint2", "Hint3"});
        gameState.resetLevelHints(level);

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
        level.setHints(new String[]{"Hint1"});
        gameState.resetLevelHints(level);

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
        level.setHints(new String[]{"Hint1"});
        gameState.resetLevelHints(level);

        ActionResult result = gameState.buyHint(level);
        assertFalse(result.isSuccess());
        assertEquals(40, gameState.getTeus(), "TEUs should remain unchanged");
    }
}


