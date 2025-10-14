package org.escaperoom;
import java.util.List;

public class GameState {
    private final Inventory inventory;
    private int teus;
    private int hintIndex;

    private static final int STARTING_TEUS = 200;
    private static final int HINT_COST = 50;

    public GameState() {
        this.inventory = new Inventory();
        this.teus = STARTING_TEUS;
        this.hintIndex = 0;
    }

    // Pick up an item
    public ActionResult pickUpItem(Item item) {
        if (item == null) {
            return new ActionResult("Cannot pick up a null item.", false);
        }
        inventory.addItem(item);
        return new ActionResult(item.getName() + " added to inventory!", true);
    }

    // Try to solve a puzzle
    public ActionResult tryPuzzle(Puzzle puzzle) {
        if (puzzle == null) {
            return new ActionResult("Puzzle is null!", false);
        }

        String[] required = puzzle.getRequiredItems();
        if (required != null) {
            for (String reqId : required) {
                if (!inventory.hasItem(reqId)) {
                    return new ActionResult(
                            "Puzzle " + puzzle.getDescription() + " cannot be solved. Missing " + reqId,
                            false
                    );
                }
            }
        }

        return new ActionResult("Puzzle " + puzzle.getDescription() + " solved! Door unlocked!", true);
    }

    // Buy a hint from the level
    public ActionResult buyHint(Level level) {
        if (level == null || level.hints == null || level.hints.length == 0) {
            return new ActionResult("No hints available.", false);
        }

        if (teus < HINT_COST) {
            return new ActionResult("Not enough TEUs for a hint.", false);
        }

        if (hintIndex >= level.hints.length) {
            return new ActionResult("No more hints available.", false);
        }

        String hint = level.hints[hintIndex++];
        teus -= HINT_COST;
        return new ActionResult("Hint: " + hint, true);
    }

    // Reset hints (useful when restarting a level)
    public void resetHints() {
        hintIndex = 0;
    }

    // Getters
    public String[] getInventoryNames() {
        List<String> names = inventory.getItemNames();
        return names.toArray(new String[0]);
    }

    public int getTeus() {
        return teus;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Set TEUs (useful for testing)
    public void setTeus(int teus) {
        this.teus = teus;
    }
}



