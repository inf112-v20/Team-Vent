package inf112.skeleton.app.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RobotStateTest {
    private RobotState state;

    @Before
    public void beforeEach() {
        Robot robot = new Robot();
        state = new RobotState(robot, robot.getLocation(), 0, false, 0, robot.getLocation());
    }

    @Test
    public void visitingAFlagIncreasesNumberOfFlags() {
        assertEquals(0, state.getCapturedFlags());
        state = state.visitFlag();
        assertEquals(1, state.getCapturedFlags());
    }

    @Test
    public void rebootingResetsToLastFlag() {
        RobotState forwardState = state.updateLocation(state.getLocation().forward());
        assertNotEquals(state.getLocation().getPosition(), forwardState.getLocation().getPosition());
        assertEquals(state.getLocation().getPosition(), forwardState.reboot().getLocation().getPosition());
    }
}