package inf112.skeleton.app.model.board;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapHandler {
    private TiledMap tiledMap;

    public MapHandler(String mapName) {
        tiledMap = new TmxMapLoader().load(mapName);
    }

    public TiledMap getBoard() { return this.tiledMap; }

    public int getTileID(int x, int y, String layerName){
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
        return tileLayer.getCell(x, y).getTile().getId();
    }

    public int getTileID(RVector2 vector, String layerName){
        return getTileID((int) vector.getVector().x, (int) vector.getVector().y, layerName);
    }
}
