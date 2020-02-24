package inf112.skeleton.app.model.cards;

import inf112.skeleton.app.model.board.Location;

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
