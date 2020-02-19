package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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

public class HelloRobot extends InputAdapter implements ApplicationListener {
    private static final int MAP_SIZE_X = 5;
    private static final int MAP_SIZE_Y = 5;
    private static final int TILE_PIXELS = 300;
    TiledMapTileLayer playerLayer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Cell playerCell;
    private Vector2 playerPos = new Vector2(2, 2);

    @Override
    public void create() {
        TiledMap tiledMap = new TmxMapLoader().load("demo.tmx");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_SIZE_X, MAP_SIZE_Y);
        camera.position.x = (float) MAP_SIZE_X / 2;
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / TILE_PIXELS);
        mapRenderer.setView(camera);
        Texture playerTexture = new Texture("floating-robot.png");
        playerCell = new Cell().setTile(new StaticTiledMapTile(new TextureRegion(playerTexture)));
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    @Override
    public void render() {
        playerLayer.setCell((int) playerPos.x, (int) playerPos.y, playerCell);
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
        switch (keycode) {
            case Input.Keys.LEFT:
                playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
                playerPos.x -= 1;
                return true;
            case Input.Keys.UP:
                playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
                playerPos.y += 1;
                return true;
            case Input.Keys.RIGHT:
                playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
                playerPos.x += 1;
                return true;
            case Input.Keys.DOWN:
                playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
                playerPos.y -= 1;
                return true;
            default:
                return false;
        }
    }
}

//@Authors: