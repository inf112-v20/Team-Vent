package inf112.skeleton.app.model.cards;

import inf112.skeleton.app.model.board.Location;

public class MoveForwardCard implements IProgramCard {
    public Location instruction(Location loc) {
        return loc.forward();
    }

    @Override
    public String toString() {
        return "MOVE FORWARD";
    }
}
