package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.view.BoardRenderer;

public class GameScreen extends ScreenAdapter {
    private final BoardRenderer boardRenderer;
    private final GameModel gameModel;
    private SpriteBatch batch;
    private BitmapFont font;
    private int width;
    private int height;

    public GameScreen(GameModel gameModel) {
        this.gameModel = gameModel;
        int tilesWide = gameModel.getMapHandler().getWidth();
        int tilesHigh = gameModel.getMapHandler().getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, tilesWide, tilesHigh);
        camera.update();
        boardRenderer = new BoardRenderer(gameModel);
        boardRenderer.setView(camera);
        loadFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        boardRenderer.render();
        renderFont();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

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

        font.draw(batch, gameModel.getPlayer().playerLifeAsString(gameModel.getPlayer().getPlayerLife()), width * 0.753f, height * 0.98f);
        font.draw(batch, gameModel.getPlayer().playerHPAsString(gameModel.getPlayer().getPlayerHP()), width * 0.753f, height * 0.95f);
        font.draw(batch, gameModel.getPlayer().handAsString(), width * 0.8f, height * 0.9f);
        font.draw(batch, gameModel.getPlayer().programmingSlotsAsString(), width * 0.8f, height * 0.4f);
        batch.end();
    }
}
