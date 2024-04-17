package com.example.demo;

import com.example.demo.Model.*;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {

    Player player;
    private static final Timer timer = new Timer();


    @BeforeEach
    public void initPowerUpTest() {
        player = new Player(40, 40, 0, false);
        player.addPowerUp(new PowerUp(40, 40, PowerUpType.MOREBOMBS));
        player.addPowerUp(new PowerUp(40, 40, PowerUpType.NOBOMBS));
    }
    @Test
    public void testPowerUp() throws IOException {
            PowerUp.isSnail(player, new PowerUp(40, 40, PowerUpType.MOREBOMBS));
            assertTrue(player.hasPowerUp(PowerUpType.MOREBOMBS));
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
                assertFalse(player.hasPowerUp(PowerUpType.MOREBOMBS));
            }
        }, 9000);
    }
    @Test
    public void testPowerUp2() throws IOException {
            PowerUp.isSnail(player, new PowerUp(40, 40, PowerUpType.SHIELD));
            assertFalse(player.hasPowerUp(PowerUpType.SHIELD));
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
                assertFalse(player.hasPowerUp(PowerUpType.SHIELD));
            }
        }, 9000);
    }


}
