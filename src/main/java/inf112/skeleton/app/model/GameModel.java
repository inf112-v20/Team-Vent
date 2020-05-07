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

    private final int PHASES = 5;
    private final LinkedList<Robot> robots;
    private final MapHandler mapHandler;
    private Player player;
    public final ArrayList<Deque<GameState>> cardSteps = new ArrayList<>();
    public final ArrayList<Deque<GameState>> tileSteps = new ArrayList<>();
    public final ArrayList<Deque<GameState>> laserSteps = new ArrayList<>();
    public final ArrayList<Deque<GameState>> endOfPhaseSteps = new ArrayList<>();
    public Timer timer = new Timer(true);
    public int delay;
    public LinkedList<Player> players;
    private HashMap<Integer, Player> playerMap;
    private int intervalTime;

    private GameState currentGameState;
    public GameState gameState;

    public GameModel(String map_filename, int numberOfPlayers, int playerIndex) {
        mapHandler = new MapHandler(map_filename);
        if (mapHandler.getStartLocations().size() < numberOfPlayers) {
            throw new IllegalStateException(String.format("There are not enough starting locations for %d players", numberOfPlayers));
        }
        intervalTime = Constants.INTERVAL_TIME;
        // initialize players and robots
        players = new LinkedList<>();
        robots = new LinkedList<>();
        playerMap = new HashMap<>();
        delay = 0;
        for (int i = 0; i < numberOfPlayers; i++) {
            Player p = new Player(new Robot(mapHandler.getStartLocations().get(i)));
            p.dealCards();
            players.add(p);
            playerMap.put(i, p);
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
        String[] names = {"Blue", "Yellow", "Red", "Green", "Orange", "Pink", "Golden", "Black" };
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
        return playerMap.get(index);
    }

    public Player getMyPlayer() {
        return this.player;
    }

    public void emptyPlayersProgrammingSlots(){
        for (Player player : players){
            player.clearProgrammingSlots();
        }
    }

    public void fillPLayersProgrammingSlots() {
        for (Player player : players) {
            player.fillEmptySlots();
        }
    }

    public void generateCardHands() {
        for (Player player : players) {
            player.dealCards();
        }
    }

    public void endTurn() {
        gameState = getInitialGameState();

        //Does the logic, goes through each robot in the list for each phase.
        //gameState is a list off all robot's state, robotState is the specific robot being done a move for.
        for (int i = 0; i < PHASES; i++) {
            for (Player player : players) {
                Robot robot = player.getRobot();
                doCard(i, gameState, gameState.getState(robot), player);
                gameState = updateLastState(gameState, cardSteps.get(i));
                doBorders(gameState);
            }

            doTiles(i, gameState);
            gameState = updateLastState(gameState, tileSteps.get(i));
            doBorders(gameState);

            doLasers(i, gameState);
            gameState = updateLastState(gameState, laserSteps.get(i));

            doFlags(i, gameState);
            gameState = updateLastState(gameState, endOfPhaseSteps.get(i));
        }

        // end of turn effects

        // make sure the end of turn effects happen in the last phase even when robots don't play cards.
        // this is only an issue when testing. without this, robots can re-spawn too early
        if (endOfPhaseSteps.get(PHASES - 1).isEmpty()) {
            gameState = gameState.copy();
            endOfPhaseSteps.get(PHASES - 1).add(gameState);
        }

        doRepairs(gameState);
        doReboot(gameState);

        //Change priority of players.
        players.add(players.pop());
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
            if (!robotState.getDead() || robotState.getLives() == 0) continue;
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

    private void doCard(int phaseNumber, GameState initialState, RobotState robotState, Player player) {
        if (robotState.getDead()) return;
        Card card = player.getCardInProgrammingSlot(phaseNumber);
        if (card == null) return;
        RobotState nextRobotState = robotState;
        switch (card) {
            case MOVE_THREE:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection());
                initialState = updateLastState(initialState, cardSteps.get(phaseNumber));
                robotState = initialState.getState(robotState.getRobot());
                // no break
            case MOVE_TWO:
                doMovement(phaseNumber, initialState, robotState, cardSteps, robotState.getLocation().getDirection());
                initialState = updateLastState(initialState, cardSteps.get(phaseNumber));
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

    private void doTiles(int phaseNumber, GameState initialState) {
        GameState state = initialState.copy();
        if (doConveyorMovement(state,true)) {
            tileSteps.get(phaseNumber).add(state);
            state = tileSteps.get(phaseNumber).getLast().copy();
        }
        boolean tilesActivated = false;
        for (Robot robot: robots) {
            if (doOtherTiles(state, state.getState(robot))) {
                tilesActivated = true;
            }
        }
        if (doConveyorMovement(state, false)) tilesActivated = true;
        if (tilesActivated) {
            tileSteps.get(phaseNumber).add(state);
        }
    }

    private boolean doOtherTiles(GameState initialState, RobotState robotState) {
        Location loc = robotState.getLocation();
        // Calculate next steps based on current position
        TileType currentTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(loc.getPosition(), loc.getDirection().right());
                initialState.edit(robotState.updateLocation(turnRight));
                return true;
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(loc.getPosition(), loc.getDirection().left());
                initialState.edit(robotState.updateLocation(turnLeft));
                return true;
            case HOLE:
                initialState.edit(robotState.updateDead());
                return true;
            default:
                return false;
        }
    }

    private boolean doConveyorMovement(GameState state, boolean onlyExpress) {
        HashMap<Robot, Boolean> moveChecked = new HashMap<>();
        boolean somethingHappened = false;
        for (Robot robot : robots) {
            moveChecked.put(robot, false);
        }
        for (Robot robot: robots) {
            if (doConveyorMove(state, state.getState(robot), moveChecked, onlyExpress))
            somethingHappened = true;
        }
        return somethingHappened;
    }

    private boolean doConveyorMove(GameState initialState, RobotState robotState, HashMap<Robot, Boolean> moveChecked, boolean onlyExpress) {
        Location loc = robotState.getLocation();
        TileType currentTileType = mapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = mapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);

        if (TileType.CONVEYOR_EXPRESS.equals(currentTileType) || (TileType.CONVEYOR_NORMAL.equals(currentTileType) && !onlyExpress)) {
            Location locWithDir = new Location(robotState.getLocation().getPosition(), currentTileDirection);
            if (mapHandler.wallInPath(locWithDir)) {
                moveChecked.put(robotState.getRobot(), true);
                return false;
            }

            Location pointOfContention = robotState.getLocation().moveDirection(currentTileDirection);
            Location twoRobotsFacingPoint = pointOfContention.moveDirection(currentTileDirection);
            for (RobotState state : initialState.getRobotStates()) {

                if (state.getLocation().getPosition().equals(twoRobotsFacingPoint.getPosition())) {
                    TileType otherRobotTileType = mapHandler.getTileType(state.getLocation().getPosition(), Constants.TILE_LAYER);
                    Direction otherRobotTileDirection = mapHandler.getDirection(state.getLocation().getPosition(), Constants.TILE_LAYER);
                    if ((TileType.CONVEYOR_EXPRESS.equals(otherRobotTileType) || (TileType.CONVEYOR_NORMAL.equals(otherRobotTileType) && !onlyExpress))
                    && otherRobotTileDirection.equals(currentTileDirection.opposite())) {
                        moveChecked.put(robotState.getRobot(), true);
                        return false;
                    }
                }

                if (state.getLocation().getPosition().equals(pointOfContention.getPosition())) {
                    if (moveChecked.get(state.getRobot())) {
                        moveChecked.put(robotState.getRobot(), true);
                        return false;
                    }
                    if (doConveyorMove(initialState, state, moveChecked, onlyExpress)) {
                        moveChecked.put(robotState.getRobot(), true);
                        return finalizeMovementWithRotation(initialState, robotState, currentTileDirection);
                    }
                    moveChecked.put(robotState.getRobot(), true);
                    return false;
                }
            }

            moveChecked.put(robotState.getRobot(), true);
            return finalizeMovementWithRotation(initialState, robotState, currentTileDirection);
        }
        moveChecked.put(robotState.getRobot(), true);
        return false;
    }

    private boolean finalizeMovementWithRotation (GameState initialState, RobotState robotState, Direction currentTileDirection) {

        Location targetLocation = robotState.getLocation().moveDirection(currentTileDirection);
        TileType targetTileType = mapHandler.getTileType(targetLocation.getPosition(), Constants.TILE_LAYER);
        Direction targetTileDirection = mapHandler.getDirection(targetLocation.getPosition(), Constants.TILE_LAYER);

        if ((TileType.CONVEYOR_EXPRESS.equals(targetTileType) || (TileType.CONVEYOR_NORMAL.equals(targetTileType)))
        && !targetTileDirection.equals(currentTileDirection)) {
            if (targetTileDirection.equals(currentTileDirection.left())) {
                Location newLoc = new Location(targetLocation.getPosition(), robotState.getLocation().getDirection().left());
                initialState.edit(robotState.updateLocation(newLoc));
                return true;
            }
            Location newLoc = new Location(targetLocation.getPosition(), robotState.getLocation().getDirection().right());
            initialState.edit(robotState.updateLocation(newLoc));
            return true;
        }
        initialState.edit(robotState.updateLocation(targetLocation));
        return true;
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

    public boolean checkWinnerOrLoser() {
        for (RobotState robotState : gameState.getRobotStates()) {
            if (robotState.getCapturedFlags() == mapHandler.getNumberOfFlags()) {
                for (RobotState robotState2 : gameState.getRobotStates()) {
                    if (!robotState2.equals(robotState)) {
                        robotState2.setLives(1);
                        gameState.add(robotState2.updateDead());
                    }
                }
                return true;
            }
            else if (robotState.getLives() == 0) {
                return true;
            }
        }
        return false;
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
        if (Constants.ENABLE_LOGGING) {
            Gdx.app.log(this.getClass().getName(), message);
        }
    }

    public Iterable<? extends GameState.LaserBeam> getLaserBeams() {
        return currentGameState.getLaserBeams();
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Change the player that this user controls. This allows testers to control multiple robots and to create specific
     * scenarios for testing game mechanics in single player games. It will not work with multiplayer games.
     */
    public void setMyPlayer(Player player) {
        if (!Constants.DEVELOPER_MODE) {
            throw new IllegalStateException("Changing player mid-game is a testing feature that is only available in" +
                    "developer mode, and should only be used for single player games");
        }
        this.player = player;
    }
}
