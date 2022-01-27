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
    private int originalRent;

    public Deed(String name, int price, int mortgageValue, int houseCost, int rent, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber);
        this.houseCost = houseCost;
        this.originalRent = rent;
        propType = SpaceType.SPACE_DEED;
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

    public boolean buyHouse(int h) {
        if (houses == 4) {
            return false;
        }
        houses += h;
        owner.removeMoney(houses * houseCost);
        updateRent();
        return true;
    }

    public boolean buyHotel() {
        if (houses < 4) {
            return false;
        }
        hotel = true;
        owner.removeMoney(houseCost);
        updateRent();
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
        updateRent();
        return true;
    }
    
    public void setHouses(int h){
        houses = h;
    }
    
    @Override
    public void setOwned(boolean b){
        if(b == false){
            houses = 0;
            hotel = false;
            isOwned = false;
            mortgage = false;
            updateRent();
        } else{
            isOwned = true;
        }
    }
    
    public void updateRent() {
        if (hotel == true) {
            rent = originalRent * houses * 50;
        } else {
            rent = originalRent * houses;
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
