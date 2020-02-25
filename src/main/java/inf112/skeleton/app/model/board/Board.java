package inf112.skeleton.app.model.board;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.model.Robot;

import java.util.HashMap;

public class Board {
    private final String TILE_LAYER_ID = "Tile";
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
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(width, height, 100, 100);
        tileLayer.setName(TILE_LAYER_ID);
        TiledMapTileLayer.Cell cell;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cell = new TiledMapTileLayer.Cell();
                tileLayer.setCell(i, j, cell);
                cell.setTile(new StaticTiledMapTile(new TextureRegion()));
            }
        }
        layers.add(tileLayer);
        this.tileTypeHashMap = TileType.tileIdHashMap();
    }

    public Board(String filePath) {
        this.tiledMap = new TmxMapLoader().load(filePath);
        this.tileTypeHashMap = TileType.tileIdHashMap();
    }

    private int getWidth() {
        return (int) this.tiledMap.getProperties().get("width");
    }

    private int getHeight() {
        return (int) this.tiledMap.getProperties().get("height");
    }

    public TiledMapTileLayer getTileLayer() {
        return (TiledMapTileLayer) tiledMap.getLayers().get(TILE_LAYER_ID);
    }

    public void setTile(int x, int y, TileType tileType) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTile tile = new StaticTiledMapTile(new TextureRegion());
        tile.setId(tileType.id());
        cell.setTile(tile);
        getTileLayer().setCell(x, y, cell);
    }

    public void addObject(MapObject object) {
        getTileLayer().getObjects().add(object);
    }

    public Robot getRobotByName(String name) {
        return (Robot) getTileLayer().getObjects().get(name);
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    /**
     * @return the TileType matching the tile id of the tile at (x, y), or the basis tile if there is no match
     */
    public TileType getTile(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return tileTypeHashMap.get(getTileLayer().getCell(x, y).getTile().getId());
    }
}
