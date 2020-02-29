package inf112.skeleton.app.screens;

import com.badlogic.gdx.ScreenAdapter;
import inf112.skeleton.app.view.GameRenderer;

public class GameScreen extends ScreenAdapter {
    private final GameRenderer renderer;

    public GameScreen(GameRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void render(float delta) {
        renderer.render();
    }
}
