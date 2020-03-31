package inf112.skeleton.app.model;

public class GameState {
    public RobotState[] robotStates;
    private int size;
    private int added;

    public GameState(int size) {
        this.size = size;
        robotStates = new RobotState[size];
        added = 0;
    }

    public void add(RobotState robotState) {
        robotStates[added] = robotState.copy();
        added++;
    }

    //you return a new gameState when a step happens (atleast one thing happens)
    public GameState update(RobotState robotState) {
        GameState newState = new GameState(size);
        for (int i = 0; i < size; i++) {
            if (robotStates[i].getRobot().equals(robotState.getRobot())) {
                newState.add(robotState);
            } else {
                newState.add(robotStates[i]);
            }
        }
        return newState;
    }

    //For when more than one thing happens in a single step, you can edit the state instead of updating.
    //For example when one robot pushes another robot, or if we decide to display conveyors at the same time for all robots
    public void edit(RobotState robotState) {
        for (int i = 0; i < size; i++) {
            if (robotStates[i].getRobot().equals(robotState.getRobot())) {
                robotStates[i] = robotState;
            }
        }
    }

    //TODO make hashmap in GameState so for loop is not required
    public RobotState getState(Robot robot) {
        for (int i = 0; i < size; i++) {
            if (robotStates[i].getRobot().equals(robot)) {
                return robotStates[i];
            }
        }
        return null;
    }
}
