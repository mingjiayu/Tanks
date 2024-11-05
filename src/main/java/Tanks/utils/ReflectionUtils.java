package Tanks.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.Objects;

public class ReflectionUtils {
    /**
     * Gets all classes under the specified package through reflection and instantiates them
     */
    public static <T> void REFLECTION_INDEX_PACKAGE(String packagePath, List<T> list) {
        List<Class<?>> classList = getClassList(packagePath);
        getNewInstance(classList, list);
    }


    private static List<Class<?>> getClassList(String url) {
        URL resource = ClassLoader.getSystemClassLoader()
                .getResource(url);
        if (resource != null) {
            File file = new File(resource.getFile());
            List<Class<?>> classList = new ArrayList<>();
            for (String s : Objects.requireNonNull(file.list())) {
                String className = s.split("\\.")[0];
                try {
                    Class<?> aClass = Class.forName(url.replace('/','.') + "." + className);
                    classList.add(aClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return classList;
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private static <T> void getNewInstance(List<Class<?>> classList, List<T> list) {
        for (Class<?> aClass : classList) {
            Constructor<?> declaredConstructor;
            try {
                declaredConstructor = aClass.getDeclaredConstructor();
                T o = (T)declaredConstructor.newInstance();
                list.add(o);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
