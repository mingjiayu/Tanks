package Tanks.entity;

import Tanks.App;
import Tanks.component.Component;
import Tanks.globe.Globe;
import Tanks.manage.ImageManager;
import Tanks.manage.LevelManager;
import Tanks.utils.MovingAverage;
import Tanks.utils.PlayerColoursUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
import processing.core.PGraphics;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Data
public class Level implements Component {
    private String layout;
    private String background;
    private String foregroundColour;
    private String trees;
    private String[][] map = new String[App.HEIGHT / 32][App.WIDTH / 32]; //2D array representing the level layout
    private int[] colors;
    private MovingAverage movingAverage; //MovingAverage object for terrain smoothing
    //Lists for background positions, tree positions, and tank positions
    private List<Position> backgroundPosition = new ArrayList<>();
    private List<Position> treesPosition = new ArrayList<>();
    private HashMap<String, Position> tankPosition = new HashMap<>();
    @Getter
    @Setter
    private boolean isRenderMapItem = false;

    private void loadMap() { //This method reads the level layout from a file and initializes the level structure.
        File file = new File(layout);
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            int j = 0;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    for (int k = 0; k < line.length(); k++) {
                        if (line.charAt(k) != ' ') {
                            map[j][k] = line.charAt(k) + "";
                        }
                        if (line.charAt(k) == Globe.GAME_TYPE.BACKGROUND.getChar()) {
                            backgroundPosition.add(new Position(k * 32, j * 32));
                        } else if (line.charAt(k) == Globe.GAME_TYPE.TREE.getChar()) {
                            treesPosition.add(new Position(k * 32, j * 32));
                        } else if (Globe.GAME_TYPE.INSTANCE.isPlayer(line.charAt(k))) {
                            tankPosition.put(String.valueOf(line.charAt(k)), new Position(k * 32, j * 32));
                        }
                        if (k >= map[0].length - 1) break;
                    }
                }
                j++;
                if (j >= map.length - 1) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // sort from lowest to highest
        backgroundPosition.sort(Comparator.comparingInt(Position::getX));
    }


    public int[] getForegroundColour() {
        if (foregroundColour != null) {
            String[] split = foregroundColour.split(",");
            return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        }
        throw new RuntimeException("No foreground colour found");
    }


    @Override
    public void start() {
        loadMap();
        String[] split = foregroundColour.split(",");
        colors = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        movingAverage = new MovingAverage(32);

    }

    @Override
    public void update() {
        if (!isRenderMapItem) {
            // clear background
            App.getInstance().getComponents()
                    .stream()
                    .filter(item -> item instanceof BackGround || item instanceof Tree)
                    .forEach(item -> App.getInstance().getComponents().remove(item));

            // render background
            List<Position> position1 = new ArrayList<>();
            for (Position position : backgroundPosition) {
                int x = position.getX();
                int y = position.getY();
                for (int j = 0; j < 32; j++) {
                    position1.add(new Position(x++, y));
                }
            }
            // calculate the moving average
            for (int k = 0; k < 2; k++) {
                for (int i = 0; i < position1.size() - 32; i++){
                    int mv = 0;
                    int x =  position1.get(i).getX();
                    for (int j = i + 1; j <= i + 32; j++){
                        mv += position1.get(j).getY();
                    }
                    mv = mv / 32;
                    position1.set(i, new Position(x, mv));
                }
            }
            position1.forEach(item -> App.getInstance().getComponents().add(new BackGround(item)));

            // render tree
            treesPosition.forEach(item -> {
                App.getInstance()
                        .getComponents()
                        .add(new Tree(trees, new Position(item.getX(), 0)));
            });
            App.getInstance().getTanks().clear();
            // add player
            ConcurrentLinkedQueue<Tank> tanks = App.getInstance().getTanks();
            int i = 0;
            tanks.add(new Tank(
                    i++,
                    Globe.GAME_TYPE.PLAYER_A.getValue(),
                    tankPosition.get(Globe.GAME_TYPE.PLAYER_A.getValue()),
                    PlayerColoursUtils.getPlayerColor(Globe.GAME_TYPE.PLAYER_A.getValue())
            ));
            tanks.add(new Tank(
                    i++,
                    Globe.GAME_TYPE.PLAYER_B.getValue(),
                    tankPosition.get(Globe.GAME_TYPE.PLAYER_B.getValue()),
                    PlayerColoursUtils.getPlayerColor(Globe.GAME_TYPE.PLAYER_B.getValue())
            ));
            tanks.add(new Tank(
                    i++,
                    Globe.GAME_TYPE.PLAYER_C.getValue(),
                    tankPosition.get(Globe.GAME_TYPE.PLAYER_C.getValue()),
                    PlayerColoursUtils.getPlayerColor(Globe.GAME_TYPE.PLAYER_C.getValue())
            ));
            tanks.add(new Tank(
                    i,
                    Globe.GAME_TYPE.PLAYER_D.getValue(),
                    tankPosition.get(Globe.GAME_TYPE.PLAYER_D.getValue()),
                    PlayerColoursUtils.getPlayerColor(Globe.GAME_TYPE.PLAYER_D.getValue())
            ));

            App.getInstance().changeTank();
            isRenderMapItem = true;

        }

    }
}
