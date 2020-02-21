package inf112.skeleton.app.board;

public enum Direction {
    NORTH {
        Direction left() {
            return WEST;
        }

        Direction right() {
            return EAST;
        }

        RVector2 vector() {
            return new RVector2(0, 1);
        }
    },
    SOUTH {
        Direction left() {
            return EAST;
        }

        Direction right() {
            return WEST;
        }

        RVector2 vector() {
            return new RVector2(0, -1);
        }
    },
    EAST {
        Direction left() {
            return NORTH;
        }

        Direction right() {
            return SOUTH;
        }

        RVector2 vector() {
            return new RVector2(1, 0);
        }
    },
    WEST {
        Direction left() {
            return SOUTH;
        }

        Direction right() {
            return NORTH;
        }

        RVector2 vector() {
            return new RVector2(-1, 0);
        }
    };

    abstract Direction left();

    abstract Direction right();

    abstract RVector2 vector();

    public Direction turn(Side side) {
        return side == Side.LEFT ? this.left() : this.right();
    }
}

