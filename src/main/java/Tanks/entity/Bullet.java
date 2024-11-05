package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.Death;
import Tanks.component.impl.BoxCollider;
import Tanks.manage.GameManager;
import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;

public class Bullet extends BoxCollider implements Component, Death {

    private final double angle; // Emission Angle, unit: degree
    private final double gravity = GameManager.getInstance().getGravity(); // Acceleration of gravity in meters per second ²

    @Getter
    @Setter
    private float vx; // Horizontal acceleration
    private float vy; // Vertical acceleration
    private final Tank tank; // Reference to the tank that fired the bullet


    public Bullet(Position position, int power, int angle, Tank tank) {
        super(position, 10, 10);
        angle = angle == 0 ? 90 : angle;
        this.angle = angle;
        vx = (float) (power * Math.tan(Math.toRadians(angle)));
        vy = (float) (power * Math.sin(Math.toRadians(angle < 0 ? -angle : angle)));
        this.tank = tank;
    }



    @Override
    public void isColliding(Collision other) { // Triggers death() and removes the bullet when it hits terrain or trees
        if (other instanceof BackGround || other instanceof Tree) {
            death();
            App.getInstance().getComponents().remove(this);
        }
    }

    @Override
    public void start() {

    }


//    Renders the bullet
//    Updates its position based on velocity and gravity
//    Removes the bullet if it goes out of bounds
    @Override
    public void update() {
        PGraphics g = App.getInstance().g;
        g.stroke(255,0,0);
        g.ellipse(getPosition().getX(), getPosition().getY(), 10, 10);

        getPosition().setX((int) (getPosition().getX() + vx));
        getPosition().setY((int) (getPosition().getY() - vy));
        vy -= (float) gravity;

        if (getPosition().getY() < 0 || getPosition().getX() < 0|| getPosition().getX() > App.WIDTH || getPosition().getY() > App.HEIGHT) {
            App.getInstance().getComponents().remove(this);
        }


    }

    // Creates a new Bomb at the bullet's position when the bullet is destroyed
    @Override
    public void death() {
        App.getInstance().getComponents().add(new Bomb(getPosition(), tank));
    }


}
