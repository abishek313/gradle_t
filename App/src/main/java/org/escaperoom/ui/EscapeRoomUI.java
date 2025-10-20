package org.escaperoom.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EscapeRoomUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Set initial medium size
        primaryStage.setTitle("Escape Room");
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

        // Optional: Toggle fullscreen when double-clicking background
        root.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

