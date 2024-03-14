package com.example.demo.Model;

import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Control {
    private final int SIZE = 40;
    private boolean isMoving0 = false;
    private boolean isMoving1 = false;
    GameModel gm;
    public Control(GameModel gm) {
        this.gm = gm;
    }

    public void moveCharacter(String direction, int playerId){
        Player player = gm.players.get(playerId);
        if(playerIntersectsEntity(player, direction)){
            switch(direction){
                case "UP" -> move(0, -4, playerId);
                case "DOWN" -> move(0, 4, playerId);
                case "LEFT" -> move(-4, 0, playerId);
                case "RIGHT" -> move(4, 0, playerId);
            }
        }
    }

    public void placeBomb(int playerId) {
        Player player = gm.players.get(playerId);
        if (player.getCountOfBombs() > 0) {
            gm.placeBomb(player);
            player.removeBomb();
        }
        System.out.println("Bomba lehelyezése");
    }

    private boolean playerIntersectsEntity(Player player, String direction){
        double x = player.x;
        double y = player.y;
        for (Wall wall : gm.walls) {
            if (Objects.equals(direction, "DOWN")) {
                System.out.println(checkInteraction(x, y+SIZE, wall.x, wall.y));
                if (checkInteraction(x, y+SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "UP")) {
                System.out.println(checkInteraction(x, y-SIZE, wall.x, wall.y));
                if (checkInteraction(x, y-SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (checkInteraction(x-SIZE, y, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (checkInteraction(x+SIZE, y, wall.x, wall.y)) return false;
            }
        }
        for (Player wall : gm.players) {
            if (Objects.equals(direction, "DOWN")) {
                System.out.println(checkInteraction(x, y+SIZE, wall.x, wall.y));
                if (checkInteraction(x, y+SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "UP")) {
                System.out.println(checkInteraction(x, y-SIZE, wall.x, wall.y));
                if (checkInteraction(x, y-SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (checkInteraction(x-SIZE, y, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (checkInteraction(x+SIZE, y, wall.x, wall.y)) return false;
            }
        }
        for (Bomb wall : gm.bombs) {
            if (Objects.equals(direction, "DOWN")) {
                System.out.println(checkInteraction(x, y+SIZE, wall.x, wall.y));
                if (checkInteraction(x, y+SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "UP")) {
                System.out.println(checkInteraction(x, y-SIZE, wall.x, wall.y));
                if (checkInteraction(x, y-SIZE, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (checkInteraction(x-SIZE, y, wall.x, wall.y)) return false;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (checkInteraction(x+SIZE, y, wall.x, wall.y)) return false;
            }
        }
        return true;
    }

    private boolean checkInteraction(double x1, double y1, double x2, double y2) {
        if (x1 + SIZE - 1 < x2 || x2 + SIZE - 1 < x1 || y1 + SIZE - 1 < y2 || y2 + SIZE - 1 < y1) {
            return false;
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
