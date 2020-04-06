package inf112.skeleton.app.model.board;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.GameState;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.RobotState;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MapHandler {
    private final TiledMap tiledMap;
    private int numberOfFlags;
    private List<Location> startLocations = new ArrayList<>(8);
    private List<Location> lasers;

    /**
     * Load a map from a file. This will only work while the application is running
     *
     * @param filename relative to the assets folder
     */
    public MapHandler(String filename) {
        tiledMap = new TmxMapLoader().load(filename);
        lasers = new LinkedList<>();
        // count the flags
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                // count the flags
                if (getFlagLayer().getCell(i, j) != null) {
                    numberOfFlags += 1;
                }
                // locate the wall lasers
                TiledMapTileLayer.Cell wallCell = getWallLayer().getCell(i, j);
                if (wallCell != null && TileType.hasLaser(wallCell)) {
                    // the direction of the laser is the opposite of the direction of the wall
                    Direction laserDirection = Objects.requireNonNull(TileType.getDirection(wallCell)).left().left();
                    lasers.add(new Location(new RVector2(i, j), laserDirection));
                }
                // locate starting positions
                if (getTileType(i, j, Constants.TILE_LAYER) == TileType.START) {
                    startLocations.add(new Location(new RVector2(i, j), Direction.EAST));
                }
            }
            // Sort start locations by their number
            startLocations.sort((o1, o2) -> {
                int startNumberX = (Integer) TileType.getProperty(getTileLayer().getCell(o1.getPosition().getX(),
                        o1.getPosition().getY()), "number");
                int startNumberY = (Integer) TileType.getProperty(getTileLayer().getCell(o2.getPosition().getX(),
                        o2.getPosition().getY()), "number");
                return Integer.compare(startNumberX, startNumberY);
            });
        }

        // Make sure robots face the center of the board
        if (!startLocations.isEmpty() && startLocations.get(0) != null && startLocations.get(0).getPosition().getX() > getWidth() / 2) {
            for (int i = 0; i < startLocations.size(); i++) {
                startLocations.set(i, startLocations.get(i).halfTurn());
            }
        }
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
        tileLayer.setName(Constants.TILE_LAYER);
        layers.add(tileLayer);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tileLayer.setCell(i, j, new TiledMapTileLayer.Cell());
                TiledMapTile tile = new StaticTiledMapTile(new TextureRegion());
                tile.getProperties().put("type", "base_tile");
                getTileLayer().getCell(i, j).setTile(tile);
            }
        }
        // create the object layer
        TiledMapTileLayer robotLayer = new TiledMapTileLayer(width, height, tileSize, tileSize);
        robotLayer.setName(Constants.ROBOT_LAYER);
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
        return getTileCell(vector.getX(), vector.getY(), layerName);
    }

    public Direction getDirection(int x, int y, String layerName) {
        TiledMapTileLayer.Cell cell = getTileCell(x, y, layerName);
        try {
            return TileType.getDirection(cell);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Direction getDirection(RVector2 vector, String layerName) {
        return getDirection(vector.getX(), vector.getY(), layerName);
    }

    public String getTileTypeString(int x, int y, String layerName) {
        TiledMapTileLayer.Cell cell = getTileCell(x, y, layerName);
        return TileType.getType(cell);
    }

    public String getTileTypeString(RVector2 vector, String layerName) {
        return getTileTypeString((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public TileType getTileType(int x, int y, String layerName) {
        return TileType.asTileType(getTileTypeString(x, y, layerName));
    }

    public TileType getTileType(RVector2 vector, String layerName) {
        return TileType.asTileType(getTileTypeString(vector.getX(), vector.getY(), layerName));
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
        Direction currentLocationWallDirection = getDirection(location.getPosition(), Constants.WALL_LAYER);
        boolean wallBlockingCurrentPath = currentLocationWallDirection == location.getDirection();
        if (wallBlockingCurrentPath) {
            return true;
        }

        Direction nextLocationWallDirection = getDirection(nextLocation.getPosition(), Constants.WALL_LAYER);
        return nextLocationWallDirection == nextLocation.getDirection().left().left();
    }

    /**
     * Return the first robot in the line of vision from a location, if there is one. Walls block the line of vision
     *
     * @param location the point of view
     * @param state    the game state
     * @return a robot that is in the line of vision or null
     */
    public Robot robotInLineOfVision(Location location, GameState state) {
        while (!(wallInPath(location) || outOfBounds(location.forward()))) {
            // if one of the robots is on the next tile then return it
            for (RobotState robotState : state.getRobotStates()) {
                if (!robotState.getDead() && robotState.getLocation().getPosition().equals(location.forward().getPosition()) ||
                        robotState.getLocation().getPosition().equals(location.getPosition())) {
                    return robotState.getRobot();
                }
            }
            location = location.forward();
        }
        return null;
    }

    public TiledMapTileLayer getRobotLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(Constants.ROBOT_LAYER);
    }

    public TiledMapTileLayer getTileLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(Constants.TILE_LAYER);
    }

    public TiledMapTileLayer getWallLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(Constants.WALL_LAYER);
    }

    public TiledMapTileLayer getFlagLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(Constants.FLAG_LAYER);
    }

    public boolean outOfBounds(Location loc) {
        return (loc.getPosition().getX() > getWidth() || loc.getPosition().getX() < 0
                || loc.getPosition().getY() > getHeight() || loc.getPosition().getY() < 0);

    }

    public List<Location> getLasersLocations() {
        return lasers;
    }

    public List<Location> getStartLocations() {
        return startLocations;
    }

    public Integer getFlag(RVector2 position) {
        return (Integer) TileType.getProperty(getFlagLayer().getCell(position.getX(),
                position.getY()), "number");
    }

    public boolean hasRepairSite(RVector2 position) {
        TileType tileType = getTileType(position, Constants.TILE_LAYER);
        return tileType == TileType.REPAIR_ONE || tileType == TileType.REPAIR_TWO;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }
}
