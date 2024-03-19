package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {
    public ArrayList<Wall> walls;
    public ArrayList<Player> players;
    public ArrayList<Monster> monsters;
    public ArrayList<Bomb> bombs;
    public ArrayList<Explosion> explosions;
    private ArrayList<Box> boxes;
    private ArrayList<PowerUp> powerUps;
    private Timer timer;
    private LayoutCreator layoutCreator;

    //private boolean tovabb; //ehelyett majd a hatótávot kell csekkolni
    //private int toUp, toRight, toDown, toLeft;


    public GameModel() {
        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.monsters = new ArrayList<>();
        //majd itt kellene megcsinálni az elégazást, hogy melyik pálya legyen meghívva
        this.layoutCreator = new LayoutCreator(this, 0);
            //példányosítással le is futnak az inicializáló függvények
        printEntity(this.players);

        timer = new Timer();

        System.out.println("Created GameModel");
    }

    public void placeBomb(Player player) {
        Bomb bomb;
        double x = Math.round(player.x / 40) * 40;
        double y = Math.round(player.y / 40) * 40;
        if(player.hasPowerUp(PowerUpType.BIGGERRADIUS)){
            bomb = new Bomb(x, y, 3);
        } else if(player.hasPowerUp(PowerUpType.SMALLERRADIUS)){
            bomb = new Bomb(x, y, 1);
        } else {
            bomb = new Bomb(x, y, 2);
        }
        this.bombs.add(bomb);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                explosion(bomb.x, bomb.y, bomb.getRadius());
                player.addBomb();
                GameModel.this.bombs.remove(bomb);
                //bomb = null; //állítólag így már nem hivatkozik rá semmi, ezért törölve lesz
            }
        }, 2000);
    }

    private void explosion(double bombX, double bombY, int radius){
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
                System.out.println(i + ". játékos meghalt");
                return true;
            }
        }
        return false;
    }
    private void rightExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX, bombX+40)){
            //System.out.println("Jobbra fal volt, nem történik semmi");
        } else {
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX+40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX+40, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
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
        if (checkForWall(bombY, bombX-40, bombX)){
            //System.out.println("Balra fal volt, nem történik semmi");
        } else {
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX-40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX-40, bombX, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
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
        if (checkForWall(bombX, bombY-40, bombY)){
            System.out.println("Felfelé fal volt, nem történik semmi");
        } else {
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY-40);
            int playerDeath = checkForPlayer(bombX, bombY-40, bombY, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
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
        if (checkForWall(bombX, bombY, bombY+40)){
            //System.out.println("Felfelé fal volt, nem történik semmi");
        } else {
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY+40);
            int playerDeath = checkForPlayer(bombX, bombY, bombY+40, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameModel.this.explosions.remove(explosion);
            }
        }, 500);
    }

    private int checkForPlayer(double same, double smaller, double bigger, boolean isHorizontal){
        for (int i = 0; i < players.size(); i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(isHorizontal){
                if(same == y && isBetween(x, smaller, bigger)){
                    return i;
                }
            } else {
                if(same == x && isBetween(y, smaller, bigger)){
                    return i;
                }
            }
        }
        return -1; //egyik játékos sem halt meg
    }

    private boolean checkForWall(double same, double smaller, double bigger){
        for(Wall wall : walls){
            if(same == wall.y && isBetween(wall.x, smaller, bigger)){
                return true;
            }
        }
        return false;
    }

    private boolean checkForMonster(double expX, double expY){
        for(Monster monster : monsters){
            if(checkInteraction(monster.x, monster.y, expX, expY)){
                monsters.remove(monster);
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
                    player.removeBomb();
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
}
