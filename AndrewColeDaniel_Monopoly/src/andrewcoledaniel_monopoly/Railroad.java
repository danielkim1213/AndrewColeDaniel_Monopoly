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
    
    /**
     * Default Constructor
     * @param name of the railroad
     * @param price of the railroad
     * @param mortgageValue how much you get from mortaging
     * @param propertyNumber number of the proeprty
     */
    public Railroad(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
        propType = SpaceType.SPACE_RAILROAD;
    }
    
    /**
     * Behaviour that updates the rent
     */
    public void updateRent() {
        if (getOwner() == null) { // if no owner 
            return; // does nothing
        }
        switch (this.getOwner().getRailroads()) { // checks to see how many railroads the owner owns
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
                this.rent = 200; // doubles with each railroad
                break;
        }
    }
    /**
     * To string method
     * @return string representation of object
     */
    @Override
    public String toString() {
        return super.toString() + "Name: " + name + "\n";
    }
    
    /**
     * Accessor
     * @return space type 
     */
    public SpaceType getType() {
        return type;
    }
}
