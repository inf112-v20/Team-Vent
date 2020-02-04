package inf112.skeleton.app.board;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BoardTest {

    @Test
    public void nullWhenPositionIsInvalid() {
        int h = 10;
        int w = 5;
        Board b = new Board(h, w);
        assertNull(b.getTile(0, w));
        assertNull(b.getTile(h, 0));
    }

    @Test
    public void tileWhenPositionIsValid() {
        int h = 2;
        int w = 12;
        Board b = new Board(h, w);
        assertNotNull(b.getTile(0, w - 1));
        assertNotNull(b.getTile(h - 1, 0));
    }

    @Test
    public void noNullPositions() {
        int h = 6;
        int w = 12;
        Board b = new Board(h, w);
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                assertNotNull(b.getTile(x, y));
            }
        }
    }
}