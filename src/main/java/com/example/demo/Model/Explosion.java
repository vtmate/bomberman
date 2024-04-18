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

    /**
     * Robbanás után a kirajzolásának eltüntetése.
     * @param gm        maga a gameModel
     * @param delay     ennyi idő után tüntetjük el
     */
    public void removeExplosion(GameModel gm, int delay) {
        timeline = new Timeline();

        // Leállítjuk a timeline-ot, ha éppen fut
        timeline.stop();

        // Töröljük az eseményeket a timeline-ból
        timeline.getKeyFrames().clear();

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gm.explosions.remove(Explosion.this);
                timeline = null;
            }
        }));

        // A timeline csak egyszer fog futni
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Timeline megállítása.
     */
    public void pause() {
        if (timeline == null) return;
        timeline.pause();
    }

    /**
     * Timeline újraindítása.
     */
    public void resume() {
        if (timeline == null) return;
        timeline.play();
    }
}
