package com.example.demo.Model;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Bomb extends Entity{
    private final int radius;
    public Timeline timeline;
    public Bomb(double x, double y, int radius) {
        super(x, y);
        this.radius = radius;
    }
    public int getRadius(){ return radius; }
    public void removeBomb(GameModel gm, Player player, int delay) {
        timeline = new Timeline();
        timeline.stop(); // Leállítjuk a timeline-ot, ha éppen fut

        timeline.getKeyFrames().clear(); // Töröljük az eseményeket a timeline-ból

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gm.explosion(x, y, getRadius());
                player.addBomb();
                gm.bombs.remove(Bomb.this);
                timeline = null;
                //bomb = null; // A bombariadót töröltük, hogy már ne hivatkozzon rá semmi
            }
        }));

        timeline.setCycleCount(1); // A timeline csak egyszer fog futni
        timeline.play();
    }

    public void pause() {
        if (timeline == null) return;
        timeline.pause();
    }

    public void resume() {
        if (timeline == null) return;
        timeline.play();
    }
}
