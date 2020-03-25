package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.network.GameClient;
import inf112.skeleton.app.network.GameHost;

public class LobbyScreen extends ScreenAdapter {
    private Stage stage;
    private Boolean isHost;
    private String hostAddress;
    private GameClient gameClient;
    private List playerList;
    private RoboRallyGame game;

    public LobbyScreen(RoboRallyGame game, Boolean isHost, String hostAddress) {
        this.isHost = isHost;
        this.hostAddress = hostAddress;
        this.game = game;

        Skin skin = new Skin(Gdx.files.internal("skin/shade/skin/uiskin.json"));
        stage = new Stage();
        Table table  = new Table();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Player in lobby list
        playerList = new List<String>(skin);
        playerList.setItems(new String[]{" ", " ", " ", " ", " ", " ", " ", " "});

        // Leave lobby button
        Button backButton = new TextButton("Leave lobby", skin);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                backToMenu();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        table.setFillParent(true);
        table.setDebug(false);
        table.add(playerList);
        table.row();
        table.add(backButton);
        stage.addActor(table);
    }

    private void actOnGameStatus(){
        String status = gameClient.getGameStatus();

        switch (status){
            case "CLOSE":
                backToMenu();
                break;
            case "LOBBY WAITING":
                updatePlayerList();
                break;
        }
    }

    private void updatePlayerList(){
        String[] players = gameClient.getPlayersInLobby();
        playerList.setItems(players);
    }

    private void backToMenu(){
        Timer.instance().clear();
        if(isHost){
            gameClient.stopHost();
        } else {
            gameClient.closeConnection();
        }
        gameClient = null;
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public void show(){
        if(isHost){
            Thread gameHostThread = new Thread(() -> {
                GameHost gameHost = new GameHost(hostAddress);
            });
            gameHostThread.setName("'Game Host Server");
            gameHostThread.start();
        }
        gameClient = new GameClient(hostAddress);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                actOnGameStatus();
            }
        }, 0, 0.2f);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
