package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class HelloRobot implements ApplicationListener {
    private static final int MAP_SIZE_X = 5;
    private static final int MAP_SIZE_Y = 5;
    private static final int TILE_PIXELS = 300;
    TiledMapTileLayer playerLayer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Cell playerAliveCell; // todo: added <Actor>, otherwise raw use of parameterized class 'Cell'
    private Cell playerWonCell;
    private Cell playerDeadCell;
    private Vector2 playerPos;

    @Override
    public void create() {
        TiledMap tiledMap = new TmxMapLoader().load("demo.tmx");
        TiledMapTileLayer boardLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Board");
        TiledMapTileLayer flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Flag");
        TiledMapTileLayer holeLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Hole");
        TiledMapTileLayer startLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Start");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_SIZE_X, MAP_SIZE_Y);
        camera.position.x = (float) MAP_SIZE_X / 2;
        camera.update();

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / TILE_PIXELS);
        mapRenderer.setView(camera);

        Texture playerTexture = new Texture("player.png");
        TextureRegion[][] textureRegions = new TextureRegion(playerTexture).split(TILE_PIXELS, TILE_PIXELS);
        playerAliveCell = new Cell().setTile(new StaticTiledMapTile(textureRegions[0][0]));
        playerDeadCell = new Cell().setTile(new StaticTiledMapTile(textureRegions[0][1]));
        playerWonCell = new Cell().setTile(new StaticTiledMapTile(textureRegions[0][2]));
        playerPos = new Vector2(0, 0);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playerLayer.setCell(0, 0, playerAliveCell); // todo
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
}
