package com.example.demo.Model;

import com.example.demo.Controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

public class Monster extends Entity{
    public String direction = "RIGHT";
    private Timeline monsterTimeline;
    public boolean isRight;
    public int id;
    public Monster(double x, double y, boolean isRight, int id) {
        super(x, y);
        this.isRight = isRight;
        this.id = id;
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
                            case "UP":
                                this.y -= 1;
                            break;
                            case "DOWN":
                                this.y += 1;
                            break;
                            case "LEFT":
                                this.x -= 1;
                                this.isRight = false;
                            break;
                            case "RIGHT":
                                this.x += 1;
                                this.isRight = true;
                            break;
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
        if (monsterTimeline == null) return;
        monsterTimeline.stop();
    }
    public void pause() {
        monsterTimeline.pause();
    }
    public void resume() {
        monsterTimeline.play();
        System.out.println("Folytatás");
    }
}
