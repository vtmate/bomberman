package com.example.demo.Model;

import java.util.ArrayList;

public class Player extends Entity{
    private String name;
    public int id;
    private int countOfBombs;
    private final ArrayList<PowerUp> powerUps;
    public Player(double x, double y, int id) {
        super(x, y);
        this.id = id;
        this.powerUps = new ArrayList<PowerUp>();
    }
    public int getCountOfBombs() {
        return countOfBombs;
    }
    public void addBomb() { countOfBombs++; }
    public void removeBomb() {countOfBombs--; }
    public void setBombs(int count) {countOfBombs = count;}
    public ArrayList<PowerUp> getPowerUps(){
        return powerUps;
    }
    public void addPowerUp(PowerUp powerUp){
        powerUps.add(powerUp);
    }
    public void removePowerUp(PowerUp powerUp){ powerUps.remove(powerUp); }
    public boolean hasPowerUp(PowerUpType powerUpType){
        for (PowerUp playerPowerUp : getPowerUps()) {
            if (playerPowerUp.getPowerUpType() == powerUpType){
                return true;
            }
        }
        return false;
    }
}
