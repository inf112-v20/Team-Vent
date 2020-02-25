package inf112.skeleton.app.model.tiles;

import inf112.skeleton.app.model.board.Direction;

import java.util.HashMap;

public class TileDirections {
    private static final HashMap<Integer, Direction> IDMap;
    static {
        IDMap = new HashMap<>();
        // Walls
        IDMap.put(22, Direction.EAST);
        IDMap.put(28, Direction.SOUTH);
        IDMap.put(29, Direction.WEST);
        IDMap.put(30, Direction.NORTH);
        // Laser walls
        IDMap.put(36, Direction.SOUTH);
        IDMap.put(37, Direction.WEST);
        IDMap.put(44, Direction.NORTH);
        IDMap.put(45, Direction.EAST);
        // Straight normal conveyor belts
        IDMap.put(48, Direction.NORTH);
        IDMap.put(49, Direction.SOUTH);
        IDMap.put(50, Direction.WEST);
        IDMap.put(51, Direction.EAST);
        // Straight express conveyor belts
        IDMap.put(12, Direction.NORTH);
        IDMap.put(13, Direction.EAST);
        IDMap.put(20, Direction.SOUTH);
        IDMap.put(21, Direction.WEST);
    }
    public static Direction getDirection(int ID){
        return IDMap.get(ID);
    }
}
