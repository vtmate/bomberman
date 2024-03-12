package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import com.example.demo.Model.Control;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class MainPageController {

    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    @FXML
    private Pane gamePane;

    @FXML
    private Button startBTN;

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("Katt");
    }

    private GameController gc;

    public MainPageController(GameController gc) {
        this.gc = gc;
    }

    /*public void initialize() {
        // gamePane inicializálása, ha szükséges
        // Például:
        if (startBTN == null) {
            System.out.println("startBTN is null");
        } else {
            System.out.println("startBTN is not null");
        }
    }*/

    @FXML
    protected void goToGameConfiguration() throws IOException {
        System.out.println("Játék konfigurálása");
        this.gc.changeScene("gameConfiguration");
    }

    @FXML
    protected void goToDescription() throws IOException {
        System.out.println("Leírás");
        this.gc.changeScene("description");
    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        this.gc.changeScene("mainPage");
    }

    @FXML
    protected void goToInGame() throws IOException {

        System.out.println("Játszma");


        this.gc.changeScene("inGame");

    }



    @FXML
    protected void exitGame() throws IOException {
        exit();
    }
}