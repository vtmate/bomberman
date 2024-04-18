package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;

public class GameModel {
    public ArrayList<Wall> walls;
    public ArrayList<EdgeWall> edgeWalls;
    public ArrayList<Player> players;
    public ArrayList<Monster> monsters;
    public ArrayList<Bomb> bombs;
    public ArrayList<Gate> gates;
    public ArrayList<Explosion> explosions;
    public ArrayList<Box> boxes;
    public ArrayList<PowerUp> powerUps;
    public Timeline lastPlayerTimeline;
    private LayoutCreator layoutCreator;
    public InGameController igc;

    private int narrowing_cnt = 0;

    //private boolean tovabb; //ehelyett majd a hatótávot kell csekkolni
    //private int toUp, toRight, toDown, toLeft;


    public GameModel(String map, InGameController igc) {
        this.igc = igc;
        this.walls = new ArrayList<>();
        this.edgeWalls = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        //majd itt kellene megcsinálni az elégazást, hogy melyik pálya legyen meghívva
        new LayoutCreator(this, map, igc);
            //példányosítással le is futnak az inicializáló függvények
        printEntity(this.players);

        System.out.println("Created GameModel");

    }

    public void placeBomb(Player player) {

        if(player != null){
            for (PowerUp powerUp : player.getPowerUps()){
                System.out.println("power: " + powerUp.getPowerUpType());
            }
            if(!player.hasPowerUp(PowerUpType.NOBOMBS)) {
                if(player.hasPowerUp(PowerUpType.DETONATOR)){
                    if(player.placedDetonators.size() == player.numOfAllBombs()){
                        //vagy ha az összes bombáját detonátor felszedése után helyezte le
                        System.out.println("size before: " + player.placedDetonators.size());
                        for(Bomb bomb : player.placedDetonators){ //összes bomba felrobbantása egyből
                            bomb.removeBomb(this, player, 1);
                        }
                        System.out.println("size after: " + player.placedDetonators.size());
                        player.placedDetonators.clear();
                        for (int i = 0; i < player.placedDetonators.size(); i++) { //minden bomba visszaadása
                            player.addBomb();
                        }
                        System.out.println("összes bomba felrobbantása detonatorból");
                    } else if(player.getCountOfBombs() > 0){
                        //van még lehelyezhető bombája
                        player.removeBomb();
                        Bomb bomb;
                        double x = Math.round(player.x / 40) * 40;
                        double y = Math.round(player.y / 40) * 40;
                        if (player.hasPowerUp(PowerUpType.BIGGERRADIUS)) {
                            bomb = new Bomb(x, y, 3);
                        } else if (player.hasPowerUp(PowerUpType.SMALLERRADIUS)) {
                            bomb = new Bomb(x, y, 1);
                        } else {
                            bomb = new Bomb(x, y, 2);
                        }
                        //this.bombs.add(bomb);
                        player.placedDetonators.add(bomb);
                        this.bombs.add(bomb);
                        System.out.println("bomba hozzáadva detonatorba");
                    }
                } else if (player.getCountOfBombs() > 0){
                    System.out.println("PLACE");
                    player.removeBomb();
                    Bomb bomb;
                    double x = Math.round(player.x / 40) * 40;
                    double y = Math.round(player.y / 40) * 40;
                    if (player.hasPowerUp(PowerUpType.BIGGERRADIUS)) {
                        bomb = new Bomb(x, y, 3);
                    } else if (player.hasPowerUp(PowerUpType.SMALLERRADIUS)) {
                        bomb = new Bomb(x, y, 1);
                    } else {
                        bomb = new Bomb(x, y, 2);
                    }

                    this.bombs.add(bomb);
                    bomb.removeBomb(this, player, 2000); // TEST
                }
            }
        }
    }

    public void placeGate(Player player){
        player.setCountOfGates(player.getCountOfGates()-1);

        double x = Math.round(player.x / 40) * 40;
        double y = Math.round(player.y / 40) * 40;
        this.gates.add(new Gate(x,y, player));
    }

    public void explosion(double bombX, double bombY, int radius){
        int toUp = 0;
        int toRight = 0;
        int toDown = 0;
        int toLeft = 0;
        if(!isPlayerOnBomb(bombX, bombY)){ //ha a karakter nem maradt a bombán, akkor tovább nézzük
            rightExplosion(bombX, bombY, radius, toRight);
            leftExplosion(bombX, bombY, radius, toLeft);
            upExplosion(bombX, bombY, radius, toUp);
            downExplosion(bombX, bombY, radius, toDown);
        }
    }

