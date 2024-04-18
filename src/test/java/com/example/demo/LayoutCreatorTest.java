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

public class LayoutCreatorTest {

    Player player;
    GameModel gm;
    LayoutCreator layoutCreator;

    @BeforeEach
    public void initControlTest() {
        player = new Player(40, 40, 0, false);
        gm = new GameModel("Dzsungel", null);
        gm.boxes.add(new Box(40,40));
        gm.boxes.add(new Box(80,40));
        gm.boxes.add(new Box(40,80));
        gm.boxes.add(new Box(120,80));
        gm.boxes.add(new Box(40,120));
        layoutCreator = new LayoutCreator(gm, "Dzsungel");
    }

    @Test
    public void testCheckEntitiesIntersection() throws IOException {
        layoutCreator.createPowerUps();
//        assertEquals(4);
    }
}
