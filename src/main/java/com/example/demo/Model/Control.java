package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Control {
    GameModel gm;
    public Control(GameModel gm) {
        this.gm = gm;
    }


    public void moveUp0() {
        if (intersection(gm.players.get(0), gm.walls, "UP")) gm.players.get(0).y -= 1;
        System.out.println("Fel");
    }

    public void moveDown0() {
        if (intersection(gm.players.get(0), gm.walls, "DOWN")) gm.players.get(0).y += 1;

        System.out.println("Le");
    }

    public void moveLeft0() {
        if (intersection(gm.players.get(0), gm.walls, "LEFT")) gm.players.get(0).x -= 1;
        System.out.println("Balra");
    }

    public void moveRight0() {

        if (intersection(gm.players.get(0), gm.walls, "RIGHT")) gm.players.get(0).x += 1;
        System.out.println("Jobbra");
    }
    public void moveUp1() {

        if (intersection(gm.players.get(1), gm.walls, "UP")) gm.players.get(1).y -= 1;
        System.out.println("Fel");
    }

    public void moveDown1() {

        if (intersection(gm.players.get(1), gm.walls, "DOWN")) gm.players.get(1).y += 1;
        System.out.println("Le");
    }

    public void moveLeft1() {

        if (intersection(gm.players.get(1), gm.walls, "LEFT")) gm.players.get(1).x -= 1;
        System.out.println("Balra");
    }

    public void moveRight1() {
        if (intersection(gm.players.get(1), gm.walls, "RIGHT")) gm.players.get(1).x += 1;
        System.out.println("Jobbra");
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
