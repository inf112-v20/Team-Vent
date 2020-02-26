package inf112.skeleton.app;

import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotEquals;

public class TestBoard {
    private Board board;
    private Robot marvin;

    @Before
    public void setUp() {
        board = new Board(5, 5);
        marvin = new Robot("Marvin", new Location(new RVector2(0, 0), Direction.NORTH));
    }

    @Test
    public void getRobotByName() {
        board.addObject(marvin);
        assertEquals(marvin, board.getRobotByName(marvin.getName()));
        assertNull(board.getRobotByName("Non-Existent"));
    }

    @Test
    public void addHole() {
        int x = 4;
        int y = 4;
        assertNotEquals(TileType.HOLE, board.getTile(x, y));
        board.setTile(x, y, TileType.HOLE);
        assertEquals(TileType.HOLE, board.getTile(x, y));
    }
}