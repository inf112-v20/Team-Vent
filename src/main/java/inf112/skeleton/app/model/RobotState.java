package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;

public class RobotState {
    private int capturedFlags;
    private Location saveLocation;
    private Location location;
    private int hp;
    private Robot robot;
    private int lives;

    public RobotState(Robot robot, Location location, int hp, int lives, int capturedFlags, Location saveLocation) {
        this.robot = robot;
        this.location = location;
        this.hp = hp;
        this.lives = lives;
        this.saveLocation = saveLocation;
        this.capturedFlags = capturedFlags;
    }

    public RobotState updateLocation(Location loc) {
        RobotState other = this.copy();
        other.location = loc.copy();
        return other;
    }

    public RobotState updateHP(int difference) {
        if (this.hp + difference <= 0) {
            return this.updateDead();
        }
        RobotState other = this.copy();
        other.hp = Math.min(this.hp + difference, Robot.getMaxHP());
        return other;
    }

    public RobotState copy() {
        return new RobotState(robot, location.copy(), this.hp, this.lives, capturedFlags, saveLocation.copy());
    }

    public RobotState updateDead() {
        if (this.getDead()) return this.copy();
        RobotState other = this.copy();
        other.hp = 0;
        other.lives = Math.max(other.lives - 1, 0);
        return other.updateLocation(new Location(new RVector2(-1, -1), Direction.NORTH));
    }

    public Robot getRobot() {
        return robot;
    }

    public int getCapturedFlags() {
        return capturedFlags;
    }

    public int getHp() {
        return hp;
    }

    public Location getLocation() {
        return location.copy();
    }

    public Location getSaveLocation() {
        return saveLocation.copy();
    }

    public boolean getDead() {
        return hp <= 0;
    }

    public RobotState visitFlag() {
        RobotState other = this.copy();
        other.saveLocation = this.location;
        other.capturedFlags += 1;
        return other;
    }

    /**
     * Come alive if dead and move to the most recently captured flag, or to the starting position if there are no
     * captured flags
     */
    public RobotState reboot(Location loc) {
        if (this.lives <= 0) return this; // the robot is out of the game
        RobotState other = this.copy();
        other.location = loc;
        other.hp = Robot.getMaxHP();
        return other;
    }

    public RobotState updateSaveLocation() {
        RobotState other = this.copy();
        other.saveLocation = this.location;
        return other;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "RobotState{" +
                robot.getName().toUpperCase() +
                ": location=" + location +
                ", hp=" + hp +
                ", lives=" + lives +
                '}';
    }
}
