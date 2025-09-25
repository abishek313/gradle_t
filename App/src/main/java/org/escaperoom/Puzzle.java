package org.escaperoom;

public class Puzzle {
    public String id;
    public String description;
    public String solution;
    public String[] clues;
    public String[] requiredItems; // just IDs
    public Puzzle() {}

    // Constructor for testing convenience
    public Puzzle(String id, String description, String[] requiredItems) {
        this.id = id;
        this.description = description;
        this.requiredItems = requiredItems;
    }
}


