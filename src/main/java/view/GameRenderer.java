package view;

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
    private static final int PIXELS_PER_TILE = 100;
    private final int TILES_WIDE;
    private final int TILES_HIGH;
    private GameModel gameModel;
    private OrthogonalTiledMapRenderer boardRenderer;
    private TiledMapTileLayer playerLayer;
    private Batch spriteBatch;
    private TiledMapTileLayer.Cell robotFacingLeftCell;
    private TiledMapTileLayer.Cell robotFacingRightCell;
    private SpriteBatch batch;
    private BitmapFont font;


    public GameRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        TiledMap tiledMap = this.gameModel.getBoard();

        // create camera
        TILES_WIDE = tiledMap.getProperties().get("width", Integer.class);
        TILES_HIGH = tiledMap.getProperties().get("height", Integer.class);
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, TILES_WIDE + TILES_WIDE / 2f, TILES_HIGH);
        camera.position.x = (float) TILES_WIDE / 2 + TILES_WIDE / 4f;
        camera.position.y = (float) TILES_HIGH / 2;
        camera.update();

        loadTextures();
        loadFont();
        boardRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / PIXELS_PER_TILE);
        boardRenderer.setView(camera);
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        spriteBatch = new SpriteBatch();
    }

    private void loadTextures() {
        TextureRegion robotFacingLeft = new TextureRegion(new Texture("Player/floating-robot.png"));
        TextureRegion robotFacingRight = new TextureRegion(robotFacingLeft);
        robotFacingRight.flip(true, false);
        robotFacingLeftCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingLeft));
        robotFacingRightCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(robotFacingRight));
    }

    public void renderRobot(Robot robot) {
        spriteBatch.begin();
        TiledMapTileLayer.Cell cell;
        if (robot.getDirection() == Direction.WEST) {
            cell = robotFacingLeftCell;
        } else {
            cell = robotFacingRightCell;
        }

        playerLayer.setCell(robot.getX(), robot.getY(), cell);
        spriteBatch.end();
    }

    public void render() {
        boardRenderer.render();
        // This is bad - I know. I plan on fixing it soon by moving the robot to the object layer
        for (int i = 0; i < gameModel.getBoard().getProperties().get("width", Integer.class); i++) {
            for (int j = 0; j < gameModel.getBoard().getProperties().get("height", Integer.class); j++) {
                playerLayer.setCell(i, j, null);
            }
        }
        renderRobot(gameModel.getRobot());
        renderFont();
    }

    private void loadFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    private void renderFont() {
        batch.begin();
        font.draw(batch, gameModel.getPlayer().generateHandAsString(), 600, 550);
        batch.end();
    }

}
