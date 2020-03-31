package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveBackwardCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {

    private static final boolean ENABLE_LOGGING = true;
    private final int PHASES = 5;
    private final LinkedList<Robot> robots;
    private final MapHandler tiledMapHandler;
    private final Player player;

    private final ArrayList<Deque<GameState>> cardSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> tileSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> laserSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> flagVisitSteps = new ArrayList<>();
    Timer timer = new Timer(true);

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
            flagVisitSteps.add(new LinkedList<>());
        }
        tiledMapHandler = new MapHandler(map_filename);
    }

    public MapHandler getMapHandler() {
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
                doRobotLaser(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, laserSteps.get(i));
            }
            for (Robot robot : robots) {
                doFlag(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, flagVisitSteps.get(i));
            }
            doWallLasers(i, gameState);
            gameState = updateLastState(gameState, laserSteps.get(i));
        }

        int delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleSteps(delay, i, cardSteps);
            delay += cardSteps.get(i).size();
            scheduleSteps(delay, i, tileSteps);
            delay += tileSteps.get(i).size();
            laserSteps.get(i).clear();
            scheduleSteps(delay, i, flagVisitSteps); // no additional delay
        }
        player.generateCardHand();
    }

    private void doTiles(int phaseNumber, GameState initialState, RobotState robotState) {
        Location loc = robotState.getLocation();
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

                loc = newState.getState(robotState.getRobot()).getLocation();
                RobotState newRobotState = newState.getState(robotState.getRobot());

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

    private void doCard(int phaseNumber, GameState initialState, RobotState robotState) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        if (cardCanBePlayed(card, robotState.getLocation(), robotState)) {
            Location loc = robotState.getLocation().copy();
            GameState newState = initialState.updateState(robotState.updateLocation(card.instruction(loc)));
            cardSteps.get(phaseNumber).add(newState);
        }

    }

    private boolean cardCanBePlayed(IProgramCard card, Location loc, RobotState robotState) {
        Location cop = loc.copy();

        boolean canBePlayed = true;
        if (card == null) {
            canBePlayed = false;
        } else if ((card instanceof MoveForwardCard && tiledMapHandler.wallInPath(cop))) {
            canBePlayed = false;
        } else if ((card instanceof MoveBackwardCard &&
                tiledMapHandler.wallInPath(robotState.getLocation().rotateLeft().rotateLeft()))) {
            canBePlayed = false;
        }
        return canBePlayed;
    }

    private void doFlag(int phaseNumber, GameState initialState, RobotState robotState) {
        if (robotState.getDead()) return;
        TiledMapTileLayer.Cell cell = getMapHandler().getFlagLayer().getCell(robotState.getLocation().getPosition().getX(),
                robotState.getLocation().getPosition().getY());
        if (cell == null) return; // there is no flag here
        int flagNumber = (int) cell.getTile().getProperties().get("number");
        RobotState newRobotState = robotState.copy();
        newRobotState.visitFlag(flagNumber, robotState.getLocation());
        GameState newState = initialState.updateState(newRobotState);
        flagVisitSteps.get(phaseNumber).add(newState);
    }

    public void scheduleSteps(int delay, int phase, ArrayList<Deque<GameState>> steps) {
        if (steps.get(phase).isEmpty()) return;
        for (int i = 0; i < steps.get(phase).size(); i++) {
            timer.schedule(doStep(phase, steps), delay * 1000 + 1000 * i);
        }
    }

    public TimerTask doStep(int phase, ArrayList<Deque<GameState>> steps) {
        return new TimerTask() {
            @Override
            public void run() {
                GameState gameState = steps.get(phase).remove();
                for (RobotState stateInfo : gameState.robotStates) {
                    stateInfo.getRobot().updateState(stateInfo);
                }
            }
        };
    }

    private GameState updateLastState(GameState state, Deque<GameState> states) {
        if (states.peekLast() != null) {
            return states.peekLast();
        }
        return state;
    }

    private void doRobotLaser(int phaseNumber, GameState gameState, RobotState robotState) {
        if (robotState.getDead()) return;
        Robot toShoot = getMapHandler().robotInLineOfVision(robotState.getLocation(), gameState);
        if (toShoot != null) {
            log(robotState.getRobot().toString() + " fired at " + toShoot.toString());
            RobotState shotState = gameState.getState(toShoot).updateDamage(-1);
            laserSteps.get(phaseNumber).add(gameState.updateState(shotState));
        }
    }

    private void doWallLasers(int phaseNumber, GameState gameState) {
        for (Location laserLocation : getMapHandler().getLasersLocations()) {
            Robot toShoot = getMapHandler().robotInLineOfVision(laserLocation, gameState);
            if (toShoot != null) {
                log(toShoot.toString() + " was shot by the wall laser at " + laserLocation.getPosition().toString());
                RobotState shotRobotState = gameState.getState(toShoot).updateDamage(-1);
                laserSteps.get(phaseNumber).add(gameState.updateState(shotRobotState));
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

    private void log(String message) {
        if (ENABLE_LOGGING) {
            Gdx.app.log(this.getClass().getName(), message);
        }
    }
}
