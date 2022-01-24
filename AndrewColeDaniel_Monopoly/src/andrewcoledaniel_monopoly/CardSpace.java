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

    public Card getCard(Card[] c, CardType t) {
        for (Card card : c) {
            if (card.getType() == t) {
                return card;
            }
        }
        return null;
    }

    public String performSpaceAction(Card c, Player p) {
        switch (c.getAction()) {
            case ACTION_GOTO:
                p.setPosition(c.getValue());
                break;
            case ACTION_GET_MONEY:
                p.addMoney(c.getValue());
                break;
            case ACTION_PAY_MONEY:
                p.removeMoney(c.getValue());
                break;
            case ACTION_GET_OUT_JAIL:
                p.setJailCards(p.getJailCards() + 1);
                break;
        }
        
        return c.getInfo();
    }
    
    public SpaceType getType() {
        return type;
    }

}
