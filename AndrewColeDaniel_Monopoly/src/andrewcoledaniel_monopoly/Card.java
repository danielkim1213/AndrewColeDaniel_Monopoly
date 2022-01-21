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
    private CardType type;
    private CardAction action;
    private int value;
    private String info;
    
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
        ACTION_GET_MONEY,
        ACTION_PAY_MONEY,
        ACTION_GET_OUT_JAIL
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
    
}
