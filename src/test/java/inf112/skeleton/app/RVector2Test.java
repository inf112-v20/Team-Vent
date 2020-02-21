package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.RVector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RVector2Test {
    private RVector2 rVector2;
    private Vector2 vector2;

    @Before
    public void before() {
        vector2 = new Vector2(1, 2);
        rVector2 = new RVector2(vector2);
    }

    @Test
    public void vectorsAreEqualIfCoordinatesAndAngleAreTheSame() {
        assertEquals(rVector2, new RVector2(vector2.cpy()));
    }

    @Test
    public void vectorsAreNotEqualUnlessCoordinatesAndAnglesAre() {
        assertNotEquals(vector2, new RVector2(vector2.rotate90(1)));
        assertNotEquals(new RVector2(1, 2), new RVector2(2, 1));
    }
}