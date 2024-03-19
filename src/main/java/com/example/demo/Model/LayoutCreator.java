package com.example.demo.Model;

public class LayoutCreator {
    private final GameModel gm;
    private final int layoutId;
    public LayoutCreator(GameModel gm, int layoutId){
        this.gm = gm;
        this.layoutId = layoutId;
        //layoutId alapján hozzuk létre a különböző pályákat

        createBorder();
        setUpPlayers();
        createMonsters();
    }

    private void createBorder() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 11; j++) {
                if ( i == 0 || i == 12) {
                    Wall wall = new Wall(i*40, j*40);
                    gm.walls.add(wall);
                } else {
                    if ( j == 0 || j == 10) {
                        Wall wall = new Wall(i*40, j*40);
                        gm.walls.add(wall);
                    }
                    else {
                        if ( i % 2 == 0 && j % 2 == 0) {
                            Wall wall = new Wall(i*40, j*40);
                            gm.walls.add(wall);
                        }
                    }
                }
            }
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
