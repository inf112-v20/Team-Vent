package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.network.GameClient;
import inf112.skeleton.app.screens.GameOverScreen;
import inf112.skeleton.app.screens.GameScreen;

public class GameController extends InputAdapter {
    private final RoboRallyGame game;
    private GameModel gameModel;
    private boolean shiftIsPressed = false;
    private String map_filename;

    public GameController(RoboRallyGame game, String map_filename) {
        this.map_filename = map_filename;
        this.gameModel = new GameModel(map_filename);
        this.game = game;
        game.setScreen(new GameScreen(gameModel));
        Gdx.input.setInputProcessor(this);
    }

    //TODO: Make use of the gameClient class to implement multiplayer
    public GameController(RoboRallyGame game, String map_filename, GameClient gameClient) {
        this.map_filename = map_filename;
        this.gameModel = new GameModel(map_filename);
        this.game = game;
        game.setScreen(new GameScreen(gameModel));
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Program the robot using the keyboard input.
     */
    private void handleCardInput(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) { // undo cards
            gameModel.getPlayer().undoProgrammingSlotPlacement(keycode - 8);
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {  // play cards
            gameModel.getPlayer().placeCardFromHandToSlot(keycode - 8);
        } else if (keycode == Input.Keys.G) {  // deal new cards
            gameModel.getPlayer().generateCardHand();
        } else if (keycode == Input.Keys.E) { // end turn
            gameModel.endTurn();
        }
    }

    /**
     * Place the robot anywhere with the arrow keys. The rules of the game do not apply.
     */
    private void handleTestingInput(int keycode) {
        Robot robot = gameModel.getRobots().get(0);
        switch (keycode) {
            case Input.Keys.LEFT:
                robot.setLocation(robot.getLocation().rotateLeft());
                break;
            case Input.Keys.UP:
                robot.setLocation(robot.getLocation().forward());
                break;
            case Input.Keys.RIGHT:
                robot.setLocation(robot.getLocation().rotateRight());
                break;
            default:
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (game.getScreen() instanceof GameOverScreen && keycode == Input.Keys.SPACE) {
            this.gameModel = new GameModel(map_filename);
            game.setScreen(new GameScreen(this.gameModel));
            return true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        handleCardInput(keycode);
        handleTestingInput(keycode);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = true;
            return true;
        }
        return false;
    }
}
