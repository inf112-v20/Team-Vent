package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.MoveInstruction;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
//import org.graalvm.compiler.lir.sparc.SPARCMove;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class GameModel {

    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;

    private ArrayList<Deque<MoveInstruction>> cardSteps = new ArrayList<>();
    private ArrayList<Deque<MoveInstruction>> tileSteps = new ArrayList<>();



    public GameModel() {
        robot = new Robot();
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

    public TiledMap getBoard() {
        return this.tiledMapHandler.getMap();
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
        String currentTileType =  tiledMapHandler.getTileType(initialLoc.getPosition(), "Tile");
        Direction currentTileDirection = tiledMapHandler.getDirection(initialLoc.getPosition(), "Tile");

        switch(currentTileType != null ? currentTileType: "none"){
            case("conveyor_normal"):
                tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(currentTileDirection), robot));
                break;
            case("conveyor_express"):
                tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(currentTileDirection), robot));
                initialLoc = tileSteps.get(phaseNumber).getLast().location;
                String nextTileType = tiledMapHandler.getTileType(initialLoc.getPosition(), "Tile");
                Direction nextTileDirection = tiledMapHandler.getDirection(initialLoc.getPosition(), "Tile");
                if ("conveyor_express".equals(nextTileType)) {
                    tileSteps.get(phaseNumber).add(new MoveInstruction(initialLoc.moveDirection(nextTileDirection), robot));
                }
                break;
            case("gear_clockwise"):
                Location turnRight = new Location(initialLoc.getPosition(), initialLoc.getDirection().right());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnRight, robot));
                break;
            case("gear_counterclockwise"):
                Location turnLeft = new Location(initialLoc.getPosition(), initialLoc.getDirection().left());
                tileSteps.get(phaseNumber).add(new MoveInstruction(turnLeft, robot));
                break;
            default:
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
}
