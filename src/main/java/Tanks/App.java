package Tanks;

import Tanks.component.Component;
import Tanks.component.impl.BoxCollider;
import Tanks.entity.Boundary;
import Tanks.entity.Level;
import Tanks.entity.Position;
import Tanks.entity.Tank;
import Tanks.manage.GameManager;
import Tanks.manage.ImageManager;
import Tanks.manage.LevelManager;
import Tanks.ui.HUD;
import Tanks.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;


import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public static String configPath;

    public static Random random = new Random();
    @Getter
    private static App instance;

    private HUD hud;

    @Getter
    private final ConcurrentLinkedQueue<Component> components = new ConcurrentLinkedQueue<>();

    @Getter
    private final ConcurrentLinkedQueue<Tank> tanks = new ConcurrentLinkedQueue<>();


    @Getter
    private Tank currentTank = null;

    @Getter
    private int deathTankCount = 0;

    private int currentTankIndex = 0;


    @Getter
    @Setter
    private boolean isRender = true;


	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        configPath = "config.json";
        instance = this;
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        // Initializes the management class
        List<Component> components = new ArrayList<>();
        // use reflection to dynamically find and instantiate classes within the specified package ("Tanks/manage") --> the xxManager classes
        // and add them to the components list.
        ReflectionUtils.REFLECTION_INDEX_PACKAGE("Tanks/manage", components);
        // Add boundary
        components.add(new Boundary("RIGHT", new Position(WIDTH, 0), 100, HEIGHT));
        components.add(new Boundary("BOTTOM", new Position(0, HEIGHT), WIDTH, 100));
        components.add(new Boundary("LEFT", new Position(0, 0), 1, HEIGHT));

        components.forEach(Component::start);
        this.components.addAll(components);
        // Initialize hud
        hud = new HUD(g);
        //See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){


        switch (event.getKeyCode()) {
            // Move
            case 37: currentTank.move(true);break;
            case 39: currentTank.move(false);break;
            // Turret movement
            case 38: currentTank.turretMove(true);break;
            case 40: currentTank.turretMove(false);break;
            // Energy increase and decrease
            case 87: currentTank.changePower(true); break;
            case 83: currentTank.changePower(false);break;
            // Fire a bullet
            case 32: currentTank.shoot(); break;
            // R -> restart
            case 82:
                if (LevelManager.isLastLevel()) {
                    LevelManager.rest();
                    GameManager.getInstance().getWind().rest();
                    currentTankIndex = 0;
                    isRender = true;
                }
                break;
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
    /*
    Main Game Loop
        - Renders the current level background
        - Performs collision detection
        - Updates all components and tanks
        - Draws the HUD
        - Handles game over state
     */
	@Override
    public void draw() {
        if (isRender) {
            // Get current level information
            Level level = LevelManager.getCurrentLevel();
            // Sets the background of the current level
            background(ImageManager.getImageByName(level.getBackground()));

            // Collision detection
            components.stream()
                    .filter(item -> item instanceof BoxCollider)
                    .forEach(item -> ((BoxCollider) item).colliding());

            // Execute component logic
            components.forEach(Component::update);


            // draw hud
            hud.draw();
            // tank colliding
            tanks.forEach(Tank::colliding);
            // Execute Tank logic
            tanks.forEach(Tank::update);
        } else {
            GameManager.getInstance().getWin().update();
        }

        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


// 2 Game Logic Methods:

    //  Switches to the next tank in the game.
    // logic:
//      - Each tank gets a turn in order.
//      - The rotation continues indefinitely, looping back to the first tank after the last one.
//      - It works correctly even if tanks are added or removed from the game (as long as it's not done during this method call).
    public void changeTank() {
        int i = 0;
        for (Tank tank : tanks) {
            if (i == currentTankIndex % tanks.size()) {
                currentTank = tank;
                currentTankIndex++;
                return;
            }
            i++;
        }
    }

    public void tankDeath(Tank tank) {
        tanks.remove(tank);
        System.out.println(tanks.size());
        if (tanks.size() == 1) {
            try {
                LevelManager.nextLevel();
            } catch (Exception e) {
                isRender = false;
            }
        }
    }

// This is the entry point of the application, starting the Processing sketch.
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }



}
