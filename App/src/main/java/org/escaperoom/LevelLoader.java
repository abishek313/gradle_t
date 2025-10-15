package org.escaperoom;

import java.util.logging.Logger;

public class LevelLoader {
    private static final Logger LOGGER = Logger.getLogger(LevelLoader.class.getName());
    private static final DataLoader dataLoader = new DataLoader(); // make static

    public static Level loadLevel(int levelId) {
    String filePath = "levels/level" + levelId + ".json";
    LOGGER.info("Loading level JSON from: " + filePath);
    LevelData data = dataLoader.loadData(filePath, LevelData.class);
    return new Level(data); // convert LevelData -> Level
}

}





