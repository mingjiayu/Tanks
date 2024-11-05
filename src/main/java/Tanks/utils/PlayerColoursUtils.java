package Tanks.utils;

import Tanks.App;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import processing.data.JSONObject;

import java.io.File;
import java.util.*;

public class PlayerColoursUtils {

    private static ImmutableMap<String,String> playerColor;
    private static ImmutableMap<String, String> enemyColor;


    private static void init() {
        if (playerColor == null || enemyColor == null) {
            JSONObject jsonObject = App.loadJSONObject(new File(App.configPath));
            JSONObject playerColours = jsonObject.getJSONObject("player_colours");
            Set keys = playerColours.keys();
            HashMap<String, String> map = new HashMap<>();
            int index = 0;
            for (Object key : keys) {
                map.put(key.toString(), playerColours.get(key.toString()).toString());
                if (index == 8) {
                    playerColor = ImmutableMap.copyOf(map);
                    map.clear();
                }
                if (index == keys.size() - 1) {
                    enemyColor = ImmutableMap.copyOf(map);
                }
                index++;
            }
        }
    }

    public static String getPlayerColor(String tankName) {
        init();
        if (playerColor.containsKey(tankName)) {
            return playerColor.get(tankName);
        }
        throw new RuntimeException("No color found for tank " + tankName);
    }

    public static String getEnemyColor(String tankName) {
        init();
        if (enemyColor.containsKey(tankName)) {
            return enemyColor.get(tankName);
        }
        throw new RuntimeException("No color found for tank " + tankName);
    }


}
