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
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.util.*;

import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

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
    public Label timerLabel;
    @FXML
    private HBox playerPowerUps1, playerPowerUps2;
    @FXML
    private VBox header;
    @FXML
    private BorderPane borderPane;
    private GameModel gm;
    public final GameController gc;
    private final String map;
    private final String playerName1;
    private final String playerName2;
    private int pauseStageCount = 0;
    public Timeline timeline;
    public Timeline timer;
    private int time;
    //képek
    Image wallImage;
    Image boxImage;
    Image player1Image;
    Image player2Image;
    Image powerUpImage;
    Image explosionImage;
    Image bombImage;
    Image monsterImage1;
    Image monsterImage2;
    Image gateImage;


    public InGameController(GameController gc, String playerName1, String playerName2, String map) {
        this.gc = gc;
        this.playerName1 = playerName1;

        System.out.println("Játékosnév: " + playerName1);
        this.playerName2 = playerName2;
        this.map = map;

    }

    public void initialize() {

        initImages();

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
        this.gm = new GameModel(map, this);



        gm.players.getFirst().name = this.playerName1;
        gm.players.getLast().name = this.playerName2;

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
                            case E -> control.placeGate(0);
                            case SPACE -> control.placeGate(1);
                            case ESCAPE -> pause();
                        }
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


        // Időzítő létrehozása és indítása
        time = 0;
        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                time++;
                int seconds = time % 60;
                int minutes = (int)Math.ceil(time / 60);
                timerLabel.setText("Idő: " + String.format("%02d:%02d", minutes, seconds));
                if (time % 5 == 0 && time != 0) {
                    System.out.println("szűkítés");
//                    gm.narrowing();
                    gm.battleRoyale();
                }
            })
        );
