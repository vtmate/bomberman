package com.example.demo.Controller;


import com.example.demo.BombermanApplication;
import com.example.demo.Model.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.util.*;

import java.io.IOException;
import java.util.ArrayList;

public class InGameController {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    @FXML
    private Pane gamePane;
    @FXML
    private Pane leftPane;
    @FXML
    private Pane rightPane;
    @FXML
    private Label playerNameLabel1;
    @FXML
    private Label playerNameLabel2;
    @FXML
    private Label timerLabel;
    @FXML
    private HBox playerPowerUps1, playerPowerUps2;
    @FXML
    private VBox header;
    @FXML
    private BorderPane borderPane;
    private GameModel gm;
    private final GameController gc;
    private final String map;
    private final String playerName1;
    private final String playerName2;
    private long startTime;
    private int pauseStageCount = 0;
    private Timeline timeline;

    public InGameController(GameController gc, String playerName1, String playerName2, String map) {
        this.gc = gc;
        this.playerName1 = playerName1;
        System.out.println("Játékosnév: " + playerName1);
        this.playerName2 = playerName2;
        this.map = map;
    }

    public void initialize() {

        Font adumuFont = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 20);
        playerNameLabel1.setFont(adumuFont);
        playerNameLabel2.setFont(adumuFont);
        timerLabel.setFont(adumuFont);

        //header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        header.setBackground(new Background(new BackgroundImage(new Image("brickwall.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, false))));

