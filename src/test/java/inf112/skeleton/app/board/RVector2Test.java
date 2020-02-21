package inf112.skeleton.app.board;

import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

public class RVector2Test {
    private Vector2 v2;
    private RVector2 rv2;

    @Before
    public void setUp() {
        v2 = new Vector2(2, 1);
        rv2 = new RVector2(v2);
    }

    @Test
    public void addingMeansAddingInternalVectors() {
        assertEquals(rv2.add(rv2.copy()), new RVector2(v2.add(v2)));
    }

    @Test
    public void addReturnsSameObject() {
        assertSame(rv2, rv2.add(rv2));
    }

    @Test
    public void copiesAreEqualButNotTheSame() {
        assertEquals(rv2, rv2.copy());
        assertNotSame(rv2, rv2.copy());
    }
}