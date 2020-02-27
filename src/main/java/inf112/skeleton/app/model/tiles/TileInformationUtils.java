package inf112.skeleton.app.model.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.model.board.Direction;

public class TileInformationUtils {
    public static String getType(TiledMapTileLayer.Cell cell){
        try{
            return (String) cell.getTile().getProperties().get("type");
        } catch (NullPointerException e){
            return null;
        }
    }
    public static Direction getDirection(TiledMapTileLayer.Cell cell) {
        int directionNumber = (int) cell.getTile().getProperties().get("direction");
        switch (directionNumber){
            case(0):
                return Direction.NORTH;
            case(1):
                return Direction.EAST;
            case(2):
                return Direction.SOUTH;
            case(3):
                return Direction.WEST;
            default:
                return null;
        }
    }
}
