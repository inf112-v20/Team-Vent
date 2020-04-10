package inf112.skeleton.app.model;


import inf112.skeleton.app.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Player {

    private final Card[] cardHand = new Card[9];
    private final Card[] programmingSlots = new Card[5];
    private final HashMap<Integer, Card> cardH = new HashMap<>();
    private final Robot robot;
    private int playerLife;
    private int playerHP;

    public Player() {
        this(new Robot());
    }

    public Player(Robot robot) {
        this.robot = robot;
        this.playerLife = 3;
        this.playerHP = 9;
    }

    // Boolean finishedPlacingCards = false;

    public void placeCardFromHandToSlot(int handSlot) {
        findOpenSlot(handSlot, cardHand, programmingSlots);
    }

    public void undoProgrammingSlotPlacement(int programmingSlot) {
        findOpenSlot(programmingSlot, programmingSlots, cardHand);
    }

    private boolean findOpenSlot(int cardSlotInOriginArray, Card[] originArray, Card[] destinationArray) {
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
        for (int i = 0; i < 9; i++) {
            int k = (int) (Math.random() * LIMIT);
            cardHand[i] = cardH.get(k);
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
        programmingSlotsAsString.append("\nE  Execute (order 66)");
        return programmingSlotsAsString.toString();
    }

    private void cardArrayToString(StringBuilder TargetString, Card[] cardArray) {
        for (int i = 0; i < cardArray.length; i++) {
            TargetString.append(i + 1);
            TargetString.append("  ");
            if (cardArray[i] != null) {
                TargetString.append(cardArray[i].toString());

            }
            TargetString.append("\n");
        }
    }
    //Methods below might be useful, but as of 31.03 we use robotsHP instead, delete if not useful
    public int getPlayerLife() {
        return playerLife;
    }

    public void setPlayerLife(int playerLife) {
        this.playerLife = playerLife;
    }

    public int getPlayerHP() {
        return playerHP;
    }

    public void setPlayerHP(int playerLife) {
        this.playerHP = playerLife;
    }

    public String playerLifeAsString(int lives) {
        String p = "playerLife: " + lives + "\n";
        return p;
    }

    public String playerHPAsString(int args) {
        String p = "playerHP: " + args + "\n";
        return p;
    }

    public Robot getRobot() {
        return robot;
    }
}
