package inf112.skeleton.app.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class GameRenderer {
    private final GameModel gameModel;
    private final OrthogonalTiledMapRenderer boardRenderer;
    private final Batch spriteBatch;
    private SpriteBatch batch;
    private BitmapFont font;
    private final TiledMapTileLayer robotLayer;
    private TiledMapTileLayer.Cell robotFacingUpCell;
    private TiledMapTileLayer.Cell robotFacingDownCell;


    public GameRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        TiledMap tiledMap = this.gameModel.getBoard().getTiledMap();
        int tilesWide = tiledMap.getProperties().get("width", Integer.class);
        int tilesHigh = tiledMap.getProperties().get("height", Integer.class);
        robotLayer = gameModel.getBoard().getRobotLayer();
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile");
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesWide + tilesWide / 2f, tilesHigh);
        camera.position.x = (float) tilesWide / 2 + tilesWide / 4f;
        camera.position.y = (float) tilesHigh / 2;
        camera.update();
        loadTextures();
        loadFont();
        boardRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / tileLayer.getTileWidth());
        boardRenderer.setView(camera);
        spriteBatch = new SpriteBatch();
    }

    private void loadTextures() {
        TextureRegion robotFacingUp = new TextureRegion(new Texture("Player/Mechs/Mech5.psd"));
        TextureRegion robotFacingDown = new TextureRegion(robotFacingUp);
        robotFacingDown.flip(false, true);
        robotFacingUpCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingUp));
        robotFacingDownCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingDown));
    }

    public void renderRobot(Robot robot) {
        // todo: this is bad - I know. I plan on fixing it soon by moving the robot to the object layer
        for (int i = 0; i < gameModel.getBoard().getTiledMap().getProperties().get("width", Integer.class); i++) {
            for (int j = 0; j < gameModel.getBoard().getTiledMap().getProperties().get("height", Integer.class); j++) {
                robotLayer.setCell(i, j, null);
            }
        }
        TiledMapTileLayer.Cell cell;
        if (robot.getDirection() == Direction.NORTH) {
            cell = robotFacingUpCell;
        } else if (robot.getDirection() == Direction.SOUTH) {
            cell = robotFacingDownCell;
        } else if (robot.getDirection() == Direction.EAST) {
            cell = robotFacingDownCell; // todo: change
        } else { // (robot.getDirection() == Direction.WEST) {
            cell = robotFacingDownCell; // todo: change
        }
        spriteBatch.begin();
        robotLayer.setCell(robot.getX(), robot.getY(), cell);
        spriteBatch.end();
    }

    public void render() {
        renderRobot(gameModel.getRobot());
        renderFont();
        boardRenderer.render();
    }

    private void loadFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    private void renderFont() {
        batch.begin();
        font.draw(batch, gameModel.getPlayer().handAsString(), 550, 550);
        font.draw(batch, gameModel.getPlayer().programmingSlotsAsString(), 550, 300);
        batch.end();
    }
}
