package inf112.skeleton.app.cards;

import inf112.skeleton.app.board.Direction;
import inf112.skeleton.app.board.Location;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CardTest {

    @Test
    public void movesForward() {
        Location loc = new Location(Direction.SOUTH.unitVector(), Direction.WEST);
        assertEquals(new MoveForwardCard().instruction(loc), loc.forward());
    }

    @Test
    public void rotateLeft() {
        Location loc = new Location(Direction.SOUTH.unitVector(), Direction.NORTH);
        assertEquals(new RotateLeftCard().instruction(loc), loc.rotateLeft());
    }

    @Test
    public void rotateRight() {
        Location loc = new Location(Direction.EAST.unitVector(), Direction.SOUTH);
        assertEquals(new RotateRightCard().instruction(loc), loc.rotateRight());
    }
}