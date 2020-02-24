package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.screens.GameScreen;

public class RoboRallyGame extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
