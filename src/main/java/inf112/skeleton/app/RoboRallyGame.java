package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.screens.MenuScreen;

public class RoboRallyGame extends Game {

    @Override
    public void create() {
        this.setScreen(new MenuScreen(this));
    }
}