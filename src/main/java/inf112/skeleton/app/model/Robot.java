package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;


public class Robot {
    private Location location;
    private int damage;
    private boolean dead;
    private int capturedFlags = 0; // number of flags that have been visited
    private Location lastSaved;  // the location the robot will return to if it dies

    public Robot(Location location) {
        this.location = location;
        this.lastSaved = location;
        this.damage = 0;
        this.dead = false;
    }

    public Robot() {
        this(new Location());
    }

    public Location getLocation() {
        return this.location.copy();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return getLocation().getDirection();
    }

    public int getX() {
        return getLocation().getPosition().getX();
    }

    public int getY() {
        return getLocation().getPosition().getY();
    }

    public void updateState(StateInfo state) {
        location = state.location.copy();
        damage = state.damage;
        dead = state.dead;
    }

    public StateInfo getState() {
        return new StateInfo(this, location, damage, dead);
    }

    public Robot copy() {
        return new Robot(this.location.copy());
    }

    public int getNumberOfFlags() {
        return capturedFlags;
    }

    public void visitFlag(int flagNumber, Location location) {
        if (flagNumber == capturedFlags + 1) { // if this flag is the next flag
            this.lastSaved = location;
            this.capturedFlags += 1;
            System.out.println("Visited flag number " + capturedFlags);
        }
    }

    /**
     * Come alive if dead and move to the most recently captured flag, or to the starting position if there are no
     * captured flags
     */
    public void reboot() {
        this.dead = false;
        this.location = this.lastSaved;
    }

    public boolean alive() {
        return !dead;
    }
}

