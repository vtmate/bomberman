package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Control {
    GameModel gm;
    public Control(GameModel gm) {
        this.gm = gm;
    }


    public void moveUp0() {
        if (intersection(gm.players.get(0), gm.walls, "UP") && intersection(gm.players.get(0), gm.bombs, "UP")) gm.players.get(0).y -= 1;
        System.out.println("Fel");
    }

    public void moveDown0() {
        if (intersection(gm.players.get(0), gm.walls, "DOWN") && intersection(gm.players.get(0), gm.bombs, "DOWN")) gm.players.get(0).y += 1;

        System.out.println("Le");
    }

    public void moveLeft0() {
        if (intersection(gm.players.get(0), gm.walls, "LEFT") && intersection(gm.players.get(0), gm.bombs, "LEFT")) gm.players.get(0).x -= 1;
        System.out.println("Balra");
    }

    public void moveRight0() {

        if (intersection(gm.players.get(0), gm.walls, "RIGHT") && intersection(gm.players.get(0), gm.bombs, "RIGHT")) gm.players.get(0).x += 1;
        System.out.println("Jobbra");
    }
    public void moveUp1() {

        if (intersection(gm.players.get(1), gm.walls, "UP") && intersection(gm.players.get(1), gm.bombs, "UP")) gm.players.get(1).y -= 1;
        System.out.println("Fel");
    }

    public void moveDown1() {

        if (intersection(gm.players.get(1), gm.walls, "DOWN") && intersection(gm.players.get(1), gm.bombs, "DOWN")) gm.players.get(1).y += 1;
        System.out.println("Le");
    }

    public void moveLeft1() {

        if (intersection(gm.players.get(1), gm.walls, "LEFT") && intersection(gm.players.get(1), gm.bombs, "LEFT")) gm.players.get(1).x -= 1;
        System.out.println("Balra");
    }

    public void moveRight1() {
        if (intersection(gm.players.get(1), gm.walls, "RIGHT") && intersection(gm.players.get(1), gm.bombs, "RIGHT")) gm.players.get(1).x += 1;
        System.out.println("Jobbra");
    }

    public void placeBomb0() {

        if (gm.players.get(0).getCountOfBombs() == 0) {
            gm.placeBomb(gm.players.get(0));
            gm.players.get(0).setCountOfBombs();

        }
        System.out.println("Bomba lehelyezése");
    }

    public void placeBomb1() {

        if (gm.players.get(1).getCountOfBombs() == 0) {
            gm.placeBomb(gm.players.get(1));
            gm.players.get(1).setCountOfBombs();

        }
        System.out.println("Bomba lehelyezése");
    }

    private boolean intersection(Player player, ArrayList<? extends Entity> entities, String direction){
        for (Entity entity : entities) {
            if (Objects.equals(direction, "DOWN")) {
                if (player.y + 1 == entity.y && player.x == entity.x) return false;
            }
            if (Objects.equals(direction, "UP")) {
                if (player.y - 1 == entity.y && player.x == entity.x) return false;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (player.y == entity.y && player.x - 1 == entity.x) return false;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (player.y == entity.y && player.x + 1 == entity.x) return false;
            }
        }
        return true;
    }
}
