package Tanks;

import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChangeTank {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }
    /**
     * test change Tank
     */
    @Test
    public void test() {
        app.changeTank();
        Tank currentTank = app.getCurrentTank();
        assertEquals("B", currentTank.getName(), "Current tank name should be B");
    }
}
