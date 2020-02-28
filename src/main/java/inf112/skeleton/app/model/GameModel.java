package inf112.skeleton.app.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.board.TileType;
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

        TileType current = TileType.getEnum(currentTileType);

        // Treat all unknown tile types as base tiles
        if (current == null) {
            Gdx.app.log(GameModel.class.getName(), String.format("Unknown tile type %s", currentTileType));
            current = TileType.BASE_TILE;
        }

        switch (current) {
            case CONVEYOR_NORMAL:
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                break;
            case CONVEYOR_EXPRESS:
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                loc = phaseSteps.getLast().copy();
                String nextTileTypeString = tiledMapHandler.getTileType(loc.getPosition(), "Tile");

                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), "Tile");
                if (current.toString().equals(nextTileTypeString)) {
                    phaseSteps.add(loc.moveDirection(nextTileDirection));
                }
                break;
            case GEAR_CLOCKWISE:
                phaseSteps.add(new Location(loc.getPosition(), loc.getDirection().right()));
                break;
            case GEAR_COUNTERCLOCKWISE:
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
