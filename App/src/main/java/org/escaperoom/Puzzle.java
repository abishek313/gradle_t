package org.escaperoom;

public class Puzzle {
    private String id;
    private String description;
    private String solution;
    private String[] clues;
    private String[] requiredItems; 
    private boolean solved = false;           // Track if puzzle is solved
    private String requiredItemName = null; 
    public Puzzle() {}

    public Puzzle(String id, String description, String[] requiredItems) {
        this.id = id;
        this.description = description;
        this.requiredItems = requiredItems;
    }

    public String getId() {
        return id;
    }
    public String getName() {
    return description;
    }
    public String getDescription() {
        return description;
    }

    public String getSolution() {
        return solution;
    }
    public boolean isSolved() {
        return solved;
    }
    public void setSolved(boolean solved) {
        this.solved = solved;
    }
    public String[] getClues() {
        return clues;
    }

    public String[] getRequiredItems() {
        return requiredItems;
    }
    public String getRequiredItemName() {
        return requiredItemName;
    }
    public void setRequiredItemName(String requiredItemName) {
        this.requiredItemName = requiredItemName;
    }
}



