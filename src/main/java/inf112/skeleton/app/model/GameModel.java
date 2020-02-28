package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;

import java.util.Deque;

public class GameModel {

    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        player.generateCardHand();
        tiledMapHandler = new MapHandler("map-1.tmx");
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

    public Deque<Location> doPhase(int phaseNumber, Deque<Location> phaseSteps) {
        Gdx.app.log(GameModel.class.getName(), Integer.toString(phaseNumber));
        Location loc = phaseSteps.getLast().copy();
        if (phaseNumber == 5) {
            return phaseSteps;
        }
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        if (card != null) {
            if (!(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(loc.copy()))) {
                phaseSteps.add(card.instruction(loc.copy()));
            }
        }
        loc = phaseSteps.getLast().copy();

        String currentTileType = tiledMapHandler.getTileType(loc.getPosition(), "Tile");
        Direction currentTileDirection = tiledMapHandler.getDirection(loc.getPosition(), "Tile");

        switch (currentTileType != null ? currentTileType : "none") {
            case ("conveyor_normal"):
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                break;
            case ("conveyor_express"):
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                loc = phaseSteps.getLast().copy();
                String nextTileType = tiledMapHandler.getTileType(loc.getPosition(), "Tile");
                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), "Tile");
                if ("conveyor_express".equals(nextTileType)) {
                    phaseSteps.add(loc.moveDirection(nextTileDirection));
                }
                break;
            case ("gear_clockwise"):
                phaseSteps.add(new Location(loc.getPosition(), loc.getDirection().right()));
                break;
            case ("gear_counterclockwise"):
                phaseSteps.add(new Location(loc.getPosition(), loc.getDirection().left()));
                break;
            default:
        }
        return doPhase(phaseNumber + 1, phaseSteps);
    }


    public boolean inTestMode() {
        return true;
    }
}
