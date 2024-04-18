package com.example.demo.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

public class Monster extends Entity{
    public String direction = "RIGHT";
    private Timeline monsterTimeline;
    public boolean isRight;
    public int id;

    /**
     *
     * @param x         a szörny x koordinátája
     * @param y         a szörny y koordinátája
     * @param isRight   a szörny jobb irányba néz-e
     * @param id        a szörny típusának azonosítója
     */
    public Monster(double x, double y, boolean isRight, int id) {
        super(x, y);
        this.isRight = isRight;
        this.id = id;
    }

    /**
     * A szörny mozgatása, annak függvényében, hogy melyik irányba néz.
     * A szörny irányt változtat 20% eséllyel.
     *
     * @param control - a Control osztály átadása
     */
    public void moveMonster(Control control){
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

    /**
     * Timeline leállítása.
     */
    public void stop(){
        if (monsterTimeline == null) return;
        monsterTimeline.stop();
    }

    /**
     * Timeline megállítása.
     */
    public void pause() {
        monsterTimeline.pause();
    }

    /**
     * Timeline újraindítása.
     */
    public void resume() {
        monsterTimeline.play();
    }
}
