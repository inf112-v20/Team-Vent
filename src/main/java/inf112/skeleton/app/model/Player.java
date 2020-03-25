package inf112.skeleton.app.model;


import inf112.skeleton.app.model.cards.*;

import java.util.*;

public class Player {

    private final IProgramCard[] cardHand = new IProgramCard[9];
    private final IProgramCard[] programmingSlots = new IProgramCard[5];
    private final HashMap<Integer, IProgramCard> cardH = new HashMap<Integer, IProgramCard>();
    private int playerLife;
    private int playerHP;

    public Player() {
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

    //Cards are stored in a hashmap, where key is the priority which is distinct and random
    public void generateCardHand() {

       ArrayList<Integer> numb = new ArrayList<Integer>();
       for (int i =0; i < 80; i++){
           numb.add(i);
       }
        Collections.shuffle(numb);
        for (int i =0; i <80; i++){
            if (i <= 25) {
                cardH.put(numb.get(i), new MoveForwardCard());
            }

            if (i > 25 && i <= 45){
                cardH.put(numb.get(i), new MoveBackwardCard());
            }
            if(i > 45 && i <= 65){
                cardH.put(numb.get(i), new RotateRightCard());
            }
            if (i >= 65){
                cardH.put(numb.get(i), new RotateLeftCard());
            }
        }
        for (int i=0;i<9;i++){
            int k = (int) (Math.random() * 80 );
            cardHand[i]=cardH.get(k);
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

    public int getPlayerLife() {
        return playerLife;
    }
    public int getPlayerHP() {
        return playerHP;
    }
    public void setPlayerLife(int playerLife) {
        this.playerLife = playerLife;
    }
    public void setPlayerHP(int playerLife) {
        this.playerHP = playerHP;
    }

    public String playerLifeAsString(int args) {
        String p = "playerLife: " + Integer.toString(args) + "\n";
        return p;
    }

    public String playerHPAsString(int args) {
        String p = "playerHP: " + Integer.toString(args) + "\n";
        return p;
    }
}
