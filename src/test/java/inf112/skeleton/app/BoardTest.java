package inf112.skeleton.app;

import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class BoardTest {
    private Board board;
    private Robot marvin;

    @Before
    public void setUp() {
        marvin = new Robot("Marvin", new Location(new RVector2(0, 0), Direction.NORTH));
        board = new Board(5, 5);
    }

    @Test
    public void addRobot() {
        board.addRobot(marvin);
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

    @Test
    public void getWidth() {
        int width = 3;
        board = new Board(width, 5);
        assertEquals(width, board.getWidth());
    }

    @Test
    public void getHeight() {
        int height = 2;
        board = new Board(1, height);
        assertEquals(height, board.getHeight());
    }

    @Test
    public void getTiledMap() {
        assertNotNull(board.getTiledMap());
    }

    @Test
    public void getTileLayer() {
        assertNotNull(board.getTileLayer());
    }

    @Test
    public void getRobotLayer() {
        assertNotNull(board.getRobotLayer());
    }

    @Test
    public void setAndGetTile() {
        assertNotEquals(TileType.HOLE, board.getTile(0, 0));
        board.setTile(0, 0, TileType.HOLE);
        assertEquals(TileType.HOLE, board.getTile(0, 0));
    }

    @Test
    public void tilesOutsideBoardGivesNull() {
        board = new Board(1, 1);
        assertNull(board.getTile(1, 1));
    }

    @Test
    public void getRobots() {
        assertFalse(board.getRobots().contains(marvin, true));
        board.addRobot(marvin);
        assertTrue(board.getRobots().contains(marvin, true));
    }
}