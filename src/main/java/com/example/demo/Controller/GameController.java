package com.example.demo.Controller;

import com.example.demo.BombermanApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GameController {
    public Stage stage;
    FXMLLoader fxmlLoader;
    String map;
    protected String playerName1;
    protected String playerName2;

    /**
     * A program elindulásakor megjeleníti a főoldal kinézetét.
     *
     * @param stage         a program ablaka
     * @throws IOException  ha a kinézet nem létezik
     */
    public GameController(Stage stage) throws IOException {
        this.stage = stage;
        changeScene("mainPage");
    }

    /**
     * Megjeleníti a megfelelő kinézetet a paraméterből az ablakban.
     *
     * @param name          a kinézet neve
     * @throws IOException  ha a kinézet nem létezik
     */
    public void changeScene(String name) throws IOException {
        this.fxmlLoader = new FXMLLoader(BombermanApplication.class.getResource(name + "-view.fxml"));

        switch (name) {
            case "mainPage" -> this.fxmlLoader.setController(new MainPageController(this));
            case "gameConfiguration" -> this.fxmlLoader.setController(new GameConfigurationController(this));
            case "description" -> this.fxmlLoader.setController(new DescriptionController(this));
            case "inGame" -> this.fxmlLoader.setController(new InGameController(this, playerName1, playerName2, map));
        }

        Parent root = this.fxmlLoader.load();
        Scene scene = new Scene(root, 900, 600);

        this.stage.setScene(scene);
        this.stage.show();
    }
}
