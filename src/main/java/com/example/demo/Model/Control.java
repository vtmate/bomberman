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

    public void moveCharacter(String direction, int playerId){
        Player player = gm.players.get(playerId);
        if(intersection(player, gm.walls, direction) && intersection(player, gm.bombs, direction)){
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
