package com.example.demo;

import com.example.demo.Model.*;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

public class ControlTest {

    Player player;
    GameModel gm;
    Control control;

    @BeforeEach
    public void initControlTest() {
        player = new Player(40, 40, 0, false);
        gm = new GameModel("Dzsungel", null);
        control = new Control(gm);
    }

    @Test
    public void testCheckEntitiesIntersection() throws IOException {
        ArrayList<Box> boxes = new ArrayList<>();
        boxes.add(new Box(40,40));
        boxes.add(new Box(80,40));
        boxes.add(new Box(40,80));
        assertTrue(control.checkEntitiesIntersection(60,40, boxes, "LEFT"));
    }
    @Test
    public void testCheckEntitiesIntersection2() throws IOException {
        ArrayList<Box> boxes = new ArrayList<>();
        boxes.add(new Box(40,40));
        boxes.add(new Box(80,40));
        boxes.add(new Box(40,80));
        assertFalse(control.checkEntitiesIntersection(130,80, boxes, "LEFT"));
    }


}
