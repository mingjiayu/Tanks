package Tanks;

import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMove {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }

    /**
     * move test
     */
    @Test
    public void test() {;
        Tank currentTank = app.getCurrentTank();
        currentTank.move(true);
        assertEquals(currentTank.getX(),currentTank.getPosition().getX() + 15);
        currentTank.move(false);
        assertEquals(currentTank.getX(),currentTank.getPosition().getX() + 15);
    }

}
