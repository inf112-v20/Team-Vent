package inf112.skeleton.app.cards;

import inf112.skeleton.app.board.Location;

public class RotateLeftCard implements IProgramCard {

    @Override
    public Location instruction(Location start) {
        return start.rotateLeft();
    }
}
