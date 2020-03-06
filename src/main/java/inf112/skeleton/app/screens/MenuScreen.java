package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.controller.GameController;

public class MenuScreen extends ScreenAdapter {
    private Stage stage;

    public MenuScreen(RoboRallyGame game){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));

        // Menu background
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        Image background = new Image(backgroundTexture);

        // RoboRally logo
        Texture logoTexture = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(logoTexture);

        // Resolution selector
        Label resolutionLabel = new Label("Resolution: ", skin);
        resolutionLabel.setFontScale(1.3f);
        SelectBox resolutionBox = new SelectBox(skin);
        String[] resolutionOptions = {"1024x576", "1280x720", "1366x768", "1600x900", "1920x1080"};
        resolutionBox.setItems(resolutionOptions);

        // Map selector
        Label mapSelectorLabel = new Label("Map: ", skin);
        mapSelectorLabel.setFontScale(1.3f);
        SelectBox mapSelectorBox = new SelectBox(skin);
        String[] mapSelectorOptions = {"map-1.tmx", "demo.tmx"};
        mapSelectorBox.setItems(mapSelectorOptions);


        // Play button
        Button playButton = new TextButton("Play", skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                String selectedResolution = (String) resolutionBox.getSelected();
                String resolutions[] = selectedResolution.split("x");
                Gdx.graphics.setWindowedMode(Integer.parseInt(resolutions[0]), Integer.parseInt(resolutions[1]));
                new GameController(game, (String) mapSelectorBox.getSelected());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        // Exit button
        Button exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Gdx.app.exit();
                System.exit(0);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            return true;
        }
    });
        stage.addActor(background);

        table.top();
        table.add(logo).top().colspan(2).padBottom(100).padTop(50);
        table.row().padBottom(25);
        table.add(playButton).prefHeight(50).prefWidth(200).colspan(2);
        table.row().padBottom(25);
        table.add(mapSelectorLabel).right();
        table.add(mapSelectorBox).left();
        table.row().padBottom(25);
        table.add(resolutionLabel).right();
        table.add(resolutionBox).left();
        table.row().padBottom(25);
        table.add(exitButton).prefHeight(50).prefWidth(200).colspan(2).padTop(50);

        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
