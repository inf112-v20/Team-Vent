package inf112.skeleton.app.model;

import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.MoveInstruction;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameModel {

    private LinkedList<Robot> robots;
    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;

    private ArrayList<Deque<MoveInstruction>> cardSteps = new ArrayList<>();
    private ArrayList<Deque<MoveInstruction>> tileSteps = new ArrayList<>();



    public GameModel(String map_filename) {
        robots = new LinkedList<>();
        robot = new Robot();
        robots.add(robot);
        player = new Player();
        player.generateCardHand();
        tiledMapHandler = new MapHandler("map-1.tmx");
        for (int i = 0; i < 5; i++) {
            cardSteps.add(new LinkedList<>());
            tileSteps.add(new LinkedList<>());
        }
        tiledMapHandler = new MapHandler(map_filename);
    }

    public Robot getRobot() {
        return this.robot;
    }

    public MapHandler getTiledMapHandler() {
        return this.tiledMapHandler;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void endTurn() {
        Location loc = robot.getLocation();

        for (int i = 0; i < 5; i++) {
            doCard(i, loc);
            loc = updateLastLoc(loc, cardSteps.get(i));
            doTiles(i, loc);
            loc = updateLastLoc(loc, tileSteps.get(i));
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

    private void doTiles(int phaseNumber, Location initialLoc) {
        Location loc = initialLoc;
        // Calculate next steps based on current position
        TileType currentTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                tileSteps.get(phaseNumber).add(new MoveInstruction(loc.moveDirection(currentTileDirection), robot));
                break;
            case CONVEYOR_EXPRESS:
                tileSteps.get(phaseNumber).add(new MoveInstruction(loc.moveDirection(currentTileDirection), robot));
                loc = tileSteps.get(phaseNumber).getLast().location;
                TileType nextTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.toString().equals(nextTileType.toString())) {
                    tileSteps.get(phaseNumber).add(new MoveInstruction(loc.moveDirection(nextTileDirection), robot));
                }
                break;
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(loc.getPosition(), loc.getDirection().right());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnRight, robot));
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(loc.getPosition(), loc.getDirection().left());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnLeft, robot));
                break;
            case HOLE:
                tileSteps.add(null); // the robot died, so it has no position
                //return tileSteps; // end the phase early
                break;
            default:
                break;
        }
    }

    private void doCard (int phaseNumber, Location initialLoc) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
            if ((card != null) && !(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(initialLoc))) {
                cardSteps.get(phaseNumber).add(new MoveInstruction(card.instruction(initialLoc.copy()), robot));
            }
    }

    public void scheduleDoCardTimed(int delay, int phase) {
        if (cardSteps.get(phase).size() != 0) {
            Timer.Task doCardTimed = new Timer.Task() {
                @Override
                public void run() {
                    MoveInstruction moveInstruction = cardSteps.get(phase).remove();
                    moveInstruction.robot.setLocation(moveInstruction.location);
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
                    MoveInstruction moveInstruction = tileSteps.get(phase).remove();
                    moveInstruction.robot.setLocation(moveInstruction.location);
                }
            };
            Timer.instance().scheduleTask(doTilesTimed, delay, 1, tileSteps.get(phase).size() - 1);
        }
    }

    private  Location updateLastLoc (Location loc, Deque<MoveInstruction> locations) {
        if (locations.peekLast() != null) {return locations.peekLast().location;}
        return loc;
    }

    public boolean inTestMode() {
        return true;
    }
    public List<Robot> getRobots() {
        return robots;
    }
}
