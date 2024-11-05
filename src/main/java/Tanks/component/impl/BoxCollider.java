package Tanks.component.impl;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.entity.BackGround;
import Tanks.entity.Bomb;
import Tanks.entity.Position;
import Tanks.manage.GameManager;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class BoxCollider implements Collision {

    private Rectangle rectangle;
    private Position position;
    @Setter
    private boolean isCollider = true;

    @Getter
    private boolean isColliding = false;

    @Getter
    @Setter
    private boolean isGravity = true;
    public BoxCollider(Position position, int width, int height) {
        // Set the location and size of the collision box
        rectangle = new Rectangle(position.getX(), position.getY(), width, height);
        this.position = position;
    }

    public BoxCollider() {}


    @Override
    public void isColliding(Collision other) {} // Method to handle collision with another object

    @Override
    public boolean colliding() {
        // Check if collision detection is enabled and the rectangle and position are not null
        if (isCollider && rectangle != null && position != null) {

            rectangle.setLocation(position.getX(), position.getY());

            App instance = App.getInstance();
            // Get all components that are BoxColliders and are not this object
            List<Component> collect = instance.getComponents()
                    .stream()
                    .filter(item -> item instanceof BoxCollider && item != this)
                    .collect(Collectors.toList());
            // Add all Tanks to the list of components to check for collision
            collect.addAll(App.getInstance().getTanks());
            // Filter the list to ensure it does not include this object
            collect = collect
                    .stream()
                    .filter(item -> item != this)
                    .collect(Collectors.toList());

            for (Component component : collect) {
                BoxCollider boxCollider = (BoxCollider) component;
                if (boxCollider.getRectangle() != null && boxCollider.getRectangle().intersects(rectangle)) {
                    isColliding(boxCollider);
                    boxCollider.isColliding(this);
                    isColliding = true;
                    return true;
                }

            }
            // If there is no collision, set the isColliding flag to false,
            // meaning the object is not touching the ground and should be in free fall
            isColliding = false;
            if (isCollider && isGravity) {
                position.setY((int) (position.getY() + GameManager.getInstance().getGravity()));
            }
            return false;
        }
        return false;
    }


}

