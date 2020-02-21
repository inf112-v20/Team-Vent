package inf112.skeleton.app.board;

public enum Direction {
    NORTH {
        Direction left() {
            return WEST;
        }

        Direction right() {
            return EAST;
        }

        RVector2 unitVector() {
            return UNIT_VECTOR_NORTH;
        }
    },
    SOUTH {
        Direction left() {
            return EAST;
        }

        Direction right() {
            return WEST;
        }

        RVector2 unitVector() {
            return UNIT_VECTOR_SOUTH;
        }
    },
    EAST {
        Direction left() {
            return NORTH;
        }

        Direction right() {
            return SOUTH;
        }

        RVector2 unitVector() {
            return UNIT_VECTOR_EAST;
        }
    },
    WEST {
        Direction left() {
            return SOUTH;
        }

        Direction right() {
            return NORTH;
        }

        RVector2 unitVector() {
            return UNIT_VECTOR_WEST;
        }
    };

    RVector2 UNIT_VECTOR_NORTH = new RVector2(0, 1);
    RVector2 UNIT_VECTOR_WEST = new RVector2(UNIT_VECTOR_NORTH.getVector().rotate90(1));
    RVector2 UNIT_VECTOR_SOUTH = new RVector2(UNIT_VECTOR_WEST.getVector().rotate90(1));
    RVector2 UNIT_VECTOR_EAST = new RVector2(UNIT_VECTOR_SOUTH.getVector().rotate90(1));

    /**
     * @return the new direction after turning left from this one
     * example: NORTH.left() is WEST
     */
    abstract Direction left();

    /**
     * @return the new direction after turning right from this one
     * example: WEST.right() is NORTH
     */
    abstract Direction right();

    /**
     * @return a vector of magnitude 1 in the direction
     */
    abstract RVector2 unitVector();
}

