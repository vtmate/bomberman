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
}
