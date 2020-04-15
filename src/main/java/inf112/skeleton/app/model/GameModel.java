package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.Card;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    private static final boolean ENABLE_LOGGING = true;
    private final int PHASES = 5;
    private final LinkedList<Robot> robots;
    private final MapHandler mapHandler;
    private final Player player;
    private final ArrayList<Deque<GameState>> cardSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> tileSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> laserSteps = new ArrayList<>();
    private final ArrayList<Deque<GameState>> endOfPhaseSteps = new ArrayList<>();
    public Timer timer = new Timer(true);
    public int delay;
    private List<Player> players;
    private GameState currentGameState;


    public GameModel(String map_filename, int numberOfPlayers, int playerIndex) {
        mapHandler = new MapHandler(map_filename);
        if (mapHandler.getStartLocations().size() < numberOfPlayers) {
            throw new IllegalStateException(String.format("There are not enough starting locations for %d players", numberOfPlayers));
        }
        // initialize players and robots
        players = new LinkedList<>();
        robots = new LinkedList<>();
        delay = 0;
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
            laserSteps.add(new LinkedList<>());
            endOfPhaseSteps.add(new LinkedList<>());
        }
        // legacy code
        String[] names = {"Blue", "Yellow", "Red", "Green"};
        for (int i = 0; i < names.length && i < robots.size(); i++) {
            robots.get(i).setName(names[i]);
        }
        player = players.get(playerIndex);
        this.currentGameState = getInitialGameState();
    }

    public MapHandler getMapHandler() {
        return this.mapHandler;
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    public Player getMyPlayer() {
        return this.player;
    }

    public void endTurn() {
        GameState gameState = getInitialGameState();

        //Does the logic, goes through each robot in the list for each phase.
        //gameState is a list off all robot's state, robotState is the specific robot being done a move for.
        for (int i = 0; i < PHASES; i++) {
            for (Player player : players) {
                Robot robot = player.getRobot();
                doCard(i, gameState, gameState.getState(robot), player);
                gameState = updateLastState(gameState, cardSteps.get(i));
            }
            for (Robot robot : robots) {
                doTiles(i, gameState, gameState.getState(robot));
                gameState = updateLastState(gameState, tileSteps.get(i));
            }
            doLasers(i, gameState);
            gameState = updateLastState(gameState, laserSteps.get(i));
            doFlags(i, gameState);
            gameState = updateLastState(gameState, endOfPhaseSteps.get(i));
            doBorders(gameState);
        }

        // end of turn effects
        doRepairs(gameState);
        doReboot(gameState);

        delay = 0;
        for (int i = 0; i < PHASES; i++) {
            scheduleSteps(delay, i, cardSteps);
            delay += cardSteps.get(i).size();
            scheduleSteps(delay, i, tileSteps);
            delay += tileSteps.get(i).size();
            scheduleSteps(delay, i, laserSteps);
            delay += laserSteps.get(i).size();
            scheduleSteps(delay, i, endOfPhaseSteps);
        }
    }

    private void doLasers(int phaseNumber, GameState initialState) {
        GameState laserBeamState = initialState.copy();
        List<Location> robotLocations = initialState.getRobotStates().stream()
                .filter(state -> !state.getDead())
                .map(RobotState::getLocation).collect(Collectors.toCollection(LinkedList::new));

        boolean shotsFired = false;

        for (Location origin : robotLocations) {
            RobotState toShoot = mapHandler.robotInLineOfVision(origin, initialState, false);
            if (toShoot == null) continue;
            shotsFired = true;
            laserBeamState.edit(toShoot.updateHP(-1));
            laserBeamState.addLaserBeam(origin, toShoot.getLocation().getPosition(),
                    !mapHandler.getLasersLocations().contains(origin));
        }


        for (Location origin : mapHandler.getLasersLocations()) {
            RobotState toShoot = mapHandler.robotInLineOfVision(origin, initialState, true);
            if (toShoot == null) continue;
            shotsFired = true;
            laserBeamState.edit(toShoot.updateHP(-1));
            laserBeamState.addLaserBeam(origin, toShoot.getLocation().getPosition(),
                    !mapHandler.getLasersLocations().contains(origin));
        }

        if (shotsFired) {
            laserSteps.get(phaseNumber).add(laserBeamState);
            laserSteps.get(phaseNumber).add(laserBeamState.clearLaserBeams()); // state without lasers
        }
    }

    /**
     * Edit a game state so that all robots that are outside the board die
     */
    private void doBorders(GameState gameState) {
        for (RobotState state : gameState.getRobotStates()) {
            if (!state.getDead() && mapHandler.outOfBounds(state.getLocation())) {
                gameState.edit(state.updateDead());
            }
        }
    }

    /**
     * Edit a game state so that dead robots re-spawn at their last saved location
     */
    public void doReboot(GameState gameState) {
        for (RobotState robotState : gameState.getRobotStates()) {
            if (!robotState.getDead()) continue;
            Location saved = robotState.getSaveLocation();
            Location respawn = saved;
            // if the saved location is not available, use the closest available location
            if (!available(gameState, saved.getPosition())) {
                Set<RVector2> candidates = new HashSet<>();
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        candidates.add(saved.getPosition().translate(dx, dy));
                    }
                }
                Optional<RVector2> closestAvailablePosition = candidates
                        .stream()
                        .filter(p -> available(gameState, p))
                        .min(Comparator.comparingInt(o -> o.distance(saved.getPosition())));
                respawn = closestAvailablePosition.map(vector2 -> new Location(vector2, saved.getDirection())).orElse(saved);
            }
            gameState.edit(robotState.reboot(respawn));
        }
    }

    /**
     * Return true if the position is within the board and no robot has that position
     */
    private boolean available(GameState gameState, RVector2 position) {
        return !mapHandler.outOfBounds(position) && gameState.getRobotStates().stream()
                .filter(state -> !state.getDead())
                .map(RobotState::getLocation)
                .map(Location::getPosition)
                .noneMatch(pos -> pos.equals(position));
    }

    /**
     * Edit a game state so that all robots that are standing on a repair tile gain one hp
     */
    public void doRepairs(GameState gameState) {
        for (RobotState robotState : gameState.getRobotStates()) {
            if (mapHandler.hasRepairSite(robotState.getLocation().getPosition())) {
                log(robotState.getRobot() + " will be repaired");
                gameState.edit(robotState.updateHP(1)); // increase hp by one
            }
        }
    }

    private void doTiles(int phaseNumber, GameState initialState, RobotState robotState) {
        Location loc = robotState.getLocation();
        // Calculate next steps based on current position
        GameState newState;
        TileType currentTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = mapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                doMovement(phaseNumber, initialState, robotState, tileSteps, currentTileDirection);
                break;
            case CONVEYOR_EXPRESS:
                doMovement(phaseNumber, initialState, robotState, tileSteps, currentTileDirection);
                newState = initialState.update(robotState.updateLocation(loc.moveDirection(currentTileDirection)));

                loc = newState.getState(robotState.getRobot()).getLocation();
                RobotState newRobotState = newState.getState(robotState.getRobot());

                TileType nextTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = mapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.equals(nextTileType)) {
                    doMovement(phaseNumber, newState, newRobotState, tileSteps, nextTileDirection);
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
                newState = initialState.update(robotState.updateDead());
                tileSteps.get(phaseNumber).add(newState);
                break;
            default:
                break;
        }
    }

    private void doCard(int phaseNumber, GameState initialState, RobotState robotState, Player player) {
        Card card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        if (card == null) return;
        RobotState nextRobotState = robotState;
        switch (card) {
            case MOVE_THREE:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection());
                initialState = cardSteps.get(phaseNumber).getLast();
                robotState = initialState.getState(robotState.getRobot());
                // no break
            case MOVE_TWO:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection());
                initialState = cardSteps.get(phaseNumber).getLast();
                robotState = initialState.getState(robotState.getRobot());
                // no break
            case MOVE_ONE:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection());
                break;
            case BACK_UP:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection().opposite());
                break;
            case ROTATE_RIGHT:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().rotateRight());
                cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
                break;
            case ROTATE_LEFT:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().rotateLeft());
                cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
                break;
            case U_TURN:
                nextRobotState = nextRobotState.updateLocation(nextRobotState.getLocation().halfTurn());
                cardSteps.get(phaseNumber).add(initialState.update(nextRobotState));
                break;
            default:
                break;
        }
    }

    private boolean doMove(GameState initialState, RobotState robotState, Direction dir) {

        Location locWithDir = new Location(robotState.getLocation().getPosition().cpy(), dir);
        if (mapHandler.wallInPath(locWithDir)) {return false;}

        Location pointOfContention = robotState.getLocation().moveDirection(dir);

        for (RobotState state : initialState.getRobotStates()) {
            if (state.getLocation().getPosition().equals(pointOfContention.getPosition())) {
                if (doMove(initialState, state, dir)) {
                    initialState.edit(robotState.updateLocation(robotState.getLocation().moveDirection(dir)));
                    return true;
                }
                return false;
            }
        }
        initialState.edit(robotState.updateLocation(robotState.getLocation().moveDirection(dir)));
        return true;
    }

    private void doMovement(int phaseNumber, GameState state, RobotState robotState, ArrayList<Deque<GameState>> xSteps, Direction dir) {
        GameState initialState = state.copy();
        if(doMove(initialState, robotState, dir)) {
            xSteps.get(phaseNumber).add(initialState);
        }

    }

    private void doFlags(int phase, GameState gameState) {
        for (RobotState robotState : gameState.getRobotStates()) {
            if (robotState.getDead()) continue;
            Integer flagNumber = mapHandler.getFlag(robotState.getLocation().getPosition());
            if (flagNumber != null && flagNumber == robotState.getCapturedFlags() + 1) {
                endOfPhaseSteps.get(phase).add(gameState.update(robotState.visitFlag()));
                log(String.format("%s will visit flag number %d", robotState.getRobot().toString(), flagNumber));
            } else if (mapHandler.hasRepairSite(robotState.getLocation().getPosition())) {
                endOfPhaseSteps.get(phase).add(gameState.update(robotState.updateSaveLocation()));
                log(String.format("%s will touch a repair site and update its save location", robotState.getRobot().toString()));
            }
        }
    }

    public void scheduleSteps(int delay, int phase, ArrayList<Deque<GameState>> steps) {
        for (int i = 0; i < steps.get(phase).size(); i++) {
            timer.schedule(doStep(phase, steps), delay * 500 + 500 * i);
        }
    }

    public TimerTask doStep(int phase, ArrayList<Deque<GameState>> steps) {
        return new TimerTask() {
            @Override
            public void run() {
                GameState gameState = steps.get(phase).remove();
                for (RobotState stateInfo : gameState.getRobotStates()) {
                    stateInfo.getRobot().updateState(stateInfo);
                }
                currentGameState = gameState;
            }
        };
    }

    private GameState updateLastState(GameState state, Deque<GameState> states) {
        if (states.peekLast() != null) {
            return states.peekLast();
        }
        return state;
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

    public Iterable<? extends GameState.LaserBeam> getLaserBeams() {
        return currentGameState.getLaserBeams();
    }
}
