package org.escaperoom;

public class Progress {
    public int[] unlockedLevels; // store unlocked level IDs
    public int teus;             // store TEUs as integer
    public String[] inventory;   // names of items
    public Settings settings;    // game settings

    // No-arg constructor for Gson
    public Progress() {}
}

