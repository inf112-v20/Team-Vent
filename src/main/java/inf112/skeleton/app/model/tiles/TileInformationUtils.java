package inf112.skeleton.app.model.tiles;

import inf112.skeleton.app.model.board.Direction;

import java.util.HashMap;

public class TileInformationUtils {
    private static final HashMap<Integer, Direction> DirectionMap;
    private static final HashMap<Integer, String> TypeMap;
    static {
        DirectionMap = new HashMap<>();
        TypeMap = new HashMap<>();

        // Walls
        DirectionMap.put(22, Direction.EAST);
        DirectionMap.put(28, Direction.SOUTH);
        DirectionMap.put(29, Direction.WEST);
        DirectionMap.put(30, Direction.NORTH);
        TypeMap.put(22, "wall");
        TypeMap.put(28, "wall");
        TypeMap.put(29, "wall");
        TypeMap.put(30, "wall");
        // Laser walls
        DirectionMap.put(36, Direction.SOUTH);
        DirectionMap.put(37, Direction.WEST);
        DirectionMap.put(44, Direction.NORTH);
        DirectionMap.put(45, Direction.EAST);
        TypeMap.put(36, "wall");
        TypeMap.put(37, "wall");
        TypeMap.put(44, "wall");
        TypeMap.put(45, "wall");
        // Straight normal conveyor belts
        DirectionMap.put(48, Direction.NORTH);
        DirectionMap.put(49, Direction.SOUTH);
        DirectionMap.put(50, Direction.WEST);
        DirectionMap.put(51, Direction.EAST);
        TypeMap.put(48, "conveyor_normal");
        TypeMap.put(49, "conveyor_normal");
        TypeMap.put(50, "conveyor_normal");
        TypeMap.put(51, "conveyor_normal");
        // Straight express conveyor belts
        DirectionMap.put(12, Direction.NORTH);
        DirectionMap.put(13, Direction.EAST);
        DirectionMap.put(20, Direction.SOUTH);
        DirectionMap.put(21, Direction.WEST);
        TypeMap.put(12, "conveyor_express");
        TypeMap.put(13, "conveyor_express");
        TypeMap.put(20, "conveyor_express");
        TypeMap.put(21, "conveyor_express");
        // Clockwise gear
        TypeMap.put(53, "gear_clockwise");
        // Counterclockwise gear
        TypeMap.put(52, "gear_counterclockwise");
    }
    public static Direction getDirection(int ID){
        return DirectionMap.get(ID);
    }

    public static String getType(int ID) {
        if (TypeMap.get(ID) == null){ return "other"; }
        return TypeMap.get(ID);
    }
}
