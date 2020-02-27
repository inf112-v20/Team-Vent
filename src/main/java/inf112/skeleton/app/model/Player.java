package inf112.skeleton.app.model;

import inf112.skeleton.app.model.cards.IProgramCard;
import inf112.skeleton.app.model.cards.MoveForwardCard;
import inf112.skeleton.app.model.cards.RotateLeftCard;
import inf112.skeleton.app.model.cards.RotateRightCard;

import java.util.Arrays;

public class Player {

    private IProgramCard[] cardHand = new IProgramCard[9];
    private IProgramCard[] programmingSlots = new IProgramCard[5];
    // Boolean finishedPlacingCards = false;

    public boolean placeCardFromHandToSlot(int handSlot) {
        return findOpenSlot(handSlot, cardHand, programmingSlots);
    }

    public boolean undoProgrammingSlotPlacement(int programmingSlot) {
        return findOpenSlot(programmingSlot, programmingSlots, cardHand);
    }

    private boolean findOpenSlot(int cardSlotInOriginArray, IProgramCard[] originArray, IProgramCard[] destinationArray) {
        if (originArray[cardSlotInOriginArray] != null) {
            for (int i = 0; i < destinationArray.length; i++) {
                if (destinationArray[i] == null) {
                    destinationArray[i] = originArray[cardSlotInOriginArray];
                    originArray[cardSlotInOriginArray] = null;
                    return true;
                }
            }
        }
        return false;
    }

    public void clearProgrammingSlots() {
        Arrays.fill(programmingSlots, null);
    }

    public void setCardinProgrammingSlot(int programmingSlot, IProgramCard programCard) {
        programmingSlots[programmingSlot] = programCard;
    }

    public IProgramCard getCardInProgrammingSlot(int slot) {
        return programmingSlots[slot];
    }

    public void setCardinHand(int handSlot, IProgramCard programCard) {
        cardHand[handSlot] = programCard;
    }

    public IProgramCard getCardinHand(int handSlot) {
        return cardHand[handSlot];
    }

    //Methods below are for testing purposes atm, should be removed/moved to other classes later.

    public void generateCardHand() {
        for (int i = 0; i < 9; i++) {
            if (i < 3) {
                cardHand[i] = new MoveForwardCard();
            } else if (i < 6) {
                cardHand[i] = new RotateLeftCard();
            } else {
                cardHand[i] = new RotateRightCard();
            }
        }
    }

    public String handAsString() {
        StringBuilder handAsString = new StringBuilder();
        cardArrayToString(handAsString, cardHand);
        handAsString.append("\nG  Generate New Hand");
        return handAsString.toString();
    }

    public String programmingSlotsAsString() {
        StringBuilder programmingSlotsAsString = new StringBuilder();
        programmingSlotsAsString.append("PROGRAMMING SLOTS: \n");
        cardArrayToString(programmingSlotsAsString, programmingSlots);
        programmingSlotsAsString.append("\nE  End Turn (Execute order 66)");
        return programmingSlotsAsString.toString();
    }

    private void cardArrayToString(StringBuilder TargetString, IProgramCard[] cardArray) {
        for (int i = 0; i < cardArray.length; i++) {
            TargetString.append(i + 1);
            TargetString.append("  ");
            if (cardArray[i] != null) {
                TargetString.append(cardArray[i].toString());
            }
            TargetString.append("\n");
        }
    }

    public IProgramCard playCardFromHand(int handSlot) {
        if (cardHand[handSlot] != null) {
            IProgramCard playCard = cardHand[handSlot];
            cardHand[handSlot] = null;
            return playCard;
        }
        return null;
    }
}
