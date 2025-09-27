package org.escaperoom;

public class Item {
    public String id;
    public String name;
    public String location;

    public Item() {
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String toString() {
        return "Item{id='" + id + "', name='" + name + "', location='" + location + "'}";
    }
        public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
