package com.example.demo.Controller;

import javafx.fxml.FXML;
import java.io.IOException;
import static javafx.application.Platform.exit;

public class MainPageController {
    private final GameController gc;

    /**
     *
     * @param gc    átveszi a GameController-t
     */
    public MainPageController(GameController gc) {
        this.gc = gc;
    }

    /**
     * Meghívja a GameController changeScene függvényét, a kívánt megjelenítés paraméterével.
     * Ez a metódus egy FXML fájlban kerül meghívásra.
     *
     * @throws IOException  ha a kinézet nem létezik
     */
    @FXML
    protected void goToGameConfiguration() throws IOException {
        this.gc.changeScene("gameConfiguration");
    }

    /**
     * Meghívja a GameController changeScene függvényét, a kívánt megjelenítés paraméterével.
     * Ez a metódus egy FXML fájlban kerül meghívásra.
     *
     * @throws IOException  ha a kinézet nem létezik
     */
    @FXML
    protected void goToDescription() throws IOException {
        this.gc.changeScene("description");
    }

    /**
     * Kilépés a játékból.
     * Ez a metódus egy FXML fájlban kerül meghívásra.
     */
    @FXML
    protected void exitGame() {
        exit();
    }
}