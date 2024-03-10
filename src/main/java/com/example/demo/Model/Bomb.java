package com.example.demo.Model;

import java.util.Timer;

public class Bomb extends Entity{
    private int radius;
    private Timer timer;
    public Bomb(double x, double y) {
        super(x, y);
    }
}
