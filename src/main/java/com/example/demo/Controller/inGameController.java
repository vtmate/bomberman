package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import com.example.demo.Model.GameModel;
import com.example.demo.Model.Wall;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
        // gamePane inicializálása, ha szükséges
        // Például:
        if (gamePane == null) {
            System.out.println(" is null");
        } else {
            System.out.println(" is not null");
        }
        this.gm = new GameModel();
        createWalls(gm.walls);
    }
    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(BombermanApplication.class.getResource("mainPage-view.fxml"));

        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene);
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
}
