package inf112.skeleton.app.board;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DirectionTest {

    public static final Direction [] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};


    @Test
    public void left() {
        for(int i = 0; i < directions.length; i++) {
            assertEquals(directions[i], directions[(i+1)%directions.length].left());
        }
    }

    @Test
    public void right() {
        for(int i = 0; i < directions.length; i++) {
            assertEquals(directions[i].right(), directions[(i+1)%directions.length]);
        }
    }

    @Test
    public void leftAndRightAreInverse() {
        for(Direction d : directions) {
            assertEquals(d.left().right(), d);
            assertEquals(d.right().left(), d);
        }
    }

    @Test
    public void vector() {
        assertEquals(Direction.NORTH.vector(), new RVector2(0, 1));
        assertEquals(Direction.SOUTH.vector(), new RVector2(0, -1));
        assertEquals(Direction.WEST.vector(), new RVector2(-1, 0));
        assertEquals(Direction.EAST.vector(), new RVector2(1, 0));

    }

    @Test
    public void turn() {
        for(Direction d: directions) {
            assertEquals(d.right(), d.turn(Side.RIGHT));
            assertEquals(d.left(), d.turn(Side.LEFT));
            assertNotEquals(d, d.right());
            assertNotEquals(d, d.left());
        }
    }
}