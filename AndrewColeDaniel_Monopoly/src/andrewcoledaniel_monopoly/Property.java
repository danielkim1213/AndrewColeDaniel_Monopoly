/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a property space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
abstract public class Property implements Space {

    protected String name;
    protected int price;
    protected int rent;
    protected int propertyNumber;
    protected int ownerID;
    protected int mortgageValue;
    protected boolean mortgage;

    public Property(String name, int price, int mortgageValue, int propertyNumber) {
        this.name = name;
        this.price = price;
        this.mortgageValue = mortgageValue;
        this.propertyNumber = propertyNumber;
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

    public int getOwner() {
        return ownerID;
    }

    public void setOwner(int id) {
        ownerID = id;
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

    public abstract String toString();
}
