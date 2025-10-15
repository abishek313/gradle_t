package org.escaperoom;

public class LevelData {
    public int levelId;
    public String name;
    public int timeLimitSec;

    public Item[] items;
    public Puzzle[] puzzles;
    public String[] hints;

    // Nested classes
    public static class Item {
        public String id;
        public String name;
    }

    public static class Puzzle {
        private String id;
        private String description;
        private String[] requiredItems;

        public Puzzle() {}

    // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String[] getRequiredItems() { return requiredItems; }
        public void setRequiredItems(String[] requiredItems) { this.requiredItems = requiredItems; }
    }
}
