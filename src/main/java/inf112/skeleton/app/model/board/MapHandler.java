package inf112.skeleton.app.model.board;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.model.tiles.TileInformationUtils;

//TODO: Create tests when map can be loaded without starting the application
public class MapHandler {
    private TiledMap tiledMap;

    public MapHandler(String mapName) {
        tiledMap = new TmxMapLoader().load(mapName);
    }

    public TiledMap getBoard() { return this.tiledMap; }

    /**
     * IMPORTANT! Tiled starts tile IDs with 0 while LibGDX stars their IDs with 1. This returns the Tiled ID
     * @return The ID of the tile on the specified layer. Returns -1 if there is no tile
     */
    public int getTileID(int x, int y, String layerName){
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        try {
            int tileID = tileLayer.getCell(x, y).getTile().getId();
            return tileID - 1;
        }
        catch (NullPointerException e){ return -1; }
    }

    public int getTileID(RVector2 vector, String layerName){
        return getTileID((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public String getTileType(int x, int y,String layerName){
        return TileInformationUtils.getType(getTileID(x, y, layerName));
    }

    public String getTileType(RVector2 vector, String layerName){
        return getTileType((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }
    /**
     * Checks if there is a wall in the path of a location
     * @param location Location of the entity
     * @return true if there is wall in the direction the entity is facing
     *         or if there is a wall facing the other direction, one tile forward
     */
    public boolean wallInPath(Location location){
        Location nextLocation = location.copy().forward();
        int currentLocationWallID = getTileID(location.getPosition(), "Wall");
        int nextLocationWallID = getTileID(nextLocation.getPosition(), "Wall");
        // Checks if there is any walls in either the current tile or the next one forward
        if (currentLocationWallID == -1 && nextLocationWallID == -1) { return false; }
        Direction currentLocationWallDirection = TileInformationUtils.getDirection(currentLocationWallID);
        boolean wallBlockingCurrentPath = currentLocationWallDirection == location.getDirection();
        if (wallBlockingCurrentPath) { return true; }

        Direction nextLocationWallDirection = TileInformationUtils.getDirection(nextLocationWallID);
        boolean wallBlockingNextPath = nextLocationWallDirection == nextLocation.getDirection().left().left();
        return wallBlockingNextPath;

    }
}
