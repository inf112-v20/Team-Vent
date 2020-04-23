package inf112.skeleton.app.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.model.Robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class StatsTable extends Table {
    private GameModel gameModel;
    private HashMap<Label, Robot> labelsToRobots;

    public StatsTable(GameModel gameModel, Skin skin) {
        this.gameModel = gameModel;
        this.labelsToRobots = new HashMap<>();
        this.defaults().left().padBottom(5);
        List<Robot> sortedRobots = new LinkedList<>(gameModel.getRobots());
        Collections.swap(sortedRobots, 0, sortedRobots.indexOf(gameModel.getMyPlayer().getRobot()));
        sortedRobots.forEach(robot -> {
            Label label = new Label("", skin);
            labelsToRobots.put(label, robot);
            this.add(label);
            this.row();
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        labelsToRobots.forEach((key, value) -> key.setText(formatRobotState(value)));
    }

    private String formatRobotState(Robot robot) {
        // note: because of a problem with the the font the string there is no way to align the strings perfectly
        String format = "%-8s  %s %d  %s %d  %s  %s";
        if (robot.equals(gameModel.getMyPlayer().getRobot())) format += " THIS IS YOU";  // emphasize the player's own robot
        return String.format(format,
                robot.toString(),
                "HP:", robot.getState().getHp(),
                "LIVES:", robot.getState().getLives(),
                "FLAGS:", String.format("%d/%d", robot.getState().getCapturedFlags(),
                        gameModel.getMapHandler().getNumberOfFlags()));
    }

}
