/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a card space on the board
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Card.*;
import java.util.Random;

/**
 *
 * @author anfeh1812
 */
public class CardSpace implements Space {

    private SpaceType type;
    private CardType cardType;
    private String name;

    public CardSpace(String name, CardType type) {
        this.name = name;
        cardType = type;
        this.type = SpaceType.SPACE_CARD;
    }

    public String getName() {
        return name;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Card getCard(Card[] c) {
        for (Card card : c) {
            if (card.getType() == cardType) {
                return card;
            }
        }
        return new Card(CardType.CARD_CHANCE, CardAction.ACTION_GOTO, 0, "Something messed up");
    }
    
    public void shuffleCards(Card[] c) {
        Random rd = new Random();
        Card temp;
        int rNum;
        for (int i = 0; i < c.length; i++) {
            rNum = rd.nextInt(c.length);
            temp = c[i];
            c[i] = c[rNum];
            c[rNum] = temp;
        }
    }

    public String performSpaceAction(Card c, Player p) {
        switch (c.getAction()) {
            case ACTION_GOTO:
                p.setPosition(c.getValue());
                break;
            case ACTION_GOTO_RELATIVE:
                p.setPosition(p.getPosition() + c.getValue());
            case ACTION_GET_MONEY:
                p.addMoney(c.getValue());
                break;
            case ACTION_PAY_MONEY:
                p.removeMoney(c.getValue());
                break;
            case ACTION_GET_MONEY_PLAYER:
                //p.addMoney(c.getValue() * playerNums);
                // for each player, removeMoney c.getValue()
                break;
            case ACTION_PAY_MONEY_PLAYER:
                //p.removeMoney(c.getValue() * playerNums);
                // for each player, addMoney c.getValue()
                break;
            case ACTION_GET_OUT_JAIL:
                p.setJailCards(p.getJailCards() + 1);
                break;
            case ACTION_GOTO_JAIL:
                p.setPosition(10);
                p.setJail(true);
                break;
            case ACTION_GOTO_RAILROAD:
                // p.setPosition(nextRailroad())
                break;
            case ACTION_GOTO_UTILITY:
                // p.setPosition(nextUtility())
                break;
            case ACTION_PAY_HOUSES:
                // p.payHouses()
                break;
        }
        
        return c.getInfo();
    }
    
    public SpaceType getType() {
        return type;
    }

}
