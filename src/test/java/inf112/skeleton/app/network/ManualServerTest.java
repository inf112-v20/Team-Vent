package inf112.skeleton.app.network;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ManualServerTest implements ApplicationListener {

    @Override
    public void create() {
        GameServer gameServer = new GameServer();
        System.out.println(gameServer.connectionList);

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public static void main(String[] Args){
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally";
        cfg.width = 1366;
        cfg.height = 768;
        cfg.resizable = false;
        new LwjglApplication(new ManualServerTest(), cfg);
    }
}
