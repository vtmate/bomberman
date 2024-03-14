package com.example.demo.Model;

import java.util.Timer;

public class Bomb extends Entity{
    private final int radius;
    private Timer timer;
    public Bomb(double x, double y, int radius) {
        super(x, y);
        this.radius = radius;
    }
    public int getRadius(){ return radius; }
}
