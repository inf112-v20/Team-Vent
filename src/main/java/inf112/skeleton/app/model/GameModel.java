package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

    private final int PHASES = 5;
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
        for (int i = 0; i < PHASES; i++) {
            cardSteps.add(new LinkedList<>());
            tileSteps.add(new LinkedList<>());
            laserSteps.add(new LinkedList<>());
        }
        tiledMapHandler = new MapHandler(map_filename);
    }

    public MapHandler getTiledMapHandler() {
        return this.tiledMapHandler;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void endTurn() {
        GameState gameState = getInitialGameState();

        //Does the logic, goes through each robot in the list for each phase.
        //gameState is a list off all robot's state, robotState is the specific robot being done a move for.
        for (int i = 0; i < PHASES; i++) {

            for (Robot robot : robots) {
                doCard(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, cardSteps.get(i));
            }
            for (Robot robot : robots) {
                doTiles(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, tileSteps.get(i));
            }
            for (Robot robot : robots) {
                doLaser(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, laserSteps.get(i));
            }
            for (Robot robot : robots) {
                doFlag(gameState.getState(robot));
            }
        }

        int delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleSteps(delay, i, cardSteps);
            delay += cardSteps.get(i).size();
            scheduleSteps(delay, i, tileSteps);
            delay += tileSteps.get(i).size();
            laserSteps.get(i).clear();
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
                newState = initialState.updateState(robotState.updateDead(true));
                tileSteps.get(phaseNumber).add(newState);
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

    private void doFlag(StateInfo state) {
        if (state.dead) return;
        TiledMapTileLayer.Cell cell = getTiledMapHandler().getFlagLayer().getCell(state.location.getPosition().getX(),
                state.location.getPosition().getY());
        if (cell == null) return; // there is no flag here
        int flagNumber = (int) cell.getTile().getProperties().get("number");
        state.robot.visitFlag(flagNumber, state.location);
        if (state.robot.getNumberOfFlags() == getTiledMapHandler().getNumberOfFlags()) {
            Gdx.app.log(this.getClass().getName(), "TODO: IMPLEMENT WINNING");
        }
    }

    public void scheduleSteps(int delay, int phase, ArrayList<Deque<GameState>> steps) {
        if (steps.get(phase).isEmpty()) return;
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                GameState gameState = steps.get(phase).remove();
                for (StateInfo stateInfo : gameState.stateInfos) {
                    stateInfo.robot.updateState(stateInfo);
                }
            }
        };
        Timer.instance().scheduleTask(task, delay, 1, steps.get(phase).size() - 1);
    }

    private GameState updateLastState (GameState state, Deque<GameState> states) {
        if (states.peekLast() != null) {return states.peekLast();}
        return state;
    }
    //Lage getLaser som finner hvor alle laserene er så if robotInPath ta damage istedenfor, phase
    private void doLaser (int phaseNumber, GameState state, StateInfo robotState) {
        Location copy = robotState.location.copy();

            while (!tiledMapHandler.wallInPath(copy.forward()) &&
                    !tiledMapHandler.outOfBounds(copy.forward())) {

                copy = copy.forward();
                if (tiledMapHandler.robotInPath(copy, state)) {
                    System.out.println("Someone got shot by a laser take 1dmg");
                    laserSteps.get(phaseNumber).add(state.updateState(robotState.updateDamage(1)));
                    break;
                }
            }
    }

    public GameState getInitialGameState() {
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
