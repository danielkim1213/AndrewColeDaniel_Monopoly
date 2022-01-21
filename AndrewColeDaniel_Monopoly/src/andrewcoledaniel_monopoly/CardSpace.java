/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a card space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class CardSpace implements Space {

    private int cardType;
    private String name;

    public CardSpace(String name, int type) {
        this.name = name;
        cardType = type;
    }

    public String getName() {
        return name;
    }

    public int getCardType() {
        return cardType;
    }
}
