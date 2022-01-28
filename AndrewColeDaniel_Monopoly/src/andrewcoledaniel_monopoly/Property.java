/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a property space on the board
 */
package andrewcoledaniel_monopoly;
import andrewcoledaniel_monopoly.Space.SpaceType;

/**
 *
 * @author anfeh1812
 */
abstract public class Property implements Space {

    protected String name;
    protected int price;
    protected int rent;
    protected int propertyNumber;
    protected Player owner;
    protected int mortgageValue;
    protected boolean mortgage;
    protected boolean isOwned;
    protected SpaceType type;
    protected SpaceType propType;

    /**
     * Basic constructor
     * @param name of the property
     * @param price of the property
     * @param mortgageValue of the property
     * @param propertyNumber 
     */
    public Property(String name, int price, int mortgageValue, int propertyNumber) {
        this.name = name;
        this.price = price; // sets all the inputs to variables
        this.mortgageValue = mortgageValue;
        this.propertyNumber = propertyNumber;
        type = SpaceType.SPACE_PROPERTY;
        owner = null;
        isOwned = false;
    }

    /**
     * Acessor
     * @return name 
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Accessor
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Accessor
     * @return rent
     */
    public int getRent() {
        return rent;
    }

    /**
     * Accessor
     * @return property number
     */
    public int getPropertyNumber() {
        return propertyNumber;
    }
    
    /**
     * Accessor
     * @return if owned or not
     */
    public boolean getOwned() {
        return isOwned;
    }
    
    /**
     * Mutator
     * @param b sets owned
     */
    public void setOwned(boolean b) {
        isOwned = b;
        updateRent();
    }

    /**
     * Acessor
     * @return owner 
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Mutator
     * @param p sets owner
     */
    public void setOwner(Player p) {
        owner = p;
    }

    /**
     * Acessor
     * @return mortage value 
     */
    public int getMortgageValue() {
        return mortgageValue;
    }

    /**
     * Acessor
     * @return gets mortgage 
     */
    public boolean getMortgage() {
        return mortgage;
    }

    /**
     * Mutator
     * @param m sets the mortage 
     */
    public void setMortgage(boolean m) {
        mortgage = m;
    }
    
    /**
     * Acessor
     * @return space type 
     */
    public SpaceType getPropType() {
        return propType;
    }

    
     // Abstrat methods
    public abstract void updateRent();
        
    public abstract String toString();
}