    public boolean isPlayerOnBomb(double bombX, double bombY){
        for (int i = 0; i < players.size(); i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(bombX == x && bombY == y){
                playerDeath(players.get(i));
                return true;
            }
        }
        checkForGate(bombX, bombY);
        return false;
    }
    private void rightExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX, bombX+40, true)){
            //System.out.println("Jobbra fal volt, nem történik semmi");
        } else {
            //System.out.println("Jobbra nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX+40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX, bombX+40, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX+40, bombY)){
                iterate = false;
            }
            if(checkForGate(bombX+40, bombY)){
                iterate = false;
            }
            if (checkForMonster(bombX+40, bombY)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if (iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    rightExplosion(bombX+40, bombY, radius, iteration);
                }
            }
        }
    }
    private void leftExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombY, bombX-40, bombX, true)){
            //System.out.println("Balra fal volt, nem történik semmi");
        } else {
            //System.out.println("Balra nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX-40, bombY);
            int playerDeath = checkForPlayer(bombY, bombX-40, bombX, true);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX-40, bombY)){
                iterate = false;
            }
            if(checkForGate(bombX-40, bombY)){
                iterate = false;
            }
            if (checkForMonster(bombX-40, bombY)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if (iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    leftExplosion(bombX-40, bombY, radius, iteration);
                }
            }
        }
    }
    private void upExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY-40, bombY, false)){
            //System.out.println("Felfelé fal volt, nem történik semmi");
        } else {
            //System.out.println("Felfelé nem volt fal");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY-40);
            int playerDeath = checkForPlayer(bombX, bombY-40, bombY, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX, bombY-40)){
                iterate = false;
            }
            if(checkForGate(bombX, bombY-40)){
                iterate = false;
            }
            if (checkForMonster(bombX, bombY-40)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if(iterate){
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    upExplosion(bombX, bombY-40, radius, iteration);
                }
            }
        }
    }
    private void downExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY, bombY+40, false)){
            //.out.println("Lefelé fal volt, nem történik semmi");
        } else {
            //System.out.println("Lefelé nem volt fal:");
            boolean iterate = true;
            //nem volt fal -> kirajzoljuk a bombát
            drawExposion(bombX, bombY+40);
            int playerDeath = checkForPlayer(bombX, bombY, bombY+40, false);
            if (playerDeath >= 0){
                //valamelyik játékos meghalt
                iterate = false;
            }
            if(checkForBox(bombX, bombY+40)){
                iterate = false;
            }
            if(checkForGate(bombX, bombY+40)){
                iterate = false;
            }
            if (checkForMonster(bombX, bombY+40)){
                //System.out.println("az egyik szörny meghalt");
                iterate = false;
            }
            if(iterate) {
                //System.out.println("Nem volt ott semmi");
                if(iteration < radius-1){
                    iteration++;
                    downExplosion(bombX, bombY+40, radius, iteration);
                }
            }
        }
    }

    private void drawExposion(double x, double y){
        Explosion explosion = new Explosion(x, y);
        this.explosions.add(explosion);
        explosion.removeExplosion(this, 500);
    }

    public void playerDeath(Player player){
        if(!player.hasPowerUp(PowerUpType.SHIELD)){
            System.out.println(player.id + ". játékos meghalt");
            this.players.remove(player);
            if (this.players.size() == 1) {
                System.out.println("Már csak egy játékosmaradt");
                if (igc != null) {
                    lastPlayerTimeline = new Timeline();
                    igc.timerLabel.setTextFill(Color.RED);
                    lastPlayerTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

                        int second = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            second++;


                            if (second == 5) {
                                lastPlayerTimeline.stop(); // Ha elértük a maximális iterációt, leállítjuk a timeline-ot
                                lastPlayerTimeline = null;
                                stopTimers();
                                new WinStage(GameModel.this);
                            }
                        }
                    }));

                    lastPlayerTimeline.setCycleCount(5); // A timeline egy végtelen ciklusban fog futni
                    lastPlayerTimeline.play();
                }
            }

            if (this.players.size() == 0) {
                lastPlayerTimeline.stop();
                lastPlayerTimeline = null;
                stopTimers();
                new WinStage(GameModel.this);
            }
        }
    }

    private int checkForPlayer(double same, double smaller, double bigger, boolean isHorizontal){
        for (int i = 0; i < players.size(); i++) {
            double x = this.players.get(i).x;
            double y = this.players.get(i).y;
            if(isHorizontal){
                if(same == y && isBetween(x, smaller, bigger)){
                    playerDeath(this.players.get(i));
                    return i;
                }
            } else {
                if(same == x && isBetween(y, smaller, bigger)){
                    playerDeath(this.players.get(i));
                    return i;
                }
            }
        }
        return -1; //egyik játékos sem halt meg
    }

    private boolean checkForWall(double same, double smaller, double bigger, boolean horizontal){
        for(Wall wall : walls){
            if(horizontal){
                if(same == wall.y && isBetween(wall.x, smaller, bigger)){
                    return true;
                }
            } else {
                if(same == wall.x && isBetween(wall.y, smaller, bigger)){
                    return true;
                }
            }
        }
        for(EdgeWall edgeWall : edgeWalls){
            if(horizontal){
                if(same == edgeWall.y && isBetween(edgeWall.x, smaller, bigger)){
                    return true;
                }
            } else {
                if(same == edgeWall.x && isBetween(edgeWall.y, smaller, bigger)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkForMonster(double expX, double expY){
        for(Monster monster : monsters){
            if(checkInteraction(monster.x, monster.y, expX, expY)){
                monsters.remove(monster);
                monster.stop();
                monster = null;
                return true;
            }
        }
        return false;
    }

    public boolean checkForBox(double expX, double expY){
        for(Box box : boxes){
            if(checkInteraction(box.x, box.y, expX, expY)){
                boxes.remove(box);
                //box = null;
                return true;
            }
        }
        return false;
    }
    public boolean checkForGate(double expX, double expY){
        for(Gate gate : gates){
            if(checkInteraction(gate.x, gate.y, expX, expY)){
                gates.remove(gate);
                gate.owner.setCountOfGates(gate.owner.getCountOfGates()+1);
                return true;
            }
        }
        return false;
    }


    public void checkImmadiateBombs(){
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if(player.hasPowerUp(PowerUpType.IMMADIATEBOMB)){
                if(player.getCountOfBombs() > 0){
                    placeBomb(player);
                }
            }
        }
    }

    public boolean isBetween(double value, double smaller, double bigger){
        return smaller <= value && bigger >= value;
    }

    public void printEntity(ArrayList<? extends Entity> entities) {
        for ( int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i).x + ", " + entities.get(i).y);
        }
    }

    public boolean checkInteraction(double x1, double y1, double x2, double y2) {
        int SIZE = 40;
        if (x1 + SIZE - 1 < x2 || x2 + SIZE - 1 < x1 || y1 + SIZE - 1 < y2 || y2 + SIZE - 1 < y1) {
            return false;
        }
        return true;
    }

    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.id == playerId) return player;
        }
        return null;
    }

    public void stopTimers() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).pause();
        }
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).pause();
        }
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).pause();
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).pause();
        }
        igc.timeline.pause();
        igc.timer.pause();
    }

    public void startTimers() {
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).resume();
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).resume();
        }
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).resume();
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).resume();
        }
        igc.timeline.play();
        igc.timer.play();
    }

    private boolean isWall(int x, int y) {
        for (Wall wall : walls) {
            if (checkInteraction(x*40, y*40, wall.x, wall.y)) {
                this.walls.remove(wall);
                return true;
            }
        }
        return false;
    }

    private boolean isPlayer(int x, int y) {
        for (Player player : players) {
            if (checkInteraction(x*40, y*40, player.x, player.y)) {
                this.players.remove(player);
                return true;
            }
        }
        return false;
    }

    private boolean isMonster(int x, int y) {
        for (Monster monster : monsters) {
            if (checkInteraction(x*40, y*40, monster.x, monster.y)) {
                this.monsters.remove(monster);
                return true;
            }
        }
        return false;
    }

    private boolean isBomb(int x, int y) {
        for (Bomb bomb : bombs) {
            if (checkInteraction(x*40, y*40, bomb.x, bomb.y)) {
                this.bombs.remove(bomb);
                return true;
            }
        }
        return false;
    }

    private boolean isBox(int x, int y) {
        for (Box box : boxes) {
            if (checkInteraction(x*40, y*40, box.x, box.y)) {
                this.boxes.remove(box);
                return true;
            }
        }
        return false;
    }

    private boolean isPowerUp(int x, int y) {
        for (PowerUp powerUp : powerUps) {
            if (checkInteraction(x*40, y*40, powerUp.x, powerUp.y)) {
                this.powerUps.remove(powerUp);
                return true;
            }
        }
        return false;
    }

    private void isGate(int x, int y) {
        for (Gate gate : gates) {
            if (checkInteraction(x*40, y*40, gate.x, gate.y)) {
                this.gates.remove(gate);
                break;
            }
        }
    }

    public void narrowing() {
        narrowing_cnt += 1;
        boolean found;
        int ind_x, ind_y;
        // Sorok vizsgálata
        for (int i = narrowing_cnt; i < (12 - narrowing_cnt); i++) {
            found = false;
            ind_x = narrowing_cnt * 40;
            ind_y = i * 40;
            // ELSŐ sor
            if (isWall(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPlayer(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isMonster(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBomb(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBox(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPowerUp(ind_x, ind_y)) {
                found = true;
            }
            if (!found) {
                isGate(ind_x, ind_y);
            }
            this.edgeWalls.add(new EdgeWall(ind_x*40, ind_y*40));

            // UTOLSÓ sor
            found = false;
            ind_x = 12 - narrowing_cnt;
            if (isWall(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPlayer(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isMonster(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBomb(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBox(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPowerUp(ind_x, ind_y)) {
                found = true;
            }
            if (!found) {
                isGate(ind_x, ind_y);
            }
            this.edgeWalls.add(new EdgeWall(ind_x*40, ind_y*40));
        }

        // Oszlopok vizsgálata
        for (int j = narrowing_cnt + 1; j < (8 - narrowing_cnt); j++) {
            // BAL oszlop
            found = false;
            ind_x = j*40;
            ind_y = narrowing_cnt*40;
            if (isWall(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPlayer(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isMonster(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBomb(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBox(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPowerUp(ind_x, ind_y)) {
                found = true;
            }
            if (!found) {
                isGate(ind_x, ind_y);
            }
            this.edgeWalls.add(new EdgeWall(ind_x*40, ind_y*40));

            // JOBB oszlop
            found = false;
            ind_x = j*40;
            ind_y = (13 - narrowing_cnt)*40;
            if (isWall(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPlayer(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isMonster(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBomb(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isBox(ind_x, ind_y)) {
                found = true;
            }
            if (!found && isPowerUp(ind_x, ind_y)) {
                found = true;
            }
            if (!found) {
                isGate(ind_x, ind_y);
            }
            this.edgeWalls.add(new EdgeWall(ind_x*40, ind_y*40));
        }
    }

    public void battleRoyale(){
        narrowing_cnt ++;
        for (int i = 0 + narrowing_cnt; i < 13 - narrowing_cnt; i++) { //felső és alsó sor
            edgeWalls.add(new EdgeWall(i*40, 0 + narrowing_cnt * 40));
            edgeWalls.add(new EdgeWall(i*40,40*10 - narrowing_cnt * 40));
            removeEntities(narrowing_cnt * 40, (12 - narrowing_cnt) * 40,
                    narrowing_cnt * 40, 40*10 - narrowing_cnt * 40, true);
        }
        for (int i = 1 + narrowing_cnt; i < 10 - narrowing_cnt; i++) { //bal és jobb oszlop
            edgeWalls.add(new EdgeWall(0 + narrowing_cnt * 40, i*40));
            edgeWalls.add(new EdgeWall(12*40 - narrowing_cnt * 40,i*40));
            removeEntities((1+narrowing_cnt)*40, (9-narrowing_cnt) * 40,
                    narrowing_cnt * 40, 12*40 - narrowing_cnt * 40, false);
        }
    }

    private void removeEntities(int from, int to, int same1, int same2, boolean isHorizontal){
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if(isHorizontal){
                if (player.x >= from && player.x <= to &&
                   ((player.y >= same1 && player.y < same1 + 40)||
                    (player.y <= same2 && player.y > same2 - 40))){
                    playerDeath(player);
                }
            } else {
                if (player.y >= from && player.y <= to &&
                   ((player.x >= same1 && player.x < same1 + 40)||
                    (player.x <= same2 && player.x > same2 - 40))){
                    playerDeath(player);
                }
            }
        }

        Iterator<Bomb> bombIterator = bombs.iterator();
        removeEntity(bombIterator, from, to, same1, same2, isHorizontal);
        Iterator<Box> boxIterator = boxes.iterator();
        removeEntity(boxIterator, from, to, same1, same2, isHorizontal);
        Iterator<Gate> gateIterator = gates.iterator();
        removeEntity(gateIterator, from, to, same1, same2, isHorizontal);
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        removeEntity(powerUpIterator, from, to, same1, same2, isHorizontal);
        Iterator<Monster> monsterIterator = monsters.iterator();
        removeEntity(monsterIterator, from, to, same1, same2, isHorizontal);
    }

    private <T extends Entity> void removeEntity(Iterator<T> iterator, int from, int to, int same1, int same2, boolean isHorizontal) {
        while (iterator.hasNext()) {
            T entity = iterator.next();
            if(isHorizontal){
                if (entity.x >= from && entity.x <= to &&
                   ((entity.y >= same1 && entity.y < same1 + 40)||
                    (entity.y <= same2 && entity.y > same2 - 40))){
                    iterator.remove();
                }
            } else {
                if(entity.y >= from && entity.y <= to &&
                   ((entity.x >= same1 && entity.x < same1 + 40)||
                   (entity.x <= same2 && entity.x > same2 - 40))){
                    iterator.remove();
                }
            }
        }
    }
}
