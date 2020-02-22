package inf112.skeleton.app.board;

import org.junit.Test;

import static inf112.skeleton.app.board.Direction.NORTH;
import static inf112.skeleton.app.board.Direction.SOUTH;
import static inf112.skeleton.app.board.Direction.EAST;
import static inf112.skeleton.app.board.Direction.WEST;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DirectionTest {

    public static final Direction [] directions = {NORTH, EAST, SOUTH, WEST};


    @Test
    public void leftIsLeft() {
        for (int i = 0; i < directions.length; i++) {
            assertEquals(directions[i], directions[(i + 1) % directions.length].left());
        }
    }

    @Test
    public void rightIsRight() {
        for (int i = 0; i < directions.length; i++) {
            assertEquals(directions[i].right(), directions[(i + 1) % directions.length]);
        }
    }

    @Test
    public void leftAndRightAreInverse() {
        for (Direction d : directions) {
            assertEquals(d.left().right(), d);
            assertEquals(d.right().left(), d);
        }
    }

    @Test
    public void turnDoesTurn() {
        for (Direction d : directions) {
            assertNotEquals(d, d.right());
            assertNotEquals(d, d.left());
        }
    }

    @Test
    public void turningFourTimesIsACircle() {
        for (Direction d : directions) {
            assertEquals(d, d.left().left().left().left());
            assertEquals(d, d.right().right().right().right());
        }
    }
}