package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameModel {
    public ArrayList<Wall> walls;
    public ArrayList<Player> players;
    public ArrayList<Monster> monsters;
    public ArrayList<Bomb> bombs;
    public ArrayList<Gate> gates;
    public ArrayList<Explosion> explosions;
    public ArrayList<Box> boxes;
    public ArrayList<PowerUp> powerUps;
    public Timeline lastPlayerTimeline;
    private LayoutCreator layoutCreator;
    public InGameController igc;

    //private boolean tovabb; //ehelyett majd a hatótávot kell csekkolni
    //private int toUp, toRight, toDown, toLeft;


    public GameModel(String map, InGameController igc) {
        this.igc = igc;
        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        //majd itt kellene megcsinálni az elégazást, hogy melyik pálya legyen meghívva
        this.layoutCreator = new LayoutCreator(this, map);
            //példányosítással le is futnak az inicializáló függvények
        printEntity(this.players);

        System.out.println("Created GameModel");
    }

    public void placeBomb(Player player) {

        for (PowerUp powerUp : player.getPowerUps()){
            System.out.println("power: " + powerUp.getPowerUpType());
        }

        if(!player.hasPowerUp(PowerUpType.NOBOMBS)) {
            player.removeBomb();
            Bomb bomb;
            double x = Math.round(player.x / 40) * 40;
            double y = Math.round(player.y / 40) * 40;
            if (player.hasPowerUp(PowerUpType.BIGGERRADIUS)) {
                bomb = new Bomb(x, y, 3);
            } else if (player.hasPowerUp(PowerUpType.SMALLERRADIUS)) {
                bomb = new Bomb(x, y, 1);
            } else {
                bomb = new Bomb(x, y, 2);
            }

            this.bombs.add(bomb);
            bomb.removeBomb(this, player, 2000); // TEST
        }
    }

    public void placeGate(Player player){
        player.setCountOfGates(player.getCountOfGates()-1);

        double x = Math.round(player.x / 40) * 40;
        double y = Math.round(player.y / 40) * 40;
        this.gates.add(new Gate(x,y));
    }

    public void explosion(double bombX, double bombY, int radius){
        int toUp = 0;
        int toRight = 0;
        int toDown = 0;
        int toLeft = 0;
        if(!isPlayerOnBomb(bombX, bombY)){ //ha a karakter nem maradt a bombán, akkor tovább nézzük
            rightExplosion(bombX, bombY, radius, toRight);
            leftExplosion(bombX, bombY, radius, toLeft);
            upExplosion(bombX, bombY, radius, toUp);
            downExplosion(bombX, bombY, radius, toDown);
        }
    }

    private boolean isPlayerOnBomb(double bombX, double bombY){
        for (int i = 0; i < players.size(); i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(bombX == x && bombY == y){
                playerDeath(players.get(i));
                return true;
            }
        }
        return false;
    }
    private void rightExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX, bombX+40, true)){
            //System.out.println("Jobbra fal volt, nem történik semmi");
        } else {
            //System.out.println("Jobbra nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX+40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX+40, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX+40, bombY)){
                iterate = false;
            }
            if (checkForMonster(bombX+40, bombY)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if (iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    rightExplosion(bombX+40, bombY, radius, iteration);
                }
            }
        }
    }
    private void leftExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX-40, bombX, true)){
            //System.out.println("Balra fal volt, nem történik semmi");
        } else {
            //System.out.println("Balra nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX-40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX-40, bombX, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX-40, bombY)){
                iterate = false;
            }
            if (checkForMonster(bombX-40, bombY)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if (iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    leftExplosion(bombX-40, bombY, radius, iteration);
                }
            }
        }
    }
    private void upExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY-40, bombY, false)){
            //System.out.println("Felfelé fal volt, nem történik semmi");
        } else {
            //System.out.println("Felfelé nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY-40);
            int playerDeath = checkForPlayer(bombX, bombY-40, bombY, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX, bombY-40)){
                iterate = false;
            }
            if (checkForMonster(bombX, bombY-40)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if(iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    upExplosion(bombX, bombY-40, radius, iteration);
                }
            }
        }
    }
    private void downExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY, bombY+40, false)){
            //.out.println("Lefelé fal volt, nem történik semmi");
        } else {
            //System.out.println("Lefelé nem volt fal:");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY+40);
            int playerDeath = checkForPlayer(bombX, bombY, bombY+40, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX, bombY+40)){
                iterate = false;
            }
            if (checkForMonster(bombX, bombY+40)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if(iterate) {
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    downExplosion(bombX, bombY+40, radius, iteration);
                }
            }
        }
    }

    private void drawExposion(double x, double y){
        Explosion explosion = new Explosion(x, y);
        this.explosions.add(explosion);
        explosion.removeExplosion(this, 500);
    }

    public void playerDeath(Player player){
        System.out.println(player.id + ". játékos meghalt");
        this.players.remove(player);
        if (this.players.size() == 1) {
            System.out.println("Már csak egy játékosmaradt");
            lastPlayerTimeline = new Timeline();
            igc.timerLabel.setTextFill(Color.RED);
            lastPlayerTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

                int second = 0;
                @Override
                public void handle(ActionEvent event) {
                    second++;


                    if (second == 5) {
                        lastPlayerTimeline.stop(); // Ha elértük a maximális iterációt, leállítjuk a timeline-ot
                        lastPlayerTimeline = null;
                        stopTimers();
                        new WinStage(GameModel.this);
                    }
                }
            }));

            lastPlayerTimeline.setCycleCount(5); // A timeline egy végtelen ciklusban fog futni
            lastPlayerTimeline.play();
        }
    }

    private int checkForPlayer(double same, double smaller, double bigger, boolean isHorizontal){
        for (int i = 0; i < players.size(); i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(isHorizontal){
                if(same == y && isBetween(x, smaller, bigger)){
                    playerDeath(this.players.get(i));
                    return i;
                }
            } else {
                if(same == x && isBetween(y, smaller, bigger)){
                    playerDeath(this.players.get(i));
                    return i;
                }
            }
        }
        return -1; //egyik játékos sem halt meg
    }

    private boolean checkForWall(double same, double smaller, double bigger, boolean horizontal){
        for(Wall wall : walls){
//            System.out.println(same + " = " + wall.y + " and " + smaller + "<" + wall.x + "<" + bigger);
            if(horizontal){
                if(same == wall.y && isBetween(wall.x, smaller, bigger)){
                    return true;
                }
            } else {
                if(same == wall.x && isBetween(wall.y, smaller, bigger)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkForMonster(double expX, double expY){
        for(Monster monster : monsters){
            if(checkInteraction(monster.x, monster.y, expX, expY)){
                monsters.remove(monster);
                monster.stop();
                monster = null;
                return true;
            }
        }
        return false;
    }

    private boolean checkForBox(double expX, double expY){
        for(Box box : boxes){
            if(checkInteraction(box.x, box.y, expX, expY)){
                boxes.remove(box);
                //box = null;
                return true;
            }
        }
        return false;
    }


    public void checkImmadiateBombs(){
        //valahogy meg lehet azt csinálni, hogy csak akkor kerüljön ez bele a gameloopba, ha az ????????????????????
        //adott játékos felveszi a powerupot?
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if(player.hasPowerUp(PowerUpType.IMMADIATEBOMB)){
                if(player.getCountOfBombs() > 0){
                    placeBomb(player);
                }
            }
        }
    }

    private boolean isBetween(double value, double smaller, double bigger){
        return smaller <= value && bigger >= value;
    }

    public void printEntity(ArrayList<? extends Entity> entities) {
        for ( int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i).x + ", " + entities.get(i).y);
        }
    }

    public boolean checkInteraction(double x1, double y1, double x2, double y2) {
        int SIZE = 40;
        if (x1 + SIZE - 1 < x2 || x2 + SIZE - 1 < x1 || y1 + SIZE - 1 < y2 || y2 + SIZE - 1 < y1) {
            return false;
        }
        return true;
    }

    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.id == playerId) return player;
        }
        return null;
    }

    public void stopTimers() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).pause();
        }
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).pause();
        }
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).pause();
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).pause();
        }
    }

    public void startTimers() {
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).resume();
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).resume();
        }
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).resume();
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).resume();
        }
    }

}
