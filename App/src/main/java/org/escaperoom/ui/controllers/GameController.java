package org.escaperoom.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import org.escaperoom.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameController {

    private GameState gameState;
    private Level currentLevel;
    private Timeline timer;
    private int remainingSeconds;
    @FXML private Label hintLabel;
    @FXML private ImageView backgroundImageView;
    @FXML private StackPane rootStack; 
    @FXML private StackPane puzzleArea;
    @FXML private StackPane inventoryPanel;
    @FXML private ListView<String> inventoryList;
    @FXML private Button inventoryBtn;
    @FXML private Button closeInventoryBtn;
    @FXML private Button menuBtn;
    @FXML private Button hintButton;
    @FXML private Label timerLabel;
    @FXML private ImageView coinIcon;
    @FXML private Label coinsLabel;
    @FXML private VBox itemsBox;
    @FXML private VBox puzzlesBox;
    @FXML private javafx.scene.shape.Rectangle accessCardArea;
    @FXML private javafx.scene.shape.Rectangle doorArea;
    @FXML private Pane clickAreaPane;

    @FXML
    public void initialize() {
        try {
        Image coinImage = new Image(getClass().getResourceAsStream("/images/coin.png"));
        coinIcon.setImage(coinImage);
        coinIcon.setPreserveRatio(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
        // Menu
        if (menuBtn != null) menuBtn.setOnAction(e -> openMenu());
        // Hint
        if (hintButton != null) hintButton.setOnAction(e -> handleHint());
        // Inventory panel
        if (inventoryBtn != null) inventoryBtn.setOnAction(e -> inventoryPanel.setVisible(true));
        if (closeInventoryBtn != null) closeInventoryBtn.setOnAction(e -> inventoryPanel.setVisible(false));
        // Inventory item click
        inventoryList.setOnMouseClicked(event -> {
            String selected = inventoryList.getSelectionModel().getSelectedItem();
            if (selected != null) useInventoryItem(selected);
        });
        // **Bind clickAreaPane size to rootStack so layoutX/Y works**
        clickAreaPane.prefWidthProperty().bind(rootStack.widthProperty());
        clickAreaPane.prefHeightProperty().bind(rootStack.heightProperty());
    }

    public void initLevel(Level level, GameState existingState) {
        this.currentLevel = level;
        this.gameState = existingState != null ? existingState : new GameState();
        hintLabel.setText("");
        itemsBox.getChildren().clear();
        puzzlesBox.getChildren().clear();

        setupBackground();
        setupItemButtons();
        setupPuzzleButtons();
        setupInventory();
        startTimer(level.getTimeLimitSec());
        updateHUD();
        positionClickAreas();
    }
    private void positionClickAreas() {
    accessCardArea.setLayoutX(currentLevel.getAccessCardX());
    accessCardArea.setLayoutY(currentLevel.getAccessCardY());

    doorArea.setLayoutX(currentLevel.getDoorX());
    doorArea.setLayoutY(currentLevel.getDoorY());
    doorArea.setWidth(currentLevel.getDoorWidth());
    doorArea.setHeight(currentLevel.getDoorHeight());
}

    private void setupBackground() {
        try {
            String path = "/images/level" + currentLevel.getLevelId() + ".png";
            Image bgImage = new Image(getClass().getResourceAsStream(path));
            backgroundImageView.setImage(bgImage);
            backgroundImageView.setPreserveRatio(true);
            backgroundImageView.setSmooth(true);
            backgroundImageView.setMouseTransparent(true);
            backgroundImageView.fitWidthProperty().bind(rootStack.widthProperty());
            backgroundImageView.fitHeightProperty().bind(rootStack.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupInventory() {
    inventoryList.getItems().setAll(
        Arrays.stream(currentLevel.getItems())
              .filter(item -> !"Access Card".equals(item.getName())) // skip Access Card
              .map(Item::getName)
              .toList()
    );
}


    private void setupItemButtons() {
        itemsBox.getChildren().clear();
        for (Item item : currentLevel.getItems()) {
             if ("Access Card".equals(item.getName())) continue; 
            Button btn = new Button(item.getName());
            btn.setOnAction(e -> handleInventoryClick(item.getName()));
            itemsBox.getChildren().add(btn);
        }
    }

    private void setupPuzzleButtons() {
        puzzlesBox.getChildren().clear();
        Puzzle[] puzzles = currentLevel.getPuzzles();
        for (int i = 0; i < puzzles.length; i++) {
            Puzzle puzzle = puzzles[i];
            if ("Door Unlock".equals(puzzle.getName()) || i == 0) continue;
            Button btn = new Button(puzzle.getName());
            int index = i;
            btn.setOnAction(e -> handlePuzzleClick(index));
            puzzlesBox.getChildren().add(btn);
        }
    }

    private void handlePuzzleClick(int puzzleIndex) {
        ActionResult result = gameState.tryPuzzle(currentLevel, puzzleIndex);
        hintLabel.setText(result.getMessage());
        if (currentLevel.areAllPuzzlesSolved()) {
            timer.stop();
            hintLabel.setText("Level Completed!");
            disableInteractions();
        }
        updateHUD();
    }

    private void handleInventoryClick(String itemName) {
        // Lookup Item object
        Item item = Arrays.stream(currentLevel.getItems())
                          .filter(i -> i.getName().equals(itemName))
                          .findFirst()
                          .orElse(null);

        if (item != null) {
            if (!gameState.hasItem(item)) {
                gameState.pickUpItem(item);
                inventoryList.getItems().setAll(gameState.getInventory().getItemNames());
                hintLabel.setText("Picked up: " + item.getName());
            } else {
                hintLabel.setText(item.getName() + " already in inventory.");
            }
        }
    }

    private void useInventoryItem(String itemName) {
        if ("Access Card".equals(itemName)) {
            hintLabel.setText("You hold the Access Card. Perhaps the door nearby will respond to it.");

        } else {
            hintLabel.setText("Cannot use " + itemName + " here.");
        }
    }

   @FXML
private void handleAccessCardClick(javafx.scene.input.MouseEvent event) {
    // Find the Access Card in the current level
    Item accessCard = Arrays.stream(currentLevel.getItems())
                            .filter(i -> "Access Card".equals(i.getName()))
                            .findFirst()
                            .orElse(null);

    if (accessCard == null) return; // sanity check

    if (!gameState.hasItem(accessCard)) {
        gameState.pickUpItem(accessCard); // add to inventory
        inventoryList.getItems().setAll(gameState.getInventory().getItemNames());
        hintLabel.setText("Picked up Access Card!");
    } else {
        hintLabel.setText("You already have the Access Card.");
    }
}

@FXML
private void handleDoorClick(javafx.scene.input.MouseEvent event) {
    // Check if the player has the Access Card
    boolean hasCard = gameState.getInventory().getItems()
                               .stream()
                               .anyMatch(i -> "Access Card".equals(i.getName()));

    if (hasCard) {
        hintLabel.setText("Door unlocked! Level complete!");
        if (timer != null) timer.stop();
        disableInteractions(); // prevent further clicks

        // Trigger next level after short delay
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> goToNextLevel());
        pause.play();
    } else {
        hintLabel.setText("The door is locked.");
    }
}


    private void enableInteractions() {
        itemsBox.getChildren().forEach(node -> node.setDisable(false));
        puzzlesBox.getChildren().forEach(node -> node.setDisable(false));
        hintButton.setDisable(false);
        inventoryBtn.setDisable(false);
        inventoryList.setDisable(false);
        accessCardArea.setDisable(false);
        doorArea.setDisable(false);
}

    private void handleHint() {
        ActionResult result = gameState.buyHint(currentLevel);
        hintLabel.setText(result.getMessage());
        updateHUD();
    }

    private void goToNextLevel() {
    int nextLevelId = currentLevel.getLevelId() + 1;
    Level nextLevel = LevelLoader.loadLevel(nextLevelId); // assumes LevelLoader exists

    if (nextLevel != null) {
        initLevel(nextLevel, gameState);  // load new level
        enableInteractions();             // re-enable buttons/rectangles
        hintLabel.setText("");            // clear previous hints
    } else {
        hintLabel.setText("Congratulations! You finished all levels!");
    }
}

    private void startTimer(int seconds) {
        remainingSeconds = seconds;
        timerLabel.setText(formatTime(remainingSeconds));
        if (timer != null) timer.stop();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
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

    private void updateHUD() {
        coinsLabel.setText("Coins: " + gameState.getTeus());
        inventoryList.getItems().setAll(gameState.getInventory().getItemNames());
    }

    private void disableInteractions() {
        itemsBox.getChildren().forEach(node -> node.setDisable(true));
        puzzlesBox.getChildren().forEach(node -> node.setDisable(true));
        hintButton.setDisable(true);
        inventoryBtn.setDisable(true);
        inventoryList.setDisable(true);
        accessCardArea.setDisable(true);
        doorArea.setDisable(true);
}

    private void openMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
