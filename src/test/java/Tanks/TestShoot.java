package Tanks;

import Tanks.component.Component;
import Tanks.entity.Bullet;
import Tanks.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestShoot {
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
        currentTank.shoot();
        List<Component> collect = app.getComponents()
                .stream()
                .filter(item -> item instanceof Bullet)
                .collect(Collectors.toList());
        assertEquals(1, collect.size(), "There should be only one Bullet");
    }
}
