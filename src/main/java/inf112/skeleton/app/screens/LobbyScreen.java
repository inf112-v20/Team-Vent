package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.controller.GameController;
import inf112.skeleton.app.network.GameClient;
import inf112.skeleton.app.network.GameHost;

import java.util.*;

public class LobbyScreen extends ScreenAdapter {
    private Stage stage;
    private Boolean isHost;
    private String hostAddress;
    private GameClient gameClient;
    private List<String> playerList;
    private RoboRallyGame game;
    private Timer timer = new Timer(true);

    public LobbyScreen(RoboRallyGame game, Boolean isHost, String hostAddress) {
        this.isHost = isHost;
        this.hostAddress = hostAddress;
        this.game = game;

        Skin skin = new Skin(Gdx.files.internal("skin/shade/skin/uiskin.json"));
        stage = new Stage();
        Table table = new Table();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Player in lobby list
        playerList = new List<>(skin);
        playerList.setItems(" ", " ", " ", " ", " ", " ", " ", " ");

        // Map selection box
        SelectBox<String> mapSelectorBox = new SelectBox<>(skin);
        String[] mapSelectorOptions = {"RiskyExchange.tmx", "IslandKing.tmx", "CaptureTheFlag.tmx"};
        mapSelectorBox.setItems(mapSelectorOptions);

        // Start game button
        Button startGameButton = new TextButton("Start Game", skin);
        startGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setGameStart(mapSelectorBox.getSelected());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Leave lobby button
        Button backButton = new TextButton("Leave lobby", skin);
        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backToMenu();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        table.setFillParent(true);
        table.setDebug(false);
        table.defaults().pad(5);
        table.add(playerList).prefWidth(200);
        table.row();
        if (isHost) {
            table.add(startGameButton).prefWidth(200);
            table.row();
            table.add(mapSelectorBox).prefWidth(200);
            table.row();
        }
        table.add(backButton).prefWidth(200);
        stage.addActor(table);
    }

    private void actOnGameStatus() {
        String[] status = gameClient.getGameStatus().split("/");

        switch (status[0]) {
            case "CLOSE":
                backToMenu();
                break;
            case "LOBBY WAITING":
                updatePlayerList();
                break;
            case "START":
                startGame(status[1]);
                break;
            default:

        }
    }

    private void startGame(String map) {
        Gdx.app.postRunnable(() -> {
            timer.cancel();
            new GameController(game, map, gameClient, isHost);
        });
    }

    private void setGameStart(String map) {
        gameClient.setGameStatus("START/" + map);
    }

    private void updatePlayerList() {
        String[] players = gameClient.getPlayersInLobby();
        if (players == null){
            players = new String[] {"","","","","","","",""};
        }
        playerList.setItems(players);
    }

    private void backToMenu() {
        timer.cancel();
        if (isHost) {
            gameClient.stopHost();
        } else {
            gameClient.closeConnection();
        }
        gameClient = null;
        Gdx.app.postRunnable(() -> game.setScreen(new MenuScreen(game)));
    }

    @Override
    public void show() {
        if (isHost) {
            Thread gameHostThread = new Thread(() -> new GameHost(hostAddress));
            gameHostThread.setName("Game Host Server");
            gameHostThread.start();
        }

        try {
            gameClient = new GameClient(hostAddress);
            // Acts on changes in the game state of the game host every 200 ms
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    actOnGameStatus();
                }
            }, 0, 200);


        } catch (GdxRuntimeException e) {
            // restart the menu screen instead of crashing when failing to connect to host
            Gdx.app.log(this.getClass().getName(), e.toString());

            MenuScreen menuScreen = new MenuScreen(game);
            menuScreen.displayConnectionError();
            game.setScreen(menuScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
