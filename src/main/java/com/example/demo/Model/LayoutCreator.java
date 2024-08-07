package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import java.util.Random;

public class LayoutCreator {
    private final GameModel gm;
    private final String map;
    public InGameController igc;

    /**
     * Létrehozzuk, és beállítjuk az egyes entitások modelljét.
     *
     * @param gm    a játék modelljének átadása
     * @param map   a pálya típusa
     * @param igc   az InGameController
     */
    public LayoutCreator(GameModel gm, String map, InGameController igc){
        this.gm = gm;
        this.map = map;
        this.igc = igc;

        createWalls();
        setUpPlayers();
        createMonsters();
        createBoxes();
        createPowerUps();
    }

    /**
     * Létrehozza, és beállítja a bónuszokat, ügyelve arra, hogy doboz alatt legyen.
     */
    public void createPowerUps(){
        int counter = 0;
        int numOfPowerUps = 4;
        while(counter < numOfPowerUps){
            boolean exists = false;
            Random random = new Random();

            int index = random.nextInt(gm.boxes.size());
            double x = gm.boxes.get(index).x;
            double y = gm.boxes.get(index).y;

            for(PowerUp powerUp : gm.powerUps){
                if(x == powerUp.x && y == powerUp.y){
                    exists = true;
                }
            }
            if(!exists){
                int typeIndex = random.nextInt(PowerUpType.values().length);
                PowerUpType type = PowerUpType.values()[typeIndex];
                gm.powerUps.add(new PowerUp(x, y, type, igc));
                counter++;
            }
        }
    }

    /**
     * Létrehozza, és beállítja a dobozokat a pálya típusának megfelelően.
     */
    private void createBoxes(){
        switch(map){
            case "Dzsungel":
                for (int i = 1; i <= 11; i++) {
                    gm.boxes.add(new Box(i*40, 5*40));
                }
                break;
            case "Pokol":
                for (int i = 3; i < 11; i+=2) {
                    gm.boxes.add(new Box(i*40, 2*40));
                    gm.boxes.add(new Box(i*40, 3*40));
                    gm.boxes.add(new Box(i*40, 7*40));
                    gm.boxes.add(new Box(i*40, 8*40));
                }
                break;
            default:
                for (int i = 1; i < 9; i++) {
                    gm.boxes.add(new Box(5*40, (i+1)*40));
                    gm.boxes.add(new Box(7*40, i *40));
                }
        }
    }

    /**
     * Létrehozza, és beállítja a falakat a pálya típusának megfelelően.
     */
    private void createWalls() {
        for (int i = 0; i < 13; i++) { //felső és alsó sor
            gm.edgeWalls.add(new EdgeWall(i*40, 0));
            gm.edgeWalls.add(new EdgeWall(i*40,40*10));
        }
        for (int i = 1; i < 10; i++) { //bal és jobb oszlop
            gm.edgeWalls.add(new EdgeWall(0, i*40));
            gm.edgeWalls.add(new EdgeWall(12*40,i*40));
        }

        switch(map){
            case "Dzsungel":
                createDzsungel();
                break;
            case "Pokol":
                createPokol();
                break;
            default:
                createVadnyugat();
        }
    }

    /**
     * Létrehozza, és beállítja a falakat a Dzsungel pályán.
     */
    private void createDzsungel(){
        for (int i = 2; i < 11; i+=2) {
            for (int j = 2; j < 9; j+=2) {
                gm.walls.add(new Wall(i*40, j*40));
            }
        }
    }

    /**
     * Létrehozza, és beállítja a falakat a Pokol pályán.
     */
    private void createPokol(){
        for (int i = 2; i < 11; i+=2) {
            gm.walls.add(new Wall(i*40, 2*40));
            gm.walls.add(new Wall(i*40, 3*40));
            gm.walls.add(new Wall(i*40, 7*40));
            gm.walls.add(new Wall(i*40, 8*40));
        }
        for (int i = 1; i < 12; i+=2) {
            gm.walls.add(new Wall(i*40, 5*40));
        }
    }

    /**
     * Létrehozza, és beállítja a falakat a Vadnyugat pályán.
     */
    private void createVadnyugat(){
        for (int i = 1; i < 9; i++) {
            gm.walls.add(new Wall(3*40, i*40));
            gm.walls.add(new Wall(9*40, (i+1) *40));
        }
    }

    /**
     * Létrehozza, és beállítja a két játékost.
     */
    private void setUpPlayers() {
        Player player1 = new Player(40,40, 0, true);
        Player player2 = new Player(440,360, 1, false);
        player1.addBomb();
        player2.addBomb();

        gm.players.add(player1);
        gm.players.add(player2);
    }

    /**
     * Létrehozza, és beállítja a szörnyeket.
     */
    private void createMonsters() {
        Monster monster = new Monster(40*4, 40, true, 0);
        gm.monsters.add(monster);
        monster = new Monster(40*7, 40*9, true, 1);
        gm.monsters.add(monster);
    }
}
