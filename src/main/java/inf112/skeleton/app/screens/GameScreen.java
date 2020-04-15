package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.view.TiledMapActor;

import java.util.*;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;
    private SpriteBatch batch;
    private BitmapFont font;

    private Viewport viewport;
    private Stage stage;

    private HashMap<Robot, Label> robotStatuses;
    private Label programmingSlotsLabel;
    private Label handLabel;


    public GameScreen(GameModel gameModel) {
        this.gameModel = gameModel;
        robotStatuses = new HashMap<>();
    }

    public void show() {
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        float unitScale = 0.5f; // the unit scale determines the size of the map
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));

        // inside sidebar: stats
        Table stats = new Table();
        stats.defaults().left();
        List<Robot> sortedRobots = new LinkedList<>(gameModel.getRobots());
        Collections.swap(sortedRobots, 0, sortedRobots.indexOf(gameModel.getMyPlayer().getRobot()));
        for (Robot robot : gameModel.getRobots()) {
            Label robotLabel = new Label(formatRobotState(robot), skin);
            robotStatuses.put(robot, robotLabel);
            stats.add(robotLabel);
            stats.row();
        }

        // inside sidebar: test utils
        Table utils = new Table();
        handLabel = new Label(gameModel.getMyPlayer().handAsString(), skin);
        utils.add(handLabel);
        utils.row();
        programmingSlotsLabel = new Label(gameModel.getMyPlayer().programmingSlotsAsString(), skin);
        utils.add(programmingSlotsLabel);

        // beside the board: stats table
        Table sideTable = new Table();
        sideTable.defaults().left();
        sideTable.add(stats);
        sideTable.row();
        sideTable.add(utils).bottom().padTop(50);

        // below the board: card table
        Table cardTable = new Table();
        cardTable.add(new Label("CARDS", skin)).expand();

        // root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(15);
        rootTable.add(new TiledMapActor(gameModel, unitScale)).left();
        rootTable.add(sideTable).expandX().top().left();
        rootTable.row();
        rootTable.add(cardTable).expandY();

        stage.addActor(rootTable);
        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        updateText();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void updateText(){
        for (Robot robot : gameModel.getRobots()) {
            robotStatuses.get(robot).setText(formatRobotState(robot));
        }
        handLabel.setText(gameModel.getMyPlayer().handAsString());
        programmingSlotsLabel.setText(gameModel.getMyPlayer().programmingSlotsAsString());
    }

    private String formatRobotState(Robot robot) {
        // note: because of a problem with the the font the string there is no way to align the strings perfectly
        return String.format("%-8s  %s %d  %s %d  %s  %s%n",
                robot.toString(),
                "HP:", robot.getState().getHp(),
                "LIVES:", robot.getState().getLives(),
                "FLAGS:", String.format("%d/%d", robot.getState().getCapturedFlags(),
                        gameModel.getMapHandler().getNumberOfFlags()));
    }
}
