package Tanks.component;

public interface Component {
    /**
     * Start the component.
     */
    void start();

    /**
     * Each frame is executed with logic
     */
    void update();
}
