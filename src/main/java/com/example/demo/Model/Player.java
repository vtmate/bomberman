package com.example.demo.Model;

public class Player extends Entity{
    private String name;
    private int countOfBombs;
    public Player(double x, double y) {
        super(x, y);
    }
    public int getCountOfBombs() {
        return countOfBombs;
    }
    public void setCountOfBombs() {
        countOfBombs++;
    }
}
