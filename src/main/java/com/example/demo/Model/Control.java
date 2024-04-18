package com.example.demo.Model;

import java.util.*;

public class Control {
    private final int SIZE = 40;
    GameModel gm;
    public Control(GameModel gm) {
        this.gm = gm;
    }

    /**
     * A beérkező input alapján a játékos mozgatását kezelő függvények
     * meghívása a megfelelő irányokkal.
     * @param direction     milyen nyilat nyomott le a játékos
     * @param playerId      melyik játékos
     */
    public void moveCharacter(String direction, int playerId){
        Player player;
        if (gm.getPlayer(playerId) != null) {
            player = gm.getPlayer(playerId);
        }
        else {
            return;
        }
        if(direction.equals("RIGHT")) player.isRight = true;
        else if(direction.equals("LEFT")) player.isRight = false;


        if(!playerIntersectsEdge(player, direction)){
            if(playerIntersectsEntity(player, direction) || player.hasPowerUp(PowerUpType.GHOST)){
                switch(direction){
                    case "UP" -> PowerUp.checkForPowerUp(player.x, player.y-40, player, gm);
                    case "DOWN" -> PowerUp.checkForPowerUp(player.x, player.y+40, player, gm);
                    case "LEFT" -> PowerUp.checkForPowerUp(player.x-40, player.y, player, gm);
                    case "RIGHT" -> PowerUp.checkForPowerUp(player.x+40, player.y, player, gm);
                }
                //ha van adott powerupja emberünknek, akkor ez így fusson le
                if(player.hasPowerUp(PowerUpType.SNAIL)){ //ez mondjuk lehetne osztályszintű metódusa a player-nek
                    switch(direction){
                        case "UP" -> move(0, -1, playerId, 40);
                        case "DOWN" -> move(0, 1, playerId, 40);
                        case "LEFT" -> move(-1, 0, playerId, 40);
                        case "RIGHT" -> move(1, 0, playerId, 40);
                    }
                } else if(player.hasPowerUp(PowerUpType.ROLLERSKATE)){
                    switch(direction){
                        case "UP" -> move(0, -20, playerId, 2);
                        case "DOWN" -> move(0, 20, playerId, 2);
                        case "LEFT" -> move(-20, 0, playerId, 2);
                        case "RIGHT" -> move(20, 0, playerId, 2);
                    }
                } else {
                    switch(direction){
                        case "UP" -> move(0, -4, playerId, 10);
                        case "DOWN" -> move(0, 4, playerId, 10);
                        case "LEFT" -> move(-4, 0, playerId, 10);
                        case "RIGHT" -> move(4, 0, playerId, 10);
                    }
                }
            }
        }
    }

    /**
     * Szörny mozgatásának átirányítása.
     * @param monster   adott monster
     */
    public void moveMonster(Monster monster) {
        monster.moveMonster(this);
    }

    /**
     * Bomba lerakásának átirányítása.
     * @param playerId  amelyik játékos lehelyezte a bombát
     */
    public void placeBomb(int playerId) {
        Player player = gm.getPlayer(playerId);
        gm.placeBomb(player);
    }

    /**
     * GATE lehelyezésének áritányítása.
     * @param playerId  amelyik játékos lehelyezte a GATE-t
     */
    public void placeGate(int playerId){
        Player player = gm.getPlayer(playerId);
        if(player != null){
            if (player.getCountOfGates() > 0) {
                gm.placeGate(player);
            }
        }
    }

    /**
     * Minden entitásra vizsgálódás, hogy az adott játékos érintkezik-e velük.
     * @param player        adott játékos
     * @param direction     az irány amerre a játékos halad
     * @return              hogy érintkezik-e
     */
    public boolean playerIntersectsEntity(Player player, String direction){
        double x = player.x;
        double y = player.y;
        if (checkEntitiesIntersection(x, y, gm.walls, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.players, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.bombs, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.boxes, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.gates, direction)) return false;
        return true;
    }

    /**
     * Vizsgálódás, hogy a játékos érintkezik-e a pálya szélével.
     * @param player        az adott játékos
     * @param direction     az irány amerre a játékos halad
     * @return              hogy érintkezik-e
     */
    private boolean playerIntersectsEdge(Player player, String direction){
        return checkEntitiesIntersection(player.x, player.y, gm.edgeWalls, direction);
    }

