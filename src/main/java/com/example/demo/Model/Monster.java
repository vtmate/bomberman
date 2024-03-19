package com.example.demo.Model;

import com.example.demo.Controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

public class Monster extends Entity{
    public String direction = "RIGHT";
    private Timeline monsterTimeline;
    public Monster(double x, double y) {
        super(x, y);
    }
    public void moveMonster(Control control){
        System.out.println("run");
        monsterTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {
                    if (this.x % 40 == 0 && this.y % 40 == 0) {
                        Random rand = new Random();
                        if (rand.nextInt(5) == 2) control.changeDirection(this, this.direction);
                    }
                    if(control.monsterIntersectsEntity(this, this.direction)) {
                        switch(this.direction){
                            case "UP" -> this.y -= 1;
                            case "DOWN" -> this.y += 1;
                            case "LEFT" -> this.x -= 1;
                            case "RIGHT" -> this.x += 1;
                        }
                    }
                    else {
                        control.changeDirection(this, this.direction);
                    }
                })
        );
        monsterTimeline.setCycleCount(Timeline.INDEFINITE);
        monsterTimeline.play();
    }

    public void stop(){
        monsterTimeline.stop();
    }
}
