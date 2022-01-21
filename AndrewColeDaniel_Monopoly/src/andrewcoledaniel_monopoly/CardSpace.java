/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a card space on the board
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Card.*;

/**
 *
 * @author anfeh1812
 */
public class CardSpace implements Space {

    private CardType cardType;
    private String name;

    public CardSpace(String name, CardType type) {
        this.name = name;
        cardType = type;
    }

    public String getName() {
        return name;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Card getCard(Card[] c, CardType t) {
        for (Card card : c) {
            if (card.getType() == t) {
                return card;
            }
        }
        return null;
    }

    public String performCardAction(Card c, Player p) {
        switch (c.getAction()) {
            // TODO: Implement goto and get out of jail cards
            // case ACTION_GOTO:
            case ACTION_GET_MONEY:
                p.addMoney(c.getValue());
                break;
            case ACTION_PAY_MONEY:
                p.removeMoney(c.getValue());
                break;
        }
        
        return c.getInfo();
    }

}
