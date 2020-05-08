package inf112.skeleton.app.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StatsTable extends Table {
    private final Skin skin;
    private GameModel gameModel;
    private HashMap<Robot, Table> robotTables;

    public StatsTable(GameModel gameModel, Skin skin) {
        this.skin = skin;
        this.gameModel = gameModel;
        this.robotTables = new HashMap<>();
        this.defaults().left().padBottom(5);
        List<Robot> sortedRobots = new LinkedList<>(gameModel.getRobots());
        Collections.swap(sortedRobots, 0, sortedRobots.indexOf(gameModel.getMyPlayer().getRobot()));

        sortedRobots.forEach(robot -> {
            String identifier = gameModel.getMyPlayer().getRobot() == robot ? robot.getName() + " (YOU)" : robot.getName();
            this.add(new Label(identifier, skin)).left().padRight(20).top();
            RobotTable r = new RobotTable(robot);
            this.add(r).padBottom(20);
            robotTables.put(robot, r);
            this.row();
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        gameModel.getRobots().forEach(robot -> robotTables.get(robot).act(delta));
    }

    class RobotTable extends Table {
        private final CustomProgressBar livesBar;
        private final CustomProgressBar flagsBar;
        private final Label flagsLabel;
        private final Label hpLabel;
        private final Label livesLabel;
        private ProgressBar hpBar;
        private Robot robot;

        public RobotTable(Robot robot) {
            this.robot = robot;
            this.defaults().space(5);
            int pbWidth = 1;
            int pbHeight = 10;

            this.hpBar = new CustomProgressBar(0, Robot.getMaxHP(), pbWidth, pbHeight, Color.RED, Color.GREEN);
            this.hpLabel = new Label("", skin);
            this.add(new Label("HP", skin)).right();
            this.add(hpBar);
            this.add(hpLabel);
            this.row();

            this.livesBar = new CustomProgressBar(0, Robot.getMaxLives(), pbWidth, pbHeight, Color.RED, Color.GREEN);
            this.livesLabel = new Label("", skin);
            this.add(new Label("LIVES", skin)).right();
            this.add(livesBar);
            this.add(livesLabel);
            this.row();

            this.flagsBar = new CustomProgressBar(0, gameModel.getMapHandler().getNumberOfFlags(), pbWidth,
                    pbHeight, Color.DARK_GRAY, Color.GOLD);
            this.flagsLabel = new Label("", skin);
            this.add(new Label("FLAGS", skin)).right();
            this.add(flagsBar);
            this.add(flagsLabel);
            this.row();

            act(0);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            hpBar.setValue(robot.getState().getHp());
            hpLabel.setText(robot.getState().getHp() + "/" + Robot.getMaxHP());
            livesBar.setValue(robot.getState().getLives());
            livesLabel.setText(robot.getState().getLives() + "/" + Robot.getMaxLives());
            flagsBar.setValue(robot.getState().getCapturedFlags());
            flagsLabel.setText(robot.getState().getCapturedFlags() + "/" + gameModel.getMapHandler().getNumberOfFlags());
        }
    }
}
