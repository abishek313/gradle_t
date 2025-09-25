package org.escaperoom;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items = new ArrayList<>();

    // Add an item to the inventory
    public void addItem(Item item) {
        items.add(item);
    }

    // Check if an item exists in the inventory by id
    public boolean hasItem(String itemId) {
        return items.stream().anyMatch(i -> i.id.equals(itemId));
    }

    // Get list of item names
    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (Item i : items) {
            names.add(i.name);
        }
        return names;
    }

    // Get the actual list of Item objects
    public List<Item> getItems() {
        return items;
    }

    // Check if inventory is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
