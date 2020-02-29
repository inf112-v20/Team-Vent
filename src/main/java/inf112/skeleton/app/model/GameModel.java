package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileType;

import java.util.Deque;

public class GameModel {

    private final Robot robot;
    private final MapHandler tiledMapHandler;
    private final Player player;

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
        //Gdx.app.log(GameModel.class.getName(), Integer.toString(phaseNumber));
        if (phaseNumber == 5) return phaseSteps;
        Location loc = phaseSteps.getLast().copy();
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null); // empty the slot
        if (card != null && !(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(loc.copy()))) {
            phaseSteps.add(card.instruction(loc.copy()));
        }
        loc = phaseSteps.getLast().copy();

        // Check if the location is outside the map
        if (loc.getPosition().getX() < 0 || loc.getPosition().getX() >= tiledMapHandler.getWidth() ||
                loc.getPosition().getY() < 0 || loc.getPosition().getY() >= tiledMapHandler.getHeight()) {
            phaseSteps.add(null); // the robot died, so it has no position
            return phaseSteps; // end the phase early
        }

        // Calculate next steps based on current position
        TileType currentTileType = tiledMapHandler.getTileType(loc.getPosition(), Constants.TILE_LAYER);
        Direction currentTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
        switch (currentTileType != null ? currentTileType : TileType.BASE_TILE) {
            case CONVEYOR_NORMAL:
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                break;
            case CONVEYOR_EXPRESS:
                phaseSteps.add(loc.moveDirection(currentTileDirection));
                loc = phaseSteps.getLast().copy();
                String nextTileTypeString = tiledMapHandler.getTileTypeString(loc.getPosition(), Constants.TILE_LAYER);
                Direction nextTileDirection = tiledMapHandler.getDirection(loc.getPosition(), Constants.TILE_LAYER);
                if (currentTileType.toString().equals(nextTileTypeString)) {
                    phaseSteps.add(loc.moveDirection(nextTileDirection));
                }
                break;
            case GEAR_CLOCKWISE:
                phaseSteps.add(new Location(loc.getPosition(), loc.getDirection().right()));
                break;
            case GEAR_COUNTERCLOCKWISE:
                phaseSteps.add(new Location(loc.getPosition(), loc.getDirection().left()));
                break;
            case HOLE:
                phaseSteps.add(null); // the robot died, so it has no position
                return phaseSteps; // end the phase early
            case BASE_TILE:
                break;
        }
        return doPhase(phaseNumber + 1, phaseSteps);
    }
}
