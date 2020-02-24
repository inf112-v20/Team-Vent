package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.model.cards.IProgramCard;

public class GameModel {

    private Robot robot;
    private TiledMap tiledMap;
    private Player player;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        player.genereateCardHand();
        tiledMap = new TmxMapLoader().load("demo.tmx");
    }

    public Robot getRobot() {
        return this.robot;
    }

    public TiledMap getBoard() {
        return this.tiledMap;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void endTurn() {
        for (int i = 0; i < 5; i++) {
            doPhase(i);
        }
        player.clearProgrammingSlots();
        player.genereateCardHand();
    }

    private void doPhase(int i) {
        IProgramCard card = player.getCardInProgrammingSlot(i);
        if (card != null) {
            robot.execute(card);
        }
    }
}
