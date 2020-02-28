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
    private GameModel gameModel;
    private OrthogonalTiledMapRenderer boardRenderer;
    private Batch spriteBatch;
    private SpriteBatch batch;
    private BitmapFont font;
    private TiledMapTileLayer playerLayer;
    private TiledMapTileLayer.Cell robotFacingUpCell;
    private TiledMapTileLayer.Cell robotFacingDownCell;
    private TiledMapTileLayer.Cell robotFacingRightCell;
    private TiledMapTileLayer.Cell robotFacingLeftCell;


    public GameRenderer(final GameModel gameModel) {
        this.gameModel = gameModel;
        TiledMap tiledMap = this.gameModel.getBoard();
        int tilesWide = tiledMap.getProperties().get("width", Integer.class);
        int tilesHigh = tiledMap.getProperties().get("height", Integer.class);
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile");
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesWide + tilesWide / 3f, tilesHigh);
        camera.update();
        loadTextures();
        loadFont();
        boardRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / tileLayer.getTileWidth());
        boardRenderer.setView(camera);
        spriteBatch = new SpriteBatch();
    }

    private void loadTextures() {
        TextureRegion robotFacingUp = new TextureRegion(new Texture("Player/Mechs/Mech1A_north.png"));
        TextureRegion robotFacingRight = new TextureRegion(new Texture("Player/Mechs/Mech1A_east.png"));
        TextureRegion robotFacingDown = new TextureRegion(new Texture("Player/Mechs/Mech1A_south.png"));
        TextureRegion robotFacingLeft = new TextureRegion(new Texture("Player/Mechs/Mech1A_west.png"));
        robotFacingUpCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingUp));
        robotFacingRightCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingRight));
        robotFacingDownCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingDown));
        robotFacingLeftCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingLeft));
    }

    public void renderRobot(Robot robot) {
        // todo: We plan on moving the robot to the object layer
        for (int i = 0; i < gameModel.getBoard().getProperties().get("width", Integer.class); i++) {
            for (int j = 0; j < gameModel.getBoard().getProperties().get("height", Integer.class); j++) {
                playerLayer.setCell(i, j, null);
            }
        }
        TiledMapTileLayer.Cell cell;
        if (robot.getDirection() == Direction.NORTH) {
            cell = robotFacingUpCell;
        } else if (robot.getDirection() == Direction.SOUTH) {
            cell = robotFacingDownCell;
        } else if (robot.getDirection() == Direction.EAST) {
            cell = robotFacingRightCell;
        } else if (robot.getDirection() == Direction.WEST) {
            cell = robotFacingLeftCell;
        } else {
            cell = robotFacingUpCell;
        }
        spriteBatch.begin();
        playerLayer.setCell(robot.getX(), robot.getY(), cell);
        assert playerLayer.getCell(robot.getX(), robot.getY()).equals(cell);
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
