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
    static Stage stage;


    @Override
    public void start(Stage stage1) throws IOException {
        stage = stage1;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                //we.consume();
                System.out.println("Stage is closing");
            }
        });

        Image icon = new Image("bomb.png");
        stage.getIcons().add(icon);

        stage.setResizable(false);
        stage.setTitle("Bomberman");
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        new GameController(stage);


    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}