package inf112.skeleton.app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.board.Direction;

import java.util.IdentityHashMap;

public class GameRenderer {
    private final GameModel gameModel;
    private final OrthogonalTiledMapRenderer boardRenderer;
    private final TiledMapTileLayer playerLayer;
    private SpriteBatch batch;
    private BitmapFont font;
    TextureRegion robotFacingUp;
    private IdentityHashMap<Robot, TiledMapTileLayer.Cell> robotsToCellsHashMap;

    public GameRenderer(final GameModel gameModel) {
        this.gameModel = gameModel;
        TiledMap tiledMap = this.gameModel.getTiledMapHandler().getMap();
        int tilesWide = gameModel.getTiledMapHandler().getWidth();
        int tilesHigh = gameModel.getTiledMapHandler().getHeight();
        playerLayer = gameModel.getTiledMapHandler().getRobotLayer();
        TiledMapTileLayer tileLayer = gameModel.getTiledMapHandler().getTileLayer();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesWide + tilesWide / 3f, tilesHigh);
        camera.update();
        loadTextures();
        loadFont();
        boardRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / tileLayer.getTileWidth());
        boardRenderer.setView(camera);
    }

    private void loadTextures() {
        robotFacingUp = new TextureRegion(new Texture("Player/Mechs/Mech1A_north.png"));
        TiledMapTileLayer.Cell robotCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingUp));
        robotsToCellsHashMap = new IdentityHashMap<>();
        // associate robots with cells for the robot layer of the map
        robotsToCellsHashMap.put(gameModel.getRobot(), robotCell);
    }

    public void renderRobots() {
        clearRobotsFromMap();
        for (Robot robot : gameModel.getRobots()) {
            // get the cell for that robot or use the default robot texture to create a new cell
            TiledMapTileLayer.Cell cell = robotsToCellsHashMap.getOrDefault(robot, new TiledMapTileLayer.Cell().
                    setTile(new StaticTiledMapTile(robotFacingUp)));
            rotateCellToMatchRobot(robot, cell);
            playerLayer.setCell(robot.getX(), robot.getY(), cell);
        }
    }

    private void clearRobotsFromMap() {
        for (int i = 0; i < gameModel.getTiledMapHandler().getWidth(); i++) {
            for (int j = 0; j < gameModel.getTiledMapHandler().getWidth(); j++) {
                playerLayer.setCell(i, j, null);
            }
        }
    }

    private void rotateCellToMatchRobot(Robot robot, TiledMapTileLayer.Cell cell) {
        // assuming the texture in the cell is facing north when rotation is 0
        Direction direction = robot.getDirection();
        new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingUp));
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

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderRobots();
        renderFont();
        boardRenderer.render();
    }

    private void loadFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void renderFont() {
        batch.begin();
        font.draw(batch, gameModel.getPlayer().handAsString(), 950, 550);
        font.draw(batch, gameModel.getPlayer().programmingSlotsAsString(), 950, 300);
        batch.end();
    }
}
