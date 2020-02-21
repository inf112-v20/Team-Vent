package inf112.skeleton.app.cards;

import inf112.skeleton.app.board.Location;

public interface IProgramCard {
    /**
     *
     * @param start the original location
     * @return the location that should result from following the instruction
     */
    Location instruction(Location start);
}