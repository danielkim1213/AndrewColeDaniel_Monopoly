/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a tax space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class TaxSpace implements Space {

    private SpaceType type;
    private int taxAmount;
    private String name;

    /**
     * Primary constructor, create TaxSpace with specified values
     * @param name name of space
     * @param amount amount to tax
     */
    public TaxSpace(String name, int amount) {
        this.name = name;
        taxAmount = amount;
        type = SpaceType.SPACE_TAX;
    }

    /**
     * Get name of space
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get amount to tax
     * @return tax amount
     */
    public int getTaxAmount() {
        return taxAmount;
    }
    
    /**
     * Get type of space
     * @return type of space
     */
    public SpaceType getType() {
        return type;
    }
    
    /**
     * Return string representation of object
     * @return string representation of object
     */
    public String toString() {
        return "TaxSpace:\nName: " + name + "\nTax amount: " + taxAmount + "\n";
    }
}
