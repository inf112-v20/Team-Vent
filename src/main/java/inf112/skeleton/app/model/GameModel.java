package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;

public class GameModel {

    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        player.generateCardHand();
        tiledMapHandler = new MapHandler("demo.tmx");
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
            doPhase(i);
        }
        player.clearProgrammingSlots();
        player.generateCardHand();
    }

    private void doPhase(int phaseNumber) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        // TODO: Find a more elegant solution
        if (card != null) {
            if (!(card instanceof MoveForwardCard) || !(tiledMapHandler.wallInPath(robot.getLocation().copy()))){
                robot.execute(card);
            }
        }
    }


    public boolean inTestMode() {
        return true;
    }
}
