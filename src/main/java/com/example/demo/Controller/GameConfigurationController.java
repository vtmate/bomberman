package com.example.demo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.IOException;
import java.util.Objects;

public class GameConfigurationController {
    private final GameController gc;
    @FXML
    TextField playerNameInput1;
    @FXML
    TextField playerNameInput2;
    @FXML
    Label playerNameLabel1;
    @FXML
    Label playerNameLabel2;
    @FXML
    ImageView mapImage1, mapImage2, mapImage3;
    @FXML
    Label mapLabelJungle, mapLabelHell, mapLabelWestend;
    @FXML
    BorderPane borderPane;
    @FXML
    Button startButton;
    @FXML
    Label title;
    private String playerName1;
    private String playerName2;
    private String map;

    /**
     *
     * @param gc    átveszi a GameController-t
     */
    public GameConfigurationController(GameController gc) {
        this.gc = gc;
    }

    /**
     * A gameConfiguration-view.fxml betöltésekor lefut egyszer.
     * Stílus beállítása.
     */
    public void initialize() {
        startButton.setDisable(true);
        Font adumuFont = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 30);
        System.out.println("Font: " + adumuFont);
        mapLabelJungle.setTextFill(Color.WHITE);
        mapLabelJungle.setFont(adumuFont);
        mapLabelHell.setFont(adumuFont);
        mapLabelWestend.setFont(adumuFont);
        title.setFont(adumuFont);
        Font adumuFontForPlayerNameLabel = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 20);
        playerNameLabel1.setFont(adumuFontForPlayerNameLabel);
        playerNameLabel2.setFont(adumuFontForPlayerNameLabel);

        playerNameInput1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            }
        });

        playerNameInput2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            }
        });

        mapImage1.setOnMouseClicked(event ->  {
            map = "Dzsungel";
            startButton.setDisable(false);
            ((DropShadow) mapImage1.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage2.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage3.getEffect()).setColor(Color.WHITE);
            gc.map = map;
            borderPane.setBackground(new Background(new BackgroundImage(new Image("jungle.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));
        });
        mapImage2.setOnMouseClicked(event ->  {
            map = "Pokol";
            startButton.setDisable(false);
            ((DropShadow) mapImage2.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage1.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage3.getEffect()).setColor(Color.WHITE);
            gc.map = map;
            borderPane.setBackground(new Background(new BackgroundImage(new Image("lava-cave.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));
        });
        mapImage3.setOnMouseClicked(event ->  {
            map = "Vadnyugat";
            startButton.setDisable(false);
            ((DropShadow) mapImage3.getEffect()).setColor(Color.YELLOW);
            ((DropShadow) mapImage2.getEffect()).setColor(Color.WHITE);
            ((DropShadow) mapImage1.getEffect()).setColor(Color.WHITE);
            gc.map = map;
            borderPane.setBackground(new Background(new BackgroundImage(new Image("bg.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));
        });
    }

    /**
     * Meghívja a GameController changeScene függvényét, a kívánt megjelenítés paraméterével.
     * Ez a metódus egy FXML fájlban kerül meghívásra.
     *
     * @throws IOException  ha a kinézet nem létezik
     */
    @FXML
    protected void goToMainPage() throws IOException {
        this.gc.changeScene("mainPage");
    }

    /**
     * Meghívja a GameController changeScene függvényét, a kívánt megjelenítés paraméterével.
     * Ez a metódus egy FXML fájlban kerül meghívásra.
     *
     * @throws IOException  ha a kinézet nem létezik
     */
    @FXML
    protected void goToInGame() throws IOException {
        if (checkInputs()) this.gc.changeScene("inGame");
    }

    /**
     * A játék elindításához szükséges paraméterek ellenőrzése.
     * Játékosnevek és a pálya nevének beállítása.
     *
     * @return  hogy minden paraméter megfelelő-e
     */
    private boolean checkInputs() {
        if (map == null) {
            return false;
        }

        if (!Objects.equals(playerNameInput1.getText(), "")) {
            playerName1 = playerNameInput1.getText();
        }
        else {
            playerName1 = "1. játékos";
        }
        gc.playerName1 = playerName1;

        if (!Objects.equals(playerNameInput2.getText(), "")) {
            playerName2 = playerNameInput2.getText();
        }
        else {
            playerName2 = "2. játékos";
        }
        gc.playerName2 = playerName2;

        return true;
    }
}
