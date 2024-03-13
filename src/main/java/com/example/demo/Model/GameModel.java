package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {
    public ArrayList<Wall> walls;
    public ArrayList<Player> players;
    private ArrayList<Monster> monsters;
    public ArrayList<Bomb> bombs;
    public ArrayList<Explosion> explosions;
    private ArrayList<Box> boxes;
    private ArrayList<PowerUp> powerUps;
    private Timer timer;
    private boolean tovabb; //ehelyett majd a hatótávot kell csekkolni


    public GameModel() {

        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        createBorder();
        setUpPlayers();
        printEntity(this.players);

        timer = new Timer();

        System.out.println("Created GameModel");
    }

    public void createBorder() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 11; j++) {
                if ( i == 0 || i == 12) {
                    Wall wall = new Wall(i*40, j*40);
                    this.walls.add(wall);
                } else {
                    if ( j == 0 || j == 10) {
                        Wall wall = new Wall(i*40, j*40);
                        this.walls.add(wall);
                    }
                    else {
                        if ( i % 2 == 0 && j % 2 == 0) {
                            Wall wall = new Wall(i*40, j*40);
                            this.walls.add(wall);
                        }
                    }
                }
            }
        }
    }

    private void setUpPlayers() {
        Player player1 = new Player(40,40);
        Player player2 = new Player(440,360);
        player1.addBomb();
        player2.addBomb();
        this.players.add(player1);
        this.players.add(player2);
    }

    public void placeBomb(Player player) {
        final Bomb bomb = new Bomb(player.x, player.y);
        this.bombs.add(bomb);
        this.tovabb = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                explosion(bomb.x, bomb.y);
                player.addBomb();
                GameModel.this.bombs.remove(bomb);
                //bomb = null; //állítólag így már nem hivatkozik rá semmi, ezért törölve lesz
            }
        }, 2000);
    }

    private void explosion(double bombX, double bombY){
        //először csak azt nézem, hogy jobbra mizu
        if (checkForWall(bombY, bombX, bombX+40)){
            System.out.println("Jobbra fal volt, nem történik semmi");
        } else {
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX+40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX+40);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } /*szörny és doboz check*/ else {
                System.out.println("Nem volt ott semmi");
                if(tovabb){
                    tovabb = false;
                    explosion(bombX+40, bombY);
                }
            }
        }
        //balra mizu
        /*if (checkForWall(bombY, bombX, bombX-40)){
            System.out.println("Balra fal volt, nem történik semmi");
        } else {
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX-40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX-40);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } szörny és doboz check else {
                System.out.println("Nem volt ott semmi");
                if(tovabb){
                    tovabb = false;
                    explosion(bombX-40, bombY);
                }
            }
        }*/
    }

    private void drawExposion(double x, double y){
        Explosion explosion = new Explosion(x, y);
        this.explosions.add(explosion);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameModel.this.explosions.remove(explosion);
            }
        }, 200);
    }

    private int checkForPlayer(double same, double smaller, double bigger){
        for (int i = 0; i < 2; i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(same == y && isBetween(x, smaller, bigger)){
                return i;
            }
        }
        return -1; //egyik játékos sem halt meg
    }

    private boolean checkForWall(double bombY, double smaller, double bigger){
        for(Wall wall : this.walls){
            if(bombY == wall.y && isBetween(wall.x, smaller, bigger)){
                return true;
            }
        }
        return false;
    }

    private boolean isBetween(double value, double smaller, double bigger){
        return smaller <= value && bigger >= value;
    }

    public void printEntity(ArrayList<? extends Entity> entities) {
        for ( int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i).x + ", " + entities.get(i).y);
        }
    }
}
