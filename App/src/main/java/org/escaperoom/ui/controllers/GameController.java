package org.escaperoom.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.escaperoom.*;

public class GameController {

    private GameState gameState;
    private Level currentLevel;

    private Timeline timer;
    private int remainingSeconds;

    @FXML private Label teusLabel;
    @FXML private ListView<String> inventoryList;
    @FXML private Button hintButton;
    @FXML private Label hintLabel;
    @FXML private VBox itemsBox;
    @FXML private VBox puzzlesBox;
    @FXML private ImageView backgroundImageView;
    @FXML private Label timerLabel;

    /** Initialize level with items, puzzles, background, and timer */
    public void initLevel(Level level, GameState existingState) {
        this.currentLevel = level;
        this.gameState = existingState != null ? existingState : new GameState();
        hintLabel.setText("");

        setupBackground();
        setupItemButtons();
        setupPuzzleButtons();
        startTimer(level.getTimeLimitSec());
        updateHUD();

        // Hint button action
        hintButton.setOnAction(e -> showHint());
    }

    /** Load level background */
    private void setupBackground() {
        try {
            String path = "/images/level" + currentLevel.getLevelId() + ".png";
            Image img = new Image(getClass().getResourceAsStream(path));
            backgroundImageView.setImage(img);
        } catch (Exception e) {
            System.out.println("Background image not found for level " + currentLevel.getLevelId());
            backgroundImageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
        }
    }

    /** Setup item buttons dynamically */
    private void setupItemButtons() {
        itemsBox.getChildren().clear();
        for (Item item : currentLevel.getItems()) {
            Button btn = new Button(item.getName());
            btn.setOnAction(e -> handleItemClick(item));
            itemsBox.getChildren().add(btn);
        }
    }

    private void handleItemClick(Item item) {
        if (!gameState.hasItem(item)) {
            gameState.pickUpItem(item);  // Using GameState logic
            inventoryList.getItems().add(item.getName());
            hintLabel.setText("Picked up: " + item.getName());
        } else {
            hintLabel.setText(item.getName() + " already in inventory.");
        }
        updateHUD();
    }

    /** Setup puzzle buttons dynamically */
    private void setupPuzzleButtons() {
        puzzlesBox.getChildren().clear();
        Puzzle[] puzzles = currentLevel.getPuzzles();
        for (int i = 0; i < puzzles.length; i++) {
            Puzzle puzzle = puzzles[i];
            Button btn = new Button(puzzle.getName());
            int index = i;  // required for lambda
            btn.setOnAction(e -> handlePuzzleClick(puzzle, index));
            puzzlesBox.getChildren().add(btn);
        }
    }

    private void handlePuzzleClick(Puzzle puzzle, int puzzleIndex) {
        ActionResult result = gameState.tryPuzzle(currentLevel, puzzleIndex);
        hintLabel.setText(result.getMessage());
        updateHUD();
        if (currentLevel.areAllPuzzlesSolved()) {
            timer.stop();
            disableInteractions();
            hintLabel.setText("Level Completed!");
        }
    }

    /** Show hint from GameState */
    private void showHint() {
        ActionResult result = gameState.buyHint(currentLevel);
        hintLabel.setText(result.getMessage());
        updateHUD();
    }

    /** Timer setup */
    private void startTimer(int seconds) {
        remainingSeconds = seconds;
        timerLabel.setText(formatTime(remainingSeconds));

        if (timer != null) timer.stop();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingSeconds--;
            timerLabel.setText(formatTime(remainingSeconds));
            if (remainingSeconds <= 0) {
                timer.stop();
                hintLabel.setText("Time's up! Level failed.");
                disableInteractions();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /** Disable all interactive elements */
    private void disableInteractions() {
        itemsBox.getChildren().forEach(node -> node.setDisable(true));
        puzzlesBox.getChildren().forEach(node -> node.setDisable(true));
        hintButton.setDisable(true);
        inventoryList.setDisable(true);
    }

    /** Update HUD elements */
    private void updateHUD() {
        teusLabel.setText("TEUs: " + gameState.getTeus());
        inventoryList.getItems().setAll(gameState.getInventory().getItemNames());
    }
}
