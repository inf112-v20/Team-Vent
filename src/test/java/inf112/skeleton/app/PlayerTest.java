package inf112.skeleton.app;

import inf112.skeleton.app.model.Player;
import inf112.skeleton.app.model.cards.Card;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {

    private final Player testPlayer = new Player();

    @Test
    public void placingACardFromHandSlot0ToProgrammingSlot0() {
        testPlayer.setCardinHand(0, Card.MOVE__ONE);
        Card testCard = testPlayer.getCardinHand(0);
        testPlayer.placeCardFromHandToSlot(0);

        assertEquals(testPlayer.getCardInProgrammingSlot(0), testCard);
        assertNotNull(testPlayer.getCardInProgrammingSlot(0));
        assertNull(testPlayer.getCardinHand(0));
    }

    @Test
    public void placingACardFromProgrammingSlotToHandSlot() {
        testPlayer.setCardinHand(0, Card.MOVE__ONE);
        Card testCard = testPlayer.getCardinHand(0);
        testPlayer.placeCardFromHandToSlot(0);
        testPlayer.undoProgrammingSlotPlacement(0);

        assertEquals(testPlayer.getCardinHand(0), testCard);
        assertNull(testPlayer.getCardInProgrammingSlot(0));
        assertNotNull(testPlayer.getCardinHand(0));
    }


}