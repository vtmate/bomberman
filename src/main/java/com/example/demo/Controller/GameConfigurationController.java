package com.example.demo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GameConfigurationController {
    private GameController gc;

    @FXML
    TextField playerNameInput1;

    @FXML
    TextField playerNameInput2;

    @FXML
    ImageView mapImage1, mapImage2, mapImage3;

    private String playerName1;
    private String playerName2;

    private String map;

    public GameConfigurationController(GameController gc) {
        this.gc = gc;


    }
    public void initialize() {
        playerNameInput1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println(playerNameInput1.getText());
            }
        });

        playerNameInput2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println(playerNameInput2.getText());
            }
        });
        mapImage1.setOnMouseClicked(event ->  {
            map = "Dzsungel";
            ((DropShadow) mapImage1.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage2.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage3.getEffect()).setColor(Color.WHITE);

        });
        mapImage2.setOnMouseClicked(event ->  {
            map = "Pokol";
            ((DropShadow) mapImage2.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage1.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage3.getEffect()).setColor(Color.WHITE);
        });
        mapImage3.setOnMouseClicked(event ->  {
            map = "Vadnyugat";
            ((DropShadow) mapImage3.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage2.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage1.getEffect()).setColor(Color.WHITE);
        });
    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        this.gc.changeScene("mainPage");
    }

    @FXML
    protected void goToInGame() throws IOException {
        System.out.println("Játszma");
        if (checkInputs()) this.gc.changeScene("inGame");
    }

    private boolean checkInputs() {
        if (map == null) {
            System.out.println("\u001B[31mNincs kiválasztott pálya!\u001B[37m");
            return false;
        }

        System.out.println("\u001B[33mA kiválasztott pálya:" + map + "\u001B[37m");

        if (!Objects.equals(playerNameInput1.getText(), "")) {
            playerName1 = playerNameInput1.getText();
        }
        else {
            playerName1 = "1. játékos";
        }
        System.out.println("\u001B[33m1. játéko neve: " + playerName1 + "\u001B[37m");

        if (!Objects.equals(playerNameInput2.getText(), "")) {
            playerName2 = playerNameInput2.getText();
        }
        else {
            playerName2 = "2. játékos";
        }
        System.out.println("\u001B[33m2. játéko neve: " + playerName2 + "\u001B[37m");

        return true;

    }
}
