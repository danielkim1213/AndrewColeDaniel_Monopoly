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

    /**
     * Default constructor
     * @param name of the deed
     * @param price of the deed
     * @param mortgageValue of the deed
     * @param houseCost how much a house costs
     * @param rent default rent
     * @param propertyNumber number of the property
     */
    public Deed(String name, int price, int mortgageValue, int houseCost, int rent, int propertyNumber) {
        super(name, price, mortgageValue, propertyNumber); // calls the super
        this.houseCost = houseCost; // sets house cost
        this.originalRent = rent; // sets rent
        propType = SpaceType.SPACE_DEED; // sets the space type to a deed
    }

    /**
     * Acessor
     * @return number of houses 
     */
    public int getHouses() {
        return houses;
    }

    /**
     * Acessor
     * @return if there is a hotel or not 
     */
    public boolean getHotel() {
        return hotel;
    }

    /**
     * Accessor
     * @return the house cost 
     */
    public int getHouseCost() {
        return houseCost;
    }

    /**
     * Behaviour to buy a house
     * @param h how many to buy
     * @return true or false
     */
    public boolean buyHouse(int h) {
        if (houses == 4) { // if you already have 4 houses
            return false;
        }
        houses += h; // increases house by the input
        owner.removeMoney(houses * houseCost); // remove money from the owner
        updateRent(); // uptdates rent
        return true; // returns true
    }

    /**
     * Behaviour buys a hoel
     * @return true or false
     */
    public boolean buyHotel() {
        if (houses < 4) { // if theres not four houses
            return false;
        }
        hotel = true; // sets hotle to true
        updateRent(); // uptdates the rent
        return true;
    }

    /**
     * Behaviour to sell a house
     * @return true or false
     */
    public boolean sellHouse() {
        if (houses == 0) { // if 0 houses
            return false; // returns
        }
        if (hotel) { // if theres a hotel
            hotel = false; // returns
        } else { // else decreases houses
            houses--;
        }
        updateRent(); // updates the rent
        return true;
    }
    
    /**
     * Mutator that sets the houses
     * @param h input
     */
    public void setHouses(int h){
        houses = h;
    }
    
    /**
     * Method to set the deed to owned or unowned
     * @param b boolean input
     */
    @Override
    public void setOwned(boolean b){
        if(b == false){ // if false
            houses = 0; // resets all the information
            hotel = false;
            isOwned = false;
            mortgage = false;
            updateRent();
        } else{
            isOwned = true; // else just sets it to owned
        }
    }
    
    /**
     * Behaviour uptdates rent
     */
    public void updateRent() {
        if (hotel == true) { // if theres a hotel
            rent = originalRent * houses * 50; // rent is original multiplied by houses multilipied by 50
        } else if(houses >= 1){ // if there is a house
            rent = originalRent * houses; // its rent multiplied by houses
        } else{ // else
            rent = originalRent; // its original rent
        }
    }
    
    /**
     * To String method
     * @return 
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Accessor
     * @return the space type
     */
    public SpaceType getType() {
        return type;
    }
}
