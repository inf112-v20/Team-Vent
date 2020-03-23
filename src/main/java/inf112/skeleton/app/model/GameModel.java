package inf112.skeleton.app.model;

import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
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

    private ArrayList<Deque<StateInfo>> cardSteps = new ArrayList<>();
    private ArrayList<Deque<StateInfo>> tileSteps = new ArrayList<>();
    private ArrayList<Deque<StateInfo>> laserSteps = new ArrayList<>();


    public GameModel(String map_filename) {
        robots = new LinkedList<>();
        robots.add(new Robot());
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
        StateInfo state = getRobots().get(0).getState(); // fixme: change to support multi-player

        for (int i = 0; i < PHASES; i++) {
            doCard(i, state);
            state = updateLastState(state, cardSteps.get(i));
            doTiles(i, state);
            state = updateLastState(state, tileSteps.get(i));
            doLaser(i, state);
            state = updateLastState(state, laserSteps.get(i));
        }

        int delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleDoCardTimed(delay, i);
            delay += cardSteps.get(i).size();
            scheduleDoTilesTimed(delay, i);
            delay += tileSteps.get(i).size();
        }
        player.generateCardHand();
    }

    private void doTiles(int phaseNumber, StateInfo initialState) {
        Location loc = initialState.location.copy();
        // Calculate next steps based on current position
        TileType currentTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                tileSteps.get(phaseNumber).add(initialState.updateLocation(loc.moveDirection(currentTileDirection)));
                break;
            case CONVEYOR_EXPRESS:
                tileSteps.get(phaseNumber).add(initialState.updateLocation(loc.moveDirection(currentTileDirection)));
                loc = tileSteps.get(phaseNumber).getLast().location.copy();
                TileType nextTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.equals(nextTileType)) {
                    tileSteps.get(phaseNumber).add(initialState.updateLocation(loc.moveDirection(nextTileDirection)));
                }
                break;
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(loc.getPosition(), loc.getDirection().right());
                tileSteps.get(phaseNumber).add(initialState.updateLocation(turnRight));
                break;
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(loc.getPosition(), loc.getDirection().left());
                tileSteps.get(phaseNumber).add(initialState.updateLocation(turnLeft));
                break;
            case HOLE:
                tileSteps.get(phaseNumber).add(initialState.updateLifeStates(true)); // the robot died, so it has no position
                break;
            default:
                break;
        }
    }

    private void doCard (int phaseNumber, StateInfo stateinfo) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
            if ((card != null) && !(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(stateinfo.location))) {
                Location loc = stateinfo.location.copy();
                cardSteps.get(phaseNumber).add(stateinfo.updateLocation(card.instruction(loc)));
            }
    }

    public void scheduleDoCardTimed(int delay, int phase) {
        scheduleSteps(delay, phase, cardSteps);
    }

    public  void scheduleDoTilesTimed(int delay, int phase) {
        scheduleSteps(delay, phase, tileSteps);
    }

    public void scheduleSteps(int delay, int phase, ArrayList<Deque<StateInfo>> steps) {
        if (steps.get(phase).isEmpty()) return;
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                StateInfo stateInfo = steps.get(phase).remove();
                stateInfo.robot.updateState(stateInfo);
            }
        };
        Timer.instance().scheduleTask(task, delay, 1, steps.get(phase).size() - 1);
    }

    private StateInfo updateLastState (StateInfo state, Deque<StateInfo> states) {
        if (states.peekLast() != null) {return states.peekLast();}
        return state;
    }
    //Lage getLaser som finner hvor alle laserene er så if robotInPath ta damage istedenfor, phase
    private void doLaser (int phaseNumber, StateInfo state) {
        StateInfo copy = state.copy();

            while (!tiledMapHandler.wallInPath(copy.location.forward()) &&
                    !tiledMapHandler.outOfBounds(copy.location.forward())) {

                copy.location = copy.location.forward();
                if (tiledMapHandler.robotInPath(copy.location, robots)) {
                    System.out.println("you get shot by a laser take 1dmg");
                    state.updateDamage(1);
                    break;
                }


            }
    }

    public List<Robot> getRobots() {
        return robots;
    }
}
