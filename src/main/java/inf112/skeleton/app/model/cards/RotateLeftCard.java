package inf112.skeleton.app.model.cards;

import inf112.skeleton.app.model.board.Location;

public class RotateLeftCard implements IProgramCard {

    @Override
    public Location instruction(Location start) {
        return start.rotateLeft();
    }

    @Override
    public String toString() {
        return "ROTATE LEFT";
    }
}
