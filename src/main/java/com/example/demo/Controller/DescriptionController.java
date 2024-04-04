package com.example.demo.Controller;

import com.example.demo.Model.Player;
import com.example.demo.Model.PowerUp;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescriptionController {
    GameController gc;
    private GridPane powerUpGrid;
    private GridPane badPowerUpGrid;
    private final int WIDTH = 700;
    private int powerUpGridRows = 0;
    private int badPowerUpGridRows = 0;
    private GridPane controlGrid;
    private int controlGridRows = 0;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label title;
    public DescriptionController(GameController gc) {
        this.gc = gc;
    }

    public void initialize() {
        scrollPane.setContent(new ImageView(new Image("bg.jpg")));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(WIDTH);

        Font adumuFont = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 30);
        title.setFont(adumuFont);

        VBox vbox = new VBox();
        vbox.setMaxWidth(WIDTH-15);
        vbox.setStyle("-fx-padding: 10 20 10 10; -fx-background-color: #161C1C;");

        Label desc1 = new Label();
        desc1.setText("A játék egy 2 dimenziós pályán játszódik, amely négyzet alakú mezőkből áll. A játékot 2 játékos játsza, " +
                "akiknek 1-1 bomberman figurát irányítva céljuk, hogy egyedüliként maradjanak életben. A játékpálya " +
                "mezőin fal elemek, dobozok, szörnyek és maguk a játékosok figurái helyezkednek el. A játékosok " +
                "bombákat lehelyezve felrobbanthatják a dobozokat, szörnyeket és a játékosokat (akár saját magukat " +
                "is). Egy játékos veszt (és ezáltal ellenfele győz), ha felrobban, vagy ha egy szörny elkapja.");
        desc1.setStyle("-fx-font-size: 20px;");
        desc1.setWrapText(true);
        desc1.setMaxWidth(WIDTH);
        desc1.setTextFill(Color.WHITE);
        desc1.setTextAlignment(TextAlignment.JUSTIFY);

        Label controlTitle = new Label("Irányítás");
        controlTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: 900; -fx-padding: 25 0 0 0");
        controlTitle.setTextFill(Color.WHITE);

        controlGrid = new GridPane(0,0);
        controlGrid.setPrefWidth(WIDTH);

        newControlDesc("Esemény", "1. játékos", "2. játékos");
        newControlDesc("Mozgás fel", "W", "UP");
        newControlDesc("Mozgás le", "S", "DOWN");
        newControlDesc("Mozgás balra", "A", "LEFT");
        newControlDesc("Mozgás jobbra", "D", "RIGHT");
        newControlDesc("Bomba lehelyezése", "Q", "CTRL");
        newControlDesc("Akadály lehelyezése", "E", "SPACE");

        Label powerUpTitle = new Label("Bónuszok");
        powerUpTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: 900; -fx-padding: 25 0 0 0");
        powerUpTitle.setTextFill(Color.WHITE);

        powerUpGrid = new GridPane(10,10);
        powerUpGrid.setPrefWidth(WIDTH);


        newPowerUpDesc("Bombák száma", "A játékos által lehelyezhető bombák száma 1-gyel növekszik.", "morebombsPowerUp.png");
        newPowerUpDesc("Robbanás hatótáva", "A játékos bombáinak hatótávja mind a 4 irányba 1-1 mezővel növekszik.", "biggerRadiusP.png");
        newPowerUpDesc("Detonátor",  " A játékos által lehelyezett bomba / bombák ne időzítő hatására robbanjanak fel, hanem az utolsó bomba lehelyezése után, a bomba lehelyezés funkciót még egyszer használva, az összes bombája robbanjon fel a játékosnak.", "detonator.png");
        newPowerUpDesc("Görkorcsolya", "A játékos karakterének sebessége növekedjen meg. Nem halmozható, az esetleges második és további görkorcsolyák felvételének nincs hatása.", "rollerskateskaterPowerUp.png");
        newPowerUpDesc("Sérthetetlenség ", "Egy rövid ideig a játékos karaktere legyen sérthetetlen. Vizuálisan is jelöljük, hogy a játékos jelenleg sérthetetlen, és azt is, ha hamarosan véget ér a hatása", "shieldPowerUp.png");
        newPowerUpDesc("Szellem", "Egy rövid ideig a játékos karaktere legyen képes a falakon, a dobozokon és a bombákon is áthaladni. Vizuálisan is jelöljük, hogy a játékos rendelkezik a képességgel és azt is, ha hamarosan véget ér a hatása. Ha a játékos a képesség végekor egy doboz vagy egy fal mezőn tartózkodik, akkor meghal. (Bombáról leléphet.)", "ghostPowerUp.png");
        newPowerUpDesc("Akadály", "A játékos helyezhessen le akadályokat, amelyek játéklogikailag úgy viselkednek, mint a dobozok, azonban ezekből bónusz (power-up) sosem kerül elő. Maximálisan 3 akadály legyen egy játékos által lehelyezhető. (Ha felrobbantásra kerülnek, lehelyezhető új.) Ez a képesség legyen halmozható, azaz a bónusz esetleges második felvételével 6 akadály legyen lehelyezhető, stb.", "gatePowerUp.png");

        Label badPowerUpTitle = new Label("Hátráltató bónuszok");
        badPowerUpTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: 900; -fx-padding: 25 0 0 0");
        badPowerUpTitle.setTextFill(Color.WHITE);

        badPowerUpGrid = new GridPane(10,10);
        badPowerUpGrid.setPrefWidth(WIDTH);

        newBadPowerUpDesc("Lassítás", "A játékos karakterének sebessége egy időre csökkenjen le.", "snailPowerUp.png");
        newBadPowerUpDesc("Hatótávzsugorítás", "A játékos által lehelyezett bombák hatótávja mindössze 1 mező legyen egy adott ideig.", "smallerRadiusP.png");
        newBadPowerUpDesc("Bombamegvonás", "A játékos ne tudjon bombákat lehelyezni egy ideig.", "noBombs.png");
        newBadPowerUpDesc("Azonnali bomba lehelyezés", "A játékos karaktere azonnal helyezze le a bombákat, amint tudja (szabad mezőn áll és tud lehelyezni bombát). Ez a hátráltatás is csak egy adott ideig fejtse ki hatását.", "immadiate.png");

        vbox.getChildren().addAll(desc1, controlTitle, controlGrid, powerUpTitle, powerUpGrid, badPowerUpTitle, badPowerUpGrid);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        scrollPane.setContent(vbox);

    }

    @FXML
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        this.gc.changeScene("mainPage");
    }

    private void newControlDesc(String eventData, String player1Data, String player2Data) {
        controlGridRows++;
        Label event = new Label(eventData);
        event.setTextFill(Color.WHITE);
        event.setStyle("-fx-pref-width: 250; -fx-font-size: 20; -fx-padding: 10 10 10 20");
        controlGrid.add(event, 1, controlGridRows);
        Label player1 = new Label(player1Data);
        player1.setTextFill(Color.WHITE);
        player1.setStyle("-fx-pref-width: 200; -fx-font-size: 20; -fx-padding: 10 10 10 20");
        controlGrid.add(player1, 2, controlGridRows);
        Label player2 = new Label(player2Data);
        player2.setTextFill(Color.WHITE);
        player2.setStyle("-fx-pref-width: 200; -fx-font-size: 20; -fx-padding: 10 10 10 20");
        controlGrid.add(player2, 3, controlGridRows);

        if (controlGridRows == 1) {
            event.setStyle("-fx-pref-width: 250; -fx-font-size: 22; -fx-font-weight: 900; -fx-background-color: #343939; -fx-padding: 10 10 10 20");
            player1.setStyle("-fx-pref-width: 200; -fx-font-size: 22; -fx-font-weight: 900; -fx-background-color: #343939; -fx-padding: 10 10 10 20");
            player2.setStyle("-fx-pref-width: 200; -fx-font-size: 22; -fx-font-weight: 900; -fx-background-color: #343939; -fx-padding: 10 10 10 20");
        }

        if (controlGridRows % 2 == 1 && controlGridRows != 1) {
            event.setStyle("-fx-pref-width: 250; -fx-font-size: 20; -fx-background-color: #252B2B; -fx-padding: 10 10 10 20");
            player1.setStyle("-fx-pref-width: 200; -fx-font-size: 20; -fx-background-color: #252B2B; -fx-padding: 10 10 10 20");
            player2.setStyle("-fx-pref-width: 200; -fx-font-size: 20; -fx-background-color: #252B2B; -fx-padding: 10 10 10 20");
        }

    }
    private void newPowerUpDesc(String nameData, String descData, String image) {
        HBox hbox = new HBox();

        Label title = new Label(nameData);
        title.setStyle("-fx-font-size: 25; -fx-font-weight: 900");
        title.setTextFill(Color.WHITE);
        ImageView iw = new ImageView(new Image(image));
        iw.setFitWidth(30);
        iw.setFitHeight(30);
        hbox.getChildren().addAll(iw, title);
        hbox.setStyle("-fx-padding: 10 0 0 0");

        powerUpGrid.add(hbox, 1, powerUpGridRows++);

        Label desc = new Label(descData);
        desc.setMaxWidth(WIDTH);
        desc.setWrapText(true);
        desc.setTextFill(Color.WHITE);
        desc.setStyle("-fx-font-size: 22");

        powerUpGrid.add(desc, 1, powerUpGridRows++);
    }

    private void newBadPowerUpDesc(String nameData, String descData, String image) {
        HBox hbox = new HBox();

        Label title = new Label(nameData);
        title.setStyle("-fx-font-size: 25; -fx-font-weight: 900");
        title.setTextFill(Color.WHITE);
        ImageView iw = new ImageView(new Image(image));
        iw.setFitWidth(30);
        iw.setFitHeight(30);
        hbox.getChildren().addAll(iw, title);
        hbox.setStyle("-fx-padding: 10 0 0 0");

        badPowerUpGrid.add(hbox, 1, badPowerUpGridRows++);

        Label desc = new Label(descData);
        desc.setMaxWidth(WIDTH);
        desc.setWrapText(true);
        desc.setTextFill(Color.WHITE);
        desc.setStyle("-fx-font-size: 22");

        badPowerUpGrid.add(desc, 1, badPowerUpGridRows++);
    }
}
