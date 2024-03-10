package com.example.demo.Model;

import java.util.ArrayList;

public class GameModel {
    public ArrayList<Wall> walls;
    private ArrayList<Player> players;
    private ArrayList<Monster> monsters;
    private ArrayList<Bomb> bombs;
    private ArrayList<Box> boxes;
    private ArrayList<PowerUp> powerUps;

    public GameModel() {

        this.walls = new ArrayList<>();
        this.players = new ArrayList<>();
        createBorder();
        setUpPlayers();
        printEntity(this.players);
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
        Player player1 = new Player(2,2 );
        Player player2 = new Player(12,12 );
        this.players.add(player1);
        this.players.add(player2);
    }

    public void printEntity(ArrayList<? extends Entity> entities) {
        for ( int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i).x + ", " + entities.get(i).y);
        }
    }
}