        borderPane.setBackground(new Background(new BackgroundImage(new Image("bg.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));

        playerPowerUps1.setSpacing(10);
        playerPowerUps2.setSpacing(10);


        this.playerNameLabel1.setText(playerName1);
        this.playerNameLabel2.setText(playerName2);
        this.gm = new GameModel(map);
        System.out.println("gamemodel created");

        Control control = new Control(this.gm);

        gamePane.setPrefSize(520, 440);
        leftPane.setPrefSize(190, 440);
        rightPane.setPrefSize(190, 440);

        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case W -> control.moveCharacter("UP", 0);
                            case S -> control.moveCharacter("DOWN", 0);
                            case A -> control.moveCharacter("LEFT", 0);
                            case D -> control.moveCharacter("RIGHT", 0);
                            case UP -> control.moveCharacter("UP", 1);
                            case DOWN -> control.moveCharacter("DOWN", 1);
                            case LEFT -> control.moveCharacter("LEFT", 1);
                            case RIGHT -> control.moveCharacter("RIGHT", 1);
                            case Q -> control.placeBomb(0);
                            case CONTROL -> control.placeBomb(1);
                            case ESCAPE -> pause();
                        }
                        refresh();
                    }
                });
            }
        });

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.033), e -> {
                    refresh();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startTime = System.currentTimeMillis();

        // Időzítő létrehozása és indítása
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = elapsedTime / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                timerLabel.setText("Idő: " + String.format("%d:%02d", minutes, seconds));
            }
        };
        timer.start();

        control.moveMonster(gm.monsters.getFirst());
        control.moveMonster(gm.monsters.get(1));
    }

    @FXML //ezt egyenlőre nem használjuk, majd arra kell, hogy a játékból vissza tudjunk menni a főoldalra (esc)
    protected void goToMainPage() throws IOException {
        System.out.println("Főoldal");
        FXMLLoader fxmlMain = new FXMLLoader(BombermanApplication.class.getResource("mainPage-view.fxml"));
        Scene scene = new Scene(fxmlMain.load(), WIDTH, HEIGHT);
    }

    public void refresh() {
        gamePane.getChildren().removeIf(node -> node instanceof Rectangle);
        createWalls(gm.walls);
        createBombs(gm.bombs);
        createPlayers(gm.players);
        createMonsters(gm.monsters);
        createExplosion(gm.explosions);
        createPowerUps(gm.powerUps);
        createBoxes(gm.boxes);
        gm.checkImmadiateBombs();
        checkPlayerPowerUp(gm.getPlayer(0));
        checkPlayerPowerUp(gm.getPlayer(1));
    }

    private void createPowerUps(ArrayList<PowerUp> powerUps){
        int size = 40;
        for (PowerUp powerUp : powerUps) {
            Rectangle r = new Rectangle();
            r.setX(powerUp.x);
            r.setY(powerUp.y);
            r.setFill(Color.AQUA);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    private void createBoxes(ArrayList<Box> boxes){
        int size = 40;
        for (Box box : boxes) {
            Rectangle r = new Rectangle();
            r.setX(box.x);
            r.setY(box.y);
            r.setFill(Color.SADDLEBROWN);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
    public void createWalls(ArrayList<Wall> walls) {
        int size = 40;
        for (Wall wall : walls) {
            Rectangle r = new Rectangle();
            r.setX(wall.x);
            r.setY(wall.y);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }
    public void createPlayers(ArrayList<Player> players) {
        int size = 40;

        for (Player player : players) {
            Rectangle r = new Rectangle();
            r.setX(player.x);
            r.setY(player.y);
            r.setFill(Color.BLUEVIOLET);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    public void createMonsters(ArrayList<Monster> monsters) {
        int size = 40;
        for (Monster monster : monsters) {
            Rectangle r = new Rectangle();
            r.setX(monster.x);
            r.setY(monster.y);
            r.setFill(Color.FORESTGREEN);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    public void createBombs(ArrayList<Bomb> bombs) {
        int size = 40;
        for (Bomb bomb : bombs) {
            Rectangle r = new Rectangle();
            r.setFill(Color.ORANGE);
            r.setX(bomb.x);
            r.setY(bomb.y);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    public void createExplosion(ArrayList<Explosion> explosions){
        int size = 40;
        for (Explosion explosion : explosions) {
            Rectangle r = new Rectangle();
            r.setFill(Color.YELLOW);
            r.setX(explosion.x);
            r.setY(explosion.y);
            r.setWidth(size);
            r.setHeight(size);
            this.gamePane.getChildren().add(r);
        }
    }

    private void checkPlayerPowerUp(Player player) {

        if (Objects.isNull(player) || player.getPowerUps().isEmpty()) return;

        ArrayList<PowerUp> powerUps = player.getPowerUps();

        if (player.id == 0) {
            playerPowerUps1.getChildren().clear();
        }
        else {
            playerPowerUps2.getChildren().clear();
        }
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp pu = powerUps.get(i);
            Image img = new Image(getPowerUpImage(pu.getPowerUpType()));
            ImageView iw = new ImageView(img);
            iw.setFitWidth(25);
            iw.setFitHeight(25);

            StackPane panePowerUp = new StackPane();

            panePowerUp.setPrefSize(35, 35);


            //panePowerUp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            Rectangle rect = new Rectangle(35, 35);
            rect.setFill(Color.WHITE);
            rect.setArcWidth(50.0);
            rect.setArcHeight(50.0);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2.0f);
            dropShadow.setOffsetY(2.0f);
            dropShadow.setColor(Color.rgb(50, 50, 50, .588));
            rect.setEffect(dropShadow);

            panePowerUp.getChildren().add(rect);
            panePowerUp.getChildren().add(iw);

            StackPane.setAlignment(rect, Pos.CENTER);
            StackPane.setAlignment(iw, Pos.CENTER);
            if (player.id != 0) {
                playerPowerUps2.getChildren().add(panePowerUp);
            }
            else {
                playerPowerUps1.getChildren().add(panePowerUp);
            }
        }
    }

    private String getPowerUpImage(PowerUpType pt) {
        return switch (pt) {
            case PowerUpType.GATE -> "gatePowerUp.png";
            case PowerUpType.MOREBOMBS -> "morebombsPowerUp.png";
            case PowerUpType.BIGGERRADIUS -> "morebombsPowerUp.png";
            case PowerUpType.SNAIL -> "snailPowerUp.png";
            case PowerUpType.SMALLERRADIUS -> "morebombsPowerUp.png";
            case PowerUpType.NOBOMBS -> "morebombsPowerUp.png";
            case PowerUpType.IMMADIATEBOMB -> "morebombsPowerUp.png";
            case PowerUpType.DETONATOR -> "morebombsPowerUp.png";
            case PowerUpType.ROLLERSKATE -> "rollerskateskaterPowerUp.png";
            case PowerUpType.SHIELD -> "shieldPowerUp.png";
            case PowerUpType.GHOST -> "ghostPowerUp.png";
        };
    }

    private void pause() {
        if (pauseStageCount != 0) return;
        pauseStageCount++;
        //timeline.pause();


        Stage pauseStage = new Stage();
        pauseStage.setResizable(false);
        pauseStage.setTitle("Bomberman - Szünet");
        pauseStage.setHeight(200);
        pauseStage.setWidth(300);


        Button backToGame = new Button();
        backToGame.setText("Folytatás");

        Button mainMenu = new Button();
        mainMenu.setText("Főmenü");

        Pane pane = new Pane();
        HBox hbox = new HBox();
        hbox.getChildren().addAll(backToGame, mainMenu);


        backToGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pauseStageCount--;
                //timeline.play();
                pauseStage.close();

            }
        });
        Scene scene = new Scene(hbox, 300, 200);
        pauseStage.setScene(scene);
        pauseStage.initModality(Modality.WINDOW_MODAL); // Set modality
        pauseStage.initOwner(gc.stage);
        pauseStage.show();
    }
}
