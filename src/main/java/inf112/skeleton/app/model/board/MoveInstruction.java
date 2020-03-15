package inf112.skeleton.app.model.board;

import inf112.skeleton.app.model.Robot;
import java.util.function.Consumer;

public class MoveInstruction {
    public Consumer<Robot> instruction;
    public Robot robot;

    public MoveInstruction (Consumer<Robot> instruction, Robot robot) {
        this.instruction = instruction;
        this.robot = robot;
    }
}
