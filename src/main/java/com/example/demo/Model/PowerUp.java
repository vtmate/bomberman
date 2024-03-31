package com.example.demo.Model;

import java.util.Timer;
import java.util.TimerTask;

public class PowerUp extends Entity{

    private final PowerUpType powerUpType;
    private static final Timer timer = new Timer();
    private static final int milliSeconds = 8000;
    public PowerUp(double x, double y, PowerUpType powerUpType) {
        super(x, y);
        this.powerUpType = powerUpType;
    }
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public static void checkForPowerUp(double x, double y, Player player, GameModel gm){
        //ez minden egyes lépésnél lefut
        for(PowerUp powerUp : gm.powerUps){
            if (gm.checkInteraction(x, y, powerUp.x, powerUp.y )){
                //ez csak akkor, ha felszedtünk egy powerUp-ot
                System.out.println(powerUp.getPowerUpType());
                player.addPowerUp(powerUp);
                gm.powerUps.remove(powerUp);

                switch (powerUp.powerUpType){
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
                }
                return;
            }
        }
    }

    private static void isSnail(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.ROLLERSKATE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isRollerskate(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.SNAIL);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isSmallerRadius(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.BIGGERRADIUS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isBiggerRadius(Player player, PowerUp powerUp) {
        player.removePowerUpByType(PowerUpType.SMALLERRADIUS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isNoBombs(Player player, PowerUp powerUp) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isShield(Player player, PowerUp powerUp) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isImmadiate(Player player, PowerUp powerUp) {
        if(player.hasPowerUp(PowerUpType.DETONATOR)){
            player.removePowerUpByType(PowerUpType.DETONATOR);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.removePowerUp(powerUp);
            }
        }, milliSeconds);
    }
    private static void isGate(Player player){
        player.setCountOfGates(player.getCountOfGates()+3);
    }
    private static void isDetonator(Player player){
        if(player.hasPowerUp(PowerUpType.IMMADIATEBOMB)){
            player.removePowerUpByType(PowerUpType.IMMADIATEBOMB);
        }
    }
    private static void isMoreBombs(Player player){
        if(player.getCountOfBombs() <= 3){ //maximum három bombája lehet a játékosnak
            player.addBomb();
        }
    }
}
