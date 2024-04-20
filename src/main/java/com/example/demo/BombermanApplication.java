package com.example.demo;

import com.example.demo.Controller.GameController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * A játék ablakának létrehozása és beállítása.
 */
public class BombermanApplication extends Application {
    public Stage stage;

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * Stílus beállítása.
     *
     * @throws IOException  ha a GameController hibát dob, kezeljük
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                System.exit(0);
            }
        });

        Image icon = new Image("bomb.png");
        this.stage.getIcons().add(icon);

        this.stage.setResizable(false);
        this.stage.setTitle("Bomberman");
        this.stage.setMinHeight(600);
        this.stage.setMinWidth(900);

        new GameController(this.stage);
    }

    public static void main(String[] args) {
        launch();
    }
}