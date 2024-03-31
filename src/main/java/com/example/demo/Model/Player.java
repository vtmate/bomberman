package com.example.demo.Model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;


public class Player extends Entity{
    public String name;
    private int count = 0;
    public int id;
    private int countOfBombs;
    private int countOfGates = 0;
    public ArrayList<Bomb> placedDetonators;
    private final ArrayList<PowerUp> powerUps;
    public boolean isMoving;
    public Timeline timeline; //timecontrol
    public Player(double x, double y, int id) {
        super(x, y);
        this.id = id;
        this.powerUps = new ArrayList<PowerUp>();
        this.placedDetonators = new ArrayList<>();
    }
    public int getCountOfBombs() {
        return countOfBombs;
    }
    public void addBomb() { countOfBombs++; }
    public void removeBomb() {countOfBombs--; }
    public void setBombs(int count) {countOfBombs = count;}
    public void setCountOfGates(int count) {countOfGates = count;}
    public int getCountOfGates() {return countOfGates;}
    public ArrayList<PowerUp> getPowerUps(){
        return powerUps;
    }
    public void addPowerUp(PowerUp powerUp){
        powerUps.add(powerUp);
    }
    public void removePowerUp(PowerUp powerUp){ powerUps.remove(powerUp); }
    public void removePowerUpByType(PowerUpType type){
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp.getPowerUpType() == type) {
                iterator.remove(); // Use the iterator's remove() method to safely remove the element
            }
        }

    }
    public boolean hasPowerUp(PowerUpType powerUpType){
        for (PowerUp playerPowerUp : getPowerUps()) {
            if (playerPowerUp.getPowerUpType() == powerUpType){
                return true;
            }
        }
        return false;
    }
    public int numOfAllBombs(){ //a countOfBombs-ba nem szeretnék belenyúlni, de az folyamatosan változik
                                //detonarorhoz szükségem van az össz bombaszámra
        int count = 1;
        for (PowerUp playerPowerUp : getPowerUps()) {
            if (playerPowerUp.getPowerUpType() == PowerUpType.MOREBOMBS){
                count++;
            }
        }
        return count;
    }

    public void moveMove(int moveX, int moveY, int iteration) {
        timeline = new Timeline();


        timeline.stop(); // Leállítjuk a timeline-ot, ha éppen fut

        timeline.getKeyFrames().clear(); // Töröljük az eseményeket a timeline-ból

        count = 0;

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {

                x += moveX;
                y += moveY;
                count++;

                if (count == iteration) {
                    timeline.stop(); // Ha elértük a maximális iterációt, leállítjuk a timeline-ot
                    isMoving = false;
                    timeline = null;
                }
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE); // A timeline egy végtelen ciklusban fog futni
        timeline.play(); // Elindítjuk a timeline-ot
        isMoving = true;
    }

    public void pause() {
        if (timeline == null) return;
        timeline.pause();
    }

    public void resume() {
        if (timeline == null) return;
        timeline.play();
    }
}
