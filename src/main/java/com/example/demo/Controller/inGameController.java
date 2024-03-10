package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import com.example.demo.Model.Control;
import com.example.demo.Model.GameModel;
import com.example.demo.Model.Wall;
import com.example.demo.Model.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class inGameController {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    @FXML
    private Pane gamePane;

    private GameModel gm;

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
                        }
                        BombermanApplication.changeScene(newScene, false);
                        gamePane.getChildren().removeIf(node -> node instanceof Rectangle);
                        createWalls(gm.walls);
                        createPlayers(gm.players);
                    }
                });
            }
        });
        createWalls(gm.walls);
        createPlayers(gm.players);

    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(BombermanApplication.class.getResource("mainPage-view.fxml"));


        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene, false);
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
            System.out.println(player.x + " " + player.y);
            Rectangle r = new Rectangle();
            r.setX(player.x * size);
            r.setY(player.y * size);
            r.setFill(Color.BLUEVIOLET);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
}
