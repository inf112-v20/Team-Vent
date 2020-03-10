package inf112.skeleton.app.model.board;

public enum Direction {
    NORTH {
        public Direction left() {
            return WEST;
        }

        public Direction right() {
            return EAST;
        }

        public RVector2 unitVector() {
            return UNIT_VECTOR_NORTH;
        }

        public Direction opposite() { return SOUTH;}

    },
    SOUTH {
        public Direction left() {
            return EAST;
        }

        public Direction right() {
            return WEST;
        }

        public RVector2 unitVector() {
            return UNIT_VECTOR_SOUTH;
        }

        public Direction opposite() { return NORTH;}

    },
    EAST {
        public Direction left() {
            return NORTH;
        }

        public Direction right() {
            return SOUTH;
        }

        public RVector2 unitVector() {
            return UNIT_VECTOR_EAST;
        }

        public Direction opposite() {return WEST;}
    },
    WEST {
        public Direction left() {
            return SOUTH;
        }

        public Direction right() {
            return NORTH;
        }

        public RVector2 unitVector() {
            return UNIT_VECTOR_WEST;
        }

        public Direction opposite() { return EAST;}
    };

    final RVector2 UNIT_VECTOR_NORTH = new RVector2(0, 1);
    final RVector2 UNIT_VECTOR_WEST = new RVector2(UNIT_VECTOR_NORTH.getVector().rotate90(1));
    final RVector2 UNIT_VECTOR_SOUTH = new RVector2(UNIT_VECTOR_WEST.getVector().rotate90(1));
    final RVector2 UNIT_VECTOR_EAST = new RVector2(UNIT_VECTOR_SOUTH.getVector().rotate90(1));

    /**
     * @return the new direction after turning left from this one
     * example: NORTH.left() is WEST
     */
    public abstract Direction left();

    /**
     * @return the new direction after turning right from this one
     * example: WEST.right() is NORTH
     */
    public abstract Direction right();

    //gives the opposite direction of a direction
    public abstract Direction opposite();
    /**
     * @return a vector of magnitude 1 in the direction
     */
    public abstract RVector2 unitVector();
}

