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
    private GameModel gameModel;
    private boolean shiftIsPressed = false;
    private GameClient gameClient;
    private Boolean multiplayer;
    private String lastServerStatus = "START";
    private int numberOfPlayers;
    private boolean roundInProgress = false;

    private Timer timer = new Timer(true);
    private Timer countDownTimer = new Timer(true);
    private InputMultiplexer inputMultiPlexer;
    private GameScreen gameScreen;
    private boolean devMode = false;
    private final int turnLimit = 60;
    private int countDown;

    /**
     * Single player constructor
     */
    public GameController(RoboRallyGame game, String map_filename) {
        this.numberOfPlayers = 8;
        this.gameModel = new GameModel(map_filename, numberOfPlayers, 0);
        gameClient = null;
        multiplayer = false;
        inputMultiPlexer = new InputMultiplexer();
        inputMultiPlexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiPlexer);

        gameScreen = new GameScreen(gameModel, this, inputMultiPlexer);
        game.setScreen(gameScreen);
        countDown = turnLimit;
        scheduleCountDowns();
    }

    /**
     * Online multiplayer constructor
     */
    public GameController(RoboRallyGame game, String map_filename, GameClient gameClient, Boolean isHost) {
        this.numberOfPlayers = gameClient.getNumberOfPlayers();
        this.gameModel = new GameModel(map_filename, numberOfPlayers, gameClient.getIndex());
        this.gameClient = gameClient;
        multiplayer = true;
        inputMultiPlexer = new InputMultiplexer();
        inputMultiPlexer.addProcessor(this);

        gameScreen = new GameScreen(gameModel, this, inputMultiPlexer);
        game.setScreen(gameScreen);

        Gdx.input.setInputProcessor(inputMultiPlexer);
        startServerListener();
        gameClient.setReady(); // lets the server know that this client has started the game
        if (isHost){
            new HostController(gameClient);
        }

        countDown = turnLimit;
        scheduleCountDowns();
    }

    /**
     * Program the robot using the keyboard input.
     */
    private void handleCardInput(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) { // undo cards
            gameModel.getMyPlayer().undoProgrammingSlotPlacement(keycode - 8);
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {  // play cards
            gameModel.getMyPlayer().placeCardFromHandToSlot(keycode - 8);
        } else if (keycode == Input.Keys.G) {  // deal new cards
            gameModel.getMyPlayer().dealCards();
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
                gameScreen.unlockCards();
                gameModel.emptyPlayersProgrammingSlots();
                if (multiplayer){
                    gameClient.setReady();
                    gameModel.getMyPlayer().dealCards();
                }
                else { gameModel.generateCardHands();}
                roundInProgress = false;
                scheduleCountDowns();
                if (gameModel.getMyPlayer().wonOrLost){
                    lockInCards();
                }
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
        if ("START ROUND".equals(status)) startRound();
    }

    public void lockInCards() {
        gameScreen.lockCards();
        if (!devMode) gameModel.getMyPlayer().fillEmptySlots();
        if (!multiplayer){
            startRound();
            return;
        }
        gameClient.setProgrammingSlots(gameModel.getMyPlayer().getProgrammingSlots());
        gameClient.setReady();
    }

    private void scheduleCountDowns() {
        countDown = turnLimit;
        for (int i = 0; i <= turnLimit; i++) {
            int count = countDown;
            countDownTimer.schedule(countDownStep(count), i*1000);
            countDown = countDown -1;
        }
        countDownTimer.schedule(forcedEndTurn(), (turnLimit+1)*1000);
    }

    private TimerTask countDownStep(int count) {
        return new TimerTask() {
            @Override
            public void run() {
                gameScreen.setEndTurnButtonText("DONE ("+ count + ")");
            }
        };
    }

    private TimerTask forcedEndTurn() {
        return new TimerTask() {
            @Override
            public void run() {
                lockInCards();
            }
        };
    }

    private void startRound(){
        countDownTimer.cancel();
        countDownTimer = new Timer(true);
        roundInProgress = true;
        if (multiplayer){
            Card[][] playerSlots = gameClient.getPlayerCards();
            for (int playerI = 0; playerI < numberOfPlayers; playerI++){
                for (int cardSlotI = 0; cardSlotI < 5; cardSlotI++){
                    gameModel.getPlayer(playerI).setCardinProgrammingSlot(cardSlotI, playerSlots[playerI][cardSlotI]);
                }
            }
        }
        if (!multiplayer) {
            gameModel.fillPLayersProgrammingSlots();
        }
        gameModel.endTurn();

        gameScreen.setEndTurnButtonText("EXECUTING");
        countDownTimer.cancel();
        countDownTimer = new Timer(true);

        int delay = 0;
        for (int i = 0; i < 5; i++) {
            timer.schedule(togglePhasePopUp(i, true), delay * 500);
            delay += 4;
            timer.schedule(togglePhasePopUp(i, false), delay * 500);
            delay += 2;
            gameModel.scheduleSteps(delay, i, gameModel.cardSteps);
            delay += gameModel.cardSteps.get(i).size();
            gameModel.scheduleSteps(delay, i, gameModel.tileSteps);
            delay += gameModel.tileSteps.get(i).size();
            gameModel.scheduleSteps(delay, i, gameModel.laserSteps);
            delay += gameModel.laserSteps.get(i).size();
            gameModel.scheduleSteps(delay, i, gameModel.endOfPhaseSteps);
        }

        if (gameModel.checkWinnerOrLoser()) {
            if(gameModel.gameState.getState(gameModel.getMyPlayer().getRobot()).getCapturedFlags() == gameModel.getMapHandler().getNumberOfFlags()) {
                timer.schedule(toggleWinOrLosePopUp(true, true), delay * 500);
                timer.schedule(toggleWinOrLosePopUp(true, false), (delay+4) * 500);
                gameModel.getMyPlayer().wonOrLost = true;
            }
            else if (gameModel.gameState.getState(gameModel.getMyPlayer().getRobot()).getLives() == 0 && !gameModel.getMyPlayer().wonOrLost) {
                timer.schedule(toggleWinOrLosePopUp(false, true), delay * 500);
                timer.schedule(toggleWinOrLosePopUp(false, false), (delay+4) * 500);
                gameModel.getMyPlayer().wonOrLost = true;
            }
            delay += 6;
        }
        timer.schedule(endOfTurn(), delay * 500);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        if (roundInProgress){
            return false;
        }
        if (devMode) handleCardInput(keycode);
        if (devMode) handleTestingInput(keycode);
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

    private TimerTask togglePhasePopUp(int phase, boolean show) {
        return  new TimerTask() {
            @Override
            public void run() {
                gameScreen.phasesImages[phase].setShow(show);
            }
        };
    }

    private TimerTask toggleWinOrLosePopUp(boolean won, boolean show) {
        return  new TimerTask() {
            @Override
            public void run() {
                if (won) {
                    gameScreen.win.setShow(show);
                }
                else { gameScreen.lose.setShow(show); }
            }
        };
    }

}
