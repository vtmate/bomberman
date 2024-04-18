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

    /**
     * Robbanás következtében a pályáról leszedjük a bombát, és visszaadjuk a játékosnak.
     * @param gm        maga a gameModel
     * @param player    a játékos, akinek visszaadjuk a bombát
     * @param delay     egyből robbanás után
     */
    public void removeBomb(GameModel gm, Player player, int delay) {
        timeline = new Timeline();
        // Leállítjuk a timeline-ot, ha éppen fut
        timeline.stop();

        // Töröljük az eseményeket a timeline-ból
        timeline.getKeyFrames().clear();

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gm.explosion(x, y, getRadius());
                player.addBomb();
                gm.bombs.remove(Bomb.this);
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
