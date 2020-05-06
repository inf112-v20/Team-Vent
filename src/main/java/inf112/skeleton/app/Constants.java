package inf112.skeleton.app;

public final class Constants {

    // Names of the tile layers in the tiled map
    public static final String WALL_LAYER = "Wall";
    public static final String TILE_LAYER = "Tile";
    public static final String FLAG_LAYER = "Flag";

    public static final boolean DEVELOPER_MODE = false;
    public static final boolean ENABLE_TIME_LIMIT = !DEVELOPER_MODE;
    public static final int TIME_LIMIT = 60; // time limit for programming in seconds
    public static final boolean ENABLE_LOGGING = false;
    public static final int INTERVAL_TIME = 500; //Interval time for each step in ms.

    private Constants() { // do not instantiate
    }
}
