package com.example.demo;

import com.example.demo.BombermanApplication;
import com.example.demo.Controller.GameController;
import com.example.demo.Controller.InGameController;
import com.example.demo.Model.Bomb;
import com.example.demo.Model.GameModel;
import com.example.demo.Model.Gate;
import com.example.demo.Model.Player;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gm;
    @BeforeEach
    public void initGameModel() {
        gm = new GameModel("Dzsungel", null);
    }
    @Test
    public void testCheckInteraction() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkInteraction(10,20,30,40) );
    }

    @Test
    public void testIsPlayerOnBomb() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.isPlayerOnBomb(40,40));
    }

    @Test
    public void testCheckForMonster() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkForMonster(40*4,40));
    }

    @Test
    public void testCheckForGate() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        gm.gates.add(new Gate(80, 40, new Player(120, 40, 3,true)));
        assertTrue(gm.checkForGate(80,40));
    }

    @Test
    public void testIsBetween1() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.isBetween(2,1, 3));
    }
    @Test
    public void testIsBetween2() throws IOException {
        //GameModel gm = new GameModel("Dzsungel", null);
        assertFalse(gm.isBetween(2,4, 3));
    }
    @Test
    public void testPlaceBomb() {
        Player player = new Player(120, 40, 3,true);
        gm.players.add(player);
        gm.placeBomb(gm.getPlayer(player.id));
        System.out.println(player.getCountOfBombs());
        boolean isBomb = false;
        for (int i = 0; i < gm.bombs.size(); i++) {
            Bomb bomb = gm.bombs.get(i);
            if (bomb.x == 120 && bomb.y == 40) {
                isBomb = true;
                break;
            }
        }

        assertTrue(isBomb);
    }
}
