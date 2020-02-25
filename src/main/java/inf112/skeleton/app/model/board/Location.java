package inf112.skeleton.app.model.board;

public class Location {
    // Location objects are immutable
    private final RVector2 position;
    private final Direction direction;

    public Location(RVector2 position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Location() {
        this(new RVector2(0, 0), Direction.NORTH);
    }

    public Location forward() {
        return new Location(this.position.add(this.direction.unitVector()), this.direction);
    }

    public Location moveDirection(Direction moveDirection){
        return new Location(this.position.add(moveDirection.unitVector()), this.direction);
    }

    /**
     * @return return a new Location that is rotated to the left (no change in position)
     */
    public Location rotateLeft() {
        return new Location(position, this.direction.left());
    }

    /**
     * @return return a new Location that is rotated to the right (no change in position)
     */
    public Location rotateRight() {
        return new Location(position, this.direction.right());
    }

    public Location copy() {
        return new Location(this.position.cpy(), this.direction);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public RVector2 getPosition() {
        return this.position.cpy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return getPosition().equals(location.getPosition()) &&
                getDirection() == location.getDirection();
    }

    @Override
    public String toString() {
        return "Location{" +
                "position=" + position +
                ", direction=" + direction +
                '}';
    }
}