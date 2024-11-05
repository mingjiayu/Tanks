package Tanks.entity;

import Tanks.App;
import Tanks.component.Collision;
import Tanks.component.Component;
import Tanks.component.Death;
import Tanks.component.impl.BoxCollider;
import Tanks.manage.GameManager;
import Tanks.manage.ImageManager;
import Tanks.manage.LevelManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.checkerframework.common.returnsreceiver.qual.This;
import processing.core.PGraphics;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tank extends BoxCollider implements Component, Death {
    private int id;
    private String name;
    private int health;
    private int speed;
    private int power;
    private int fuel;
    private int score;
    private int x;
    private int y;
    private int[] color;
    private int turretX;
    private int turretY;
    private int currentPower;
    private Position position;
    private boolean canMove = true;
    private int angle = 45;
    private int parachute = 4;
    private int timer = 0;

    public Tank(String name, int x, int y, String color) {
        init(name, x, y, color);
    }

    public Tank(int id, String name, Position position, String color) {
        super(position, 18, 32);
        this.id = id;
        position.setY(64);
        init(name, position.getX(), position.getY(), color);
        this.position = position;
        this.position.setX(this.position.getX() - 15);
    }

    private void init(String name, int x, int y, String color) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.fuel = 250;
        this.speed = 1;
        this.power = this.currentPower = 50;
        this.health = 100;
        if (color.equals("random")) {
            Random random = new Random();
            this.color = new int[]{random.nextInt(255), random.nextInt(255), random.nextInt(255)};
        } else {
            String[] split = color.split(",");
            this.color = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        }
    }



    public void draw() {
        PGraphics g = App.getInstance().g;

        double angleInRadians = Math.toRadians(angle); // 将角度转换为弧度
        int y1 = position.getY();
        int y2 = position.getY() + 17;
        double x1Prime = Math.cos(angleInRadians) - (y1 - y2) * Math.sin(angleInRadians) + x;
        double y1Prime = Math.sin(angleInRadians) + (y1 - y2) * Math.cos(angleInRadians) + y2;
        setTurretX((int) x1Prime);
        setTurretY((int) y1Prime);
        // turret
        g.stroke(0, 0 ,0);
        g.strokeWeight(5);
        g.line((float) x1Prime, (float) y1Prime, x, position.getY() + 17);


        // body
        g.stroke(color[0], color[1], color[2]);
        g.line(x - 10, position.getY() + 20, x + 10, position.getY() + 20);
        g.line(x - 15, position.getY() + 23, x + 15, position.getY() + 23);
    }

    public void shoot() { // Creates a new Bullet and changes the active tank.
        App instance = App.getInstance();
        App.getInstance()
                .getComponents()
                .add(new Bullet(new Position(turretX, turretY), currentPower, angle, this));
        instance.changeTank();
        GameManager.getInstance().getWind().changeWinValue();
        timer = 0;
    }

    public void move(boolean isLeft) { // Moves the tank left or right, consuming fuel.
        if (fuel > 0 && canMove) {
            x += isLeft ? -speed : speed;
            position.setX(x - 15);
            fuel -= speed;
        }
        canMove = true;
    }

    public void turretMove(boolean isUp) { // Adjusts the turret angle.
        if (isUp) {
            if (angle < 45) angle += (int) 3.6;
        } else {
            if (angle > -45) angle -= (int) 3.6;
        }
    }


    public void changePower(boolean isUp) { // Adjusts the shooting power.
        if (isUp) {
            if (currentPower < power) {
                currentPower++;
            }
        } else {
            if (currentPower > 0) {
                currentPower--;
            }
        }
    }

    public void wounded(int damage) {
        if (health > 0) {
            health -= damage;
            if (health <= 0) {
                health = 0;
                App.getInstance().tankDeath(this);
            }
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void isColliding(Collision other) { // Handle collisions with boundaries
        if (other instanceof Boundary) {
            Boundary boundary = (Boundary) other;
            if (boundary.getName().equals("LEFT") || boundary.getName().equals("RIGHT")) {
                canMove = false;
            }
        } //TODO: change canMove state after collided tank turn around???
    }


    @Override
    public void start() {

    }

// key points about this update method:
//    It handles both logic (falling, parachute use) and visual elements (drawing the tank and indicators).
//    It interacts with other parts of the game (App, GameManager, ImageManager) to get necessary information and resources.
//    It uses a timer to control the duration of the current tank indicator.
//    The method takes into account different states of the tank (alive/dead, falling/stable, current/not current) and behaves accordingly.

    @Override
    public void update() {
        if (isAlive()) {
            draw(); // This calls the tank's draw method to render it on the screen.

            // This block only runs if the tank is not colliding with anything and is not the current active tank.
            if (!isColliding() && !App.getInstance().getCurrentTank().equals(this)) {
                if (parachute > 0) { // If the tank has parachutes left, it draws a parachute image and makes the tank fall slowly (affected by gravity).
                    App.getInstance().image(ImageManager.getImageByName("parachute.png"), getPosition().getX() - 15, getPosition().getY() - 50);
                    getPosition().setY((int) (getPosition().getY() + (10 - GameManager.getInstance().getGravity())));
                } else { // If no parachutes are left, the tank falls faster.
                    getPosition().setY(getPosition().getY() + 10);
                }
            }

            // Current Tank Indicator(down arrow):
//              -  This block runs only for the current active tank.
//              -  It draws a down arrow above the tank for 60 frames (2 seconds at 30 FPS) to indicate it's the active tank.
//              -  The timer is incremented each frame.

            if (timer <= 60 && App.getInstance().getCurrentTank().equals(this)) {
                PGraphics g = App.getInstance().g;
                // Down arrow
                g.line(getPosition().getX() + 18, getPosition().getY() - 64, getPosition().getX() + 18, getPosition().getY() - 32);
                g.line(getPosition().getX() + 30, getPosition().getY() - 50, getPosition().getX() + 18, getPosition().getY() - 32);
                g.line(getPosition().getX() + 5, getPosition().getY() - 50, getPosition().getX() + 18, getPosition().getY() - 32);
                timer++;
            }
        }

    }

    @Override
    public void death() {
        if (isAlive()) {
            App.getInstance().tankDeath(this);
        }
    }
}
