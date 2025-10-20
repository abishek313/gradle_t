package org.escaperoom.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Slider soundSlider;

    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        // Print value whenever slider changes
        soundSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Sound level: " + newVal.intValue());
            // Here you could connect this value to actual audio volume
        });

        backBtn.setOnAction(e -> {
            // Close the settings popup
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.close();
        });
    }
}