package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameState {
    private HashMap<Robot, RobotState> robotMap;
    private List<LaserBeam> laserBeams = new LinkedList<>();

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

    public void addLaserBeam(Location shooterLocation, RVector2 targetPosition, boolean shooterIsRobot) {
        if (shooterLocation.getPosition().getX() != (targetPosition.getX()) &&
                shooterLocation.getPosition().getY() != (targetPosition.getY())) {
            throw new IllegalArgumentException("Points must align vertically or horizontally");
        } else if (shooterLocation.getPosition().equals(targetPosition)) {
            throw new IllegalArgumentException("Points must be apart");
        }
        laserBeams.add(new LaserBeam(shooterLocation, targetPosition, shooterIsRobot));
    }

    public GameState clearLaserBeams() {
        GameState other = this.copy();
        other.laserBeams.clear();
        return other;
    }

    public List<LaserBeam> getLaserBeams() {
        return laserBeams;
    }

    public RobotState getState(Robot robot) {
        return robotMap.get(robot).copy();
    }

    public GameState copy() {
        GameState other = new GameState();
        for (RobotState state : robotMap.values()) {
            other.robotMap.put(state.getRobot(), state.copy());
        }
        other.laserBeams = new LinkedList<>(this.laserBeams);
        return other;
    }

    public java.util.Collection<RobotState> getRobotStates() {
        return robotMap.values();
    }

    public static class LaserBeam {
        public final Location origin;
        public final RVector2 target;
        public boolean shooterIsRobot;

        public LaserBeam(Location shooterLocation, RVector2 targetPosition, boolean shooterIsRobot) {
            this.origin = shooterLocation;
            this.target = targetPosition;
            this.shooterIsRobot = shooterIsRobot;
        }
    }
}
