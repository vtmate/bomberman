package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import com.example.demo.Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class InGameController {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    @FXML
    private Pane gamePane;
    private GameModel gm;
    private GameController gc;


    public InGameController(GameController gc) {
        this.gc = gc;
    }

    public void initialize() {

        this.gm = new GameModel();
        System.out.println("gamemodel created");

        Control control = new Control(this.gm);

        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case W -> control.moveUp0();
                            case S -> control.moveDown0();
                            case A -> control.moveLeft0();
                            case D -> control.moveRight0();
                            case UP -> control.moveUp1();
                            case DOWN -> control.moveDown1();
                            case LEFT -> control.moveLeft1();
                            case RIGHT -> control.moveRight1();
                            case Q -> control.placeBomb(0);
                            case CONTROL -> control.placeBomb(1);
                        }
                        gamePane.getChildren().removeIf(node -> node instanceof Rectangle);
                        createWalls(gm.walls);
                        createBombs(gm.bombs);
                        createPlayers(gm.players);
                    }
                });
            }
        });

        createWalls(gm.walls);
        createPlayers(gm.players);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.033), e -> {
                    refresh();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML //ezt egyenlőre nem használjuk, majd arra kell, hogy a játékból vissza tudjunk menni a főoldalra (esc)
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(BombermanApplication.class.getResource("mainPage-view.fxml"));
        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
    }

    public void refresh() {
        gamePane.getChildren().removeIf(node -> node instanceof Rectangle);
        createWalls(gm.walls);
        createBombs(gm.bombs);
        createPlayers(gm.players);
    }

    public void createWalls(ArrayList<Wall> walls) {
        int size = 40;
        for (Wall wall : walls) {
            Rectangle r = new Rectangle();
            r.setX(wall.x * size);
            r.setY(wall.y * size);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
    public void createPlayers(ArrayList<Player> players) {
        int size = 40;

        for (Player player : players) {
            Rectangle r = new Rectangle();
            r.setX(player.x);
            r.setY(player.y);
            r.setFill(Color.BLUEVIOLET);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    public void createBombs(ArrayList<Bomb> bombs) {
        int size = 40;
        for (Bomb bomb : bombs) {
            Rectangle r = new Rectangle();
            r.setFill(Color.ORANGE);
            r.setX(bomb.x);
            r.setY(bomb.y);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
}
