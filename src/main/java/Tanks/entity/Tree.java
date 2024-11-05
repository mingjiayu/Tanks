package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.impl.BoxCollider;
import Tanks.manage.ImageManager;


public class Tree extends BoxCollider implements Component {

    private final Position position;
    private final String trees;


    public Tree(String trees, Position position) {
        super(position, 18, 28);
        this.trees = trees;
        this.position = position;
    }


    @Override
    public void start() {

    }
    @Override
    public void update() {
        App instance = App.getInstance();

        instance.image(
                ImageManager.getImageByName(trees),
                position.getX(), position.getY(),
                30, 30);

    }
}