    /**
     * Adott entitás típusú tömbön az érintkezés figyelése.
     * @param x             a figyelt karakter x koordinátája
     * @param y             a figyelt karakter y koordinátája
     * @param entities      az adott tömb
     * @param direction     az irány, amerre a karakter halad
     * @return              hogy érintkezik-e
     */
    public boolean checkEntitiesIntersection(double x, double y, ArrayList<? extends Entity> entities, String direction) {
        for (Entity entity : entities) {
            if (Objects.equals(direction, "DOWN")) {
                if (gm.checkInteraction(x, y+SIZE, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "UP")) {
                if (gm.checkInteraction(x, y-SIZE, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (gm.checkInteraction(x-SIZE, y, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (gm.checkInteraction(x+SIZE, y, entity.x, entity.y)) return true;
            }
        }
        return false;
    }

    /**
     * Ne lehessen megváltoztatni a játékos haladási irányát miközben mozog, mivel
     * ekkor nem csak x*40-el lehet mozgazni a játékost.
     * @param x             a pixelszám amennyivel x-en mozgazjuk
     * @param y             a pixelszám amennyivel Y-On mozgazjuk
     * @param playerId      adott játékos
     * @param iteration     hányszor fut le az eltolás
     */
    private void move(int x, int y, int playerId, int iteration) {
        if (playerId == 0){
            if (gm.getPlayer(0).isMoving) {
                return;
            }
            gm.getPlayer(0).isMoving = true;
            gm.getPlayer(0).move(x, y, iteration);
        }
        else {
            if (gm.getPlayer(1).isMoving) {
                return;
            }
            gm.getPlayer(1).isMoving = true;
            gm.getPlayer(1).move(x, y, iteration);
        }
    }

    /**
     * Szörny irányának megváltoztatása.
     * @param monster       adott szörny
     * @param direction     új irány
     */
    public void changeDirection(Monster monster, String direction) {
        ArrayList<String> directions = new ArrayList<>(Arrays.asList("UP", "DOWN", "LEFT", "RIGHT"));
        String newDirection = direction;
        Random rand = new Random();

        while (newDirection.equals(direction)) {
            int random = rand.nextInt(4);
            newDirection = directions.get(random);
        }

        monster.direction = newDirection;
    }

    /**
     * Minden entitásra vizsgálódás, hogy az adott szörny érintkezik-e velük.
     * @param monster       az adott szörny
     * @param direction     az irány, amerre a szörny halad
     * @return              érintkezik-e
     */
    public boolean monsterIntersectsEntity(Monster monster, String direction){
        double x = monster.x;
        double y = monster.y;
        if (checkEntitiesIntersectionM(x, y, gm.walls, direction)) return false;
        if (checkEntitiesIntersection(x, y, gm.monsters, direction)) return false;
        if (checkEntitiesIntersectionM(x, y, gm.bombs, direction)) return false;
        if (checkEntitiesIntersectionM(x, y, gm.boxes, direction)) return false;
        if (checkEntitiesIntersectionM(x, y, gm.gates, direction)) return false;
        if (checkEntitiesIntersectionM(x, y, gm.edgeWalls, direction)) return false;
        checkPlayer(x, y);
        return true;
    }

    /**
     * Adott entitás típusú tömbön az érintkezés figyelése szörny esetén.
     * @param x             a figyelt szörny x koordinátája
     * @param y             a figyelt szörny y koordinátája
     * @param entities      az adott tömb
     * @param direction     az irány, amerre a szörny halad
     * @return              hogy érintkezik-e
     */
    private boolean checkEntitiesIntersectionM(double x, double y, ArrayList<? extends Entity> entities, String direction) {
        for (Entity entity : entities) {
            if (Objects.equals(direction, "DOWN")) {
                if (gm.checkInteraction(x, y+1, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "UP")) {
                if (gm.checkInteraction(x, y-1, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "LEFT")) {
                if (gm.checkInteraction(x-1, y, entity.x, entity.y)) return true;
            }
            if (Objects.equals(direction, "RIGHT")) {
                if (gm.checkInteraction(x+1, y, entity.x, entity.y)) return true;
            }
        }
        return false;
    }

    /**
     * Játékos és szörny találkozásának átirányítása
     * @param x     a szörny x koordinátája
     * @param y     a szörny y koordinátája
     */
    private void checkPlayer(double x, double y) {
        for (int i = 0; i < gm.players.size(); i++) {
            if (gm.checkInteraction(x, y, gm.players.get(i).x, gm.players.get(i).y)) {
                gm.playerDeath(gm.players.get(i));
            }
        }
    }
}
