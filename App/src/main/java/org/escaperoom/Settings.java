package org.escaperoom;

public class Settings {
    public boolean sound; // true = enabled, false = disabled
    public boolean music; // true = enabled, false = disabled

    // No-arg constructor for Gson
    public Settings() {}

    // Optional constructor for manual creation
    public Settings(boolean sound, boolean music) {
        this.sound = sound;
        this.music = music;
    }
}

