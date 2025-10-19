package org.escaperoom.ui.controllers;

import javafx.fxml.FXML;

public class PauseController {

    @FXML
    private void resumeGame() {
        System.out.println("Resume Game (hook to timer pause/resume)");
    }

    @FXML
    private void quitGame() {
        System.exit(0);
    }
}
