package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.MapHandler;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.tiles.TileInformationUtils;

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
                    int newTileID = tiledMapHandler.getTileID(robot.getLocation().getPosition(), "Tile");
                    robot.moveInDirection(TileInformationUtils.getDirection(newTileID));
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
    }


    public boolean inTestMode() {
        return true;
    }
}
