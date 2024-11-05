package Tanks.manage;

import Tanks.App;
import Tanks.component.Component;
import Tanks.entity.Wind;
import Tanks.globe.Globe;
import Tanks.views.Win;
import lombok.Getter;

import java.util.HashMap;

public class GameManager implements Component {

    @Getter
    private static GameManager instance;

    @Getter
    private final float gravity = 3.5f;

    @Getter
    private final Wind wind = new Wind();

    @Getter
    private final Win win = new Win();

    @Getter
    private final HashMap<String, Integer> scores = new HashMap<>();



    public GameManager() {
        instance = this;
        scores.put(Globe.GAME_TYPE.PLAYER_A.getValue(), 0);
        scores.put(Globe.GAME_TYPE.PLAYER_B.getValue(), 0);
        scores.put(Globe.GAME_TYPE.PLAYER_C.getValue(), 0);
        scores.put(Globe.GAME_TYPE.PLAYER_D.getValue(), 0);
    }




    @Override
    public void start() {

    }

    @Override
    public void update() {
        wind.update();
    }
}
