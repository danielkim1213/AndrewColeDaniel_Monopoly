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
    
    public Railroad(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
        propType = SpaceType.SPACE_RAILROAD;
    }
    
    public void updateRent() {
        if (getOwner() == null) {
            return;
        }
        switch (this.getOwner().getRailroads()) {
            case 1:
                this.rent = 25;
                break;
            case 2:
                this.rent = 50;
                break;
            case 3:
                this.rent = 100;
                break;
            case 4:
                this.rent = 200;
                break;
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
