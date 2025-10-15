package org.escaperoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class ProgressManagerTest {

    private ProgressManager progressManager;

    @BeforeEach
    void setUp() {
        progressManager = new ProgressManager();
        File file = new File("progress.json");
        if (file.exists()) file.delete(); // start fresh
    }

    @Test
    void testDefaultProgressCreated() {
        ProgressData progress = progressManager.loadProgress();
        assertNotNull(progress);
        assertEquals(200, progress.getTeus());
        assertArrayEquals(new int[]{1}, progress.getUnlockedLevels());
        assertNotNull(progress.getSettings());
        assertTrue(progress.getSettings().isSoundEnabled());
        assertTrue(progress.getSettings().isHintsEnabled());
    }

    @Test
    void testSaveAndLoadProgress() {
        ProgressData progress = new ProgressData();
        progress.setTeus(500);
        progress.setUnlockedLevels(new int[]{1, 2, 3});
        progress.setSettings(new ProgressData.Settings(false, true));

        progressManager.saveProgress(progress);

        ProgressData loaded = progressManager.loadProgress();
        assertEquals(500, loaded.getTeus());
        assertArrayEquals(new int[]{1,2,3}, loaded.getUnlockedLevels());
        assertFalse(loaded.getSettings().isSoundEnabled());
        assertTrue(loaded.getSettings().isHintsEnabled());
    }

    @Test
    void testInvalidTeusThrowsException() {
        ProgressData progress = new ProgressData();
        assertThrows(IllegalArgumentException.class, () -> progress.setTeus(-10));
    }
}

