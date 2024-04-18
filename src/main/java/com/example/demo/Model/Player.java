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
    public Timeline timeline;
    public boolean isRight;

    /**
     *
     * @param x         a játékos x koordinátája
     * @param y         a játékos y koordinátája
     * @param id        a játékos egyedi azonosítója
     * @param isRight   a játékos jobb irányba néz-e
     */
    public Player(double x, double y, int id, boolean isRight) {
        super(x, y);
        this.id = id;
        this.powerUps = new ArrayList<PowerUp>();
        this.placedDetonators = new ArrayList<>();
        this.isRight = isRight;
    }
    public int getCountOfBombs() {
        return countOfBombs;
    }

    /**
     * Lehelyezhető bombák számának növelése.
     */
    public void addBomb() { countOfBombs++; }

    /**
     * Lehelyezhető bombák számának csökkentése.
     */
    public void removeBomb() {countOfBombs--; }
    public void setCountOfGates(int count) {
        countOfGates = count;
    }
    public int getCountOfGates() {
        return countOfGates;
    }
    public ArrayList<PowerUp> getPowerUps(){
        return powerUps;
    }

    /**
     * Bónusz hozzáadása a játékoshoz.
     *
     * @param powerUp   a bónusz
     */
    public void addPowerUp(PowerUp powerUp){
        powerUps.add(powerUp);
    }

    /**
     * Bónusz elvétele a játékostól.
     *
     * @param powerUp   a bónusz
     */
    public void removePowerUp(PowerUp powerUp){ powerUps.remove(powerUp); }
    public void removePowerUpByType(PowerUpType type){
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp.getPowerUpType() == type) {
                iterator.remove();
            }
        }
    }

    /**
     * Visszaadja, hogy van-e a paraméterben megadott típusú bónusza a játékosnak.
     *
     * @param powerUpType   a bónusz típusa
     * @return              hogy van-e a megadott típusú bónusza a játékosnak
     */
    public boolean hasPowerUp(PowerUpType powerUpType){
        for (PowerUp playerPowerUp : getPowerUps()) {
            if (playerPowerUp.getPowerUpType() == powerUpType){
                return true;
            }
        }
        return false;
    }

    /**
     * Megadja, hogy hány bombája lehet a játékosnak.
     *
     * @return hogy hány bombája van a játékosnak
     */
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

    /**
     * A játékos mozgása
     *
     * @param moveX     a mozgás horizontális mértéke
     * @param moveY     a mozgás vertikális mértéke
     * @param iteration a mozgásmérték változtatásának száma
     */
    public void move(int moveX, int moveY, int iteration) {
        timeline = new Timeline();
        timeline.stop();
        timeline.getKeyFrames().clear();

        count = 0;

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                x += moveX;
                y += moveY;
                count++;

                if (count == iteration) {
                    timeline.stop();
                    isMoving = false;
                    timeline = null;
                }
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        isMoving = true;
    }

    /**
     * Timeline leállítása.
     */
    public void pause() {
        if (timeline == null) return;
        timeline.pause();
    }

    /**
     * Timeline újraindítása.
     */
    public void resume() {
        if (timeline == null) return;
        timeline.play();
    }
}
