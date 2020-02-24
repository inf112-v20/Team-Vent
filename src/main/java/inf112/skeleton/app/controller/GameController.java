package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import inf112.skeleton.app.model.cards.RotateRightCard;
import inf112.skeleton.app.screens.GameOverScreen;

public class GameController {
    private final GameModel gameModel;
    private final Robot robot;
    private final TiledMapTileLayer tileLayer;
    private RoboRallyGame game;

    public GameController(GameModel gameModel, RoboRallyGame game) {
        this.gameModel = gameModel;
        this.robot = gameModel.getRobot();
        this.tileLayer = (TiledMapTileLayer) gameModel.getBoard().getLayers().get("Tile");
        this.game = game;
    }

    public boolean handleKeyUp(int keycode) {
        // moving with cards
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            IProgramCard card = gameModel.getPlayer().playCard(keycode - 8);
            if (card != null) {
                robot.execute(card);
                updateRobotAfterMove();
                return true; // game model has changed
            }
        }

        // generating a new hand of cards
        if (keycode == Input.Keys.G) {
            gameModel.getPlayer().genereateCardHand();
            return true; // game model has changed
        }

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
}
