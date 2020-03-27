package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Location;

public class RobotState {
    private int capturedFlags;
    private Location saveLocation;
    private Location location;
    private int damage;
    private boolean dead;
    private Robot robot;

    public RobotState(Robot robot, Location location, int damage, boolean dead, int capturedFlags, Location saveLocation) {
        this.robot = robot;
        this.location = location;
        this.damage = damage;
        this.dead = dead;
        this.saveLocation = saveLocation;
        this.capturedFlags = capturedFlags;
    }

    public RobotState updateLocation(Location loc) {
        RobotState other = this.copy();
        other.location = loc.copy();
        return other;
    }

    public RobotState updateDamage(int damage) {
        RobotState other = this.copy();
        other.damage = damage;
        return other;
    }

    public RobotState copy() {
        return new RobotState(robot, location.copy(), this.damage, this.dead, capturedFlags, saveLocation);
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

    public int getDamage() {
        return damage;
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

    /**
     * Update the save location and increase the number of captured flags if this flag is the next one
     *
     * @param number   the flag number
     * @param location the location of the flag
     */
    public void visitFlag(int number, Location location) {
        if (number == capturedFlags + 1) { // if this flag is the next flag
            this.saveLocation = location;
            this.capturedFlags += 1;
            System.out.println("Visited flag number " + capturedFlags);
        }
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
}
