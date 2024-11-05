package Tanks;

import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTankPower {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }

    /**
     * test tank power
     */
    @Test
    public void Test() {
        Tank currentTank = app.getCurrentTank();
        currentTank.changePower(false);
        assertEquals(currentTank.getPower() - 1,currentTank.getCurrentPower());
        // Since the limit is 50, nothing will change
        currentTank.changePower(true);
        assertEquals(currentTank.getPower(),currentTank.getCurrentPower(), "Energy should be reduced");
    }
}
