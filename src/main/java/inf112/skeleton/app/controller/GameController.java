package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import inf112.skeleton.app.model.cards.RotateRightCard;
import inf112.skeleton.app.screens.GameOverScreen;
import inf112.skeleton.app.screens.GameScreen;
import inf112.skeleton.app.view.GameRenderer;

import java.util.Deque;
import java.util.LinkedList;

public class GameController extends InputAdapter {
    private final RoboRallyGame game;
    private final Timer.Task task;
    private final Timer timer;
    private GameModel gameModel;
    private boolean shiftIsPressed = false;
    private Deque<Location> phaseSteps = new LinkedList<>();

    public GameController(RoboRallyGame game) {
        this.gameModel = new GameModel();
        this.game = game;
        game.setScreen(new GameScreen(new GameRenderer(this.gameModel)));
        task = new Timer.Task() {
            @Override
            public void run() {
                Location nextLocation = phaseSteps.remove();
                if (nextLocation != null) {
                    gameModel.getRobot().setLocation(nextLocation);
                } else {
                    game.setScreen(new GameOverScreen());
                }
            }
        };
        timer = new Timer();
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
            phaseSteps.add(gameModel.getRobot().getLocation().copy());
            phaseSteps = gameModel.doPhase(0, phaseSteps);
            timer.scheduleTask(task, 0, 1, phaseSteps.size() - 1);
            gameModel.getPlayer().generateCardHand();
        }
    }

    /**
     * Place the robot anywhere with the arrow keys. The rules of the game do not apply.
     */
    private void handleTestingInput(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                gameModel.getRobot().execute(new RotateLeftCard());
                break;
            case Input.Keys.UP:
                gameModel.getRobot().execute(new MoveForwardCard());
                break;
            case Input.Keys.RIGHT:
                gameModel.getRobot().execute(new RotateRightCard());
                break;
            default:
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (game.getScreen() instanceof GameOverScreen && keycode == Input.Keys.SPACE) {
            this.gameModel = new GameModel();
            game.setScreen(new GameScreen(new GameRenderer(this.gameModel)));
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
