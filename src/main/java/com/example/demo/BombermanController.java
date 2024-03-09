package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class BombermanController {

    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("Katt");
    }

    @FXML
    protected void goToGameConfiguration() throws IOException {
        System.out.println("Játék konfigurálása");
        FXMLLoader fxmlGameConf = new FXMLLoader(getClass().getResource("gameConfiguration-view.fxml"));
        Scene scene = new Scene(fxmlGameConf.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene);
    }

    @FXML
    protected void goToDescription() throws IOException {
        System.out.println("Leírás");
        FXMLLoader fxmlDesc = new FXMLLoader(getClass().getResource("description-view.fxml"));
        Scene scene = new Scene(fxmlDesc.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene);
    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene);
    }

    @FXML
    protected void goToInGame() throws IOException {
        System.out.println("Játszma");
        FXMLLoader fxmlInGame = new FXMLLoader(getClass().getResource("inGame-view.fxml"));
        Scene scene = new Scene(fxmlInGame.load(), WIDTH, HEIGHT);
        BombermanApplication.changeScene(scene);
    }



    @FXML
    protected void exitGame() throws IOException {
        exit();
    }
}