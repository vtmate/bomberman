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
                    Wall wall = new Wall(i, j);
                    this.walls.add(wall);
                } else {
                    if ( j == 0 || j == 10) {
                        Wall wall = new Wall(i, j);
                        this.walls.add(wall);
                    }
                    else {
                        if ( i % 2 == 0 && j % 2 == 0) {
                            Wall wall = new Wall(i, j);
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
        System.out.println("kezdeti bomba: " + player1.getCountOfBombs());
    }

    public void placeBomb(Player player) {
        Bomb bomb = new Bomb(player.x, player.y);
        this.bombs.add(bomb);
        explosion(player);
    }

    private void explosion(Player player){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Delayed execution after 2000 milliseconds");
                player.addBomb();
            }
        }, 1000);
    }

    public void printEntity(ArrayList<? extends Entity> entities) {
        for ( int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i).x + ", " + entities.get(i).y);
        }
    }
}
