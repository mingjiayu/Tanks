package Tanks.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourcesUtils {

    private final static ClassLoader classLoader = ResourcesUtils.class.getClassLoader();
    private final static String BASE_PATH = "assets/";

    public static List<String> getResourcesFileByName(String path) {
        List<String> list = new ArrayList<>();
        URL url = classLoader.getResource(BASE_PATH + path);
        if (url != null) {
            File file = new File(url.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    Arrays.stream(files).forEach(file1 -> list.add(file1.getName()));
                }
            }
        }
        return list;
    }

}
