package com.example.demo.Model;

import java.util.ArrayList;
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
    //private boolean tovabb; //ehelyett majd a hatótávot kell csekkolni
    //private int toUp, toRight, toDown, toLeft;


    public GameModel() {

        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.monsters = new ArrayList<>();
        createBorder();
        setUpPlayers();
        createMonsters();
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

    private void createMonsters() {
        Monster monster = new Monster(40*4, 40*1);
        monsters.add(monster);
        monster = new Monster(40*7, 40*4);
        monsters.add(monster);
    }

    private void setUpPlayers() {
        Player player1 = new Player(40,40);
        Player player2 = new Player(440,360);
        player1.addBomb();
        player2.addBomb();

        //powerUp-ok kipróbálása:
        //PowerUp p = new PowerUp(0,0,PowerUpType.ROLLERSKATE);
        //PowerUp p2 = new PowerUp(0,0,PowerUpType.SNAIL);
        //player1.addPowerUp(p2);
        //player2.addPowerUp(new PowerUp(0,0, PowerUpType.SMALLERRADIUS));
        //player2.addPowerUp(new PowerUp(0,0, PowerUpType.IMMADIATEBOMB));

        this.players.add(player1);
        this.players.add(player2);
    }

    public void placeBomb(Player player) {
        Bomb bomb;
        if(player.hasPowerUp(PowerUpType.BIGGERRADIUS)){
            bomb = new Bomb(player.x, player.y, 3);
        } else if(player.hasPowerUp(PowerUpType.SMALLERRADIUS)){
            bomb = new Bomb(player.x, player.y, 1);
        } else {
            bomb = new Bomb(player.x, player.y, 2);
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
        for (int i = 0; i < 2; i++) {
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
            System.out.println("Jobbra fal volt, nem történik semmi");
        } else {
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX+40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX+40, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } /*szörny és doboz check*/ else {
                System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    rightExplosion(bombX+40, bombY, radius, iteration);
                }
            }
        }
    }
    private void leftExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX-40, bombX)){
            System.out.println("Balra fal volt, nem történik semmi");
        } else {
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX-40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX-40, bombX, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } /*szörny és doboz check*/ else {
                System.out.println("Nem volt ott semmi");
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
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY-40);
            int playerDeath = checkForPlayer(bombX, bombY-40, bombY, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } /*szörny és doboz check*/ else {
                System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    upExplosion(bombX, bombY-40, radius, iteration);
                }
            }
        }
    }
    private void downExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY, bombY+40)){
            System.out.println("Felfelé fal volt, nem történik semmi");
        } else {
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY+40);
            int playerDeath = checkForPlayer(bombX, bombY, bombY+40, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                System.out.println(playerDeath + ". játékos meghalt");
            } /*szörny és doboz check*/ else {
                System.out.println("Nem volt ott semmi");
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
        }, 200);
    }

    private int checkForPlayer(double same, double smaller, double bigger, boolean isHorizontal){
        for (int i = 0; i < 2; i++) {
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
        for(Wall wall : this.walls){
            if(same == wall.y && isBetween(wall.x, smaller, bigger)){
                return true;
            }
        }
        return false;
    }

    public void checkImmadiateBombs(){
        //valahogy meg lehet azt csinálni, hogy csak akkor kerüljön ez bele a gameloopba, ha az ????????????????????
        //adott játékos felveszi a powerupot?
        for (int i = 0; i < 2; i++) {
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
}
