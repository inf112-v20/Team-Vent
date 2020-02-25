package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;

public class GameModel {

    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;
    private int phaseNumber;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        player.generateCardHand();
        tiledMapHandler = new MapHandler("demo.tmx");
        phaseNumber = 0;
    }

    public Robot getRobot() {
        return this.robot;
    }

    public TiledMap getBoard() {
        return this.tiledMapHandler.getBoard();
    }

    public Player getPlayer() {
        return this.player;
    }

    public void endTurn() {
        for (int i = 0; i < 5; i++) {
            doPhase();
        }
        player.clearProgrammingSlots();
        player.generateCardHand();
    }

    private void doPhase() {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        if (card != null) {
            robot.execute(card);
        }
    }

    public boolean inTestMode() {
        return true;
    }
}
