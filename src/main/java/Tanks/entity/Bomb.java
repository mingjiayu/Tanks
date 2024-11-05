package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.Death;
import Tanks.component.impl.BoxCollider;
import Tanks.manage.GameManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import processing.core.PGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bomb extends BoxCollider implements Component, Death {
    // Explosion radii for different colors (red, orange, yellow)
    private float redRadius = 0;
    private float orangeRadius = 0;
    private float yellowRadius = 0;
    private float explosionRadius = 30;
    // List of damaged tanks
    private List<Tank> isDamage = new ArrayList<>();
    // Reference to the tank that created the bomb
    private Tank tank;
    // Static death count
    public static int deathCount = 0; // solely for test bomb happened or not

    public Bomb(Position position, Tank tank) {
        super(position, 60, 60);
        position.setX((int) (position.getX() - explosionRadius));
        position.setY((int) (position.getY() - explosionRadius));
        setGravity(false);
        this.tank = tank;
    }


    @Override
    public void isColliding(Collision other) {
        death();
    } //When the bomb collides with something, it triggers its death() method

    @Override
    public void start() {

    }
    @Override
    public void update() {
        PGraphics g = App.getInstance().g;
        g.strokeWeight(0);
        // Draw a red circle
        g.fill(255, 0, 0);
        g.ellipse(getPosition().getX() + explosionRadius, getPosition().getY() + explosionRadius, redRadius*2, redRadius*2);

        // 绘制橙色圆圈
        g.fill(255, 165, 0);
        g.ellipse(getPosition().getX() + explosionRadius, getPosition().getY() + explosionRadius, orangeRadius*2, orangeRadius*2);

        // Draw orange circles
        g.fill(255, 255, 0);
        g.ellipse(getPosition().getX() + explosionRadius, getPosition().getY() + explosionRadius, yellowRadius*2, yellowRadius*2);

        // Increases the radii of these circles over time
        float frameRate = App.getInstance().frameRate;
        redRadius += (float) (explosionRadius / (0.2 * frameRate));
        orangeRadius += (float) (explosionRadius * 0.5 / (0.2 * frameRate));
        yellowRadius += (float) (explosionRadius * 0.2 / (0.2 * frameRate));


        // Removes the bomb from the game when the explosion is complete
        if (redRadius >= explosionRadius && orangeRadius >= explosionRadius * 0.5 && yellowRadius >= explosionRadius * 0.2) {
            App.getInstance().getComponents().remove(this);
        }
    }



//    Checks all tanks for collision with the explosion
//    Calculates and applies damage based on distance from explosion center
//    Updates scores for the tank that fired the bomb
//    Modifies terrain affected by the explosion
    @Override
    public void death() {
        deathCount++;
        App.getInstance().getTanks().forEach(tank -> {
            if (tank.getRectangle().intersects(getRectangle())) {
                Point tankPoint = new Point(tank.getPosition().getX(), tank.getPosition().getY());
                Point bombPoint = new Point(getPosition().getX(), getPosition().getY());
                double distance = tankPoint.distance(bombPoint);
                if (distance <= explosionRadius && !isDamage.contains(tank)) {
                    double damage = 60 * (1 - distance / explosionRadius);
                    this.tank.setScore((int) damage);
                    GameManager.getInstance().getScores().compute(this.tank.getName(), (k, i) -> i + (int) damage);
                    tank.wounded((int) damage);
                    isDamage.add(tank);
                }
            }
        });

        List<Component> collect = App.getInstance().getComponents()
                .stream()
                .filter(item -> item instanceof BackGround)
                .collect(Collectors.toList());
        collect.forEach(item -> {
            BackGround boxCollider = (BackGround) item;
            if (boxCollider.getRectangle().intersects(getRectangle())) {
                System.out.println(1);
                boxCollider.getPosition().setY(boxCollider.getPosition().getY() + 10);
            }
        });

    }
}
