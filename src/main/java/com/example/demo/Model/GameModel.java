package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
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

    /**
     *
     * @param map   a pálya típusa
     * @param igc   InGameController
     */
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

        new LayoutCreator(this, map, igc);
    }

    /**
     * Bomba lehelyezése.
     * A bomba lehelyezésének ellenőrzése. (detonátor, lehelyezhető bombák száma)
     *
     * @param player    a játékos
     */
    public void placeBomb(Player player) {

        if(player != null){
            if(!player.hasPowerUp(PowerUpType.NOBOMBS)) {
                if(player.hasPowerUp(PowerUpType.DETONATOR)){
                    if(player.placedDetonators.size() == player.numOfAllBombs()){
                        for(Bomb bomb : player.placedDetonators){ //összes bomba felrobbantása egyből
                            bomb.removeBomb(this, player, 1);
                        }
                        player.placedDetonators.clear();
                        for (int i = 0; i < player.placedDetonators.size(); i++) { //minden bomba visszaadása
                            player.addBomb();
                        }
                    } else if(player.getCountOfBombs() > 0){
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
                        player.placedDetonators.add(bomb);
                        this.bombs.add(bomb);
                    }
                } else if (player.getCountOfBombs() > 0){
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

    /**
     * Akadály lehelyezése.
     *
     * @param player    a játékos
     */
    public void placeGate(Player player){
        player.setCountOfGates(player.getCountOfGates()-1);

        double x = Math.round(player.x / 40) * 40;
        double y = Math.round(player.y / 40) * 40;
        this.gates.add(new Gate(x,y, player));
    }

    /**
     * Robbanás kezelése minden irányba.
     *
     * @param bombX     a bomba x koordinátája
     * @param bombY     a bomba y koordinátája
     * @param radius    a bomba robbanásának hatótávja
     */
    public void explosion(double bombX, double bombY, int radius){
        int toUp = 0;
        int toRight = 0;
        int toDown = 0;
        int toLeft = 0;
        if(!isPlayerOnBomb(bombX, bombY)){
            rightExplosion(bombX, bombY, radius, toRight);
            leftExplosion(bombX, bombY, radius, toLeft);
            upExplosion(bombX, bombY, radius, toUp);
            downExplosion(bombX, bombY, radius, toDown);
        }
    }

    /**
     * Ellenőrizzük, hogy a játékos bombán áll-e.
     *
     * @param bombX a bomba x koordinátája
     * @param bombY a bomba y koordinátája
     * @return      hogy a játékos bombán áll-e
     */
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

    /**
     * Kezeljük a bomba robbanását jobbra:
     * - van-e játékos, ha igen akkor a játékos meghal
     * - van-e szörny, ha igen akkor a szörny meghal
     * - van-e doboz vagy akadály, ha igen, akkor a robbanás nem terjed tovább
     *
     * @param bombX     a bomba x koordinátája
     * @param bombY     a bomba y koordinátája
     * @param radius    a bomba robbanásának hatótávja
     * @param iteration iterátor
     */
    private void rightExplosion(double bombX, double bombY, int radius, int iteration){
        if (!checkForWall(bombY, bombX, bombX+40, true)) {
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
                iterate = false;
            }
            if (iterate){
                if(iteration < radius-1){
                    iteration++;
                    rightExplosion(bombX+40, bombY, radius, iteration);
                }
            }
        }
    }

    /**
     * Kezeljük a bomba robbanását balra:
     * - van-e játékos, ha igen akkor a játékos meghal
     * - van-e szörny, ha igen akkor a szörny meghal
     * - van-e doboz vagy akadály, ha igen, akkor a robbanás nem terjed tovább
     *
     * @param bombX     a bomba x koordinátája
     * @param bombY     a bomba y koordinátája
     * @param radius    a bomba robbanásának hatótávja
     * @param iteration iterátor
     */
    private void leftExplosion(double bombX, double bombY, int radius, int iteration){
        if (!checkForWall(bombY, bombX-40, bombX, true)){
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
                iterate = false;
            }
            if (iterate){
                if(iteration < radius-1){
                    iteration++;
                    leftExplosion(bombX-40, bombY, radius, iteration);
                }
            }
        }
    }

    /**
     * Kezeljük a bomba robbanását felfelé:
     * - van-e játékos, ha igen akkor a játékos meghal
     * - van-e szörny, ha igen akkor a szörny meghal
     * - van-e doboz vagy akadály, ha igen, akkor a robbanás nem terjed tovább
     *
     * @param bombX     a bomba x koordinátája
     * @param bombY     a bomba y koordinátája
     * @param radius    a bomba robbanásának hatótávja
     * @param iteration iterátor
     */
    private void upExplosion(double bombX, double bombY, int radius, int iteration){
        if (checkForWall(bombX, bombY-40, bombY, false)){
        } else {
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
                iterate = false;
            }
            if(iterate){
                if(iteration < radius-1){
                    iteration++;
                    upExplosion(bombX, bombY-40, radius, iteration);
                }
            }
        }
    }

    /**
     * Kezeljük a bomba robbanását lefelé:
     * - van-e játékos, ha igen akkor a játékos meghal
     * - van-e szörny, ha igen akkor a szörny meghal
     * - van-e doboz vagy akadály, ha igen, akkor a robbanás nem terjed tovább
     *
     * @param bombX     a bomba x koordinátája
     * @param bombY     a bomba y koordinátája
     * @param radius    a bomba robbanásának hatótávja
     * @param iteration iterátor
     */
    private void downExplosion(double bombX, double bombY, int radius, int iteration){
        if (!checkForWall(bombX, bombY, bombY+40, false)){
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
                iterate = false;
            }
            if(iterate) {
                if(iteration < radius-1){
                    iteration++;
                    downExplosion(bombX, bombY+40, radius, iteration);
                }
            }
        }
    }

    /**
     * Robbanása hozzáadása a robbanások listájához.
     *
     * @param x a robbanás x koordinátája
     * @param y a robbanás y koordinátája
     */
    private void drawExposion(double x, double y){
        Explosion explosion = new Explosion(x, y);
        this.explosions.add(explosion);
        explosion.removeExplosion(this, 500);
    }

    /**
     * A játékos halálának kezelése.
     * Időzítő indítása, ilyenkor a másik játékosnak 5 másodpercig kell életben maradnia.
     * Ha ezen idő alatt meghal a másik játékos akkor a játszma döntetlen eredménnyel zárul.
     * Az 5 másodperc leteltével, vagy az döntetlen eredmény után megjelenik egy ablak, ahol
     * kiírjuk a győztes játékos, vagy a döntetlen eredmény.
     * Ebből az ablakból navigálhatunk tovább. (Új játék, Játék bezárása)
     *
     *
     * @param player a játékos
     */
    public void playerDeath(Player player){
        if(!player.hasPowerUp(PowerUpType.SHIELD)){
            this.players.remove(player);
            if (this.players.size() == 1) {
                if (igc != null) {
                    lastPlayerTimeline = new Timeline();
                    igc.timerLabel.setTextFill(Color.RED);
                    lastPlayerTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                        int second = 0;
                        @Override
                        public void handle(ActionEvent event) {
                            second++;

                            if (second == 5) {
                                // Ha elértük a maximális iterációt, leállítjuk a timeline-ot
                                lastPlayerTimeline.stop();
                                lastPlayerTimeline = null;
                                stopTimers();
                                new WinStage(GameModel.this);
                            }
                        }
                    }));

                    // A timeline egy végtelen ciklusban fog futni
                    lastPlayerTimeline.setCycleCount(5);
                    lastPlayerTimeline.play();
                }
            }

            if (this.players.isEmpty()) {
                lastPlayerTimeline.stop();
                lastPlayerTimeline = null;
                stopTimers();
                new WinStage(GameModel.this);
            }
        }
    }

    /**
     * Robbanás következtében megnézzük, hogy áll-e valamelyik játékos a robbanás útjában.
     *
     * @param same          az azonos tengely (x vagy y) koordinátája
     * @param smaller       a robbanás kezdeti koordinátája
     * @param bigger        a robbanás végső koordinátája
     * @param isHorizontal  y tengelyen van-e a robbanás
     * @return              a meghalt játékos sorszámát, vagy -1-et ha nem halt meg egyikőjük sem
     */
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

    /**
     * Robbanás következtében megnézzük, hogy áll-e fal az útjában.
     *
     * @param same          az azonos tengely (x vagy y) koordinátája
     * @param smaller       a robbanás kezdeti koordinátája
     * @param bigger        a robbanás végső koordinátája
     * @param horizontal    y tengelyen van-e a robbanás
     * @return              hogy van-e ott fal
     */
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

    /**
     * Robbanás következtében megnézzük, hogy áll-e szörny az útjában.
     *
     * @param expX  a robbanás x koordinátája
     * @param expY  a robbanás y koordinátája
     * @return      hogy van-e ott szörny
     */
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

    /**
     * Robbanás következtében megnézzük, hogy áll-e doboz az útjában.
     *
     * @param expX  a robbanás x koordinátája
     * @param expY  a robbanás y koordinátája
     * @return      hogy van-e ott doboz
     */
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

    /**
     * Robbanás következtében megnézzük, hogy áll-e gát az útjában.
     *
     * @param expX  a robbanás x koordinátája
     * @param expY  a robbanás y koordinátája
     * @return      hogy van-e ott gát
     */
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

    /**
     * Ha a játékosnak azonnali bomba lehelyezése bónusza van, akkor lerakja a bombát.
     */
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

    /**
     * Vizsgáljuk, hogy egy megadott érték két másik érték között van-e.
     *
     * @param value     a vizsgált érték
     * @param smaller   a kisebb érték
     * @param bigger    a nagyobb érték
     * @return          hogy a megadott érték a két másik érték között van-e
     */
    public boolean isBetween(double value, double smaller, double bigger){
        return smaller <= value && bigger >= value;
    }

    /**
     * Két téglalap alapú alakzat fedi-e egymást.
     * A két téglalapnak csak az egyik csúcsának koordinátáit adjuk meg.
     *
     * @param x1    1. alakzat x koordinátája
     * @param y1    1. alakzat y koordinátája
     * @param x2    2. alakzat x koordinátája
     * @param y2    2. alakzat y koordinátája
     *
     * @return      hogy a két alakzat fedi-e egymást
     */
    public boolean checkInteraction(double x1, double y1, double x2, double y2) {
        int SIZE = 40;
        if (x1 + SIZE - 1 < x2 || x2 + SIZE - 1 < x1 || y1 + SIZE - 1 < y2 || y2 + SIZE - 1 < y1) {
            return false;
        }
        return true;
    }

    /**
     * Játékos objektum megkeresése id alapján.
     *
     * @param playerId  a keresett id
     * @return          a megtalált játékos objektum
     */
    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.id == playerId) return player;
        }
        return null;
    }

    /**
     * A játék időzítőjének leállítása.
     */
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

    /**
     * A játék időzítőjének elindítása.
     */
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

    //NOUSAGE
    private boolean isWall(int x, int y) {
        for (Wall wall : walls) {
            if (checkInteraction(x*40, y*40, wall.x, wall.y)) {
                this.walls.remove(wall);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private boolean isPlayer(int x, int y) {
        for (Player player : players) {
            if (checkInteraction(x*40, y*40, player.x, player.y)) {
                this.players.remove(player);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private boolean isMonster(int x, int y) {
        for (Monster monster : monsters) {
            if (checkInteraction(x*40, y*40, monster.x, monster.y)) {
                this.monsters.remove(monster);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private boolean isBomb(int x, int y) {
        for (Bomb bomb : bombs) {
            if (checkInteraction(x*40, y*40, bomb.x, bomb.y)) {
                this.bombs.remove(bomb);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private boolean isBox(int x, int y) {
        for (Box box : boxes) {
            if (checkInteraction(x*40, y*40, box.x, box.y)) {
                this.boxes.remove(box);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private boolean isPowerUp(int x, int y) {
        for (PowerUp powerUp : powerUps) {
            if (checkInteraction(x*40, y*40, powerUp.x, powerUp.y)) {
                this.powerUps.remove(powerUp);
                return true;
            }
        }
        return false;
    }

    //NOUSAGE
    private void isGate(int x, int y) {
        for (Gate gate : gates) {
            if (checkInteraction(x*40, y*40, gate.x, gate.y)) {
                this.gates.remove(gate);
                break;
            }
        }
    }

    //NOUSAGE
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

    /**
     * A BattleRoyale mód logikája.
     */
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

    /**
     * Elemek eltávolítása egy oszlopban vagy sorban.
     * A terület megállapítása után meghívjuk az összes típusú elemre az eltávolítást.
     *
     * @param from          a sor/oszlop kezdőpontja
     * @param to            a sor/oszlop végpontja
     * @param same1         a sor/oszlop szélességének kezdőpontja
     * @param same2         a sor/oszlop szélességének kezdőpontja
     * @param isHorizontal  horizontálisan nézzük-e az elemeket
     */
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

    /**
     * Az elemek tényleges eltávolítása.
     *
     * @param iterator      adott elem típusú iterátor
     * @param from          a sor/oszlop kezdőpontja
     * @param to            a sor/oszlop végpontja
     * @param same1         a sor/oszlop szélességének kezdőpontja
     * @param same2         a sor/oszlop szélességének kezdőpontja
     * @param isHorizontal  horizontálisan nézzük-e az elemeket
     * @param <T>           az adott elem típusa
     */
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
