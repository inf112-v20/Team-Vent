package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Board;
import inf112.skeleton.app.model.board.TileType;
import inf112.skeleton.app.model.cards.IProgramCard;

public class GameModel {

    private Robot robot;
    private final Board board;
    private Player player;

    /**
     * Start a game by loading a tiled map from a file
     *
     * @param filepath relative to the assets folder
     */
    public GameModel(String filepath) {
        board = new Board(filepath);
        initialize();
    }

    /**
     * Start the game with a an empty board (intended for unit testing)
     *
     * @param width  in tiles
     * @param height in tiles
     */
    public GameModel(int width, int height) {
        board = new Board(width, height);
        initialize();
    }

    public void initialize() {
        robot = new Robot();
        player = new Player();
        player.generateCardHand();
    }

    public Robot getRobot() {
        return this.robot;
    }

    public Board getBoard() {
        return this.board;
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

    private void doPhase(int i) {
        IProgramCard card = player.getCardInProgrammingSlot(i);
        if (card != null) {
            applyCardToRobot(card, robot);
        }
    }

    private void log(String message) {
        System.out.println(message);
    }

    public void applyCardToRobot(IProgramCard card, Robot robot) {
        robot.execute(card);
        TileType tileType = board.getTile(robot.getX(), robot.getY());
        log(String.format("Robot location: %s", robot.getLocation().toString()));
        if (tileType == null) {
            log("Robot fell outside the board");
            robot.die();
        } else if (tileType == TileType.HOLE) {
            robot.die();
        } else {
            log("Robot fell into a hole.");
        }
    }
}
