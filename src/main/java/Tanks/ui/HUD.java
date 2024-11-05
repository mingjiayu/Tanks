package Tanks.ui;

import Tanks.App;
import Tanks.entity.Tank;
import Tanks.entity.Wind;
import Tanks.manage.GameManager;
import Tanks.manage.ImageManager;
import processing.core.PGraphics;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HUD{

    private final PGraphics g;


    public HUD(PGraphics g) {
        this.g = g;
    }


    public void draw() {
        drawPlayerInfo();
        drawFuel();
        drawHealth();
        drawWind();
        drawTankScore();
    }


    private void drawPlayerInfo() {
        // Font color
        g.fill(0,0,0);
        g.textSize(20);
        g.text("Player "+ App.getInstance().getCurrentTank().getName()+ "'s turn", 20, 20);
    }

    private void drawFuel() {
        App.getInstance().image(ImageManager.getImageByName("fuel.png"), 5*32, 0, 32, 32);
        g.text(App.getInstance().getCurrentTank().getFuel(), 6*32, 25);

    }

    private void drawHealth() {
        g.text("Health:", 12*32, 25);
        g.fill(0, 0, 255);
        // border-line
        g.strokeWeight(3);
        // border color
        g.stroke(0,0,0);
        App.getInstance().rect(14*32, 9, 100, 20);
        // health bar
        int playerHealth = App.getInstance().getCurrentTank().getHealth();
        g.strokeWeight(3);
        g.stroke(192,192,192);
        App.getInstance().rect(14*32, 9, playerHealth, 20);
        // line
        g.stroke(255,0,0);
        g.line(14*32 + playerHealth, 4, 14*32 + playerHealth, 35);
        // health number
        g.fill(0,0,0);
        g.text(playerHealth, 14*32 + 101, 25);
        // power
        int power = App.getInstance().getCurrentTank().getCurrentPower();
        g.text("Power:" + power, 12*32, 50);
    }

    private void drawWind() {
        Wind wind = GameManager.getInstance().getWind();

        App.getInstance().image(ImageManager.getImageByName(wind.getChangeValue() > 0 ? "wind.png" : "wind-1.png"), App.WIDTH - 72, 0, 32, 32);
        g.text(Math.abs(wind.getWindValue()), App.WIDTH - 33, 25);
    }

    private void drawTankScore() {
        // top rect
        g.fill(255,255,255,0);
        g.stroke(0,0,0);
        g.rect(App.WIDTH - 132, 50, 128, 25);
        g.fill(0,0,0);
        g.text("Scores:", App.WIDTH - 132, 70);
        // bottom rect
        g.fill(255,255,255,0);
        g.stroke(0,0,0);
        g.rect(App.WIDTH - 132, 50 + 25, 128, 110);
        // tank score info
        GameManager.getInstance().getScores().forEach((key, value) -> {
            switch (key) {
                case "A" :
                    g.fill(0,0,255);
                    g.text("Player A:  " + value, App.WIDTH - 132, 100);
                    break;
                case "B" :
                    g.fill(255,0,0);
                    g.text("Player B:  " + value, App.WIDTH - 132, 125);
                    break;
                case "C" :
                    g.fill(0,255,255);
                    g.text("Player C:  " + value, App.WIDTH - 132, 150);
                    break;
                case "D" :
                    g.fill(255,255,0);
                    g.text("Player D:  " + value, App.WIDTH - 132, 175);
                    break;
            }
        });
    }


}
