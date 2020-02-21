package inf112.skeleton.app.board;

import java.util.Objects;

public class Location {
    private final RVector2 position;
    private final Direction direction;

    public Location(RVector2 position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Location forward() {
        return new Location(this.position.add(this.direction.vector()), this.direction) ;
    }

    public Location rotateLeft() {
        return new Location(position, this.direction.turn(Side.LEFT));
    }

    public Location rotateRight() {
        return new Location(position, this.direction.turn(Side.RIGHT));
    }

    public Location copy() {
        return new Location(this.position.copy(), this.direction);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public RVector2 getPosition() {
        return this.position.copy();
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
    public int hashCode() {
        return Objects.hash(getPosition(), getDirection());
    }
}