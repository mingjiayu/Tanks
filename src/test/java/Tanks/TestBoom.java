package Tanks;

import Tanks.component.Component;
import Tanks.entity.Bomb;
import Tanks.entity.Bullet;
import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBoom {
    private App app;
    @BeforeEach
    public void setUp() {
        PApplet.main("Tanks.App");
        app = App.getInstance();
        app.setup();
        app.draw();
    }


    @Test
    public void test() throws InterruptedException {
        Tank currentTank = app.getCurrentTank();
        currentTank.shoot();
        List<Component> collect = app.getComponents()
                .stream()
                .filter(item -> item instanceof Bullet)
                .collect(Collectors.toList());
        assertEquals(1, collect.size(), "There should be only one Bullet");
        Component component = collect.get(0);
        Bullet bullet = (Bullet) component;
        bullet.death();
        collect = app.getComponents()
                .stream()
                .filter(item -> item instanceof Bomb)
                .collect(Collectors.toList());
        assertEquals(1, collect.size(), "There should be only one Bomb");
        Bomb bomb = (Bomb) collect.get(0);
        bomb.death();
        assertTrue(Bomb.deathCount > 0, "There was an explosion.");
    }
}
