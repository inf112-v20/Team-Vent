package inf112.skeleton.app.model.board;

public enum TileType {
    BASE_TILE,
    HOLE,
    CONVEYOR_NORMAL,
    CONVEYOR_EXPRESS,
    GEAR_CLOCKWISE,
    GEAR_COUNTERCLOCKWISE;

    public static TileType getEnum(String name) {
        try {
            return valueOf(TileType.class, name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e2) {
            // IllegalArumentException: when the specified enum type has no constant with the specified name,
            // or the specified class object does not represent an enum type
            // NullPointerException: when name is null
            return null;
        }
    }

    public String toString() {

        valueOf("a");

        return this.name().toLowerCase();
    }
}

