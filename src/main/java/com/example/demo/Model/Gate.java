package com.example.demo.Model;

public class Gate extends Entity{
    public Player owner;
    public Gate(double x, double y, Player owner) {
        super(x, y);
        this.owner = owner;
    }
}
