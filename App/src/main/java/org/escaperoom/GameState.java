package org.escaperoom;

public class GameState {
    private Inventory inventory = new Inventory();
    private int teus = 200;
    private int hintIndex = 0;

    public String pickUpItem(Item item) {
        inventory.addItem(item);
        return item.name + " added to inventory!";
    }

    public String tryPuzzle(Puzzle puzzle) {
        if (puzzle.requiredItems != null) {
            for (String reqId : puzzle.requiredItems) {
                if (!inventory.hasItem(reqId)) {
                    return "Puzzle " + puzzle.id + " cannot be solved. Missing " + reqId;
                }
            }
        }
        return "Puzzle " + puzzle.id + " solved! Door unlocked!";
    }

    public String[] getInventoryNames() {
        return inventory.getItemNames().toArray(new String[0]);
    }

    public String buyHint(Level level) {
        if (teus < 50) return "Not enough TEUs for a hint.";
        if (hintIndex >= level.hints.length) return "No more hints available.";
        teus -= 50;
        return "Hint: " + level.hints[hintIndex++];
    }

    public int getTeus() {
        return teus;
    }
}

