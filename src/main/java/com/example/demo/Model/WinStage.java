package com.example.demo.Model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class WinStage extends Stage {
    public WinStage(GameModel gm) {
        super();

        Stage winStage = new Stage();
        HBox hbox = new HBox();
        Button newGame = new Button();
        Button exitGame = new Button();
        Label winnersName = new Label(gm.players.getFirst().name + " nyert!");
        Scene scene = new Scene(hbox, 300, 300);

        newGame.setText("Új játék");
        exitGame.setText("Játék bezárása");
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

        hbox.getChildren().addAll(winnersName, newGame, exitGame);

        winStage.setScene(scene);
        winStage.show();
    }
}
