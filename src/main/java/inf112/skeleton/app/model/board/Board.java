package inf112.skeleton.app.model.board;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.model.Robot;

import java.util.HashMap;

public class Board {
    private final String TILE_LAYER_ID = "Tile";
    private final String ROBOT_LAYER_ID = "Player";  // todo: change map layer name for consistency
    private final TiledMap tiledMap;
    private HashMap<Integer, TileType> tileTypeHashMap;

    /**
     * Create an empty board (used for unit testing)
     *
     * @param width  in number of tiles
     * @param height in number of tiles
     */
    public Board(int width, int height) {
        tiledMap = new TiledMap();
        tiledMap.getProperties().put("width", width);
        tiledMap.getProperties().put("height", height);
        MapLayers layers = tiledMap.getLayers();
        addTileLayer(width, height, layers);
        addRobotLayer(width, height, layers);
        this.tileTypeHashMap = TileType.tileIdHashMap();
    }

    public Board(String filePath) {
        this.tiledMap = new TmxMapLoader().load(filePath);
        this.tileTypeHashMap = TileType.tileIdHashMap();
    }

    private void addTileLayer(int width, int height, MapLayers layers) {
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(width, height, 100, 100);
        tileLayer.setName(TILE_LAYER_ID);
        layers.add(tileLayer);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setTile(i, j, TileType.BASE_TILE);
            }
        }
    }

    private void addRobotLayer(int width, int height, MapLayers layers) {
        TiledMapTileLayer robotLayer = new TiledMapTileLayer(width, height, 100, 100);
        robotLayer.setName(ROBOT_LAYER_ID);
        layers.add(robotLayer);
    }

    public int getWidth() {
        return (int) this.tiledMap.getProperties().get("width");
    }

    public int getHeight() {
        return (int) this.tiledMap.getProperties().get("height");
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public TiledMapTileLayer getTileLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(TILE_LAYER_ID);
    }

    public TiledMapTileLayer getRobotLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(ROBOT_LAYER_ID);
    }

    public void setTile(int x, int y, TileType tileType) {
        TiledMapTile tile = new StaticTiledMapTile(new TextureRegion());
        tile.setId(tileType.id());
        getTileLayer().setCell(x, y, new TiledMapTileLayer.Cell().setTile(tile));
    }

    public void addRobot(Robot robot) {
        getRobotLayer().getObjects().add(robot);
    }

    public Robot getRobotByName(String name) {
        return (Robot) getRobotLayer().getObjects().get(name);
    }

    public Array<Robot> getRobots() {
        return getRobotLayer().getObjects().getByType(Robot.class);
    }

    /**
     * @return the TileType matching the tile id of the tile at (x, y), or the basis tile if there is no match
     */
    public TileType getTile(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return tileTypeHashMap.getOrDefault(getTileLayer().getCell(x, y).getTile().getId(), TileType.BASE_TILE);
    }
}
