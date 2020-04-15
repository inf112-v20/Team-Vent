package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.view.StatsTable;
import inf112.skeleton.app.view.TestUtilsTable;
import inf112.skeleton.app.view.TiledMapActor;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;

    private Viewport viewport;
    private Stage stage;

    public GameScreen(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void show() {
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        float unitScale = 0.5f; // the unit scale determines the size of the map
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));

        // beside the board: stats table
        Table sideTable = new Table(skin);
        sideTable.defaults().left();
        sideTable.add(new StatsTable(gameModel, skin));
        sideTable.row();
        sideTable.add(new TestUtilsTable(gameModel.getMyPlayer(), skin)).padTop(40);

        // below the board: card table
        Table cardTable = new Table();
        cardTable.add(new Label("CARDS", skin));

        // root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(15);
        rootTable.add(new TiledMapActor(gameModel, unitScale)).top().left();
        rootTable.add(sideTable).expandX().top().left();
        rootTable.row();
        rootTable.add(cardTable).expandY();

        stage.addActor(rootTable);
        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
