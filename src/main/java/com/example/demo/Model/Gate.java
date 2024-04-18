package com.example.demo.Model;

public class Gate extends Entity{
    public Player owner;

    /**
     * Akadály létrehozása.
     *
     * @param x     az akadály x koordinátája
     * @param y     az akadály y koordinátája
     * @param owner az akadály tulajdonosa
     */
    public Gate(double x, double y, Player owner) {
        super(x, y);
        this.owner = owner;
    }
}
