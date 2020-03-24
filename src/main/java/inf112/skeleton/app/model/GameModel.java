package inf112.skeleton.app.model;

import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameModel {

    private LinkedList<Robot> robots;
    private MapHandler tiledMapHandler;
    private Player player;

    private ArrayList<Deque<GameState>> cardSteps = new ArrayList<>();
    private ArrayList<Deque<GameState>> tileSteps = new ArrayList<>();
    private ArrayList<Deque<GameState>> laserSteps = new ArrayList<>();


    public GameModel(String map_filename) {
        robots = new LinkedList<>();
        robots.add(new Robot(new Location(new RVector2(15, 5), Direction.WEST)));
        robots.add(new Robot(new Location(new RVector2(15, 6), Direction.WEST)));
        robots.add(new Robot(new Location(new RVector2(14, 3), Direction.WEST)));
        robots.add(new Robot(new Location(new RVector2(14, 8), Direction.WEST)));
        player = new Player();
        player.generateCardHand();
        for (int i = 0; i < 5; i++) {
            cardSteps.add(new LinkedList<>());
            tileSteps.add(new LinkedList<>());
            laserSteps.add(new LinkedList<>());
        }
        tiledMapHandler = new MapHandler(map_filename);
    }

    public Robot getRobot(int index) {
        return robots.get(index);
    }

    public MapHandler getTiledMapHandler() {
        return this.tiledMapHandler;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void endTurn() {
        GameState gameState = getGameState();

        //Does the logic, goes through each robot in the list for each phase.
        //gameState is a list off all robot's state, robotState is the specific robot being done a move for.
        for (int i = 0; i < 5; i++) {

            for (Robot robot : robots) {
                doCard(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, cardSteps.get(i));
            }
            for (Robot robot : robots) {
                doTiles(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, tileSteps.get(i));
            }
            for (Robot robot : robots) {
                System.out.println("test do laser i phase " + i + gameState);
                doLaser(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, laserSteps.get(i));
            }
        }

        int delay = 0;
        for (int i = 0; i < 5; i++) {
            scheduleDoCardTimed(delay, i);
            delay += cardSteps.get(i).size();
            scheduleDoTilesTimed(delay, i);
            delay += tileSteps.get(i).size();
        }
        player.generateCardHand();
    }

    private void doTiles(int phaseNumber, GameState initialState, StateInfo robotState) {
        Location loc = robotState.location.copy();
        // Calculate next steps based on current position
        GameState newState;
        TileType currentTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                newState = initialState.updateState(robotState.updateLocation(loc.moveDirection(currentTileDirection)));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case CONVEYOR_EXPRESS:
                newState = initialState.updateState(robotState.updateLocation(loc.moveDirection(currentTileDirection)));
                tileSteps.get(phaseNumber).add(newState);

                loc = newState.getState(robotState.robot).location;
                StateInfo newRobotState = newState.getState(robotState.robot);

                TileType nextTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.equals(nextTileType)) {
                    newState = newState.updateState(newRobotState.updateLocation(loc.moveDirection(nextTileDirection)));
                    tileSteps.get(phaseNumber).add(newState);
                }
                break;
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(loc.getPosition(), loc.getDirection().right());
                newState = initialState.updateState(robotState.updateLocation(turnRight));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(loc.getPosition(), loc.getDirection().left());
                newState = initialState.updateState(robotState.updateLocation(turnLeft));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case HOLE:
                newState = initialState.updateState(robotState.updateLifeStates(true));
                tileSteps.get(phaseNumber).add(newState); // the robot died, so it has no position
                //return tileSteps; // end the phase early
                break;
            default:
                break;
        }
    }

    private void doCard (int phaseNumber, GameState initialState, StateInfo robotState) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
            if ((card != null) && !(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(robotState.location))) {
                Location loc = robotState.location.copy();
                GameState newState = initialState.updateState(robotState.updateLocation(card.instruction(loc)));
                cardSteps.get(phaseNumber).add(newState);
            }
    }

    public void scheduleDoCardTimed(int delay, int phase) {
        if (cardSteps.get(phase).size() != 0) {
            Timer.Task doCardTimed = new Timer.Task() {
                @Override
                public void run() {
                    GameState gameState = cardSteps.get(phase).remove();
                    for (StateInfo stateInfo : gameState.stateInfos) {
                        stateInfo.robot.updateState(stateInfo);
                    }
                }
            };
            Timer.instance().scheduleTask(doCardTimed, delay, 1, cardSteps.get(phase).size() - 1);
        }
    }

    public  void scheduleDoTilesTimed(int delay, int phase) {
        if (tileSteps.get(phase).size() != 0) {
            Timer.Task doTilesTimed = new Timer.Task() {
                @Override
                public void run() {
                    GameState gameState = tileSteps.get(phase).remove();
                    for (StateInfo stateInfo : gameState.stateInfos)
                    stateInfo.robot.updateState(stateInfo);
                }
            };
            Timer.instance().scheduleTask(doTilesTimed, delay, 1, tileSteps.get(phase).size() - 1);
        }
    }



    private GameState updateLastState (GameState state, Deque<GameState> states) {
        if (states.peekLast() != null) {return states.peekLast();}
        return state;
    }
    //Lage getLaser som finner hvor alle laserene er så if robotInPath ta damage istedenfor, phase
    private void doLaser (int phaseNumber, GameState state, StateInfo robotState) {
        StateInfo copy = robotState.copy();

            while (!tiledMapHandler.wallInPath(copy.location.forward()) &&
                    !tiledMapHandler.outOfBounds(copy.location.forward())) {

                copy.location = copy.location.forward();
                if (tiledMapHandler.robotInPath(copy.location, robots)) {
                    System.out.println("you get shot by a laser take 1dmg");
                    laserSteps.get(phaseNumber).add(state.updateState(robotState.updateDamage(1)));
                    break;
                }


            }
    }



    public boolean inTestMode() {
        return true;
    }

    public GameState getGameState() {
        GameState state = new GameState(robots.size());
        for (Robot robot : robots) {
            state.add(robot.getState());
        }
        return state;
    }

    public List<Robot> getRobots() {
        return robots;
    }
}
