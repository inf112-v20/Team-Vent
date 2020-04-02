package inf112.skeleton.app.model;

import java.util.HashMap;

public class GameState {
    private HashMap<Robot, RobotState> robotMap;

    public GameState() {
        robotMap = new HashMap<>();
    }

    public void add(RobotState robotState) {
        robotMap.put(robotState.getRobot(), robotState.copy());
    }

    public GameState update(RobotState robotState) {
        GameState other = this.copy();
        other.robotMap.put(robotState.getRobot(), robotState);
        return other;
    }

    public void edit(RobotState robotState) {
        robotMap.put(robotState.getRobot(), robotState);
    }

    public RobotState getState(Robot robot) {
        return robotMap.get(robot).copy();
    }

    public GameState copy() {
        GameState other = new GameState();
        for (RobotState state : robotMap.values()) {
            other.robotMap.put(state.getRobot(), state.copy());
        }
        return other;
    }

    public java.util.Collection<RobotState> getRobotStates() {
        return robotMap.values();
    }
}
