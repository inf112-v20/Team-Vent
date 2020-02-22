package inf112.skeleton.app;

import static org.junit.Assert.assertEquals;

import inf112.skeleton.app.cards.IProgramCard;
import inf112.skeleton.app.cards.MoveForwardCard;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void placingACardFromHandSlot0ToProgrammingSlot0Works() {
        Player testPlayer = new Player();
        testPlayer.setCardinHand(0, new MoveForwardCard());
        IProgramCard testCard = testPlayer.getCardHand()[0];
        testPlayer.placeCardFromHandToSlot(0,0);

        assertEquals(testPlayer.getProgrammingSlots()[0], testCard);
    }

}