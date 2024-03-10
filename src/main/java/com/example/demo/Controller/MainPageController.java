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

    public void initialize() {
        // gamePane inicializálása, ha szükséges
        // Például:
        if (startBTN == null) {
            System.out.println("startBTN is null");
        } else {
            System.out.println("startBTN is not null");
        }
    }

    @FXML
    protected void goToGameConfiguration() throws IOException {
        System.out.println("Játék konfigurálása");
        FXMLLoader fxmlGameConf = new FXMLLoader(BombermanApplication.class.getResource("gameConfiguration-view.fxml"));
        Scene scene = new Scene(fxmlGameConf.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene, false);
    }

    @FXML
    protected void goToDescription() throws IOException {
        System.out.println("Leírás");
        FXMLLoader fxmlDesc = new FXMLLoader(BombermanApplication.class.getResource("description-view.fxml"));
        Scene scene = new Scene(fxmlDesc.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene, false);
    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(BombermanApplication.class.getResource("mainPage-view.fxml"));

        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene, false);
    }

    @FXML
    protected void goToInGame() throws IOException {

        System.out.println("Játszma");
        FXMLLoader fxmlInGame = new FXMLLoader(BombermanApplication.class.getResource("inGame-view.fxml"));
        //System.out.println(this.gamePane);
        //this.gamePane.getChildren().add(r);
        Parent root = fxmlInGame.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        BombermanApplication.changeScene(scene, true);

    }



    @FXML
    protected void exitGame() throws IOException {
        exit();
    }
}