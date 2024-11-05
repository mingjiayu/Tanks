package Tanks.manage;

import Tanks.App;
import Tanks.component.Component;
import Tanks.entity.BackGround;
import Tanks.entity.Level;
import Tanks.entity.Tank;
import Tanks.entity.Tree;
import Tanks.utils.PlayerColoursUtils;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static processing.core.PApplet.loadJSONObject;


public class LevelManager implements Component {

    @Getter
    private static ImmutableList<Level> levels;

    @Getter
    private static int currentLevelIndex = 0;

    /**
     * Next Stage
     */
    public static void nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            return;
        }
        throw new RuntimeException("No more levels");
    }

    /**
     * Set a level
         * @param index Should be small with the number of existing levels
     */
    public static void setIndexLevel(int index) {
        if (index < levels.size()) {
            currentLevelIndex = index;
            return;
        }
        throw new RuntimeException("No more levels");
    }

    public static Level getCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }

        throw new RuntimeException("Index out of bounds");
    }

    public static void rest() {
        App instance = App.getInstance();
        instance.getComponents().forEach(item -> {
            if (item instanceof BackGround || item instanceof Tree) {
                instance.getComponents().remove(item);
            }
        });

        GameManager.getInstance().getScores().forEach((key, value) -> {
            GameManager.getInstance().getScores().put(key, 0);
        });

        instance.getTanks().clear();
        setIndexLevel(0);
        getLevels().forEach(item -> item.setRenderMapItem(false));

    }

    public static boolean isLastLevel() {
        return currentLevelIndex == levels.size() - 1;
    }


    @Override
    public void start() {
        // Load the level configuration information
        JSONObject jsonObject = loadJSONObject(new File(App.configPath)); // config.json
        // Creates a new instance of the Gson class, which is a library for converting Java objects to JSON and vice versa.
        Gson gson = new Gson();
        Type type = TypeToken.getParameterized(List.class, Level.class).getType();
        List<Level> levelList = gson.fromJson(jsonObject.getJSONArray("levels").toString(), type);
        levels = ImmutableList.copyOf(levelList);
        levels.forEach(Level::start);
    }
//    In summary, this code:
//
//    Loads a JSON configuration file
//    Parses the JSON to extract level information
//    Converts the JSON data into a list of Level objects
//    Creates an immutable copy of this list
//    Initializes each level by calling its start() method



    @Override
    public void update() {
        levels.get(getCurrentLevelIndex()).update();
    }


}
