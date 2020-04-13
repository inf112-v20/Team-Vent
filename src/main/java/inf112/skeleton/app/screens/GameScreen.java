package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.view.TiledMapActor;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;
    private SpriteBatch batch;
    private BitmapFont font;

    private Viewport viewport;
    private Stage stage;


    public GameScreen(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void show() {
        loadFont();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        float unitScale = 0.55f; // the unit scale determines the size of the map
        stage.addActor(new TiledMapActor(gameModel, unitScale));
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        renderFont();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void loadFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void renderFont() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float horizontalAlign = width * 0.67f;
        float verticalPosition = height;
        float lineHeight = width * 0.015f;
        batch.begin();
        verticalPosition -= lineHeight;
        // Draw the state of this player's robot
        Robot mine = gameModel.getPlayer().getRobot();
        font.draw(batch, formatRobotState(mine), horizontalAlign, verticalPosition);
        // Draw other robots
        for (Robot robot : gameModel.getRobots()) {
            if (!robot.equals(mine)) {
                verticalPosition -= lineHeight;
                font.draw(batch, formatRobotState(robot), horizontalAlign, verticalPosition);
            }
        }

        // Draw the hand, and programming slots
        font.draw(batch, gameModel.getPlayer().handAsString(), horizontalAlign, height * 0.75f);
        font.draw(batch, gameModel.getPlayer().programmingSlotsAsString(), horizontalAlign, height * 0.45f);
        batch.end();
    }

    private String formatRobotState(Robot robot) {
        // note: because of a problem with the the font the string there is no way to align the strings perfectly
        // added to project board
        return String.format("%s: %-8s  %s: %d  %s: %d  %s:  %s%n",
                "NAME", robot.toString(),
                "HP", robot.getState().getHp(),
                "LIVES", robot.getRobotLife(),
                "FLAGS", String.format("%d / %d", robot.getState().getCapturedFlags(),
                        gameModel.getMapHandler().getNumberOfFlags()));
    }
}
