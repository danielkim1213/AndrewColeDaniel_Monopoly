/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public Card(CardType type, CardAction action, int value, String info) {
        this.type = type;
        this.action = action;
        this.value = value;
        this.info = info;
    }
    public enum CardType {
        CARD_CHANCE,
        CARD_COMMUNITY_CHEST
    }
    
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
    
    public CardType getType() {
        return type;
    }
    
    public CardAction getAction() {
        return action;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getInfo() {
        return info;
    }
    
    public String toString() {
        return "Card:\nType: " + type + "\nAction: " + action + "\nValue: " + value + "\nInfo: " + info + "\n";
    }   
}
