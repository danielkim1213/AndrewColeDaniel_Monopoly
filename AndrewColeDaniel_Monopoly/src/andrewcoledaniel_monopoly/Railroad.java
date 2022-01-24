/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a railroad space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class Railroad extends Property {
    
    private SpaceType type;

    public Railroad(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
        type = SpaceType.SPACE_RAILROAD;
    }
    
    public void updateRent() {
        ;
    }
    
    @Override
    public String toString() {
        return "";
    }
    
    public SpaceType getType() {
        return type;
    }
}
