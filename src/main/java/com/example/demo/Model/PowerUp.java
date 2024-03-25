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

                if(powerUp.powerUpType == PowerUpType.MOREBOMBS){
                    if(player.getCountOfBombs() <= 3){ //maximum három bombája lehet a játékosnak
                        player.addBomb();
                    }
                }

                if(powerUp.powerUpType == PowerUpType.NOBOMBS){
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("powerup kiszedve");
                            player.removePowerUp(powerUp);
                        }
                    }, milliSeconds);
                }
                return;
            }
        }
    }


}
