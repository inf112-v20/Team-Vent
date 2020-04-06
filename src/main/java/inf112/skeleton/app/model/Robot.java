package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;


public class Robot {
    private String name = "Anonymous";
    private RobotState state;
    private int robotLife;
    public Robot(Location location) {
        this.state = new RobotState(this, location, getMaxHP(), false,
                0, location);
        this.robotLife = 3;
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

    public boolean alive() {
        return !state.getDead();
    }

    public String robotHPAsString(int args) {
        return "robot's HP: " + args + "\n";
    }

    public int getRobotLife(){return robotLife;}

    public String robotLifeAsString(int args) {
        return "robot's life: " + args + "\n";
    }

    public static int getMaxHP() {
        return 9;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
