package inf112.skeleton.app.model.board;

import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RVector2Test {
    private Vector2 v2;
    private RVector2 rv2;

    @Before
    public void setUp() {
        v2 = new Vector2(2, 1);
        rv2 = new RVector2(v2);
    }

    @Test
    public void checkInvariant() {
        try {
            new RVector2(new Vector2(0.3f, 0.5f));
            fail();
        } catch (IllegalStateException e) {
            // this should happen
        }
    }

    @Test
    public void addingMeansAddingInternalVectors() {
        assertEquals(new RVector2(v2.cpy().add(v2)), rv2.add(rv2));
    }

    @Test
    public void addReturnsSameObject() {
        assertSame(rv2, rv2.add(rv2));
    }

    @Test
    public void copiesAreEqualButNotTheSame() {
        assertEquals(rv2, rv2.cpy());
        assertNotSame(rv2, rv2.cpy());
    }
}