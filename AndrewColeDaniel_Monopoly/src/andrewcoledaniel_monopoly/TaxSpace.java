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

    private int taxAmount;
    private String name;

    public TaxSpace(String name, int amount) {
        this.name = name;
        taxAmount = amount;
    }

    public String getName() {
        return name;
    }

    public int getTaxAmount() {
        return taxAmount;
    }
}
