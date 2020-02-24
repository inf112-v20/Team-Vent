package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GameModel {

    Robot robot;
    private TiledMap tiledMap;
    private Player player;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        tiledMap = new TmxMapLoader().load("demo.tmx");
    }

    public Robot getRobot() {
        return this.robot;
    }

    public TiledMap getBoard() {
        return this.tiledMap;
    }

    public Player getPlayer() {
        return this.player;
    }
}
