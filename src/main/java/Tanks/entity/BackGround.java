package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.impl.BoxCollider;
import Tanks.manage.LevelManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import processing.core.PGraphics;

@EqualsAndHashCode(callSuper = true)
@Data
public class BackGround extends BoxCollider implements Component {

    private Position position;
    private int px;
    private int height;

    public BackGround(Position position) {
        super(position, 1, App.HEIGHT - 32);
        this.position = position;
        this.px = 1;
        this.height = App.HEIGHT - 32;
        this.setGravity(false);
    }


    @Override
    public void start() {

    }

    @Override
    public void update() {
        PGraphics g = App.getInstance().g;
        Level level = LevelManager.getCurrentLevel();
        g.stroke(level.getForegroundColour()[0], level.getForegroundColour()[1], level.getForegroundColour()[2]);
        Level currentLevel = LevelManager.getCurrentLevel();
        int[] colors = currentLevel.getColors();
        g.fill(colors[0], colors[1], colors[2]);
        g.rect(position.getX(), position.getY(), px, height);
    }
}
