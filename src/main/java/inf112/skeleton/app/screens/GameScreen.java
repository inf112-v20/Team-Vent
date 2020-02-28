package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import inf112.skeleton.app.view.GameRenderer;

public class GameScreen implements Screen {
    private GameRenderer renderer;

    public GameScreen(GameRenderer renderer) {
        this.renderer = renderer;
    }

    private static void log(String message) {
        Gdx.app.log(GameScreen.class.getName(), message);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        log(String.format("Resized to width=%d, height=%d", width, height));
    }

    @Override
    public void pause() {
        log("Paused");
    }

    @Override
    public void resume() {
        log("Resumed");
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
