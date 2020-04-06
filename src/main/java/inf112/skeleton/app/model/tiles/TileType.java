package inf112.skeleton.app.model.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.model.board.Direction;

public enum TileType {
    BASE_TILE,
    HOLE,
    CONVEYOR_NORMAL,
    CONVEYOR_EXPRESS,
    GEAR_CLOCKWISE,
    GEAR_COUNTERCLOCKWISE,
    REPAIR_ONE,
    REPAIR_TWO,
    START;

    public static TileType asTileType(String name) {
        try {
            return valueOf(TileType.class, name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e2) {
            // IllegalArumentException: when the specified enum type has no constant with the specified name,
            // or the specified class object does not represent an enum type
            // NullPointerException: when name is null
            return null;
        }
    }

    public static Object getProperty(TiledMapTileLayer.Cell cell, String property) {
        if (cell == null) return null;
        try {
            return cell.getTile().getProperties().get(property);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getType(TiledMapTileLayer.Cell cell) {
        try {
            return (String) cell.getTile().getProperties().get("type");
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static Direction getDirection(TiledMapTileLayer.Cell cell) {
        int directionNumber = (int) cell.getTile().getProperties().get("direction");
        switch (directionNumber) {
            case (0):
                return Direction.NORTH;
            case (1):
                return Direction.EAST;
            case (2):
                return Direction.SOUTH;
            case (3):
                return Direction.WEST;
            default:
                return null;
        }
    }

    public static boolean hasLaser(TiledMapTileLayer.Cell wallCell) {
        try {
            return (boolean) wallCell.getTile().getProperties().get("has_laser");
        } catch (NullPointerException e) {
            return false;
        }
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}
