package inf112.skeleton.app.controller;

import com.badlogic.gdx.Input;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import inf112.skeleton.app.model.cards.RotateRightCard;
import inf112.skeleton.app.screens.GameOverScreen;

public class GameController {
    private final GameModel gameModel;
    private final RoboRallyGame game;
    private boolean shiftIsPressed = false;

    public GameController(GameModel gameModel, RoboRallyGame game) {
        this.gameModel = gameModel;
        this.game = game;
    }

    /**
     * Program the robot using the keyboard input.
     *
     * @return true iff the robot moved
     */
    private boolean handleCardInput(int keycode) {
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

    /**
     * Move the robot with the arrow-keys without programming int. Useful for testing.
     *
     * @return true iff the robot moved
     */
    private IProgramCard cardInputFromArrowKeys(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                return new RotateLeftCard();
            case Input.Keys.UP:
                return new MoveForwardCard();
            case Input.Keys.RIGHT:
                return new RotateRightCard();
            default:
                return null; // game stayed the same
        }
    }


    public boolean handleKeyUp(int keycode) {
        IProgramCard card = cardInputFromArrowKeys(keycode);
        if (card != null) {
            gameModel.applyCardToRobot(card, gameModel.getRobot());
        }

        // Programming the robot
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }

        boolean moved = handleCardInput(keycode);


        if (!gameModel.getRobot().alive()) {
            game.setScreen(new GameOverScreen(game));
        }
        return true; // no further input handling needed
    }


    public boolean handleKeyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = true;
            return true;
        }
        return false;
    }
}
