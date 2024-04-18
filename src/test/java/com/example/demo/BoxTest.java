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

public class BoxTest {

    ArrayList<Box> boxes;
    @BeforeEach
    public void initBoxTest() {
        boxes = new ArrayList<>();
        boxes.add(new Box(40,40));
        boxes.add(new Box(80,40));
        boxes.add(new Box(40,80));
    }

    @Test
    public void hasBoxOnTopText() {
        assertTrue(Box.hasBoxOnTop(boxes, 40, 40));
    }
    @Test
    public void hasBoxOnTopText2() {
        assertFalse(Box.hasBoxOnTop(boxes, 80, 80));
    }

}
