package inf112.skeleton.app;

import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.RotateLeftCard;
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
        assertEquals(originalLocation, robot.getLocation());
    }

    @Test
    public void getXY() {
        assertEquals((int) originalLocation.getPosition().getVector().x, robot.getX());
        assertEquals((int) originalLocation.getPosition().getVector().y, robot.getY());
    }

    @Test
    public void setLocation() {
        robot.setLocation(originalLocation.forward());
        assertEquals(originalLocation.forward(), robot.getLocation());
    }
}