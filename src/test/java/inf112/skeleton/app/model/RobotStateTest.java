package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RobotStateTest {
    private RobotState state;

    @Before
    public void beforeEach() {
        Robot robot = new Robot();
        state = new RobotState(robot, robot.getLocation(), 0, false, 0, robot.getLocation());
    }

    @Test
    public void visitingFlagIncreasesNumberOfCapturedFlags() {
        state = state.visitFlag();
        assertEquals(1, state.getCapturedFlags());
    }

    @Test
    public void visitingFlagUpdatesSaveLocation() {
        RobotState moved = state.updateLocation(state.getLocation().forward());
        moved = moved.visitFlag();
        assertEquals(moved.getSaveLocation(), moved.getLocation());
    }

    @Test
    public void rebootingResetsToLastSavedLocation() {
        Location flagLocation = state.getLocation().forward();
        Location otherLocation = state.getLocation().backward();
        RobotState moved = state.updateLocation(flagLocation);
        moved = moved.visitFlag();
        moved = moved.updateLocation(otherLocation);
        assertEquals(moved.getSaveLocation(), flagLocation);
    }
}