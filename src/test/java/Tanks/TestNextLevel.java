package Tanks;

import Tanks.entity.Level;
import Tanks.manage.LevelManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestNextLevel {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }
    /**
     * test next leve
     */
    @Test
    public void test() {
        Level level = LevelManager.getCurrentLevel();
        LevelManager.nextLevel();
        assertNotEquals(LevelManager.getCurrentLevel().getLayout(), level.getLayout(), level.getLayout() + "->" + LevelManager.getCurrentLevel().getLayout());
    }
}
