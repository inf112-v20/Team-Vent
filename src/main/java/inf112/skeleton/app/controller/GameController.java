package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import inf112.skeleton.app.model.cards.RotateRightCard;
import inf112.skeleton.app.screens.GameOverScreen;

public class GameController {
    private final GameModel gameModel;
    private final Robot robot;
    private final TiledMapTileLayer tileLayer;
    private RoboRallyGame game;
    private boolean shiftIsPressed = false;

    public GameController(GameModel gameModel, RoboRallyGame game) {
        this.gameModel = gameModel;
        this.robot = gameModel.getRobot();
        this.tileLayer = (TiledMapTileLayer) gameModel.getBoard().getLayers().get("Tile");
        this.game = game;
    }

    private boolean cardKeyCodes(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) {
            gameModel.getPlayer().undoProgrammingSlotPlacement(keycode - 8);
            return true;
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            gameModel.getPlayer().placeCardFromHandToSlot(keycode - 8);
            return true; // input has been handled, no need to handle further
        } else if (keycode == Input.Keys.G) {
            gameModel.getPlayer().generateCardHand();
            return true;
        } else if (keycode == Input.Keys.E) {
            gameModel.endTurn();
            return true;
        }
        return false;
    }

    public boolean handleKeyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        // moving with cards
        cardKeyCodes(keycode);

        // moving with arrows (useful when testing)
        switch (keycode) {
            case Input.Keys.LEFT:
                robot.execute(new RotateLeftCard());
                break;
            case Input.Keys.UP:
                robot.execute(new MoveForwardCard());
                break;
            case Input.Keys.RIGHT:
                robot.execute(new RotateRightCard());
                break;
            default:
                return false; // game stayed the same
        }
        updateRobotAfterMove();
        return true; // game model has changed
    }

    private void updateRobotAfterMove() {
        TiledMapTileLayer.Cell cellUnderRobot = tileLayer.getCell(robot.getX(), robot.getY());
        if (cellUnderRobot == null) {
            robot.die();
            return;
        }
        final int TILE_ID_HOLE = 6;
        if (cellUnderRobot.getTile() == null) {
            log("Robot went off the board and DIED");
            robot.die();
        } else if (cellUnderRobot.getTile().getId() == TILE_ID_HOLE) {
            log("Robot was DAMAGED");
            robot.takeDamage();
            if (!robot.alive()) {
                log("Robot took too much damage and DIED");
                this.game.setScreen(new GameOverScreen(this.game));
            }
        } else {
            log(String.format("Robot MOVED to %s", robot.getLocation().toString()));
        }
    }

    private void log(String message) {
        Gdx.app.log(GameController.class.getName(), message);
    }

    public boolean handleKeyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = true;
            return true;
        }
        return false;
    }
}
