package inf112.skeleton.app;

import inf112.skeleton.app.cards.IProgramCard;

public class Player {

   IProgramCard[] cardHand = new IProgramCard[9];
   IProgramCard[] programmingSlots = new IProgramCard[5];



    public void placeCardFromHandToSlot(int handSlot, int programmingSlot) {
        programmingSlots[programmingSlot] = cardHand[handSlot];
        cardHand[handSlot] = null;
    }

    public void setProgrammingSlots (int slot, IProgramCard programCard) {
        programmingSlots[slot] = programCard;
    }

    public IProgramCard[] getProgrammingSlots() {
        return programmingSlots;
    }

    public void setCardinHand(int handSlot, IProgramCard programCard) {
        cardHand[handSlot] = programCard;
    }

    public IProgramCard[] getCardHand() {
        return cardHand;
    }
}
