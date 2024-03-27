package com.example.demo.Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Explosion extends Entity{
    public Timeline timeline;
    public Explosion(double x, double y) {
        super(x, y);
    }
    public void removeExplosion(GameModel gm, int delay) {
        timeline = new Timeline();
        timeline.stop(); // Leállítjuk a timeline-ot, ha éppen fut

        timeline.getKeyFrames().clear(); // Töröljük az eseményeket a timeline-ból

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gm.explosions.remove(Explosion.this);
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
