package inf112.skeleton.app.board;

import java.util.HashMap;

public class Tile implements ITile {
    HashMap<Direction, Boolean> walls;

    @Override
    public boolean hasWall(Direction d) {
        //return walls.getOrDefault(d, false);
        throw new UnsupportedOperationException("Walls are not implemented");
    }
}