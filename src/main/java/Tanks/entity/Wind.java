package Tanks.entity;
import Tanks.App;
import Tanks.component.Component;
import lombok.Getter;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public class Wind implements Component {
    private int windValue;
    private int changeValue;
    private double acceleration;
    private static final double WIND_ACCELERATION = 0.03;
    private static final int MIN_WIND_VALUE = -35;
    private static final int MAX_WIND_VALUE = 35;
    private static final int MIN_CHANGE_VALUE = -5;
    private static final int MAX_CHANGE_VALUE = 5;
    private final Random random = new Random();

    public Wind() {
        windValue = MIN_WIND_VALUE + random.nextInt(MAX_WIND_VALUE - MIN_WIND_VALUE + 1);
    }

    public void changeWinValue() {
        changeValue = MIN_CHANGE_VALUE + random.nextInt(MAX_CHANGE_VALUE - MIN_CHANGE_VALUE + 1);
        windValue += changeValue;
    }

    public void rest() {
        changeValue = MIN_CHANGE_VALUE + random.nextInt(MAX_CHANGE_VALUE - MIN_CHANGE_VALUE + 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        acceleration = windValue * WIND_ACCELERATION;
        List<Component> collect = App.getInstance().getComponents()
                .stream()
                .filter(item -> item instanceof Bullet)
                .collect(Collectors.toList());
        for (Component component : collect) {
            Bullet bullet = (Bullet) component;
            bullet.getPosition().setX((int) (bullet.getPosition().getX() + acceleration));
        }
    }


}
