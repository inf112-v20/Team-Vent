package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.controller.GameController;
import inf112.skeleton.app.model.GameModel;
import view.GameRenderer;

public class GameScreen extends InputAdapter implements Screen {
    private final RoboRallyGame game;
    private GameRenderer renderer;
    private GameController controller;

    public GameScreen(RoboRallyGame game) {
        this.game = game;
    }

    private static void log(String message) {
        Gdx.app.log(GameScreen.class.getName(), message);
    }

    @Override
    public void show() {
        GameModel gameModel = new GameModel();
        this.renderer = new GameRenderer(gameModel);
        this.controller = new GameController(gameModel, game);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //controller.update(delta);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyUp(int keycode) {
        log(String.format("Input: %s released", Input.Keys.toString(keycode).toUpperCase()));
        return controller.handleKeyUp(keycode);
    }
}
