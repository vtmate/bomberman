package com.example.demo.Model;

import java.util.ArrayList;

public class Player extends Entity{
    private String name;
    private int countOfBombs;
    private final ArrayList<PowerUp> powerUps;
    public Player(double x, double y) {
        super(x, y);
        this.powerUps = new ArrayList<PowerUp>();
    }
    public int getCountOfBombs() {
        return countOfBombs;
    }
    public void addBomb() { countOfBombs++; }
    public void removeBomb() {countOfBombs--; }
    public ArrayList<PowerUp> getPowerUps(){
        return powerUps;
    }
    public void addPowerUp(PowerUp powerUp){
        powerUps.add(powerUp);
    }
}
