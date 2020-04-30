package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Launcher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Team Vent's RoboRally";
        cfg.width = 1200;
        cfg.height = 768;
        cfg.resizable = true;
        new LwjglApplication(new RoboRallyGame(), cfg);
    }
}