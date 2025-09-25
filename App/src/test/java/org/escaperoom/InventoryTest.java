package org.escaperoom;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class InventoryTest {

    @Test
    void testAddAndCheckItem() {
        Inventory inv = new Inventory();

        Item card = new Item();
        card.id = "card1";
        card.name = "Access Card";

        inv.addItem(card);

        // Check if item is present
        assertTrue(inv.hasItem("card1"));
        assertFalse(inv.hasItem("fakeId"));

        // Check item names
        assertEquals(1, inv.getItemNames().size());
        assertEquals("Access Card", inv.getItemNames().get(0));
    }
}
