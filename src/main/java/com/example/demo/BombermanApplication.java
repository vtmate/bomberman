package com.example.demo;

import com.example.demo.Controller.GameController;
import com.example.demo.Model.GameModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class BombermanApplication extends Application {
    public Stage stage;

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