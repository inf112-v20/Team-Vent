package inf112.skeleton.app.board;

import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

// RVector2 encapsulates Vector2 and ensures the coordinates are integers.
public class RVector2 {
    private final Vector2 vector;

    public RVector2(int x, int y) {
        this(new Vector2(x, y));
    }

    public RVector2(Vector2 vector) {
        this.vector = vector;
        this.validate();
    }

    public Vector2 getVector() {
        return this.vector.cpy();
    }

    public RVector2 add(RVector2 other) {
        return new RVector2(this.vector.cpy().add(other.vector));
    }

    /**
     * @throws IllegalStateException if x, y are not integers
     */
    private void validate() throws IllegalStateException {
        if (this.vector.x % 1 != 0 || this.vector.y % 1 != 0) {
            throw new IllegalStateException(String.format("RVector %s is invalid.", this.toString()));
        }
    }

    @Override
    public String toString() {
        return vector.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RVector2 that = (RVector2) o;
        return Objects.equals(vector, that.vector);
    }

    public RVector2 copy() {
        return new RVector2(this.vector.cpy());
    }
}
