package com.example.demo;

import com.example.demo.Model.*;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    public void initPlayerTest() {
        player = new Player(40, 40, 0, false);
        player.addPowerUp(new PowerUp(40, 40, PowerUpType.MOREBOMBS, null));
    }

    @Test
    public void testHasPowerUp() throws IOException {
        assertTrue(player.hasPowerUp(PowerUpType.MOREBOMBS));
    }
    @Test
    public void testHasPowerUp2() throws IOException {
        assertFalse(player.hasPowerUp(PowerUpType.ROLLERSKATE));
    }
    @Test
    public void testBombCount() throws IOException {
        assertEquals(0, player.getCountOfBombs());
    }
    @Test
    public void testBombCount2() throws IOException {
        player.addBomb();
        player.addBomb();
        assertEquals(2, player.getCountOfBombs());
    }
}
