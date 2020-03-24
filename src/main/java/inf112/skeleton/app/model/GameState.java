package inf112.skeleton.app.model;

public class GameState {
    int size;
    StateInfo[] stateInfos;
    int added;

    public GameState (int size) {
        this.size = size;
        stateInfos = new StateInfo[size];
        int added = 0;
    }

    public void add (StateInfo stateInfo) {
        stateInfos[added] = stateInfo.copy();
        added++;
    }

    //you return a new gameState when a step happens (atleast one thing happens)
    public GameState updateState(StateInfo stateInfo) {
        GameState newState = new GameState(size);
        for (int i = 0; i < size; i++) {
            if (stateInfos[i].robot.equals(stateInfo.robot)) {
                newState.add(stateInfo);
            }
            else {
                newState.add(stateInfos[i]);
            }
        }
        return newState;
    }

    //For when more than one thing happens in a single step, you can edit the state instead of updating.
    public void editState(StateInfo stateInfo) {
        for (int i = 0; i < size; i++) {
            if (stateInfos[i].robot.equals(stateInfo.robot)) {
                stateInfos[i] = stateInfo;
            }
        }
    }

    //TODO make hashmap in GameState so for loop is not required
    public StateInfo getState (Robot robot) {
        for (int i = 0; i < size; i++) {
            if (stateInfos[i].robot.equals(robot)) {
                return stateInfos[i];
            }
        }
        return null;
    }

}
