package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.controller.GameController;

public class MenuScreen extends ScreenAdapter {
    private Stage stage;
    private Label messageLabel;

    public MenuScreen(RoboRallyGame game) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        // RoboRally logo
        Texture logoTexture = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(logoTexture);

        // Resolution selector
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));
        Label resolutionLabel = new Label("Resolution: ", skin);
        SelectBox<String> resolutionBox = new SelectBox<>(skin);
        String[] resolutionOptions = {"1366x768"};
        resolutionBox.setItems(resolutionOptions);

        // Map selector
        SelectBox<String> mapSelectorBox = new SelectBox<>(skin);
        String[] mapSelectorOptions = {"RiskyExchange.tmx", "IslandKing.tmx", "CaptureTheFlag.tmx"};
        mapSelectorBox.setItems(mapSelectorOptions);

        // Host checkbox
        CheckBox hostCheckBox = new CheckBox(" Host", skin);

        // Label for displaying messages to the user
        messageLabel = new Label("", skin);

        // Ip text field
        TextField ipTextField = new TextField("127.0.0.1", skin);
        ipTextField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                messageLabel.setText("");
            }
        });

        // Sub-table with multi-player options
        Table multiplayerTable = new Table();
        multiplayerTable.add(new Label("Host IP:", skin));
        multiplayerTable.add(ipTextField).padLeft(5);
        multiplayerTable.row();
        multiplayerTable.add(hostCheckBox).padTop(10);

        // Play button
        Button playButton = new TextButton("Singleplayer", skin);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                new GameController(game, mapSelectorBox.getSelected());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Play multiplayer button
        Button multiplayerPlayButton = new TextButton("Multiplayer", skin);
        multiplayerPlayButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LobbyScreen(game, hostCheckBox.isChecked(), ipTextField.getText()));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Exit button
        Button exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                System.exit(0);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        table.add(logo).top().colspan(2).padBottom(100).padTop(50);
        table.row().padBottom(25);
        table.add(playButton).prefHeight(50).prefWidth(200).colspan(2);
        table.row().padBottom(25);
        table.add(multiplayerPlayButton).prefHeight(50).prefWidth(200).colspan(2);
        table.row().padBottom(10);

        table.add(multiplayerTable).colspan(2).expandX();
        table.row();
        table.add(messageLabel).colspan(2);
        table.row().padBottom(25);

        table.add(mapSelectorBox).colspan(2).prefWidth(200).padTop(20);
        table.row().padBottom(25);

        table.add(exitButton).prefHeight(50).prefWidth(200).colspan(2);

        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void displayConnectionError() {
        messageLabel.setText("Failed to connect to the host");
    }
}
