/*
 * Andrew, Cole, Daniel
 * 2022-01-27
 * Class that represents a chance or community chest card
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class Card {
    private final CardType type;
    private final CardAction action;
    private final int value;
    private final String info;
    
    /**
     * Primary constructor, create Card with values
     * @param type type of card
     * @param action action for card to perform
     * @param value value that goes with action
     * @param info description of card action
     */
    public Card(CardType type, CardAction action, int value, String info) {
        this.type = type;
        this.action = action;
        this.value = value;
        this.info = info;
    }
    
    // Types of cards
    public enum CardType {
        CARD_CHANCE,
        CARD_COMMUNITY_CHEST
    }
    
    // Actions of cards
    public enum CardAction {
        ACTION_GOTO,
        ACTION_GOTO_RELATIVE,
        ACTION_GET_MONEY,
        ACTION_PAY_MONEY,
        ACTION_GET_MONEY_PLAYER,
        ACTION_PAY_MONEY_PLAYER,
        ACTION_GET_OUT_JAIL,
        ACTION_GOTO_JAIL,
        ACTION_GOTO_RAILROAD,
        ACTION_GOTO_UTILITY,
        ACTION_PAY_HOUSES
    }
    
    /**
     * Get card type
     * @return card type
     */
    public CardType getType() {
        return type;
    }
    
    /**
     * Get card action
     * @return card action
     */
    public CardAction getAction() {
        return action;
    }
    
    /**
     * Get card value
     * @return card value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Get card info
     * @return card info
     */
    public String getInfo() {
        return info;
    }
    
    /**
     * Return string representation of object
     * @return string representation of object
     */
    public String toString() {
        return "Card:\nType: " + type + "\nAction: " + action + "\nValue: " + value + "\nInfo: " + info + "\n";
    }   
}
