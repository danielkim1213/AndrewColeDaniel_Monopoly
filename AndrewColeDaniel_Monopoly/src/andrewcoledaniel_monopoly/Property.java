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

    public Property(String name, int price, int mortgageValue, int propertyNumber) {
        this.name = name;
        this.price = price;
        this.mortgageValue = mortgageValue;
        this.propertyNumber = propertyNumber;
        type = SpaceType.SPACE_PROPERTY;
        owner = null;
        isOwned = false;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public int getPropertyNumber() {
        return propertyNumber;
    }
    
    public boolean getOwned() {
        return isOwned;
    }
    
    public void setOwned(boolean b) {
        isOwned = b;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player p) {
        owner = p;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public boolean getMortgage() {
        return mortgage;
    }

    public void setMortgage(boolean m) {
        mortgage = m;
    }
    
    public SpaceType getPropType() {
        return propType;
    }

    public abstract void updateRent();
        
    public abstract String toString();
}
