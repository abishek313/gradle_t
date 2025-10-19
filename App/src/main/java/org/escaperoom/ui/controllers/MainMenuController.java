package org.escaperoom.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.escaperoom.LevelLoader;
import org.escaperoom.Level;
import org.escaperoom.ui.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MainMenuController {

    @FXML private Button startBtn;
    @FXML private Button settingsBtn;
    @FXML private Button exitBtn;

    @FXML
    public void initialize() {
        startBtn.setOnAction(e -> startGame());
        settingsBtn.setOnAction(e -> openSettings());
        exitBtn.setOnAction(e -> exitGame());
    }

    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameUI.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.setScene(scene);

            // Initialize first level
            GameController controller = loader.getController();
            Level firstLevel = LevelLoader.loadLevel(1);
            controller.initLevel(firstLevel,null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openSettings() {
        System.out.println("Settings clicked (to implement)");
    }

    private void exitGame() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }
}
