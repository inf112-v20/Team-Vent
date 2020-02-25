package inf112.skeleton.app.model.board;

import java.util.HashMap;

public enum TileType {
    TILE {
        @Override
        public int id() {
            return 5;
        }
    },
    HOLE {
        @Override
        public int id() {
            return 6;
        }
    };

    public static HashMap<Integer, TileType> tileIdHashMap() {
        HashMap<Integer, TileType> tileIdToTileType = new HashMap<>();
        for (TileType tileType : TileType.values()) {
            tileIdToTileType.put(tileType.id(), tileType);
        }
        return tileIdToTileType;
    }

    public abstract int id();
}
