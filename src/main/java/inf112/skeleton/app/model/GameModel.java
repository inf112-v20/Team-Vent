package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Timer;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileInformationUtils;

public class GameModel {

    private Robot robot;
    private MapHandler tiledMapHandler;
    private Player player;

    private Timer a;
    private Timer.Task b;
    private int phase = 0;

    public GameModel() {
        robot = new Robot();
        player = new Player();
        player.generateCardHand();
        tiledMapHandler = new MapHandler("demo.tmx");

        a = new Timer();
        b = new Timer.Task() {
            @Override
            public void run() {
                doPhase(phase);
            }
        };
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
        a.scheduleTask(b,0,1,4);
        //player.clearProgrammingSlots();
        player.generateCardHand();
    }

    private void doPhase(int phaseNumber) {
        IProgramCard card = player.getCardInProgrammingSlot(phaseNumber);
        player.setCardinProgrammingSlot(phaseNumber, null);
        // TODO: Find a more elegant solution
        if (card != null) {
            if (!(card instanceof MoveForwardCard && tiledMapHandler.wallInPath(robot.getLocation().copy()))){
                robot.execute(card);
            }
        }
        int currentTileID = tiledMapHandler.getTileID(robot.getLocation().getPosition(), "Tile");
        String currentTileType = tiledMapHandler.getTileType(robot.getLocation().getPosition(), "Tile");

        switch(currentTileType){
            case("conveyor_normal"):
                robot.moveInDirection(TileInformationUtils.getDirection(currentTileID));
                break;
            case("conveyor_express"):
                robot.moveInDirection(TileInformationUtils.getDirection(currentTileID));
                String newTileType = tiledMapHandler.getTileType(robot.getLocation().getPosition(), "Tile");
                if ("conveyor_express".equals(newTileType)) {
                    robot.moveInDirection(TileInformationUtils.getDirection(currentTileID));
                }
                break;
            case("gear_clockwise"):
                robot.setLocation(new Location(robot.getLocation().getPosition(), robot.getDirection().right()));
                break;
            case("gear_counterclockwise"):
                robot.setLocation(new Location(robot.getLocation().getPosition(), robot.getDirection().left()));
                break;
            default:
        }
        phase++;
        if (phase == 5) {phase = 0;}
    }


    public boolean inTestMode() {
        return true;
    }
}
