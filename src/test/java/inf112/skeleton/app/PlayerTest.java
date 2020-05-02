package inf112.skeleton.app;

import inf112.skeleton.app.model.Player;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.cards.Card;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class PlayerTest {

    private final Player testPlayer = new Player(new Robot());

    @Test
    public void placingACardFromHandSlot0ToProgrammingSlot0() {
        testPlayer.setCardinHand(0, Card.MOVE_ONE);
        Card testCard = testPlayer.getCardinHand(0);
        testPlayer.placeCardFromHandToSlot(0);

        assertEquals(testPlayer.getCardInProgrammingSlot(0), testCard);
        assertNotNull(testPlayer.getCardInProgrammingSlot(0));
        assertNull(testPlayer.getCardinHand(0));
    }

    @Test
    public void placingACardFromProgrammingSlotToHandSlot() {
        testPlayer.setCardinHand(0, Card.MOVE_ONE);
        Card testCard = testPlayer.getCardinHand(0);
        testPlayer.placeCardFromHandToSlot(0);
        testPlayer.undoProgrammingSlotPlacement(0);

        assertEquals(testPlayer.getCardinHand(0), testCard);
        assertNull(testPlayer.getCardInProgrammingSlot(0));
        assertNotNull(testPlayer.getCardinHand(0));
    }

    @Test
    public void testDeckOfCards() {
        List<Card> deck = testPlayer.deckOfCards();
        assertEquals(18 * 3 + 12 + 6 * 3, deck.size());
        assertEquals(18, deck.stream().filter(x -> x == Card.MOVE_ONE).count());
        assertEquals(12, deck.stream().filter(x -> x == Card.MOVE_TWO).count());
        assertEquals(6, deck.stream().filter(x -> x == Card.MOVE_THREE).count());
        assertEquals(6, deck.stream().filter(x -> x == Card.BACK_UP).count());
        assertEquals(18, deck.stream().filter(x -> x == Card.ROTATE_LEFT).count());
        assertEquals(18, deck.stream().filter(x -> x == Card.ROTATE_RIGHT).count());
        assertEquals(6, deck.stream().filter(x -> x == Card.U_TURN).count());
    }
}