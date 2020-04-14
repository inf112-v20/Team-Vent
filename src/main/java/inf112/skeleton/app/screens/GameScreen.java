package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.view.BoardRenderer;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthogonalTiledMapRenderer renderer;
    private int bottomTableHeight;
    private OrthographicCamera camera;


    public GameScreen(GameModel gameModel) {
        this.gameModel = gameModel;
        loadFont();
        // Note: the height of the bottom table is static and determines the size of the map
        this.bottomTableHeight = Gdx.graphics.getWidth() / 8;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, ((float) Gdx.graphics.getWidth() / (Gdx.graphics.getHeight() -
                bottomTableHeight)) * 10, 10);
        camera.update();
        renderer = new BoardRenderer(gameModel);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        Gdx.gl.glViewport(0, bottomTableHeight, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - bottomTableHeight);
        camera.update();
        renderFont();
    }

    private void loadFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void renderFont() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float horizontalAlign = width * 0.72f;
        float verticalPosition = height;
        float lineHeight = width * 0.02f;
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
        font.draw(batch, gameModel.getPlayer().handAsString(), horizontalAlign, height * 0.7f);
        font.draw(batch, gameModel.getPlayer().programmingSlotsAsString(), horizontalAlign, height * 0.3f);
        batch.end();
    }

    private String formatRobotState(Robot robot) {
        // note: because of a problem with the the font the string there is no way to align the strings perfectly
        // added to project board
        return String.format("%s: %-8s  %s: %d  %s: %d  %s:  %s%n",
                "NAME", robot.toString(),
                "HP", robot.getState().getHp(),
                "LIVES", robot.getState().getLives(),
                "FLAGS", String.format("%d / %d", robot.getState().getCapturedFlags(),
                        gameModel.getMapHandler().getNumberOfFlags()));
    }
}
