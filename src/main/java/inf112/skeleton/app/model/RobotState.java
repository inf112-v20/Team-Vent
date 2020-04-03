package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Location;

public class RobotState {
    private int capturedFlags;
    private Location saveLocation;
    private Location location;
    private int hp;
    private boolean dead;
    private Robot robot;

    public RobotState(Robot robot, Location location, int hp, boolean dead, int capturedFlags, Location saveLocation) {
        this.robot = robot;
        this.location = location;
        this.hp = hp;
        this.dead = dead;
        this.saveLocation = saveLocation;
        this.capturedFlags = capturedFlags;
    }

    public RobotState updateLocation(Location loc) {
        RobotState other = this.copy();
        other.location = loc.copy();
        return other;
    }

    public RobotState updateHP(int decrease) {
        RobotState other = this.copy();
        other.hp += decrease;
        if (other.hp <= 0){
            other.dead = true;
        }
        return other;
    }

    public RobotState copy() {
        return new RobotState(robot, location.copy(), this.hp, this.dead, capturedFlags, saveLocation);
    }

    public RobotState updateDead(boolean dead) {
        RobotState other = this.copy();
        other.dead = dead;
        return other;
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
        return dead;
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
    public RobotState reboot() {
        RobotState other = this.copy();
        other.dead = false;
        other.location = this.saveLocation;
        return other;
    }

    public RobotState updateSaveLocation() {
        RobotState other = this.copy();
        other.saveLocation = this.location;
        return other;
    }
}
