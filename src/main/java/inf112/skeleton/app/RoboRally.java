package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class RoboRally extends Game {
    private Screen gameScreen;
    private Screen gameOverScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(gameScreen);
    }

    public void setGameOverScreen() {
        setScreen(gameOverScreen);
    }

    public void setGameScreen() {
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        gameOverScreen.dispose();
        gameScreen.dispose();
    }
}
