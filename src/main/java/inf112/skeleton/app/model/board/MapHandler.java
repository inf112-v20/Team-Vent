package inf112.skeleton.app.model.board;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.model.tiles.TileInformationUtils;

public class MapHandler {
    private TiledMap tiledMap;
    private String WALL_LAYER_NAME = "Wall";
    private String ROBOT_LAYER_NAME = "Robot";
    private String TILE_LAYER_NAME = "Tile";
    //private String LASER_LAYER_NAME = "Laser";
    //private String FLAG_LAYER_NAME = "Flag";

    /**
     * Load a map from a file. This will only work while the application is running
     *
     * @param filename relative to the assets folder
     */
    public MapHandler(String filename) {
        tiledMap = new TmxMapLoader().load(filename);
    }

    /**
     * Create a map filled with base tiles for testing purposes
     *
     * @param width  in number of tiles
     * @param height in number of tiles
     */
    public MapHandler(int width, int height) {
        // create a new tiled map
        tiledMap = new TiledMap();
        tiledMap.getProperties().put("width", width);
        tiledMap.getProperties().put("height", height);
        MapLayers layers = tiledMap.getLayers();
        // create the tile layer and fill it with base tiles
        int tileSize = 100;
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(width, height, tileSize, tileSize);
        tileLayer.setName(TILE_LAYER_NAME);
        layers.add(tileLayer);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tileLayer.setCell(i, j, new TiledMapTileLayer.Cell());
                setTile(i, j, "");
            }
        }
        // create the object layer
        TiledMapTileLayer robotLayer = new TiledMapTileLayer(width, height, tileSize, tileSize);
        robotLayer.setName(ROBOT_LAYER_NAME);
        layers.add(robotLayer);
    }

    public int getWidth() {
        return (int) this.tiledMap.getProperties().get("width");
    }

    public int getHeight() {
        return (int) this.tiledMap.getProperties().get("height");
    }

    public TiledMap getMap() {
        return this.tiledMap;
    }

    public TiledMapTileLayer.Cell getTileCell(int x, int y, String layerName) {
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        try {
            return tiledLayer.getCell(x, y);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public TiledMapTileLayer.Cell getTileCell(RVector2 vector, String layerName) {
        return getTileCell((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public Direction getDirection(int x, int y, String layerName) {
        TiledMapTileLayer.Cell cell = getTileCell(x, y, layerName);
        try {
            return TileInformationUtils.getDirection(cell);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Direction getDirection(RVector2 vector, String layerName) {
        return getDirection((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public String getTileType(int x, int y, String layerName) {
        TiledMapTileLayer.Cell cell = getTileCell(x, y, layerName);
        return TileInformationUtils.getType(cell);
    }

    public String getTileType(RVector2 vector, String layerName) {
        return getTileType((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    /**
     * Checks if there is a wall in the path of a location
     *
     * @param location Location of the entity
     * @return true if there is wall in the direction the entity is facing
     * or if there is a wall facing the other direction, one tile forward
     */
    public boolean wallInPath(Location location) {
        Location nextLocation = location.copy().forward();
        Direction currentLocationWallDirection = getDirection(location.getPosition(), WALL_LAYER_NAME);
        boolean wallBlockingCurrentPath = currentLocationWallDirection == location.getDirection();
        if (wallBlockingCurrentPath) {
            return true;
        }

        Direction nextLocationWallDirection = getDirection(nextLocation.getPosition(), WALL_LAYER_NAME);
        return nextLocationWallDirection == nextLocation.getDirection().left().left();
    }

    public void setTile(int x, int y, String type) {
        TiledMapTile tile = new StaticTiledMapTile(new TextureRegion());
        tile.getProperties().put("type", type);
        getTileLayer().getCell(x, y).setTile(tile);
    }

    /**
     * @return a list of all objects in all map layers on the board
     */
    public MapObjects getRobotMapObjects() {
        return getRobotLayer().getObjects();
    }

    public MapLayer getRobotLayer() {
        return tiledMap.getLayers().get(ROBOT_LAYER_NAME);
    }

    public TiledMapTileLayer getTileLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(TILE_LAYER_NAME);
    }

    public TiledMapTileLayer getWallLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(WALL_LAYER_NAME);
    }

    //public TiledMapTileLayer getLaserLayer() {
    //    return (TiledMapTileLayer) tiledMap.getLayers().get(LASER_LAYER_NAME);
    //}

    //public TiledMapTileLayer getFlagLayer() {
    //    return (TiledMapTileLayer) tiledMap.getLayers().get(FLAG_LAYER_NAME);
    //}
}
