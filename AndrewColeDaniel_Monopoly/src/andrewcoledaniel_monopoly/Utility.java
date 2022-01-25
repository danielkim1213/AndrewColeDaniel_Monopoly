/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a utility space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class Utility extends Property {
    
    private SpaceType type;

    public Utility(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
        type = SpaceType.SPACE_UTILITY;
    }

    public void updateRent() {
        if (this.getOwner().getUtilities() == 1) {
            //rent = diceRoll * 4;
        } else if (this.getOwner().getUtilities() == 2) {
            //rent = diceRoll * 10;
        }
    }

    @Override
    public String toString() {
        return "";
    }
    
    public SpaceType getType() {
        return type;
    }
}
