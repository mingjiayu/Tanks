package Tanks.views;

import Tanks.App;
import Tanks.component.Component;
import Tanks.entity.Tank;
import Tanks.manage.GameManager;
import Tanks.manage.LevelManager;
import Tanks.utils.PlayerColoursUtils;
import processing.core.PGraphics;

import java.util.*;
import java.util.stream.Collectors;

public class Win implements Component {


    @Override
    public void start() {

    }

    @Override
    public void update() {
        // Convert HashMap to List
        List<Map.Entry<String, Integer>> list = new LinkedList<>(GameManager.getInstance().getScores().entrySet());

        // Sort the List
        list.sort(Map.Entry.comparingByValue());

        // Invert the List so that the person with the highest score is at the top
        Collections.reverse(list);

        App instance = App.getInstance();
        PGraphics g = instance.g;
        String[] split = PlayerColoursUtils.getPlayerColor(list.get(0).getKey()).split(",");
        instance.background(Integer.parseInt(split[0]),Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        g.fill(0,0,0);
        g.text("GAME OVER", ((float) App.WIDTH / 2) - 60, ((float) App.HEIGHT / 3) - 60);


        // Output sorted results
        int rank = 0;
        int y = 60;
        for (Map.Entry<String, Integer> entry : list) {
            if (rank == 0) {
                g.text("winner " + entry.getKey() + ", score:" + entry.getValue(), ((float) App.WIDTH / 2) - 100, ((float) App.HEIGHT / 3));
            } else {
                g.text("loser " + entry.getKey() + ", score:" + entry.getValue(), ((float) App.WIDTH / 2) - 100, ((float) App.HEIGHT / 3) + y);
                y += 60;
            }
            rank++;
        }

    }
}
