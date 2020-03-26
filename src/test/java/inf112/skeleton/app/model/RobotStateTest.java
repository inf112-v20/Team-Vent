package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RobotStateTest {
    RobotState state;

    @Before
    public void beforeEach() {
        Robot robot = new Robot();
        state = new RobotState(robot, robot.getLocation(), 0, false, 0, robot.getLocation());
    }

    @Test
    public void visitingTheWrongFlagHasNoEffect() {
        state.visitFlag(state.getCapturedFlags() + 10, state.getLocation().forward());
        assertEquals(0, state.getCapturedFlags());
    }

    @Test
    public void visitingTheRightFlagIncreasesNumberOfFlags() {
        Location flagLocation = state.getLocation().forward();
        state.visitFlag(state.getCapturedFlags() + 1, flagLocation);
        assertEquals(1, state.getCapturedFlags());
    }

    @Test
    public void rebootingResetsToLastFlag() {
        Location flagLocation = state.getLocation().forward();
        state.visitFlag(state.getCapturedFlags() + 1, flagLocation);
        state.reboot();
        assertEquals(flagLocation, state.getLocation());
    }
}