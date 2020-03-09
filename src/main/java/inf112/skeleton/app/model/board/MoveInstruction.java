package inf112.skeleton.app.model.board;

import inf112.skeleton.app.model.Robot;

public class MoveInstruction {
    public Location location;
    public Robot robot;

    public MoveInstruction (Location location, Robot robot) {
        this.location = location;
        this.robot = robot;
    }
}
