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
    private RoboRallyGame game;
    private Stage stage;

    public MenuScreen(RoboRallyGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        Table table = new Table();
        table.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));


        // RoboRally logo
        Texture logoTexture = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(logoTexture);
        table.add(logo);
        table.row();

        // Play button
        Button playButton = new TextButton("Play", skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                Gdx.graphics.setWindowedMode(1280, 720);
                new GameController(game, "map-1.tmx");
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });
        table.add(playButton).prefHeight(50).prefWidth(200).expandY();
        table.row();

        // Exit button
        Button exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Gdx.app.exit();
                System.exit(-1);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            return true;
        }
    });
        table.add(exitButton).prefHeight(50).prefWidth(200).expandY();

        table.setDebug(true);
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
