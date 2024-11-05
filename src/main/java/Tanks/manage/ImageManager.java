package Tanks.manage;

import Tanks.App;
import Tanks.component.Component;
import Tanks.utils.ResourcesUtils;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PImage;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class ImageManager implements Component {

    private static ImmutableMap<String, PImage> images;

    /**
     * Gets the specified image resource
     * @param name Image names are suffixed
     * @return PImage
     */
    public static PImage getImageByName(String name) {
        if (images.containsKey(name)) {
            return images.get(name);
        }
        throw new RuntimeException("Image not found: " + name);
    }




    @Override
    public void start() {
        List<String> img = ResourcesUtils.getResourcesFileByName("img");
        HashMap<String, PImage> map = new HashMap<>();
        img.forEach(filename -> {
            map.put(filename, App.getInstance().loadImage("assets/img/" + filename));
        });
        images = ImmutableMap.copyOf(map);
    }

    @Override
    public void update() {

    }
}
