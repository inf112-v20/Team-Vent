package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.Card;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.*;

public class GameModel {

    private static final boolean ENABLE_LOGGING = false;
    private final int PHASES = 5;
    private final LinkedList<Robot> robots;
    private final MapHandler mapHandler;
    private final Player player;
    private final ArrayList<Deque<GameState>> cardSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> tileSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> robotLaserSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> wallLaserSteps = new ArrayList<>();
    Timer timer = new Timer(true);
    private List<Player> players;

    public GameModel(String map_filename, int numberOfPlayers) {
        mapHandler = new MapHandler(map_filename);
        if (mapHandler.getStartLocations().size() < numberOfPlayers) {
            throw new IllegalStateException(String.format("There are not enough starting locations for %d players", numberOfPlayers));
        }
        // initialize players and robots
        players = new LinkedList<>();
        robots = new LinkedList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player p = new Player(new Robot(mapHandler.getStartLocations().get(i)));
            p.generateCardHand();
            players.add(p);
            robots.add(p.getRobot());
        }
        // initialize phase lists
        for (int i = 0; i < PHASES; i++) {
            cardSteps.add(new LinkedList<>());
            tileSteps.add(new LinkedList<>());
            robotLaserSteps.add(new LinkedList<>());
            wallLaserSteps.add(new LinkedList<>());
        }
        // legacy code
        player = players.get(0);
    }

    public MapHandler getMapHandler() {
        return this.mapHandler;
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
            doRobotLasers(i, gameState);
            gameState = updateLastState(gameState, robotLaserSteps.get(i));
            doWallLasers(i, gameState);
            gameState = updateLastState(gameState, wallLaserSteps.get(i));
        }
        gameState = doFlags(gameState);

        int delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleSteps(delay, i, cardSteps);
            delay += cardSteps.get(i).size();
            scheduleSteps(delay, i, tileSteps);
            delay += tileSteps.get(i).size();
            scheduleSteps(delay, i, robotLaserSteps);
            delay += robotLaserSteps.get(i).size();
            scheduleSteps(delay, i, wallLaserSteps);
            delay += wallLaserSteps.get(i).size();
        }
        scheduleStep(delay, PHASES, gameState); // after phases
        player.generateCardHand();
    }

    private void doTiles(int phaseNumber, GameState initialState, RobotState robotState) {
        Location loc = robotState.getLocation();
        // Calculate next steps based on current position
        GameState newState;
        TileType currentTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = mapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                newState = initialState.update(robotState.updateLocation(loc.moveDirection(currentTileDirection)));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case CONVEYOR_EXPRESS:
                newState = initialState.update(robotState.updateLocation(loc.moveDirection(currentTileDirection)));
                tileSteps.get(phaseNumber).add(newState);

                loc = newState.getState(robotState.getRobot()).getLocation();
                RobotState newRobotState = newState.getState(robotState.getRobot());

                TileType nextTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = mapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.equals(nextTileType)) {
                    newState = newState.update(newRobotState.updateLocation(loc.moveDirection(nextTileDirection)));
                    tileSteps.get(phaseNumber).add(newState);
                }
                break;
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(loc.getPosition(), loc.getDirection().right());
                newState = initialState.update(robotState.updateLocation(turnRight));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(loc.getPosition(), loc.getDirection().left());
                newState = initialState.update(robotState.updateLocation(turnLeft));
                tileSteps.get(phaseNumber).add(newState);
                break;
            case HOLE:
                newState = initialState.update(robotState.updateDead(true));
                tileSteps.get(phaseNumber).add(newState);
                break;
            default:
                break;
        }
    }

    private void doCard(int phaseNumber, GameState initialState, RobotState robotState) {
        Card card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        if (card == null) return;
        RobotState nextRobotState = robotState;
        switch (card) {
            case MOVE_THREE:
                nextRobotState = movedOne(nextRobotState);
                cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
                // no break
            case MOVE__TWO:
                nextRobotState = movedOne(nextRobotState);
                cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
                // no break
            case MOVE__ONE:
                nextRobotState = movedOne(nextRobotState);
                break;
            case BACK_UP:
                nextRobotState = backedUpOne(nextRobotState);
                break;
            case ROTATE_RIGHT:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().rotateRight());
                break;
            case ROTATE_LEFT:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().rotateLeft());
                break;
            case U_TURN:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().halfTurn());
                break;
            default:
                break;
        }
        cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
    }

    private RobotState movedOne(RobotState robotState) {
        if (!mapHandler.wallInPath(robotState.getLocation())) {
            robotState = robotState.updateLocation(robotState.getLocation().forward());
        }
        return robotState;
    }

    private RobotState backedUpOne(RobotState robotState) {
        if (!mapHandler.wallInPath(robotState.getLocation().halfTurn())) {
            robotState = robotState.updateLocation(robotState.getLocation().backward());
        }
        return robotState;
    }

    private GameState doFlags(GameState gameState) {
        GameState nextState = gameState;
        for (RobotState robotState : gameState.getRobotStates()) {
            if (robotState.getDead()) continue;
            Integer flagNumber = mapHandler.getFlag(robotState.getLocation().getPosition());
            if (flagNumber != null && flagNumber == robotState.getCapturedFlags() + 1) {
                nextState = nextState.update(robotState.visitFlag());
                log(String.format("%s visited flag number %d", robotState.getRobot().toString(), flagNumber));
            }
        }
        return nextState;
    }

    public void scheduleSteps(int delay, int phase, ArrayList<Deque<GameState>> steps) {
        for (int i = 0; i < steps.get(phase).size(); i++) {
            scheduleStep(delay, phase, steps.get(phase).remove());
        }
    }

    public void scheduleStep(int delay, int phase, GameState gameState) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (RobotState robotState : gameState.getRobotStates()) {
                    robotState.getRobot().updateState(robotState);
                }
            }
        }, delay * 200 + 200 * phase);
    }

    private GameState updateLastState(GameState state, Deque<GameState> states) {
        if (states.peekLast() != null) {
            return states.peekLast();
        }
        return state;
    }

    private void doRobotLasers(int phaseNumber, GameState gameState) {
        GameState next = gameState.copy();
        for (RobotState robotState : gameState.getRobotStates()) {
            if (robotState.getDead()) continue;
            Robot toShoot = getMapHandler().robotInLineOfVision(robotState.getLocation(), gameState);
            if (toShoot != null) {
                log(toShoot.toString() + " was shot by " + robotState.getRobot().toString());
                RobotState shotRobotState = gameState.getState(toShoot).updateDamage(-1);
                next = gameState.update(shotRobotState);
            }
        }
        robotLaserSteps.get(phaseNumber).add(next);
    }

    private void doWallLasers(int phaseNumber, GameState gameState) {
        GameState next = gameState.copy();
        for (Location laserLocation : getMapHandler().getLasersLocations()) {
            Robot toShoot = getMapHandler().robotInLineOfVision(laserLocation, gameState);
            if (toShoot != null) {
                log(toShoot.toString() + " was shot by the wall laser at " + laserLocation.getPosition().toString());
                RobotState shotRobotState = gameState.getState(toShoot).updateDamage(-1);
                next = gameState.update(shotRobotState);
            }
        }
        wallLaserSteps.get(phaseNumber).add(next);
    }

    public GameState getInitialGameState() {
        GameState state = new GameState();
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
