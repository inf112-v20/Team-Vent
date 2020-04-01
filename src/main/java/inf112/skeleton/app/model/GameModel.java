package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    private final ArrayList<Deque<GameState>> flagVisitSteps = new ArrayList<>();
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
            flagVisitSteps.add(new LinkedList<>());
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
            for (Robot robot : robots) {
                doRobotLaser(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, robotLaserSteps.get(i));
            }
            for (Robot robot : robots) {
                doFlag(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, flagVisitSteps.get(i));
            }
            doWallLasers(i, gameState);
            gameState = updateLastState(gameState, wallLaserSteps.get(i));
        }

        int delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleSteps(delay, i, cardSteps);
            delay += cardSteps.get(i).size();
            scheduleSteps(delay, i, tileSteps);
            delay += tileSteps.get(i).size();
            scheduleSteps(delay, i, robotLaserSteps);
            // delay += robotLaserSteps.get(i).size(); todo: re-enable delay for lasers when there are visualizations
            scheduleSteps(delay, i, wallLaserSteps);
            // delay += robotLaserSteps.get(i).size();
            scheduleSteps(delay, i, flagVisitSteps);
        }
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

    private void doFlag(int phaseNumber, GameState initialState, RobotState robotState) {
        if (robotState.getDead()) return;
        TiledMapTileLayer.Cell cell = getMapHandler().getFlagLayer().getCell(robotState.getLocation().getPosition().getX(),
                robotState.getLocation().getPosition().getY());
        if (cell == null) return; // there is no flag here
        int flagNumber = (int) cell.getTile().getProperties().get("number");
        RobotState newRobotState = robotState.copy();
        newRobotState.visitFlag(flagNumber, robotState.getLocation());
        GameState newState = initialState.update(newRobotState);
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
                for (RobotState robotState : gameState.robotStates) {
                    robotState.getRobot().updateState(robotState);
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
            robotLaserSteps.get(phaseNumber).add(gameState.update(shotState));
        }
    }

    private void doWallLasers(int phaseNumber, GameState gameState) {
        for (Location laserLocation : getMapHandler().getLasersLocations()) {
            Robot toShoot = getMapHandler().robotInLineOfVision(laserLocation, gameState);
            if (toShoot != null) {
                log(toShoot.toString() + " was shot by the wall laser at " + laserLocation.getPosition().toString());
                RobotState shotRobotState = gameState.getState(toShoot).updateDamage(-1);
                wallLaserSteps.get(phaseNumber).add(gameState.update(shotRobotState));
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
