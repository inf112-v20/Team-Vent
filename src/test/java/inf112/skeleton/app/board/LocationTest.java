package inf112.skeleton.app.board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {

    private Location locNorth;
    private Location locEast;

    @Before
    public void setup() {
        RVector2 r2 = new RVector2(2, 3);
        locNorth = new Location(r2, Direction.NORTH);
        locEast = new Location(r2, Direction.EAST);
    }

    @Test
    public void forward() {
        assertEquals(locNorth.forward(), locNorth.forward());
        assertNotSame(locNorth, locNorth.forward()); // immutable, returns new instance
        for(Direction d : DirectionTest.directions) {
            assertEquals(new Location(new RVector2(0, 0), d).forward(), new Location(d.vector(), d));
        }
    }

    @Test
    public void rotateIsInPlace() {
        assertEquals(locNorth.getPosition(), locNorth.rotateRight().getPosition());
        assertEquals(locNorth.getPosition(), locNorth.rotateLeft().getPosition());
    }

    @Test
    public void rotateChangesDirection() {
        assertNotEquals(locNorth.getDirection(), locNorth.rotateRight().getDirection());
        assertNotEquals(locNorth.getDirection(), locNorth.rotateLeft().getDirection());
    }

    @Test
    public void copy() {
        assertEquals(locEast, locEast.copy());
        assertNotSame(locEast, locEast.copy());
    }
}