package inf112.skeleton.app;

import inf112.skeleton.app.board.Direction;
import inf112.skeleton.app.board.Location;
import inf112.skeleton.app.board.RVector2;
import inf112.skeleton.app.cards.RotateLeftCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RobotTest {

    private Robot robot;
    private Location originalLocation;

    @Before
    public void setUp() {
        originalLocation = new Location(new RVector2(2, 3), Direction.WEST);
        robot = new Robot(originalLocation);
    }

    @Test
    public void execute() {
        assertEquals(originalLocation, robot.getLocation());
        robot.execute(new RotateLeftCard());
        assertNotEquals(originalLocation, robot.getLocation());
        robot.execute(new RotateLeftCard());
        assertNotEquals(originalLocation, robot.getLocation());
        robot.execute(new RotateLeftCard());
        assertNotEquals(originalLocation, robot.getLocation());
        robot.execute(new RotateLeftCard());
        assertEquals(originalLocation, robot.getLocation());
    }

    @Test
    public void robotsHaveDifferentIdentities() {
        assertNotEquals(new Robot(), new Robot());
    }

    @Test
    public void getLocation() {
        assertEquals(robot.getLocation(), originalLocation);
    }

    @Test
    public void getXY() {
        assertEquals(robot.getX(), (int) originalLocation.getPosition().getVector().x);
        assertEquals(robot.getY(), (int) originalLocation.getPosition().getVector().y);
    }

    @Test
    public void setLocation() {
        robot.setLocation(originalLocation.forward());
        assertEquals(robot.getLocation(), originalLocation.forward());
    }
}