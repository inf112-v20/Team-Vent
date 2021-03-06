package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;

public class Robot {
    private String name = "Anonymous";
    private RobotState state;

    public Robot(Location location) {
        this.state = new RobotState(this, location, getMaxHP(), getMaxLives(),
                0, location);
    }

    public Robot() {
        this(new Location());
    }

    public static int getMaxLives() {
        return 3;
    }

    public static int getMaxHP() {
        return 10;
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

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
