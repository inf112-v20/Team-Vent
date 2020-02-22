package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.board.Direction;
import inf112.skeleton.app.board.Location;
import inf112.skeleton.app.board.RVector2;
import inf112.skeleton.app.cards.IProgramCard;
import inf112.skeleton.app.cards.MoveForwardCard;
import inf112.skeleton.app.cards.RotateLeftCard;
import inf112.skeleton.app.cards.RotateRightCard;

public class RoboRallyGame extends InputAdapter implements ApplicationListener {
    private static final int MAP_SIZE_X = 5;
    private static final int MAP_SIZE_Y = 5;
    private static final int TILE_PIXELS = 100;
    private TiledMapTileLayer playerLayer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Cell playerCell;
    private Robot robot;
    private Player mrT;

    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        TiledMap tiledMap = new TmxMapLoader().load("demo.tmx");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        //TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Board");
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_SIZE_X+MAP_SIZE_X / 2, MAP_SIZE_Y);
        camera.position.x = (float) MAP_SIZE_X / 2  + MAP_SIZE_X / 4;
        camera.position.y = (float) MAP_SIZE_Y / 2;
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / TILE_PIXELS);
        mapRenderer.setView(camera);
        robot = new Robot(new Location(new RVector2(2, 2), Direction.NORTH));
        Texture playerTexture = new Texture("Player/floating-robot.png");
        playerCell = new Cell().setTile(new StaticTiledMapTile(new TextureRegion(playerTexture)));
        Gdx.input.setInputProcessor(this);
        tiledMap.getLayers().get("PLayer");
        playerLayer.setCell(robot.getX(), robot.getY(), playerCell);

        mrT = new Player();
        mrT.genereateCardHand();
        createFont();
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderfont();
        playerLayer.setCell(robot.getX(), robot.getY(), playerCell);
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyUp(int keycode) {
        cardKeyCodes(keycode);
        switch (keycode) {
            case Input.Keys.LEFT:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new RotateLeftCard());
                return true;
            case Input.Keys.UP:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new MoveForwardCard());
                return true;
            case Input.Keys.RIGHT:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new RotateRightCard());
                return true;
            case Input.Keys.DOWN:
                //maybe implement later
                return true;
            default:
                return false;
        }
    }


    private void createFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    private void renderfont() {
        batch.begin();
        font.draw(batch, mrT.generateHandAsString(),600,550);
        batch.end();
    }

    private void cardKeyCodes(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            IProgramCard card = mrT.playCard(keycode - 8);
            if (card != null) {
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(card);
            }
        }
        if (keycode == Input.Keys.G) {mrT.genereateCardHand();}
    }


}

//@Authors: