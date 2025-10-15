package org.escaperoom;

public class GameState {
    private final Inventory inventory;
    private int teus;

    private static final int STARTING_TEUS = 200;
    private static final int HINT_COST = 50;

    public GameState() {
        this.inventory = new Inventory();
        this.teus = STARTING_TEUS;
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
    public ActionResult tryPuzzle(Level level, int puzzleIndex) {
    System.out.println("=== TRY PUZZLE DEBUG ===");

    if (level == null) {
        System.out.println("Level is null!");
        return new ActionResult("Puzzle is null!", false);
    }

    Puzzle[] puzzles = level.getPuzzles();
    if (puzzles == null) {
        System.out.println("Level has no puzzles!");
        return new ActionResult("Invalid puzzle index!", false);
    }

    System.out.println("Puzzle index: " + puzzleIndex);
    if (puzzleIndex < 0 || puzzleIndex >= puzzles.length) {
        System.out.println("Index out of bounds! Total puzzles: " + puzzles.length);
        return new ActionResult("Invalid puzzle index!", false);
    }

    Puzzle puzzle = puzzles[puzzleIndex];
    if (puzzle == null) {
        System.out.println("Puzzle object is null!");
        return new ActionResult("Puzzle is null!", false);
    }

    System.out.println("Trying puzzle: " + puzzle.getId() + " → " + puzzle.getDescription());

    // Show required items
    String[] required = puzzle.getRequiredItems();
    if (required == null) {
        System.out.println("Required items: null (no requirements)");
    } else {
        System.out.println("Required items: " + String.join(", ", required));
    }

    // Show inventory
    System.out.println("Inventory contents:");
    for (Item i : inventory.getItems()) {
        System.out.println("- " + i.getId() + " (" + i.getName() + ")");
    }

    // Check required items
    if (required != null && required.length > 0) {
        for (String reqId : required) {
            boolean hasIt = inventory.hasItem(reqId);
            System.out.println("Checking if player has '" + reqId + "': " + hasIt);
            if (!hasIt) {
                System.out.println("Missing item: " + reqId);
                return new ActionResult(
                        "Puzzle '" + puzzle.getDescription() + "' cannot be solved. Missing item: " + reqId,
                        false
                );
            }
        }
    }

    // Try solving
    boolean solved = level.solvePuzzle(puzzleIndex,this.inventory);
    System.out.println("Puzzle solved status: " + solved);

    if (solved) {
        System.out.println("✅ Puzzle success: " + puzzle.getDescription());
        return new ActionResult("Puzzle '" + puzzle.getDescription() + "' solved!", true);
    } else {
        System.out.println("⚠️ Puzzle already solved: " + puzzle.getDescription());
        return new ActionResult("Puzzle already solved.", false);
    }
}


    // Buy a hint from the level
    public ActionResult buyHint(Level level) {
        if (level == null || level.getHints() == null || level.getHints().length == 0) {
            return new ActionResult("No hints available for this level.", false);
        }

        if (teus < HINT_COST) {
            return new ActionResult("Not enough TEUs for a hint.", false);
        }

        String hint = level.getNextHint();
        if (hint == null) {
            return new ActionResult("No more hints available.", false);
        }

        teus -= HINT_COST;
        return new ActionResult("Hint: " + hint, true);
    }

    // Reset hints for a level
    public void resetLevelHints(Level level) {
        if (level != null) {
            level.resetHints();
        }
    }

    // ----------------------------
    // Getters and setters
    // ----------------------------
    public String[] getInventoryNames() {
        return inventory.getItemNames().toArray(new String[0]);
    }

    public int getTeus() {
        return teus;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setTeus(int teus) {
        this.teus = teus;
    }
}
