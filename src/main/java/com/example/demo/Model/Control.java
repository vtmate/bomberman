package com.example.demo.Model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Control {
    private boolean isMoving0 = false;
    private boolean isMoving1 = false;
    GameModel gm;
    public Control(GameModel gm) {
        this.gm = gm;
    }


    public void moveUp0() {
        if (intersection(gm.players.get(0), gm.walls, "UP") && intersection(gm.players.get(0), gm.bombs, "UP")) move(0, -4, 0);
        System.out.println("Fel");
    }

    public void moveDown0() {
        if (intersection(gm.players.get(0), gm.walls, "DOWN") && intersection(gm.players.get(0), gm.bombs, "DOWN")) move(0, 4, 0);

        System.out.println("Le");
    }

    public void moveLeft0() {
        if (intersection(gm.players.get(0), gm.walls, "LEFT") && intersection(gm.players.get(0), gm.bombs, "LEFT")) move(-4, 0, 0);
        System.out.println("Balra");
    }

    public void moveRight0() {
        if (intersection(gm.players.get(0), gm.walls, "RIGHT") && intersection(gm.players.get(0), gm.bombs, "RIGHT")) move(4, 0, 0);
        System.out.println("Jobbra");
    }
    public void moveUp1() {

        if (intersection(gm.players.get(1), gm.walls, "UP") && intersection(gm.players.get(1), gm.bombs, "UP")) move(0, -4, 1);
        System.out.println("Fel");
    }

    public void moveDown1() {

        if (intersection(gm.players.get(1), gm.walls, "DOWN") && intersection(gm.players.get(1), gm.bombs, "DOWN")) move(0, +4, 1);
        System.out.println("Le");
    }

    public void moveLeft1() {

        if (intersection(gm.players.get(1), gm.walls, "LEFT") && intersection(gm.players.get(1), gm.bombs, "LEFT")) move(-4, 0, 1);
        System.out.println("Balra");
    }

    public void moveRight1() {
        if (intersection(gm.players.get(1), gm.walls, "RIGHT") && intersection(gm.players.get(1), gm.bombs, "RIGHT")) move(4, 0, 1);
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
                if (player.y + 40 == entity.y *40 && player.x == entity.x*40) return false;
            }
            if (Objects.equals(direction, "UP")) {
                if (player.y - 40 == entity.y*40 && player.x == entity.x*40) return false;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (player.y == entity.y*40 && player.x - 40 == entity.x*40) return false;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (player.y == entity.y*40 && player.x + 40 == entity.x*40) return false;
            }
        }
        return true;
    }

    private void move(int x, int y, int player) {
        if (player == 0){
            if (isMoving0) {
                return;
            }
            isMoving0 = true;
        }
        else {
            if (isMoving1) {
                return;
            }
            isMoving1 = true;

        }
        moveMove(x, y, player);
    }

    private void moveMove(int x, int y, int player) {



        Timer timer = new Timer();

        // Időzített feladat definiálása
        TimerTask task = new TimerTask() {
            int count = 0; // Változó a függvényhívások számának nyomon követésére

            @Override
            public void run() {
                // Függvényhívás
                gm.players.get(player).x += x;
                gm.players.get(player).y += y;

                // Növeljük a hívások számát
                count++;

                // Ha elérjük az öt hívást, leállítjuk a timert
                if (count == 10) {
                    if (player == 0) {
                        isMoving0 = false;
                    }
                    else {
                        isMoving1 = false;
                    }

                    timer.cancel();
                }
            }
        };

        // Időzített feladatok ütemezése
        timer.scheduleAtFixedRate(task, 0, 10);
    }
}
