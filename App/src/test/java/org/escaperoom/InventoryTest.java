package org.escaperoom;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    @Test
    void testInventoryStartsEmpty() {
        Inventory inv = new Inventory();
        assertTrue(inv.isEmpty(), "Inventory should start empty");
        assertEquals(0, inv.getItems().size(), "Inventory list should be empty");
    }

    @Test
    void testAddAndCheckItem() {
        Inventory inv = new Inventory();

        Item card = new Item("card1", "Access Card");
        inv.addItem(card);

        assertTrue(inv.hasItem("card1"));
        assertFalse(inv.hasItem("fakeId"));

        List<String> names = inv.getItemNames();
        assertEquals(1, names.size());
        assertEquals("Access Card", names.get(0));
    }

    @Test
    void testAddMultipleItems() {
        Inventory inv = new Inventory();

        Item card = new Item("card1", "Access Card");
        Item key = new Item("key1", "Golden Key");

        inv.addItem(card);
        inv.addItem(key);

        assertTrue(inv.hasItem("card1"));
        assertTrue(inv.hasItem("key1"));
        assertEquals(2, inv.getItems().size());

        List<String> names = inv.getItemNames();
        assertTrue(names.contains("Access Card"));
        assertTrue(names.contains("Golden Key"));
    }

    @Test
    void testIsEmptyAfterAddingItems() {
        Inventory inv = new Inventory();
        assertTrue(inv.isEmpty());

        inv.addItem(new Item("card1", "Access Card"));
        assertFalse(inv.isEmpty());
    }

    @Test
    void testGetItemsReturnsCorrectList() {
        Inventory inv = new Inventory();
        Item card = new Item("card1", "Access Card");
        inv.addItem(card);

        List<Item> items = inv.getItems();
        assertEquals(1, items.size());
        assertEquals("card1", items.get(0).getId());
        assertEquals("Access Card", items.get(0).getName());
    }

    @Test
    void testInventoryHandlesDuplicates() {
        Inventory inv = new Inventory();
        Item card = new Item("card1", "Access Card");

        inv.addItem(card);
        inv.addItem(card); // add same item again

        assertEquals(2, inv.getItems().size(), "Inventory currently allows duplicates");
    }
}

