/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a card space on the board
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Card.CardAction;
import andrewcoledaniel_monopoly.Card.CardType;

/**
 *
 * @author anfeh1812
 */
public class CardSpace implements Space {

    private SpaceType type;
    private CardType cardType;
    private String name;

    /**
     * Primary constructor, create CardSpace with name and type
     *
     * @param name name of space
     * @param type type of card space
     */
    public CardSpace(String name, CardType type) {
        this.name = name;
        cardType = type;
        this.type = SpaceType.SPACE_CARD;
    }

    /**
     * Get name of space
     *
     * @return name of space
     */
    public String getName() {
        return name;
    }

    /**
     * Get type of Card
     *
     * @return card type
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Search for specific card type in card array and return it
     *
     * @param c Card array
     * @return Card with specific type
     */
    public Card getCard(Card[] c) {
        for (Card card : c) {
            // If card is type of space
            if (card.getType() == cardType) {
                return card;
            }
        }
        // Couldn't find card
        return new Card(CardType.CARD_CHANCE, CardAction.ACTION_GOTO, 0, "Something messed up");
    }

    /**
     * Perform action of card
     *
     * @param c Card
     * @param p Player object
     * @param b Board Object
     * @param players Player array
     * @return Card description
     */
    public String performSpaceAction(Card c, Player p, Board b, Player[] players) {
        switch (c.getAction()) {
            // Go to specific space
            case ACTION_GOTO:
                p.setPosition(c.getValue());
                break;
            // Move relative to player's position
            case ACTION_GOTO_RELATIVE:
                p.setPosition(p.getPosition() + c.getValue());
            // Get money
            case ACTION_GET_MONEY:
                p.addMoney(c.getValue());
                break;
            // Pay money
            case ACTION_PAY_MONEY:
                p.removeMoney(c.getValue());
                break;
            // Get money from each player
            case ACTION_GET_MONEY_PLAYER:
                for (Player player : players) {
                    // If not current player, remove money
                    if (!player.equals(p)) {
                        player.removeMoney(c.getValue());
                    }
                }
                // Get money from each player
                p.addMoney(c.getValue() * players.length);
                break;
            // Pay money to each player
            case ACTION_PAY_MONEY_PLAYER:
                // Remove money from player
                p.removeMoney(c.getValue() * players.length);
                // Add money to each other player
                for (Player player : players) {
                    if (!player.equals(p)) {
                        player.addMoney(c.getValue());
                    }
                }
                break;
            // Get out of jail free
            case ACTION_GET_OUT_JAIL:
                p.setJailCards(p.getJailCards() + 1);
                break;
            // Go to jail
            case ACTION_GOTO_JAIL:
                p.setPosition(10);
                p.setJail(true);
                break;
            // Go to railroad
            case ACTION_GOTO_RAILROAD:
                p.setPosition(b.findNextProperty(p.getPosition(), SpaceType.SPACE_RAILROAD));
                break;
            // Go to utility
            case ACTION_GOTO_UTILITY:
                p.setPosition(b.findNextProperty(p.getPosition(), SpaceType.SPACE_UTILITY));
                break;
            // Pay for each house and hotel
            case ACTION_PAY_HOUSES:
                p.payProperties();
                break;
        }
        // Get card description
        return c.getInfo();
    }

    /**
     * Get type of space
     *
     * @return type of space
     */
    public SpaceType getType() {
        return type;
    }

    /**
     * Return string representation of object
     *
     * @return string representation of object
     */
    public String toString() {
        return "Card Space:\nType: " + name + "\n";
    }

}
