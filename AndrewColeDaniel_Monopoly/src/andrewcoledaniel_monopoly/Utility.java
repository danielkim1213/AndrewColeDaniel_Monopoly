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
    
    /**
     * Default Constructor
     * @param name of the property
     * @param price $$
     * @param mortgageValue amount you gain from mortaging it
     * @param propertyNumber the property number
     */
    public Utility(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber); // calls the super
        propType = SpaceType.SPACE_UTILITY; // sets this to a utility space
    }

    /**
     * method to update the rent
     */
    public void updateRent() {
        if (this.getOwner().getUtilities() == 1) { // checks how many utilits the owner owns
            rent = GameScreen.moves * 4; // if 1 the rent if your moves * 4
        } else if (this.getOwner().getUtilities() == 2) {
            rent = GameScreen.moves * 10; // if 2 the rent if your moves * 10
        }
    }

    /**
     * To String Method
     * @return nothing
     */
    @Override
    public String toString() {
        return "";
    }
    
    /**
     * Accessor
     * @return space type 
     */
    public SpaceType getType() {
        return type;
    }
}
