package inf112.skeleton.app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.model.GameModel;

public class GameRenderer {
    private final GameModel gameModel;
    private final BoardRenderer boardRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameRenderer(final GameModel gameModel) {
        this.gameModel = gameModel;
        TiledMap tiledMap = this.gameModel.getTiledMapHandler().getMap();
        int tilesWide = gameModel.getTiledMapHandler().getWidth();
        int tilesHigh = gameModel.getTiledMapHandler().getHeight();
        TiledMapTileLayer tileLayer = gameModel.getTiledMapHandler().getTileLayer();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesWide + tilesWide / 3f, tilesHigh);
        camera.update();
        loadFont();
        boardRenderer = new BoardRenderer(tiledMap, 1 / tileLayer.getTileWidth(), gameModel);
        boardRenderer.setView(camera);
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
