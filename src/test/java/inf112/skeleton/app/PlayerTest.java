package inf112.skeleton.app;

import inf112.skeleton.app.model.Player;
import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {

    @Test
    public void placingACardFromHandSlot0ToProgrammingSlot0() {
        Player testPlayer = new Player();
        testPlayer.setCardinHand(0, new MoveForwardCard());
        IProgramCard testCard = testPlayer.getCardinHand(0);
        testPlayer.placeCardFromHandToSlot(0);

        assertEquals(testPlayer.getCardInProgrammingSlot(0), testCard);
        assertNotNull(testPlayer.getCardInProgrammingSlot(0));
        assertNull(testPlayer.getCardinHand(0));
    }


}