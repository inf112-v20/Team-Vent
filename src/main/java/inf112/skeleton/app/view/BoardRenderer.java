package inf112.skeleton.app.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;

import java.util.IdentityHashMap;

import static inf112.skeleton.app.model.GameState.LaserBeam;

public class BoardRenderer extends OrthogonalTiledMapRenderer {
    private final GameModel gameModel;
    private final Cell HORIZONTAL_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(39));
    private final Cell VERTICAL_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(47));
    private final Cell CROSS_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(40));
    private final Cell HORIZONTAL_LASER_TILE_CELL_SHIFTED_EAST;
    private final Cell HORIZONTAL_LASER_TILE_CELL_SHIFTED_WEST;
    private final Cell VERTICAL_LASER_TILE_CELL_SHIFTED_NORTH;
    private final Cell VERTICAL_LASER_TILE_CELL_SHIFTED_SOUTH;
    private IdentityHashMap<Robot, Cell> robotsToCellsHashMap;
    private TiledMapTileLayer robotLayer;
    private TiledMapTileLayer laserLayer;

    public BoardRenderer(GameModel gameModel) {
        super(gameModel.getMapHandler().getMap(), 1 / gameModel.getMapHandler().getTileLayer().
                getTileWidth());
        this.gameModel = gameModel;
        loadTextures();
        // create additional layers for objects that move or change based on the game model
        TiledMapTileLayer tileLayer = gameModel.getMapHandler().getTileLayer();
        this.robotLayer = new TiledMapTileLayer(tileLayer.getWidth(), tileLayer.getHeight(),
                (int) tileLayer.getTileWidth(), (int) tileLayer.getTileHeight());
        robotLayer.setName(Constants.ROBOT_LAYER);
        this.laserLayer = new TiledMapTileLayer(tileLayer.getWidth(), tileLayer.getHeight(),
                (int) tileLayer.getTileWidth(), (int) tileLayer.getTileHeight());
        laserLayer.setName(Constants.LASER_LAYER);

        // initialize shifted tiles
        float tileWidth = laserLayer.getTileWidth();
        float tileHeight = laserLayer.getTileHeight();
        TiledMapTile shiftedWest = new StaticTiledMapTile((StaticTiledMapTile) HORIZONTAL_LASER_TILE_CELL.getTile());
        shiftedWest.setOffsetX(-tileWidth / 2);
        TiledMapTile shiftedEast = new StaticTiledMapTile((StaticTiledMapTile) HORIZONTAL_LASER_TILE_CELL.getTile());
        shiftedEast.setOffsetX(tileWidth / 2);
        TiledMapTile shiftedNorth = new StaticTiledMapTile((StaticTiledMapTile) VERTICAL_LASER_TILE_CELL.getTile());
        shiftedNorth.setOffsetY(tileHeight / 2);
        TiledMapTile shiftedSouth = new StaticTiledMapTile((StaticTiledMapTile) VERTICAL_LASER_TILE_CELL.getTile());
        shiftedSouth.setOffsetY(-tileHeight / 2);
        HORIZONTAL_LASER_TILE_CELL_SHIFTED_EAST = new Cell().setTile(shiftedEast);
        HORIZONTAL_LASER_TILE_CELL_SHIFTED_WEST = new Cell().setTile(shiftedWest);
        VERTICAL_LASER_TILE_CELL_SHIFTED_NORTH = new Cell().setTile(shiftedNorth);
        VERTICAL_LASER_TILE_CELL_SHIFTED_SOUTH = new Cell().setTile(shiftedSouth);
    }

    @Override
    public void render() {
        clearObjectLayers();
        updateRobotLayer();
        updateLaserBeamLayer();
        super.render();  // render all static layers
        this.beginRender();
        this.renderMapLayer(laserLayer);
        this.renderMapLayer(robotLayer);
        // re-render wall layer so that wall layers are on top of beams
        this.renderMapLayer(gameModel.getMapHandler().getWallLayer());
        this.endRender();
    }

    private void loadTextures() {
        TextureRegion robotFacingNorth = new TextureRegion(new Texture("Player/Mechs/Mech1A_north.png"));
        TextureRegion robot2FacingNorth = new TextureRegion(new Texture("Player/Mechs/Mech2.png"));
        TextureRegion robot3FacingNorth = new TextureRegion(new Texture("Player/Mechs/Mech3.png"));
        TextureRegion robot4FacingNorth = new TextureRegion(new Texture("Player/Mechs/Mech4.png"));
        Cell robotCell;
        robotsToCellsHashMap = new IdentityHashMap<>();
        int i = 0;
        // associate robots with cells for the robot layer of the map
        for (Robot robot : gameModel.getRobots()) {
            robotCell = new Cell().setTile(new StaticTiledMapTile(robotFacingNorth));

            i++;
            if (i ==2){
                robotCell = new Cell().setTile(new StaticTiledMapTile(robot2FacingNorth));
            }
            if (i == 3){
                robotCell = new Cell().setTile(new StaticTiledMapTile(robot3FacingNorth));
            }
            if (i==4){
                robotCell = new Cell().setTile(new StaticTiledMapTile(robot4FacingNorth));
            }
            robotsToCellsHashMap.put(robot, robotCell);
        }
    }

    public void updateRobotLayer() {
        for (Robot robot : gameModel.getRobots()) {
            if (!robot.alive()) continue;
            Cell cell = robotsToCellsHashMap.get(robot);
            rotateCellToMatchRobot(robot, cell);
            robotLayer.setCell(robot.getX(), robot.getY(), cell);
        }
    }

    public void updateLaserBeamLayer() {

        for (LaserBeam beam : gameModel.getLaserBeams()) {
            Location next = beam.origin;
            // draw lasers on the tiles in-between the shooter and the target
            boolean horizontal = beam.origin.getDirection() == Direction.WEST ||
                    beam.origin.getDirection() == Direction.EAST;
            while (!next.forward().getPosition().equals(beam.target)) {
                next = next.forward();
                setLaserTile(next.getPosition(), horizontal);
            }
            // then draw the positions of the shooter and target separately
            Cell shooterCell;
            Cell targetCell;
            if (beam.origin.getDirection() == Direction.WEST) {
                targetCell = HORIZONTAL_LASER_TILE_CELL_SHIFTED_EAST;
                shooterCell = beam.shooterIsRobot ? HORIZONTAL_LASER_TILE_CELL_SHIFTED_WEST : HORIZONTAL_LASER_TILE_CELL;
            } else if (beam.origin.getDirection() == Direction.EAST) {
                targetCell = HORIZONTAL_LASER_TILE_CELL_SHIFTED_WEST;
                shooterCell = beam.shooterIsRobot ? HORIZONTAL_LASER_TILE_CELL_SHIFTED_EAST : HORIZONTAL_LASER_TILE_CELL;
            } else if (beam.origin.getDirection() == Direction.NORTH) {
                targetCell = VERTICAL_LASER_TILE_CELL_SHIFTED_SOUTH;
                shooterCell = beam.shooterIsRobot ? VERTICAL_LASER_TILE_CELL_SHIFTED_NORTH : VERTICAL_LASER_TILE_CELL;
            } else { // beam.origin.getDirection() == Direction.SOUTH
                targetCell = VERTICAL_LASER_TILE_CELL_SHIFTED_NORTH;
                shooterCell = beam.shooterIsRobot ? VERTICAL_LASER_TILE_CELL_SHIFTED_SOUTH : VERTICAL_LASER_TILE_CELL;
            }
            laserLayer.setCell(beam.origin.getPosition().getX(), beam.origin.getPosition().getY(), shooterCell);
            laserLayer.setCell(beam.target.getX(), beam.target.getY(), targetCell);
        }
    }

    public void setLaserTile(RVector2 position, boolean horizontal) {
        Cell cell = laserLayer.getCell(position.getX(), position.getY());
        if ((horizontal && cell == VERTICAL_LASER_TILE_CELL) || (!horizontal && cell == HORIZONTAL_LASER_TILE_CELL)) {
            cell = CROSS_LASER_TILE_CELL;
        } else if (horizontal) {
            cell = HORIZONTAL_LASER_TILE_CELL;
        } else {
            cell = VERTICAL_LASER_TILE_CELL;
        }
        laserLayer.setCell(position.getX(), position.getY(), cell);
    }

    private void clearObjectLayers() {
        for (int i = 0; i < gameModel.getMapHandler().getWidth(); i++) {
            for (int j = 0; j < gameModel.getMapHandler().getWidth(); j++) {
                robotLayer.setCell(i, j, null);
                laserLayer.setCell(i, j, null);
            }
        }
    }

    private void rotateCellToMatchRobot(Robot robot, Cell cell) {
        // assuming the texture in the cell is facing north when rotation is 0
        Direction direction = robot.getDirection();
        if (direction == Direction.NORTH) {
            cell.setRotation(0);
        } else if (direction == Direction.WEST) {
            cell.setRotation(1);
        } else if (direction == Direction.SOUTH) {
            cell.setRotation(2);
        } else if (direction == Direction.EAST) {
            cell.setRotation(3);
        } else {
            throw new IllegalArgumentException("Unexpected fifth direction");
        }
    }

}
