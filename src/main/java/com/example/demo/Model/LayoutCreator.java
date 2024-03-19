package com.example.demo.Model;

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
    }

    private void createWalls() {

        for (int i = 0; i < 13; i++) { //felső és alsó sor
            gm.walls.add(new Wall(i*40, 0));
            gm.walls.add(new Wall(i*40,40*10));
        }
        for (int i = 1; i < 11; i++) { //bal és jobb oszlop
            gm.walls.add(new Wall(0, i*40));
            gm.walls.add(new Wall(12*40,i*40));
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
        player2.addBomb();
        player2.addBomb();
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
