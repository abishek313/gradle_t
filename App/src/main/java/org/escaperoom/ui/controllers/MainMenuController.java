package org.escaperoom.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import org.escaperoom.DataLoader;
import org.escaperoom.Level;
import org.escaperoom.ui.controllers.GameController;

import java.io.IOException;

public class MainMenuController {

    @FXML private Button startBtn;
    @FXML private ImageView backgroundImageView;
    @FXML private Button settingsBtn;
    @FXML private Button exitBtn;

    @FXML
    public void initialize() {
        // Wire the start button
        System.out.println("MainMenuController initialized, startBtn = " + startBtn);
        startBtn.setOnAction(e -> {
        System.out.println("Start button clicked!");
        startGame();
        });

        // Bind background image to scene size once the scene is ready
        startBtn.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImageView.fitWidthProperty().bind(newScene.widthProperty());
                backgroundImageView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });  
        if (settingsBtn != null) settingsBtn.setOnAction(e -> openSettingsPopup());
            if (exitBtn != null) exitBtn.setOnAction(e -> exitGame());;
    }
    
    /** Start the first level and switch to game scene */
    @FXML
private void startGame() {
    System.out.println("Start button clicked!");

    try {
        // Load GameUI.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameUI.fxml"));
        StackPane root = loader.load();
        System.out.println("FXML loaded");

        // Load level 1 JSON
        DataLoader dataLoader = new DataLoader();
        Level firstLevel = dataLoader.loadData("levels/level1.json", Level.class);
        System.out.println("Level loaded");

        // Initialize GameController
        GameController controller = loader.getController();
        if (controller == null) {
            System.out.println("Controller is null!");
        } else {
            System.out.println("Controller obtained: " + controller);
        }
        controller.initLevel(firstLevel, null);
        System.out.println("Controller initialized");

        // Create new Scene with medium size
        Scene scene = new Scene(root, 800, 600);

        // Get current stage
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        System.out.println("Scene switched");

        // Allow fullscreen toggle with F11
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F11 -> stage.setFullScreen(!stage.isFullScreen());
            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    /** Open settings popup as modal */
    private void openSettingsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.initModality(Modality.WINDOW_MODAL);

            // Set owner if available
            if (settingsBtn.getScene() != null) {
                Stage primaryStage = (Stage) settingsBtn.getScene().getWindow();
                settingsStage.initOwner(primaryStage);

                // Center popup relative to main window
                settingsStage.setX(primaryStage.getX() + primaryStage.getWidth()/2 - 200);
                settingsStage.setY(primaryStage.getY() + primaryStage.getHeight()/2 - 150);
            }

            settingsStage.setResizable(false);
            settingsStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Close the game */
    private void exitGame() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }
}
