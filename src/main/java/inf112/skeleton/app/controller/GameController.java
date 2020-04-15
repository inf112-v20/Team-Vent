package inf112.skeleton.app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.RobotState;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.cards.Card;
import inf112.skeleton.app.network.GameClient;
import inf112.skeleton.app.screens.GameScreen;

import java.util.*;

public class GameController extends InputAdapter {
    private final RoboRallyGame game;
    private GameModel gameModel;
    private boolean shiftIsPressed = false;
    private GameClient gameClient;
    private Boolean multiplayer;
    private String lastServerStatus = "START";
    private int numberOfPlayers;
    private int playerIndex;
    private boolean roundInProgress = false;
    private Timer timer = new Timer(true);
    private InputMultiplexer inputMultiPlexer;

    /**
     * Single player constructor
     */
    public GameController(RoboRallyGame game, String map_filename) {
        this.numberOfPlayers = 4;
        playerIndex = 0;
        this.gameModel = new GameModel(map_filename, numberOfPlayers, playerIndex);
        this.game = game;
        gameClient = null;
        multiplayer = false;
        inputMultiPlexer = new InputMultiplexer();
        inputMultiPlexer.addProcessor(this);
        game.setScreen(new GameScreen(gameModel, inputMultiPlexer));
        Gdx.input.setInputProcessor(inputMultiPlexer);
    }

    /**
     * Online multiplayer constructor
     */
    public GameController(RoboRallyGame game, String map_filename, GameClient gameClient, Boolean isHost) {
        this.numberOfPlayers = gameClient.getNumberOfPlayers();
        playerIndex = gameClient.getIndex();
        this.gameModel = new GameModel(map_filename, numberOfPlayers, playerIndex);
        this.game = game;
        this.gameClient = gameClient;
        multiplayer = true;
        inputMultiPlexer = new InputMultiplexer();
        inputMultiPlexer.addProcessor(this);
        game.setScreen(new GameScreen(gameModel, inputMultiPlexer));
        Gdx.input.setInputProcessor(inputMultiPlexer);
        startServerListener();
        gameClient.setReady(); // lets the server know that this client has started the game
        if (isHost){
            new HostController(gameClient);
        }
    }

    /**
     * Program the robot using the keyboard input.
     */
    private void handleCardInput(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) { // undo cards
            gameModel.getPlayer(playerIndex).undoProgrammingSlotPlacement(keycode - 8);
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {  // play cards
            gameModel.getPlayer(playerIndex).placeCardFromHandToSlot(keycode - 8);
        } else if (keycode == Input.Keys.G) {  // deal new cards
            gameModel.getPlayer(playerIndex).generateCardHand();
        } else if (keycode == Input.Keys.E) { // end turn
            lockInCards();
        }
    }

    /**
     * Place the robot anywhere with the arrow keys. The rules of the game do not apply.
     */
    private void handleTestingInput(int keycode) {
        Robot robot = gameModel.getRobots().get(0);
        RobotState newState = robot.getState().copy();
        Location current = newState.getLocation();
        switch (keycode) {
            case Input.Keys.LEFT:
                robot.updateState(robot.getState().updateLocation(current.rotateLeft()));
                break;
            case Input.Keys.UP:
                robot.updateState(robot.getState().updateLocation(current.forward()));
                break;
            case Input.Keys.RIGHT:
                robot.updateState(robot.getState().updateLocation(current.rotateRight()));
                break;
            default:
        }
    }

    private TimerTask listenToServer(){
        return new TimerTask() {
            @Override
            public void run() {
                actOnGameStatus();
            }
        };
    }

    private TimerTask endOfTurn(){
        return new TimerTask() {
            @Override
            public void run() {
                gameModel.emptyPlayersProgrammingSlots();
                if (multiplayer){
                    gameClient.setReady();
                }
                gameModel.getPlayer(playerIndex).generateCardHand();
                roundInProgress = false;
            }
        };
    }

    private void startServerListener(){
        timer.schedule(listenToServer(), 0, 200);
    }

    private void actOnGameStatus(){
        String status = gameClient.getGameStatus();
        if (status.equals(lastServerStatus)){ // avoids executing on same server status more than once
            return;
        }
        lastServerStatus = status;
        switch (status) {
            case "START ROUND":
                startRound();
                break;
            default:
                break;
        }
    }

    private void lockInCards(){
        if (!multiplayer){
            startRound();
            return;
        }
        gameClient.setProgrammingSlots(gameModel.getPlayer(playerIndex).getProgrammingSlots());
        gameClient.setReady();
    }

    // TODO: Fix this in cases where a player slot is empty between two players; Player1 i = 0, Player2 i = 2
    private void startRound(){
        roundInProgress = true;
        if (multiplayer){
            Card[][] playerSlots = gameClient.getPlayerCards();
            for (int playerI = 0; playerI < numberOfPlayers; playerI++){
                for (int cardSlotI = 0; cardSlotI < 5; cardSlotI++){
                    gameModel.getPlayer(playerI).setCardinProgrammingSlot(cardSlotI, playerSlots[playerI][cardSlotI]);
                }
            }
        }
        gameModel.endTurn();
        gameModel.timer.schedule(endOfTurn(), gameModel.delay * 500);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        if (roundInProgress){
            return false;
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
