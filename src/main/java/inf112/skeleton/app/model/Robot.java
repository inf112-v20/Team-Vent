package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;


public class Robot {
    private RobotState state;
    private int robotHP;
    private int robotLife;
    public Robot(Location location) {
        this.state = new RobotState(this, location, 0, false,
                0, location);
        this.robotHP = 9;
        this.robotLife = 3;
    }

    public Robot(RobotState state) {
        this.state = state;
    }

    public Robot() {
        this(new Location());
    }


    public Location getLocation() {
        return this.state.getLocation();
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

    public void updateState(RobotState state) {
        this.state = state;
    }

    public RobotState getState() {
        return state.copy();
    }

    public Robot copy() {
        return new Robot(getLocation().copy());
    }

    public int getCapturedFlags() {
        return state.getCapturedFlags();
    }

    public boolean alive() {
        return !state.getDead();
    }

    public void setRobotHP(int healthPoints) {
        this.robotHP = healthPoints;
    }
    public int getRobotHP() { return robotHP;}

    public String robotHPAsString(int args) {
        String p = "robot's HP: " + args + "\n";
        return p;
    }
    public void setRobotLife(int lifePoints) {
        this.robotLife = lifePoints;
    }
    public int getRobotLife(){return robotLife;}

    public String robotLifeAsString(int args) {
        String l = "robot's life: " + args + "\n";
        return l;
    }
}
