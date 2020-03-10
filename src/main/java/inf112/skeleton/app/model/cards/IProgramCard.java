package inf112.skeleton.app.model.cards;

import inf112.skeleton.app.model.board.Location;

public interface IProgramCard {
    /**
     * @param start the original location
     * @return the new location after following the instruction
     */
    Location instruction(Location start);

}