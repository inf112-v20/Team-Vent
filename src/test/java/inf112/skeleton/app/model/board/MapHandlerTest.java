package inf112.skeleton.app.model.board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapHandlerTest {
    private MapHandler mapHandler;

    @Before
    public void setUp() {
        mapHandler = new MapHandler(10, 10);
    }

    @Test
    public void layersTest() {
        // There is a tile layer filled with base tiles
        for (int i = 0; i < mapHandler.getWidth(); i++) {
            for (int j = 0; j < mapHandler.getHeight(); j++) {
                assertNotNull(mapHandler.getTileLayer().getCell(i, j).getTile());
            }
        }
        // There are no objects in the robot layer
        assertEquals(0, mapHandler.getRobotMapObjects().getCount());
    }

    @Test
    public void dimensionsTest() {
        mapHandler = new MapHandler(10, 20);
        assertEquals(10, mapHandler.getWidth());
        assertEquals(20, mapHandler.getHeight());
    }

    @Test
    public void getMapTest() {
        assertNotNull(mapHandler.getMap());
    }
}