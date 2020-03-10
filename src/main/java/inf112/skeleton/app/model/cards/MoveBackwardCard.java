package inf112.skeleton.app.model.cards;

import inf112.skeleton.app.model.board.Location;

public class MoveBackwardCard implements IProgramCard {

    public Location instruction(Location loc) {
        return loc.backward();
    }

    @Override
    public String toString() {
        return "MOVE BACKWARDS";
    }

    public int value() {
    return 2;
    }
}
