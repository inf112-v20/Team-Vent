package inf112.skeleton.app.board;

import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RVector2Test {
    Vector2 v2;
    RVector2 rv2;

    @Before
    public void setup() {
        v2 = new Vector2(2, 1);
        rv2 = new RVector2(v2);
    }

    @Test
    public void add() {
        new RVector2(v2.add(v2));
        assertEquals(rv2.add(rv2.copy()), new RVector2(v2.add(v2)));
        assertNotEquals(rv2, rv2.add(rv2));
        assertNotSame(rv2, rv2.add(rv2));
    }

    @Test
    public void copy() {
        assertEquals(rv2, rv2.copy());
        assertNotSame(rv2, rv2.copy());
    }
}