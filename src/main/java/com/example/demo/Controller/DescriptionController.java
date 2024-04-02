package com.example.demo.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescriptionController {
    GameController gc;
    @FXML
    ScrollPane scrollPane;
    public DescriptionController(GameController gc) {
        this.gc = gc;
    }

    public void initialize() {
        scrollPane.setContent(new ImageView(new Image("bg.jpg")));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.setPrefWidth(600);

        Label desc1 = new Label();
        desc1.setText("A játék egy 2 dimenziós pályán játszódik, amely négyzet alakú mezőkből áll. A játékot 2 játékos játsza, " +
                "akiknek 1-1 bomberman figurát irányítva céljuk, hogy egyedüliként maradjanak életben. A játékpálya " +
                "mezőin fal elemek, dobozok, szörnyek és maguk a játékosok figurái helyezkednek el. A játékosok " +
                "bombákat lehelyezve felrobbanthatják a dobozokat, szörnyeket és a játékosokat (akár saját magukat " +
                "is). Egy játékos veszt (és ezáltal ellenfele győz), ha felrobban, vagy ha egy szörny elkapja.");
        desc1.setStyle("-fx-font-size: 20px;");
        desc1.setWrapText(true);
        desc1.setMaxWidth(600);
        desc1.setTextAlignment(TextAlignment.JUSTIFY);

        Label controlTitle = new Label("Irányítás");
        controlTitle.setStyle("-fx-font-size: 25px;");

        GridPane controlGrid = new GridPane(10,10);
        controlGrid.setPrefWidth(600);
        controlGrid.add(new Label("Esemény"), 1, 1);
        controlGrid.add(new Label("1. játékos"), 2, 1);
        controlGrid.add(new Label("2. játékos"), 3, 1);

        controlGrid.add(new Label("Mozgás fel"), 1, 2);
        controlGrid.add(new Label("W"), 2, 2);
        controlGrid.add(new Label("UP"), 3, 2);

        controlGrid.add(new Label("Mozgás le"), 1, 3);
        controlGrid.add(new Label("S"), 2, 3);
        controlGrid.add(new Label("DOWN"), 3, 3);

        controlGrid.add(new Label("Mozgás balra"), 1, 4);
        controlGrid.add(new Label("A"), 2, 4);
        controlGrid.add(new Label("LEFT"), 3, 4);

        controlGrid.add(new Label("Mozgás jobbra"), 1, 5);
        controlGrid.add(new Label("D"), 2, 5);
        controlGrid.add(new Label("RIGHT"), 3, 5);

        controlGrid.add(new Label("Bomba lehelyezése"), 1, 6);
        controlGrid.add(new Label("Q"), 2, 6);
        controlGrid.add(new Label("CTRL"), 3, 6);

        controlGrid.add(new Label("Akadály lehelyezése"), 1, 7);
        controlGrid.add(new Label("E"), 2, 7);
        controlGrid.add(new Label("SPACE"), 3, 7);

        Label powerUpTitle = new Label("Búnuszok");
        powerUpTitle.setStyle("-fx-font-size: 25px;");

        GridPane powerUpGrid = new GridPane(10,10);

        powerUpGrid.add(new Label("Detonátor"), 1, 1);
        powerUpGrid.add(new Label("a játékos által lehelyezett bomba / bombák ne időzítő hatására robbanjanak fel, hanem az utolsó bomba lehelyezése után,\n a bomba lehelyezés funkciót még egyszer használva, az összes bombája robbanjon fel a játékosnak."), 1, 2);



        vbox.getChildren().addAll(desc1, controlTitle, controlGrid, powerUpTitle, powerUpGrid);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        scrollPane.setContent(vbox);

    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        this.gc.changeScene("mainPage");
    }
}
