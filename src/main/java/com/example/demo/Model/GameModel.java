package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {
    public ArrayList<Wall> walls;
    public ArrayList<Player> players;
    private ArrayList<Monster> monsters;
    public ArrayList<Bomb> bombs;
    private ArrayList<Box> boxes;
    private ArrayList<PowerUp> powerUps;
    private Timer timer;

    public GameModel() {

        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                explosion(player, bomb);
                GameModel.this.bombs.remove(bomb);
                //bomb = null; //állítólag így már nem hivatkozik rá semmi, ezért törölve lesz
            }
        }, 3000);
    }

    private void explosion(Player player, Bomb bomb){
        player.addBomb();
        for (int i = 0; i < 2; i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;

            if (bomb.x == x && isBetween(y, bomb.y-40, bomb.y)){
                System.out.println("bomba felett volt egyel");
            }
            if (bomb.x == x && isBetween(y, bomb.y, bomb.y+40)){
                System.out.println("bomba alatt volt egyel");
            }
            if (bomb.y == y && isBetween(x, bomb.x-40, bomb.x)){
                System.out.println("bomba mellett balra volt egyel");
            }
            if (bomb.y == y && isBetween(x, bomb.x, bomb.x+40)){
                System.out.println("bomba mellett jobbra volt egyel");
            } else if (checkForWall(bomb.y, bomb.x, bomb.x+40 )){
                System.out.println("fal van tőle jobbra");
            }
        }
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
