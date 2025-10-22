package org.escaperoom;

import java.util.Arrays;

public class Level {
    public Level() {
        this.items = new Item[0];
        this.puzzles = new Puzzle[0];
        this.hints = new String[0];
        this.puzzlesSolved = new boolean[0];
        this.itemsCollected = new boolean[0];
        this.hintsUsed = 0;
    }

    private int levelId;
    private String name;
    private int timeLimitSec;
    
    // New fields for click areas
    private int accessCardX;
    private int accessCardY;
    private int doorX;
    private int doorY;
    private int doorWidth;
    private int doorHeight;

    private Item[] items;
    private Puzzle[] puzzles;
    private String[] hints;

    // Runtime state
    private boolean[] puzzlesSolved;
    private boolean[] itemsCollected;
    private int hintsUsed;  // Track how many hints have been used for this level

    public Level(LevelData data) {
        this.levelId = data.levelId;
        this.name = data.name;
        this.timeLimitSec = data.timeLimitSec;

        // Clickable areas
        this.accessCardX = data.accessCardX;
        this.accessCardY = data.accessCardY;
        this.doorX = data.doorX;
        this.doorY = data.doorY;
        this.doorWidth = data.doorWidth;
        this.doorHeight = data.doorHeight;

        // Items
        if (data.items != null) {
            this.items = new Item[data.items.length];
            for (int i = 0; i < data.items.length; i++) {
                LevelData.Item jsonItem = data.items[i];
                this.items[i] = new Item(jsonItem.id, jsonItem.name);
            }
        } else this.items = new Item[0];

        // Puzzles
        if (data.puzzles != null) {
            this.puzzles = new Puzzle[data.puzzles.length];
            for (int i = 0; i < data.puzzles.length; i++) {
                LevelData.Puzzle jsonPuzzle = data.puzzles[i];
                this.puzzles[i] = new Puzzle(
                        jsonPuzzle.getId(),
                        jsonPuzzle.getDescription(),
                        jsonPuzzle.getRequiredItems()
                );
            }
        } else {
            this.puzzles = new Puzzle[0];}

        // Hints
        this.hints = data.hints != null ? Arrays.copyOf(data.hints, data.hints.length) : new String[0];

        // Runtime tracking
        this.puzzlesSolved = new boolean[puzzles.length];
        this.itemsCollected = new boolean[items.length];
        this.hintsUsed = 0;
    }
    // Inside Level.java
    public boolean areAllPuzzlesSolved() {
        for (boolean solved : puzzlesSolved) {
            if (!solved) return false;
        }
        return true;
    }

    // ----------------------------
    // Runtime actions
    // ----------------------------
    public boolean collectItem(int index) {
        if (index < 0 || index >= items.length) return false;
        itemsCollected[index] = true;
        return true;
    }

    public boolean solvePuzzle(int index, Inventory inventory) {
    if (index < 0 || index >= puzzles.length) return false;

    Puzzle puzzle = puzzles[index];
    if (puzzle == null) return false;
    if (puzzlesSolved[index]) return false;

    // Check required items
    String[] required = puzzle.getRequiredItems();
    if (required != null && required.length > 0) {
        for (String reqId : required) {
            if (!inventory.hasItem(reqId)) {
                // Player missing a required item → cannot solve
                return false;
            }
        }
    }

    // All required items present → mark as solved
    puzzlesSolved[index] = true;
    return true;
}

    public String getNextHint() {
        if (hintsUsed >= hints.length) return null;
        return hints[hintsUsed++];
    }

    public void resetHints() {
        hintsUsed = 0;
    }

    // ----------------------------
    // Getters
    // ----------------------------
    public int getLevelId() { return levelId; }
    public String getName() { return name; }
    public int getTimeLimitSec() { return timeLimitSec; }
    public Item[] getItems() { return items; }
    public Puzzle[] getPuzzles() { return puzzles; }
    public String[] getHints() { return hints; }
    public int getHintsUsed() { return hintsUsed; }
    public boolean[] getPuzzlesSolved() { return puzzlesSolved; }
    public boolean[] getItemsCollected() { return itemsCollected; }
    public int getAccessCardX() { return accessCardX; }
    public void setAccessCardX(int accessCardX) { this.accessCardX = accessCardX; }

    public int getAccessCardY() { return accessCardY; }
    public void setAccessCardY(int accessCardY) { this.accessCardY = accessCardY; }

    public int getDoorX() { return doorX; }
    public void setDoorX(int doorX) { this.doorX = doorX; }

    public int getDoorY() { return doorY; }
    public void setDoorY(int doorY) { this.doorY = doorY; }

    public int getDoorWidth() { return doorWidth; }
    public void setDoorWidth(int doorWidth) { this.doorWidth = doorWidth; }

    public int getDoorHeight() { return doorHeight; }
    public void setDoorHeight(int doorHeight) { this.doorHeight = doorHeight; }
    //setters
    public void setHints(String[] hints) {
        this.hints = hints;
        this.hintsUsed = 0; // reset hints tracking
            }

    public void setPuzzles(Puzzle[] puzzles) {
        this.puzzles = puzzles;
        this.puzzlesSolved = new boolean[puzzles.length]; // reset puzzle tracking
    }

    public void setItems(Item[] items) {
        this.items = items;
        this.itemsCollected = new boolean[items.length]; // reset inventory tracking
    }

}
