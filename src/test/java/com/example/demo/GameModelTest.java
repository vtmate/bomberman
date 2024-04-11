package com.example.demo;

import com.example.demo.BombermanApplication;
import com.example.demo.Controller.GameController;
import com.example.demo.Controller.InGameController;
import com.example.demo.Model.GameModel;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    @BeforeAll
    public static void initGameModel() {
        GameModel gm = new GameModel("Dzsungel", null);
    }
    @Test
    public void testCheckInteraction() throws IOException {
        GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkInteraction(10,20,30,40) );
    }

    @Test
    public void testIsPlayerOnBomb() throws IOException {
        GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.isPlayerOnBomb(40,40));
    }

    @Test
    public void testCheckForMonster() throws IOException {
        GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkForMonster(40*4,40));
    }

    @Test
    public void testCheckForBox() throws IOException {
        GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkForBox(40,200));
    }
}
