package org.escaperoom;

public class GameState {
    private Inventory inventory = new Inventory();
    private int teus = 200;
    private int hintIndex = 0;

    
    public GameState() {
        this.inventory = new Inventory(); // ensure inventory is never null
        this.teus = 200;                  // starting TEUs
        this.hintIndex = 0;               // reset hint index
    }

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

    if (puzzle.getRequiredItems() != null) {
        for (String reqId : puzzle.getRequiredItems()) {
            if (!inventory.hasItem(reqId)) {
                return new ActionResult(
                    "Puzzle " + puzzle.getDescription() + " cannot be solved. Missing " + reqId,
                    false
                );
            }
        }
    }

    // All required items present → success
    return new ActionResult(
        "Puzzle " + puzzle.getDescription() + " solved! Door unlocked!",
        true
    );
}


    public String[] getInventoryNames() {
        return inventory.getItemNames().toArray(new String[0]);
    }

    public ActionResult buyHint(Level level) {
        if (level == null || level.hints == null || level.hints.length == 0){
            return new ActionResult("No hints available.", false);}
        if (teus < 50){
            return new ActionResult("Not enough TEUs for a hint.", false);}
        if (hintIndex >= level.hints.length){
            return new ActionResult("No more hints available.", false);}

        teus -= 50;
        return new ActionResult("Hint: " + level.hints[hintIndex++], true);
    }

    public int getTeus() {
        return teus;
    }
}


