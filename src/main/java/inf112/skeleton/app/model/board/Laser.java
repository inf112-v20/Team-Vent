package inf112.skeleton.app.model.board;

import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.StateInfo;

import java.util.LinkedList;

public class Laser{
    private MapHandler tiledMapHandler;
    private StateInfo state;
    private LinkedList<Robot> robots;
    public Laser(int phaseNumber, StateInfo state, LinkedList<Robot> robots){
        this.state = state;
        this.robots = robots;
        System.out.println(state.location.forward());
        }



    public void move() {
        while (!tiledMapHandler.wallInPath(state.location.forward())){
            System.out.println("Test før if");
           state.location = state.location.forward();
           if (tiledMapHandler.robotInPath(state.location, robots)){
               state.robot.takeDamage();
               break;
           }
        }

    }
}
