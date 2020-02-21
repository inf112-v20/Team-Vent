package inf112.skeleton.app.cards;

import inf112.skeleton.app.board.Location;
import inf112.skeleton.app.board.Side;

public class RotateCard implements IProgramCard {
    private Side side;

    public RotateCard(Side side) {
        this.side = side;
    }

    public Location instruction(Location loc) {
        return side.equals(Side.LEFT) ? loc.rotateLeft() : loc.rotateRight();
    }

}