/*        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = elapsedTime / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                timerLabel.setText("Idő: " + String.format("%d:%02d", minutes, seconds));
            }
        };*/
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        control.moveMonster(gm.monsters.getFirst());
        control.moveMonster(gm.monsters.get(1));
    }

    public void refresh() {
        gamePane.getChildren().removeIf(node -> node instanceof Rectangle);
        gamePane.getChildren().removeIf(node -> node instanceof ImageView);
        for (int i = 0; i < 11; i++) {
            createWalls(gm.walls, i);
            createGates(gm.gates, i);
            createBombs(gm.bombs, i);
            createPowerUps(gm.powerUps, i);
            createBoxes(gm.boxes, i);
            createMonsters(gm.monsters, i);
            createPlayers(gm.players, i);
            createExplosion(gm.explosions, i);
            createEdgeWalls(gm.edgeWalls, i);
        }
        gm.checkImmadiateBombs();
        checkPlayerPowerUp(gm.getPlayer(0));
        checkPlayerPowerUp(gm.getPlayer(1));
    }

    private void initImages(){
        switch (map){
            case "Dzsungel":
                wallImage = new Image("wall2.png");
                boxImage = new Image("box2.png");
                player1Image = new Image("player1.png");
                player2Image = new Image("player2.png");
                powerUpImage = new Image("powerUp1.png");
                explosionImage = new Image("exposion1.png");
                bombImage = new Image("bomb1.png");
                monsterImage1 = new Image("monster2.png");
                monsterImage2 = new Image("monster2.png");
                gateImage = new Image("gate2.png");
            break;
            case "Pokol":
                wallImage = new Image("wall3.png");
                boxImage = new Image("box3.png");
                player1Image = new Image("player1.png");
                player2Image = new Image("player2.png");
                powerUpImage = new Image("powerUp1.png");
                explosionImage = new Image("exposion1.png");
                bombImage = new Image("bomb1.png");
                monsterImage1 = new Image("monster3.png");
                monsterImage2 = new Image("monster3.png");
                gateImage = new Image("gate3.png");
            break;
            default:
                wallImage = new Image("wall1.png");
                boxImage = new Image("box1.png");
                player1Image = new Image("player1.png");
                player2Image = new Image("player2.png");
                powerUpImage = new Image("powerUp1.png");
                explosionImage = new Image("exposion1.png");
                bombImage = new Image("bomb1.png");
                monsterImage1 = new Image("monsterr1.png");
                monsterImage2 = new Image("monsterr1.png");
                gateImage = new Image("gate1.png");
            break;
        }
    }

    private void createPowerUps(ArrayList<PowerUp> powerUps, int i){
        createImageView(powerUps, powerUpImage, i);
    }
    private void createBoxes(ArrayList<Box> boxes, int i){
        createImageView(boxes, boxImage, i);
    }
    private void createGates(ArrayList<Gate> gates, int i){
        createImageView(gates, gateImage, i);
    }
    public void createEdgeWalls(ArrayList<EdgeWall> edgeWalls, int i) {
        createImageView(edgeWalls, wallImage, i);
    }
    public void createPlayers(ArrayList<Player> players, int i) {
        if(players.size() == 2){
            createImageView(players.get(0), player1Image, i);
            createImageView(players.get(1), player2Image, i);
        } else if (players.size() == 1){
            Image img = players.get(0).id == 0 ? player1Image : player2Image;
            createImageView(players.get(0), img, i);
        }
    }
    public void createMonsters(ArrayList<Monster> monsters, int i) {
        if(monsters.size() == 2){
            createImageView(monsters.get(0), monsterImage1, i);
            createImageView(monsters.get(1), monsterImage2, i);
        } else if (monsters.size() == 1){
            Image img = monsters.get(0).id == 0 ? monsterImage1 : monsterImage2;
            createImageView(monsters.get(0), img, i);
        }
    }
    public void createBombs(ArrayList<Bomb> bombs, int i) {
        createImageView(bombs, bombImage, i);
    }
    public void createExplosion(ArrayList<Explosion> explosions, int i){
        createImageView(explosions, explosionImage, i);
    }
    private void createWalls(ArrayList<Wall> walls, int i) {
        createImageView(walls, wallImage, i);
    }

    private void createImageView(ArrayList<? extends Entity> entities, Image image, int i) {
        int size = 55;
        for (Entity entity : entities) {
            if(entity.y >= i*40 && entity.y < (i+1)*40){
                ImageView imageView = new ImageView(image);
                imageView.setX(entity.x);
                imageView.setY(entity.y - 10);
                imageView.setFitWidth(size);
                imageView.setFitHeight(size);
                this.gamePane.getChildren().add(imageView);
            }
        }
    }
    private void createImageView(Entity entity, Image image, int i) {
        int size = 60;
        if(entity.y >= i*40 && entity.y < (i+1)*40){
            ImageView imageView = new ImageView(image);
            imageView.setX(entity.x);
            imageView.setY(entity.y - 10);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            if(gm.players.size() == 2){
                if(image == player1Image && !gm.players.get(0).isRight) imageView.setScaleX(-1);
                if(image == player2Image && !gm.players.get(1).isRight) imageView.setScaleX(-1);
            } else if (gm.players.size() == 1){
                if(!gm.players.getFirst().isRight) imageView.setScaleX(-1);
            }
            if(gm.monsters.size() == 2){
                if(image == monsterImage1 && !gm.monsters.get(0).isRight) imageView.setScaleX(-1);
                if(image == monsterImage2 && !gm.monsters.get(1).isRight) imageView.setScaleX(-1);
            } else if (gm.monsters.size() == 1){
                if(!gm.monsters.getFirst().isRight) imageView.setScaleX(-1);
            }
            this.gamePane.getChildren().add(imageView);
        }
    }

    private void checkPlayerPowerUp(Player player) {

        if (Objects.isNull(player)) return;

        ArrayList<PowerUp> powerUps = player.getPowerUps();

        if (player.id == 0) {
            playerPowerUps1.getChildren().clear();
        }
        else {
            playerPowerUps2.getChildren().clear();
        }
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp pu = powerUps.get(i);

            StackPane panePowerUp = new StackPane();

            panePowerUp.setPrefSize(35, 35);


            //panePowerUp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            Rectangle rect = new Rectangle(35, 35);
            rect.setFill(WHITE);
            rect.setArcWidth(50.0);
            rect.setArcHeight(50.0);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2.0f);
            dropShadow.setOffsetY(2.0f);
            dropShadow.setColor(rgb(50, 50, 50, .588));
            rect.setEffect(dropShadow);

            panePowerUp.getChildren().add(rect);
            panePowerUp.getChildren().add(new ImageView(powerUps.get(i).image));

            StackPane.setAlignment(rect, Pos.CENTER);
            StackPane.setAlignment(new ImageView(powerUps.get(i).image), Pos.CENTER);
            if (player.id != 0) {
                playerPowerUps2.getChildren().add(panePowerUp);
            }
            else {
                playerPowerUps1.getChildren().add(panePowerUp);
            }
        }
    }

    private void pause() {
        if (pauseStageCount != 0) return;
        pauseStageCount++;
        //timeline.pause();
        gm.stopTimers();


        Stage pauseStage = new Stage();
        pauseStage.setResizable(false);
        pauseStage.setTitle("Bomberman - Szünet");
        pauseStage.setHeight(300);
        pauseStage.setWidth(400);
        pauseStage.initStyle(StageStyle.UNDECORATED);


        Button backToGame = new Button();
        backToGame.setText("Folytatás");

        backToGame.setPrefWidth(150);
        backToGame.setPrefHeight(25);
        backToGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        backToGame.setOnMouseEntered(e -> {
            backToGame.setStyle("-fx-cursor: HAND;" + "-fx-font-size: 18px;" + "-fx-background-color: white;" + "-fx-font-weight: 900");
        });
        backToGame.setOnMouseExited(e -> {
            backToGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        });

        Button newGame = new Button();
        newGame.setText("Új játék");

        newGame.setPrefWidth(150);
        newGame.setPrefHeight(25);
        newGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        newGame.setOnMouseEntered(e -> {
            newGame.setStyle("-fx-cursor: HAND;" + "-fx-font-size: 18px;" + "-fx-background-color: white;" + "-fx-font-weight: 900");
        });
        newGame.setOnMouseExited(e -> {
            newGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        });

        Button exitGame = new Button();
        exitGame.setText("Játék bezárása");

        exitGame.setPrefWidth(150);
        exitGame.setPrefHeight(25);
        exitGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        exitGame.setOnMouseEntered(e -> {
            exitGame.setStyle("-fx-cursor: HAND;" + "-fx-font-size: 18px;" + "-fx-background-color: white;" + "-fx-font-weight: 900");
        });
        exitGame.setOnMouseExited(e -> {
            exitGame.setStyle("-fx-font-size: 18px;" + "-fx-background-color: #C9C9C9;" + "-fx-font-weight: 900");
        });

        Label label = new Label("Szünet");
        label.setTextFill(WHITE);
        //label.setStyle("-fx-font-size: 30px;");
        Font adumuFont = Font.loadFont(getClass().getResourceAsStream("/Adumu.ttf"), 30);
        label.setFont(adumuFont);

        Pane pane = new Pane();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, backToGame, newGame, exitGame);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);


        backToGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gm.startTimers();
                pauseStageCount--;
                pauseStage.close();

            }
        });
        pauseStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                gm.startTimers();
                pauseStageCount--;
                pauseStage.close();
            }
        });

        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pauseStage.close();
                try {
                    gc.changeScene("gameConfiguration");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        exitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gc.stage.close();
                System.exit(0);
            }
        });


        Scene scene = new Scene(vbox, 300, 300);
        vbox.setStyle("-fx-border-color: white;" +
                        "-fx-border-width: 5px;" +
                "-fx-background-color: #161C1C;");
        pauseStage.setScene(scene);
        pauseStage.initModality(Modality.WINDOW_MODAL); // Set modality
        pauseStage.initOwner(gc.stage);
        pauseStage.show();
    }


}
