package com.example.demo.Model;

import java.util.Random;

public class LayoutCreator {
    private final GameModel gm;
    private final String map;
    public LayoutCreator(GameModel gm, String map){
        this.gm = gm;
        this.map = map;
        System.out.println("map: " + map);

        createWalls();
        setUpPlayers();
        createMonsters();
        createBoxes();
        createPowerUps();
    }

    private void createPowerUps(){
        System.out.println("createpowerups");
        int counter = 0;
        int numOfPowerUps = 4;
        while(counter < numOfPowerUps){
            boolean exists = false;
            Random random = new Random();

            //random doboz kiválasztása
            int index = random.nextInt(gm.boxes.size());
            double x = gm.boxes.get(index).x;
            double y = gm.boxes.get(index).y;

            for(PowerUp powerUp : gm.powerUps){
                if(x == powerUp.x && y == powerUp.y){
                    exists = true; //ha már az adott doboz alatt van powerUp
                }
            }
            if(!exists){
                //random powerUp kiválasztása
                int typeIndex = random.nextInt(PowerUpType.values().length);
                PowerUpType type = PowerUpType.values()[typeIndex];
                gm.powerUps.add(new PowerUp(x, y, type));
                counter++;
            }
        }

        //teszt powerup
        gm.powerUps.add(new PowerUp(8*40, 9*40, PowerUpType.IMMADIATEBOMB));
        gm.powerUps.add(new PowerUp(9*40, 9*40, PowerUpType.MOREBOMBS));
        gm.powerUps.add(new PowerUp(10*40, 9*40, PowerUpType.GHOST));
    }

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

    private void createWalls() {

        for (int i = 0; i < 13; i++) { //felső és alsó sor
            gm.edgeWalls.add(new EdgeWall(i*40, 0));
            gm.edgeWalls.add(new EdgeWall(i*40,40*10));
        }
        for (int i = 1; i < 11; i++) { //bal és jobb oszlop
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

    private void createDzsungel(){
        for (int i = 2; i < 11; i+=2) {
            for (int j = 2; j < 9; j+=2) {
                gm.walls.add(new Wall(i*40, j*40));
            }
        }
    }
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
    private void createVadnyugat(){
        for (int i = 1; i < 9; i++) {
            gm.walls.add(new Wall(3*40, i*40));
            gm.walls.add(new Wall(9*40, (i+1) *40));
        }

    }

    private void setUpPlayers() {
        Player player1 = new Player(40,40, 0);
        Player player2 = new Player(440,360, 1);
        player1.addBomb();
        player2.addBomb();

        //powerUp-ok kipróbálása:
        //PowerUp p = new PowerUp(0,0,PowerUpType.ROLLERSKATE);
        //PowerUp p2 = new PowerUp(0,0,PowerUpType.SNAIL);
        //player1.addPowerUp(p2);
        //player2.addPowerUp(new PowerUp(0,0, PowerUpType.SMALLERRADIUS));
        //player2.addPowerUp(new PowerUp(0,0, PowerUpType.IMMADIATEBOMB));

        gm.players.add(player1);
        gm.players.add(player2);
    }
    private void createMonsters() {
        Monster monster = new Monster(40*4, 40);
        gm.monsters.add(monster);
        monster = new Monster(40*7, 40*4);
        gm.monsters.add(monster);
    }
}
