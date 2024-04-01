package com.example.demo.Model;

import java.util.ArrayList;

public class Box extends Entity{
    public Box(double x, double y) {
        super(x, y);
    }
    public static boolean hasBoxOnTop(ArrayList<Box> boxes, double x, double y){
        for(Box box : boxes){
            if(box.x == x && box.y == y){
                return true;
            }
        }
        return false;
    }
}
