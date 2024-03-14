package com.example.demo.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.util.*;

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

    public void moveMonster(Monster monster) {
        System.out.println("run");
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    if (monster.x % 40 == 0 && monster.y % 40 == 0) {
                        Random rand = new Random();
                        if (rand.nextInt(5) == 2) changeDirection(monster, monster.direction);
                    }
                    if(monsterIntersectsEntity(monster, monster.direction)) {
                        switch(monster.direction){
                            case "UP" -> monster.y -= 1;
                            case "DOWN" -> monster.y += 1;
                            case "LEFT" -> monster.x -= 1;
                            case "RIGHT" -> monster.x += 1;
                        }
                    }
                    else {
                        changeDirection(monster, monster.direction);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
        if (checkEntitiesIntersection(x, y, gm.walls, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.players, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.bombs, direction)) return false;
        return true;
    }

    private boolean checkEntitiesIntersection(double x, double y, ArrayList<? extends Entity> entities, String direction) {
        for (Entity entity : entities) {
            if (Objects.equals(direction, "DOWN")) {
                if (checkInteraction(x, y+SIZE, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "UP")) {
                if (checkInteraction(x, y-SIZE, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (checkInteraction(x-SIZE, y, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (checkInteraction(x+SIZE, y, entity.x, entity.y)) return true;
            }
        }
        return false;
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

    private void changeDirection(Monster monster, String direction) {
        ArrayList<String> directions = new ArrayList<>(Arrays.asList("UP", "DOWN", "LEFT", "RIGHT"));
        String newDirection = direction;
        Random rand = new Random();

        while (newDirection.equals(direction)) {
            int random = rand.nextInt(4);
            newDirection = directions.get(random);
        }

        monster.direction = newDirection;
    }

    private boolean monsterIntersectsEntity(Monster monster, String direction){
        double x = monster.x;
        double y = monster.y;
        if (checkEntitiesIntersectionM(x, y, gm.walls, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.monsters, direction)) return false;
        if (checkEntitiesIntersectionM(x, y, gm.bombs, direction)) return false;
        return true;
    }

    private boolean checkEntitiesIntersectionM(double x, double y, ArrayList<? extends Entity> entities, String direction) {
        for (Entity entity : entities) {
            if (Objects.equals(direction, "DOWN")) {
                if (checkInteraction(x, y+1, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "UP")) {
                if (checkInteraction(x, y-1, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (checkInteraction(x-1, y, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (checkInteraction(x+1, y, entity.x, entity.y)) return true;
            }
        }
        return false;
    }

}
