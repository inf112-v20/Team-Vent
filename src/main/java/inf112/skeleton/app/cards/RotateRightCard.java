package inf112.skeleton.app.cards;

import inf112.skeleton.app.board.Location;

public class RotateRightCard implements IProgramCard {

    @Override
    public Location instruction(Location start) {
        return start.rotateRight();
    }

    @Override
    public String toString() {
        return "ROTATE RIGHT";
    }
}
