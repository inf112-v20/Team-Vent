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
        other.damage = other.damage + damage;
        int hpCopy = getRobot().getRobotHP();
        int dmgTaken = hpCopy + damage;
        if (dmgTaken < 0 || dmgTaken == 0 || hpCopy < 1){
            getRobot().setRobotLife(getRobot().getRobotLife()-1);
            getRobot().setRobotHP(9);
            dmgTaken=0;

        }
        else {
            getRobot().setRobotHP(dmgTaken);
        }

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
}
