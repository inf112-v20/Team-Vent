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



    public GameModel() {
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

        doCard(0, loc);
        loc = updateLastLoc(loc, cardSteps.get(0));

        doTiles(0, loc);
        loc = updateLastLoc(loc, tileSteps.get(0));

        for (int i = 1; i < 5; i++) {
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

        // Calculate next steps based on current position
        TileType currentTileType = tiledMapHandler.getTileType(initialLoc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = tiledMapHandler.getDirection(initialLoc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(currentTileDirection), robot));
                break;
            case CONVEYOR_EXPRESS:
                tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(currentTileDirection), robot));
                initialLoc = tileSteps.get(phaseNumber).getLast().location;
                TileType nextTileType = tiledMapHandler.getTileType(initialLoc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = tiledMapHandler.getDirection(initialLoc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.toString().equals(nextTileType.toString())) {
                    tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(nextTileDirection), robot));
                }
                break;
            case GEAR_CLOCKWISE:
                Location turnRight = new Location(initialLoc.getPosition(), initialLoc.getDirection().right());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnRight, robot));
            case GEAR_COUNTERCLOCKWISE:
                Location turnLeft = new Location(initialLoc.getPosition(), initialLoc.getDirection().left());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnLeft, robot));
                break;
            case HOLE:
                tileSteps.add(null); // the robot died, so it has no position
                //return tileSteps; // end the phase early
            default:
                break;
        }
    }

    private void doCard (int phaseNumber, Location initialLoc) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        if (card != null) {
            if (!(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(initialLoc))) {
                cardSteps.get(phaseNumber).add(new MoveInstruction(card.instruction(initialLoc.copy()), robot));
            }
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
