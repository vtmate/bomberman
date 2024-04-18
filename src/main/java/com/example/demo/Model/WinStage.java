package com.example.demo.Model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import static javafx.scene.paint.Color.WHITE;

public class WinStage extends Stage {
    /**
     * Megjelnik egy új ablakot amikor a játék véget ért.
     * Tájékoztatást nyújt arról, hogy melyik játékos nyert, illetve azt hogy döntetlen lett-e.
     * Gombok segítségével indíthatunk új játékot vagy bezárhatjuk a játékot.
     *
     * @param gm a játék modelljének átadása
     */
    public WinStage(GameModel gm) {
        super();

        Stage winStage = new Stage();
        VBox vbox = new VBox();
        Button newGame = new Button();
        Button exitGame = new Button();
        Label winnersName;

        if (gm.players.size() == 1) {
            winnersName = new Label(gm.players.getFirst().name + " nyert!");
        }
        else {
            winnersName = new Label("Döntetlen!");
        }

        Scene scene = new Scene(vbox, 400, 300);

        Font adumuFont = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 30);
        winnersName.setFont(adumuFont);
        winnersName.setTextFill(WHITE);

        newGame.setText("Új játék");
        exitGame.setText("Játék bezárása");

        newGame.setPrefWidth(150);
        newGame.setPrefHeight(25);
        newGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        newGame.setOnMouseEntered(e -> {
            newGame.setStyle("-fx-cursor: HAND;" + "-fx-font-size: 18px;" + "-fx-background-color: white;" + "-fx-font-weight: 900");
        });
        newGame.setOnMouseExited(e -> {
            newGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        });

        exitGame.setPrefWidth(150);
        exitGame.setPrefHeight(25);
        exitGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        exitGame.setOnMouseEntered(e -> {
            exitGame.setStyle("-fx-cursor: HAND;" + "-fx-font-size: 18px;" + "-fx-background-color: white;" + "-fx-font-weight: 900");
        });
        exitGame.setOnMouseExited(e -> {
            exitGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        });

        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                winStage.close();
                try {
                    gm.igc.gc.changeScene("gameConfiguration");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        exitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                winStage.close();
                System.exit(0);
            }
        });

        vbox.getChildren().addAll(winnersName, newGame, exitGame);

        vbox.setStyle("-fx-border-color: white;" +
                "-fx-border-width: 5px;" +
                "-fx-background-color: #161C1C;");

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        winStage.initModality(Modality.WINDOW_MODAL); // Set modality
        winStage.initOwner(gm.igc.gc.stage);
        winStage.initStyle(StageStyle.UNDECORATED);

        winStage.setScene(scene);
        winStage.show();
    }
}
