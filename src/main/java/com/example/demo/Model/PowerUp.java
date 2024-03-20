package com.example.demo.Model;

public class PowerUp extends Entity{

    private final PowerUpType powerUpType;
    public PowerUp(double x, double y, PowerUpType powerUpType) {
        super(x, y);
        this.powerUpType = powerUpType;
    }
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public static void checkForPowerUp(double x, double y, Player player, GameModel gm){
        for(PowerUp powerUp : gm.powerUps){
            if (gm.checkInteraction(x, y, powerUp.x, powerUp.y )){
                System.out.println(powerUp.getPowerUpType());
                player.addPowerUp(powerUp);
                gm.powerUps.remove(powerUp);

                if(powerUp.powerUpType == PowerUpType.MOREBOMBS){
                    player.addBomb();
                }

                if(powerUp.powerUpType == PowerUpType.NOBOMBS){
                    System.out.println("NOOOOBOOOMBBBBBS");
                    player.setBombs(0);
                }
                return;
            }
        }
    }


}
