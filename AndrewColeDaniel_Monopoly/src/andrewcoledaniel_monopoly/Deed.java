/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a deed space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class Deed extends Property {

    private int houses;
    private int houseCost;
    private boolean hotel;

    public Deed(String name, int price, int mortgageValue, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
    }

    public int getHouses() {
        return houses;
    }

    public boolean getHotel() {
        return hotel;
    }

    public int getHouseCost() {
        return houseCost;
    }

    public boolean buyHouse() {
        if (houses == 4) {
            return false;
        }
        houses++;
        return true;
    }

    public boolean buyHotel() {
        if (houses < 4) {
            return false;
        }
        hotel = true;
        return true;
    }

    public boolean sellHouse() {
        if (houses == 0) {
            return false;
        }
        if (hotel) {
            hotel = false;
        } else {
            houses--;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
}
