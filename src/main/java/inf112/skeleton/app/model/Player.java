package inf112.skeleton.app.model;


import inf112.skeleton.app.model.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Player {

    private final Card[] cardHand = new Card[9];
    private final Card[] programmingSlots = new Card[5];
    private final boolean[] programmingSlotsLocked = new boolean[5];
    private final HashMap<Integer, Card> cardH = new HashMap<>();
    private final Robot robot;

    public Player() {
        this(new Robot());
    }

    public Player(Robot robot) {
        this.robot = robot;
    }

    public void placeCardFromHandToSlot(int handSlot) {
        placeCardInFirstOpenSlot(handSlot, cardHand, programmingSlots);
    }

    public void undoProgrammingSlotPlacement(int programmingSlot) {
        if (!programmingSlotsLocked[programmingSlot]) {
            placeCardInFirstOpenSlot(programmingSlot, programmingSlots, cardHand);
        }
    }

    private void placeCardInFirstOpenSlot(int cardSlotInOriginArray, Card[] originArray, Card[] destinationArray) {
        if (originArray[cardSlotInOriginArray] != null) {
            for (int i = 0; i < destinationArray.length; i++) {
                if (destinationArray[i] == null) {
                    destinationArray[i] = originArray[cardSlotInOriginArray];
                    originArray[cardSlotInOriginArray] = null;
                    return;
                }
            }
        }
    }

    public void clearProgrammingSlots() {
        Arrays.fill(programmingSlotsLocked, false);

        int hp = robot.getState().getHp();
        for (int i = 5; i >= hp && i >= 1; i--) {
            programmingSlotsLocked[i-1] = true;
        }
        for (int i = 0; i < programmingSlots.length; i++) {
            if (!programmingSlotsLocked[i]) {
                programmingSlots[i] = null;
            }
        }
    }

    public void setCardinProgrammingSlot(int programmingSlot, Card programCard) {
        programmingSlots[programmingSlot] = programCard;
    }

    public Card getCardInProgrammingSlot(int slot) {
        return programmingSlots[slot];
    }

    public void setCardinHand(int handSlot, Card programCard) {
        cardHand[handSlot] = programCard;
    }

    public Card getCardinHand(int handSlot) {
        return cardHand[handSlot];
    }

    public Card[] getProgrammingSlots() {
        return programmingSlots;
    }

    //Methods below are for testing purposes atm, should be removed/moved to other classes later.

    //Cards are stored in a hashmap, where key is the priority which is distinct and random
    public void generateCardHand() {
        Arrays.fill(cardHand, null);
        int LIMIT = 120;
        ArrayList<Integer> numb = new ArrayList<>();
        for (int i = 0; i < LIMIT; i++) {
            numb.add(i);
        }
        Collections.shuffle(numb);
        for (int i = 0; i < LIMIT; i++) {
            if (i < 20) {
                cardH.put(numb.get(i), Card.MOVE_ONE);
            } else if (i < 40) {
                cardH.put(numb.get(i), Card.MOVE_TWO);
            } else if (i < 60) {
                cardH.put(numb.get(i), Card.MOVE_THREE);
            } else if (i < 80) {
                cardH.put(numb.get(i), Card.BACK_UP);
            } else if (i < 100) {
                cardH.put(numb.get(i), Card.ROTATE_LEFT);
            } else {
                cardH.put(numb.get(i), Card.ROTATE_RIGHT);
            }
        }
        int hp = robot.getState().getHp();
        for (int i = 0; i < hp-1; i++) {
            int k = (int) (Math.random() * LIMIT);
            cardHand[i] = cardH.get(k);
        }
    }

    public Robot getRobot() {
        return robot;
    }

    public Card[] getHand() {
        return cardHand;
    }
}
