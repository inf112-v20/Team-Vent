package inf112.skeleton.app.model;

import java.util.IdentityHashMap;

public class GameState {
    private IdentityHashMap<Robot, RobotState> robotMap;

    public GameState() {
        robotMap = new IdentityHashMap<>();
    }

    public void add(RobotState robotState) {
        robotMap.put(robotState.getRobot(), robotState.copy());
    }

    public GameState update(RobotState robotState) {
        GameState other = this.copy();
        other.robotMap.put(robotState.getRobot(), robotState);
        return other;
    }

    public RobotState getState(Robot robot) {
        return robotMap.get(robot).copy();
    }

    public GameState copy() {
        GameState other = new GameState();
        other.robotMap = new IdentityHashMap<>(this.robotMap);
        return other;
    }

    public java.util.Collection<RobotState> getRobotStates() {
        return robotMap.values();
    }
}
