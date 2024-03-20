package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import com.example.demo.Model.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    @FXML
    private Label playerNameLabel1;
    @FXML
    private Label playerNameLabel2;
    @FXML
    private Label timerLabel;
    private GameModel gm;
    private GameController gc;
    private String map;
    private String playerName1;
    private String playerName2;
    private long startTime;

    public InGameController(GameController gc, String playerName1, String playerName2, String map) {
        this.gc = gc;
        this.playerName1 = playerName1;
        System.out.println("Játékosnév: " + playerName1);
        this.playerName2 = playerName2;
        this.map = map;
    }

    public void initialize() {
        this.playerNameLabel1.setText(playerName1);
        this.playerNameLabel2.setText(playerName2);
        this.gm = new GameModel(map);
        System.out.println("gamemodel created");

        Control control = new Control(this.gm);

        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case W -> control.moveCharacter("UP", 0);
                            case S -> control.moveCharacter("DOWN", 0);
                            case A -> control.moveCharacter("LEFT", 0);
                            case D -> control.moveCharacter("RIGHT", 0);
                            case UP -> control.moveCharacter("UP", 1);
                            case DOWN -> control.moveCharacter("DOWN", 1);
                            case LEFT -> control.moveCharacter("LEFT", 1);
                            case RIGHT -> control.moveCharacter("RIGHT", 1);
                            case Q -> control.placeBomb(0);
                            case CONTROL -> control.placeBomb(1);
                        }
                        refresh();
                    }
                });
            }
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.033), e -> {
                    refresh();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startTime = System.currentTimeMillis();

        // Időzítő létrehozása és indítása
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = elapsedTime / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                timerLabel.setText("Idő: " + String.format("%d:%02d", minutes, seconds));
            }
        };
        timer.start();

        control.moveMonster(gm.monsters.getFirst());
        control.moveMonster(gm.monsters.get(1));
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
        createMonsters(gm.monsters);
        createExplosion(gm.explosions);
        createPowerUps(gm.powerUps);
        createBoxes(gm.boxes);
        gm.checkImmadiateBombs();
    }

    private void createPowerUps(ArrayList<PowerUp> powerUps){
        int size = 40;
        for (PowerUp powerUp : powerUps) {
            Rectangle r = new Rectangle();
            r.setX(powerUp.x);
            r.setY(powerUp.y);
            r.setFill(Color.AQUA);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    private void createBoxes(ArrayList<Box> boxes){
        int size = 40;
        for (Box box : boxes) {
            Rectangle r = new Rectangle();
            r.setX(box.x);
            r.setY(box.y);
            r.setFill(Color.SADDLEBROWN);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
    public void createWalls(ArrayList<Wall> walls) {
        int size = 40;
        for (Wall wall : walls) {
            Rectangle r = new Rectangle();
            r.setX(wall.x);
            r.setY(wall.y);
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

    public void createMonsters(ArrayList<Monster> monsters) {
        int size = 40;
        for (Monster monster : monsters) {
            Rectangle r = new Rectangle();
            r.setX(monster.x);
            r.setY(monster.y);
            r.setFill(Color.FORESTGREEN);
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

    public void createExplosion(ArrayList<Explosion> explosions){
        int size = 40;
        for (Explosion explosion : explosions) {
            Rectangle r = new Rectangle();
            r.setFill(Color.YELLOW);
            r.setX(explosion.x);
            r.setY(explosion.y);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
}
