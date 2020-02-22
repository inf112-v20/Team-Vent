package inf112.skeleton.app;

import inf112.skeleton.app.cards.IProgramCard;
import inf112.skeleton.app.cards.MoveForwardCard;
import inf112.skeleton.app.cards.RotateLeftCard;
import inf112.skeleton.app.cards.RotateRightCard;

public class Player {

   IProgramCard[] cardHand = new IProgramCard[9];
   IProgramCard[] programmingSlots = new IProgramCard[5];
   Boolean FinishedPlacingCards = false;

    public boolean placeCardFromHandToSlot(int handSlot, int programmingSlot) {
        if (cardHand[handSlot] != null && programmingSlots[programmingSlot] == null) {
            programmingSlots[programmingSlot] = cardHand[handSlot];
            cardHand[handSlot] = null;
            return true;
        }
        return false;
    }

    public IProgramCard[] getProgrammingSlots() {
        return programmingSlots;
    }

    public void setCardinHand(int handSlot, IProgramCard programCard) {
        cardHand[handSlot] = programCard;
    }

    public IProgramCard[] getCardHand() { return cardHand; }

    //Methods below are for testing purposes atm, should be removed/ moved to other classes later.

    public void genereateCardHand() {
        for (int i = 0; i < 9; i++) {
            if (i < 3) {
                cardHand[i] = new MoveForwardCard();
            }
            else if (i < 6) {
                cardHand[i] = new RotateLeftCard();
            }
            else {
                cardHand[i] = new RotateRightCard();
            }
        }
    }
}
