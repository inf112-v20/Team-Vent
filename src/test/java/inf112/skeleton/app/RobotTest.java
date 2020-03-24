package inf112.skeleton.app;

import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RobotTest {

    private Robot robot;
    private Location originalLocation;

    @Before
    public void setUp() {
        originalLocation = new Location(new RVector2(2, 3), Direction.WEST);
        robot = new Robot(originalLocation);
    }

    @Test
    public void robotsHaveDifferentIdentities() {
        assertNotEquals(new Robot(), new Robot());
    }

    @Test
    public void getLocation() {
        assertEquals(originalLocation, robot.getLocation());
        assertNotSame(robot.getLocation(), robot.getLocation()); // assert location immutable
    }

    @Test
    public void getXY() {
        assertEquals(originalLocation.getPosition().getX(), robot.getX());
        assertEquals(originalLocation.getPosition().getY(), robot.getY());
    }

    @Test
    public void setLocation() {
        robot.setLocation(originalLocation.forward());
        assertEquals(originalLocation.forward(), robot.getLocation());
    }

    @Test
    public void visitFlag() {
        // todo
    }
}