package com.example.demo.Model;

import java.util.ArrayList;

public class Box extends Entity{
    public Box(double x, double y) {
        super(x, y);
    }

    /**
     * Ha szellem a játékos, akkor rá tud menni a dobozokra,
     * ezért meg kell nézni, hogy egy adott powerUp felett van-e.
     * doboz (,hogy így ne tudja felvenni)
     * @param boxes     a játékban található aktív dobozok
     * @param x         a powerUp x koordinátája
     * @param y         a powerUp y koordinátája
     * @return          hogy van-e a powerUpon aktív doboz
     */
    public static boolean hasBoxOnTop(ArrayList<Box> boxes, double x, double y){
        for(Box box : boxes){
            if(box.x == x && box.y == y){
                return true;
            }
        }
        return false;
    }
}
