package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.controller.GameController;

public class RoboRallyGame extends Game {

    @Override
    public void create() {
        new GameController(this);
    }
}