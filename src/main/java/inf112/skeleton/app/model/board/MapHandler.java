package inf112.skeleton.app.model.board;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.model.tiles.TileInformationUtils;

public class MapHandler {
    private TiledMap tiledMap;

    public MapHandler(String mapName) {
        tiledMap = new TmxMapLoader().load(mapName);
    }

    public TiledMap getBoard() { return this.tiledMap; }

    private TiledMapTileLayer.Cell getTile(int x, int y, String layerName){
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        try { return tiledLayer.getCell(x, y); }
        catch (NullPointerException e){ return null; }
    }

    private TiledMapTileLayer.Cell getTile(RVector2 vector, String layerName) {
        return getTile((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public Direction getDirection(int x, int y, String layerName){
        TiledMapTileLayer.Cell cell = getTile(x, y, layerName);
        try { return TileInformationUtils.getDirection(cell); }
        catch (NullPointerException e){ return null; }
    }

    public Direction getDirection(RVector2 vector, String layerName){
        return getDirection((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }

    public String getTileType(int x, int y, String layerName){
        TiledMapTileLayer.Cell cell = getTile(x, y, layerName);
        return TileInformationUtils.getType(cell);
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
        Direction currentLocationWallDirection = getDirection(location.getPosition(), "Wall");
        boolean wallBlockingCurrentPath = currentLocationWallDirection == location.getDirection();
        if (wallBlockingCurrentPath) { return true; }

        Direction nextLocationWallDirection = getDirection(nextLocation.getPosition(), "Wall");
        return nextLocationWallDirection == nextLocation.getDirection().left().left();

    }
}
