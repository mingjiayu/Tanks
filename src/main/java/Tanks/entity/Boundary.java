package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.Death;
import Tanks.component.impl.BoxCollider;
import lombok.Getter;
import processing.core.PGraphics;

@Getter
public class Boundary extends BoxCollider implements Component {

    private final String name;

    public Boundary(String name, Position position, int width, int height) {
        super(position, width, height);
        this.name = name;
        setCollider(false);
    }

    @Override
    public void isColliding(Collision other) {
        if (other instanceof Death && name.equals("BOTTOM")) {
            Death death = (Death) other;
            death.death();
        }
        if (other instanceof Tank && name.equals("BOTTOM")) {
            Tank tank = (Tank) other;
            tank.setHealth(0);
        }
        if (other instanceof Bullet) {
            if (name.equals("BOTTOM")) {
                App.getInstance().getComponents().remove((Component) other);
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
    }
}
