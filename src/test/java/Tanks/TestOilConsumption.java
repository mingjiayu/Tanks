package Tanks;

import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOilConsumption {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }


    @Test
    public void test() {
        Tank currentTank = app.getCurrentTank();
        currentTank.move(true);
        currentTank.move(false);
        assertEquals(248,currentTank.getFuel(), "fuel should be 248");
    }

}
