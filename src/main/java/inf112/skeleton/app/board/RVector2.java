package inf112.skeleton.app.board;

import com.badlogic.gdx.math.Vector2;

// RVector2 encapsulates Vector2 and ensures the coordinates are integers.
public class RVector2 {
    private Vector2 vector;

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

    /**
     * Adds the given vector to this vector
     *
     * @param v the vector to add
     * @return this vector for chaining
     */
    public RVector2 add(RVector2 v) {
        this.vector = this.vector.add(v.vector);
        return this;
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
        return String.format("(%.0f, %.0f)", vector.x, vector.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RVector2 that = (RVector2) o;
        return vector.epsilonEquals(that.vector);
    }

    public RVector2 cpy() {
        return new RVector2(this.vector.cpy());
    }
}
