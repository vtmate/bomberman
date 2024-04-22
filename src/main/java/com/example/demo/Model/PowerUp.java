package com.example.demo.Model;

import com.example.demo.Controller.InGameController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class PowerUp extends Entity{
    private final PowerUpType powerUpType;
    private final Timer timer = new Timer();
    private final int milliSeconds = 8000;
    public Image image;
    public ImageView iw;

    /**
     *
     * @param x             a bónusz x koordinátája
     * @param y             a bónusz y koordinátája
     * @param powerUpType   a bónusz típusa
     * @param igc           InGameController átadása
     */
    public PowerUp(double x, double y, PowerUpType powerUpType, InGameController igc) {
        super(x, y);
        this.powerUpType = powerUpType;
        if (igc != null) {
            image = new Image(getPowerUpImage(powerUpType));
            iw = new ImageView(image);
            iw.setFitWidth(25);
            iw.setFitHeight(25);
        }
    }

    /**
     * A bónusz lejárta előtti utolsó 3 másodpercben a bónusz ikonja félig átlátszó lesz,
     * így jelezve, hogy hamarosan lejár a hatása.
     */
    public void timeIsDown(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                iw.setOpacity(0.3);
            }
        }, milliSeconds-3000);
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    /**
     *
     * @param pt    a bónusz típusa
     * @return      hogy mi a bónusz típusához tartozó kép
     */
    private String getPowerUpImage(PowerUpType pt) {
        return switch (pt) {
            case PowerUpType.GATE -> "gatePowerUp.png";
            case PowerUpType.MOREBOMBS -> "morebombsPowerUp.png";
            case PowerUpType.BIGGERRADIUS -> "biggerRadiusP.png";
            case PowerUpType.SNAIL -> "snailPowerUp.png";
            case PowerUpType.SMALLERRADIUS -> "smallerRadiusP.png";
            case PowerUpType.NOBOMBS -> "noBombs.png";
            case PowerUpType.IMMADIATEBOMB -> "immadiate.png";
            case PowerUpType.DETONATOR -> "detonator.png";
            case PowerUpType.ROLLERSKATE -> "rollerskateskaterPowerUp.png";
            case PowerUpType.SHIELD -> "shieldPowerUp.png";
            case PowerUpType.GHOST -> "ghostPowerUp.png";
        };
    }

    /**
     * Bónusz kezelése, mikor a játékos felveszi.
     *
     * @param powerUp   a bónusz
     * @param player    a játékos, aki felvette
     * @param gm        a játék modellje
     */
    public void handlePowerUp(PowerUp powerUp, Player player, GameModel gm){

        player.addPowerUp(powerUp);
        gm.powerUps.remove(powerUp);

        switch (this.powerUpType){
            case PowerUpType.MOREBOMBS:
                isMoreBombs(player);
            break;
            case PowerUpType.NOBOMBS:
                isNoBombs(player, powerUp);
            break;
            case PowerUpType.BIGGERRADIUS:
                isBiggerRadius(player, powerUp);
            break;
            case PowerUpType.SMALLERRADIUS:
                isSmallerRadius(player, powerUp);
            break;
            case PowerUpType.ROLLERSKATE:
                isRollerskate(player, powerUp);
            break;
            case PowerUpType.SNAIL:
                isSnail(player, powerUp);
            break;
            case PowerUpType.GATE:
                isGate(player);
            case PowerUpType.IMMADIATEBOMB:
                isImmadiate(player, powerUp);
            break;
            case PowerUpType.DETONATOR:
                isDetonator(player);
            break;
            case PowerUpType.SHIELD:
                isShield(player, powerUp);
            break;
            case PowerUpType.GHOST:
                isGhost(player, powerUp, gm);
            break;
        }
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ütköző bónusz törlése: ROLLERSKATE
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    public void isSnail(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.ROLLERSKATE);
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ütköző bónusz törlése: SNAIL
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isRollerskate(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.SNAIL);
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ütköző bónusz törlése: BIGGERRADIUS
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isSmallerRadius(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.BIGGERRADIUS);
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ütköző bónusz törlése: SMALLERRADIUS
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isBiggerRadius(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.SMALLERRADIUS);
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isNoBombs(Player player, PowerUp powerUp) {
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isShield(Player player, PowerUp powerUp) {
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ütköző bónusz törlése: DETONATOR
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     */
    private void isImmadiate(Player player, PowerUp powerUp) {
        if(player.hasPowerUp(PowerUpType.DETONATOR)){
            player.removePowerUpByType(PowerUpType.DETONATOR);
        }
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }

    /**
     * Akadály elem lehelyezésszámának növelése.
     *
     * @param player    a játékos
     */
    private void isGate(Player player){
        player.setCountOfGates(player.getCountOfGates()+3);
    }

    /**
     * Ütköző bónusz törlése: IMMADIATEBOMB
     * @param player    a játékos
     */
    private void isDetonator(Player player){
        if(player.hasPowerUp(PowerUpType.IMMADIATEBOMB)){
            player.removePowerUpByType(PowerUpType.IMMADIATEBOMB);
        }
    }

    /**
     * Időzítő beállítása a bónuszhoz, majd a lejártával a bónusz törlése.
     * Ha a játékos falon vagy dobozon veszti el a képességét, akkor meghal.
     *
     * @param player    a játékos
     * @param powerUp   a bónusz
     * @param gm        a játék modellje
     */
    private void isGhost(Player player, PowerUp powerUp, GameModel gm) {
        timeIsDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
                for(Wall wall : gm.walls){
                    if(gm.checkInteraction(player.x, player.y, wall.x, wall.y)) gm.playerDeath(player);
                }
                for(EdgeWall edgeWall : gm.edgeWalls) {
                    if (gm.checkInteraction(player.x, player.y, edgeWall.x, edgeWall.y)) gm.playerDeath(player);
                }
                for(Box box : gm.boxes){
                    if(gm.checkInteraction(player.x, player.y, box.x, box.y)) gm.playerDeath(player);
                }
            }
        }, milliSeconds);
    }

    /**
     * Növeljük a játékos lehelyezhető bombáinak számát, maximális érték a 3 lehet.
     *
     * @param player    a játékos
     */
    private void isMoreBombs(Player player){
        if(player.getCountOfBombs() <= 3){
            player.addBomb();
        }
    }
}
