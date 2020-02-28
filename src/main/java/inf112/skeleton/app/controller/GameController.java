package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    private GameModel gameModel;
    private RoboRallyGame game;
    private boolean shiftIsPressed = false;
    private Timer.Task task;
    private Timer timer;
    private Deque<Location> phaseSteps = new LinkedList<>();

    public GameController(RoboRallyGame game) {
        this.gameModel = new GameModel();
        this.game = game;
        game.setScreen(new GameScreen(new GameRenderer(this.gameModel)));
        task = new Timer.Task() {
            @Override
            public void run() {
                System.out.println(phaseSteps.toString());
                gameModel.getRobot().setLocation(phaseSteps.remove());
                updateRobotAfterMove();
            }
        };
        timer = new Timer();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Program the robot using the keyboard input.
     *
     * @return true iff the robot moved
     */
    private boolean handleCardInput(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) { // undo cards
            gameModel.getPlayer().undoProgrammingSlotPlacement(keycode - 8);
            return true;
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {  // play cards
            gameModel.getPlayer().placeCardFromHandToSlot(keycode - 8);
            return true; // input has been handled, no need to handle further
        } else if (keycode == Input.Keys.G) {  // deal new cards
            gameModel.getPlayer().generateCardHand();
            return true;
        } else if (keycode == Input.Keys.E) { // end turn
            phaseSteps.add(gameModel.getRobot().getLocation().copy());
            phaseSteps = gameModel.doPhase(0, phaseSteps);
            timer.scheduleTask(task, 0, 1, phaseSteps.size() - 1);
            gameModel.getPlayer().generateCardHand();
            return true;
        }
        return false;
    }

    /**
     * Move the robot with the arrow-keys without programming int. Useful for testing.
     *
     * @return true iff the robot moved
     */
    private boolean handleTestingInput(int keycode) {
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
                return false; // game stayed the same
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        log(String.format("%s up", Input.Keys.toString(keycode).toUpperCase()));
        if (game.getScreen() instanceof GameOverScreen && keycode == Input.Keys.SPACE) {
            this.gameModel = new GameModel();
            game.setScreen(new GameScreen(new GameRenderer(this.gameModel)));
            return true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        boolean moved = handleCardInput(keycode);
        if (!moved && gameModel.inTestMode()) {
            moved = handleTestingInput(keycode);
        }
        if (moved) {
            updateRobotAfterMove();
        }
        return moved; // game model has changed
    }

    private void updateRobotAfterMove() {
        TiledMapTileLayer.Cell cellUnderRobot = ((TiledMapTileLayer) gameModel.getBoard().getLayers().get("Tile"))
                .getCell(gameModel.getRobot().getX(), gameModel.getRobot().getY());
        final int TILE_ID_HOLE = 6;
        if (cellUnderRobot == null || cellUnderRobot.getTile() == null) {
            log("Robot went off the board and DIED");
            gameModel.getRobot().die();
            this.game.setScreen(new GameOverScreen());
        } else if (cellUnderRobot.getTile().getId() == TILE_ID_HOLE) {
            log("Robot fell into a HOLE");
            gameModel.getRobot().die();
            this.game.setScreen(new GameOverScreen());
        } else {
            log(String.format("Robot MOVED to %s", gameModel.getRobot().getLocation().toString()));
        }
    }

    private void log(String message) {
        if (gameModel.inTestMode()) {
            Gdx.app.log(GameController.class.getName(), message);
        }
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
