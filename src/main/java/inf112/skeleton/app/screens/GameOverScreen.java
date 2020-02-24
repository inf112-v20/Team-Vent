package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import inf112.skeleton.app.RoboRallyGame;

public class GameOverScreen extends InputAdapter implements Screen {
    private static final Float FONT_SCALE = 1.0f;
    private final RoboRallyGame game;
    private BitmapFont font;
    private SpriteBatch batch;

    public GameOverScreen(RoboRallyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(FONT_SCALE);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "OH NO!\nPRESS SPACE TO REPAIR YOUR ROBOT", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f, 0, Align.left, false);
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        font.dispose();
        batch.dispose();
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            game.setScreen(new GameScreen(this.game));
            return true;
        }
        return false;
    }
}
