package com.example.demo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class BombermanApplication extends Application {
    static Stage stage;

    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BombermanApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        //scene.getStylesheets().add(getClass().getResource("mainpage.css").toExternalForm());

        stage = new Stage();
        //stage.getIcons().add(new Image("/resources/icon.png"));
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
        changeScene(scene);

    }

    public static void changeScene(Scene scene) {

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}