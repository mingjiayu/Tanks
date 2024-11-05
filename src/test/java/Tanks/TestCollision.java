package Tanks;

import Tanks.entity.BackGround;
import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCollision {

    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }

    /**
     * test collision
     */
    @Test
    public void test() {
        Tank currentTank = app.getCurrentTank();
        BackGround boxCollider = new BackGround(currentTank.getPosition());
        app.getComponents().add(boxCollider);
        assertTrue(boxCollider.colliding());
    }
}
