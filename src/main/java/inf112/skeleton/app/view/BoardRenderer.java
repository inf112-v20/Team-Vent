package inf112.skeleton.app.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Constants;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.RVector2;

import java.util.IdentityHashMap;

public class BoardRenderer extends OrthogonalTiledMapRenderer {
    private final GameModel gameModel;
    private final Cell HORIZONTAL_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(39));
    private final Cell VERTICAL_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(47));
    private final Cell CROSS_LASER_TILE_CELL = new Cell().setTile(getMap().getTileSets().getTile(40));
    private IdentityHashMap<Robot, Cell> robotsToCellsHashMap;
    private TiledMapTileLayer robotLayer;
    private TiledMapTileLayer laserLayer;

    public BoardRenderer(GameModel gameModel) {
        super(gameModel.getMapHandler().getMap(), 1 / gameModel.getMapHandler().getTileLayer().
                getTileWidth());
        this.gameModel = gameModel;
        loadTextures();
        // add layers to render for objects that move or change based on the game model
        TiledMapTileLayer tileLayer = gameModel.getMapHandler().getTileLayer();
        this.robotLayer = new TiledMapTileLayer(tileLayer.getWidth(), tileLayer.getHeight(),
                (int) tileLayer.getTileWidth(), (int) tileLayer.getTileHeight());
        robotLayer.setName(Constants.ROBOT_LAYER);
        map.getLayers().add(robotLayer);
        this.laserLayer = new TiledMapTileLayer(tileLayer.getWidth(), tileLayer.getHeight(),
                (int) tileLayer.getTileWidth(), (int) tileLayer.getTileHeight());
        laserLayer.setName(Constants.LASER_LAYER);
        map.getLayers().add(laserLayer);
    }

    @Override
    public void render() {
        super.render();
        clearObjectLayers();
        renderRobots();
    }

    //TODO update for several robots instead of static 0
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

    public void renderRobots() {
        for (Robot robot : gameModel.getRobots()) {
            if (!robot.alive()) continue;
            Cell cell = robotsToCellsHashMap.get(robot);
            rotateCellToMatchRobot(robot, cell);
            robotLayer.setCell(robot.getX(), robot.getY(), cell);
        }
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
