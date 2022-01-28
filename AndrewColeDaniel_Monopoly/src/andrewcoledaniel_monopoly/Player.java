/*
Andrew Daniel Cole January 21 2022
Player class for the game of monopoly
This class creates a player object that holds the players money, properties, if they are bankrupt or not
and if they are in jail or not and if they are how long have they been in there for
 */
package andrewcoledaniel_monopoly;

import java.io.Serializable;
import java.util.ArrayList;
import andrewcoledaniel_monopoly.Space.SpaceType;

public class Player implements Serializable {

    int currentPosition;
    int playerNumber;
    int money;
    boolean inJail;
    ArrayList<Property> properties = new ArrayList();
    int turnsInJail;
    boolean bankrupt;
    int jailCards;

    /**
     * Default constructor
     * @param playerNumber asks for the number of player 
     */
    public Player(int playerNumber) {
        this.playerNumber = playerNumber; // sets the player number to input
        currentPosition = 0; // current positon is 0
        jailCards = 0; // jail cards is 0
        inJail = false; // in jail is false
        turnsInJail = 0; // 0 turns
        bankrupt = false; // not bankrupt
    }

    /**
     * Constructor for starting the game
     * @param playerNumber // number of the player
     * @param money // the money they start with
     */
    public Player(int playerNumber, int money) {
        this(playerNumber); // calls the previous constructor
        this.money = money; // sets the money
    }

    /**
     * Method for loading the game
     * @param playerNumber inputs from the same file
     * @param money
     * @param properties
     * @param inJail
     * @param turnsInJail
     * @param bankrupt 
     */
    public Player(int playerNumber, int money, ArrayList<Property> properties, boolean inJail, int turnsInJail, boolean bankrupt) {
        this(playerNumber);
        this.money = money;
        this.properties = properties; // sets all the variables to the inputs
        this.inJail = inJail;
        this.turnsInJail = turnsInJail;
        this.bankrupt = bankrupt;
    }

    /**
     * Accessor
     * @return player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Accessor
     * @return money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Mutator
     * @param money changes money 
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Behaviour
     * @param money adds money 
     */
    public void addMoney(int money) {
        this.money += money;
        if(this.money > 0){ // if money is positive
            this.bankrupt = false; // they are not bankrupt
        }
    }

    /**
     * Behaviour
     * @param money  removes money
     */
    public void removeMoney(int money) {
        this.money -= money;
        if(this.money < 0){ // if money is negative
            this.bankrupt = true; // they are bankrupt
        }
    }

    /**
     * Acessor
     * @return properties arraylist 
     */
    public ArrayList getProperties() {
        return properties;
    }
    
    /**
     * Accessor for the names of the owned proerties
     * @return property names
     */
    public String propertyNames(){
        String output = "";
        for(int i =0; i < properties.size(); i ++){
            if (properties.get(i).getPropType() != SpaceType.SPACE_DEED) { // checks to see if the proerty is a deed or not
                output += (properties.get(i).getName() + "\n"); // gets the name
            } else if (((Deed)properties.get(i)).getHotel()) { // if the property has a house
                output += (properties.get(i).getName() + " - 1 hotel\n"); // adds the name and hotel
            } else if (((Deed)properties.get(i)).getHouses() > 0) { // if the houses are greater than 0
                output += (properties.get(i).getName() + " - " + ((Deed)properties.get(i)).getHouses() + " houses\n"); // adds the houses to the output
            } else {
                output += (properties.get(i).getName() + "\n"); // else adds the property name itself
            }
        }
        return output; // returns it
    }

    /**
     * Mutator
     * @param properties input of properties
     */
    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    /**
     * Behaviour
     * @param property to buy
     */
    public void buyProperty(Property property) {
        this.removeMoney(property.getPrice()); // removes money equal to the price
        this.properties.add(property); // adds property
        property.setOwner(this); // sets the owner to this player
    }
    
    public void buyPropertyAuction(Property p, int price) {
        removeMoney(price);
        properties.add(p);
        p.setOwner(this);
    }

    /** Behaviour
     * Method to mortage a property
     * @param property to mortage 
     */
    public void mortgageProperty(Property property) {
        if(property.getMortgage() == false){ // if mortage is false
            this.addMoney(property.mortgageValue); // adds money
            property.setMortgage(true); // sets mortgage to true
        } else{ // if it is already mortagaged
            this.removeMoney(property.mortgageValue); //removes money 
            property.setMortgage(false); // sets it to false
        }
    }

    /**
     * Accessor
     * @return jail 
     */
    public boolean getJail() {
        return inJail;
    }

    /**
     * Mutator
     * @param jail if they are in jail or not 
     */
    public void setJail(boolean jail) {
        this.inJail = jail;
    }

    /**
     * Acessor
     * @return turns in jail 
     */
    public int getTurnsInJail() {
        return turnsInJail;
    }

    /**
     * Mutator
     * @param turnsInJail amount to set 
     */
    public void setTurnsInJail(int turnsInJail) {
        this.turnsInJail = turnsInJail;
    }

    /**
     * Acessor
     * @return position on the board 
     */
    public int getPosition() {
        return currentPosition;
    }

    /**
     * Mutator
     * @param pos position to set 
     */
    public void setPosition(int pos) {
        currentPosition = pos;
    }

    /**
     * Acessor
     * @return gets number of get ouf out jail cards 
     */
    public int getJailCards() {
        return jailCards;
    }

    /**
     * Mutator
     * @param n amount to set get out of jail cards to 
     */
    public void setJailCards(int n) {
        jailCards = n;
    }
    
    /**
     * Mutator removes all properties from this player
     */
    public void removeProperties(){
        properties.removeAll(properties);
    }
    
    /**
     * Acessor that gets the number of utilites
     * @return the amount of utilites this player owns
     */
    public int getUtilities() {
        int num = 0;
        for (int i = 0; i < properties.size(); i++) { // loops through the array
            if (properties.get(i).getType() == Space.SpaceType.SPACE_UTILITY) { // checks if the space is a utility
                num++; // if it is increases number
            }
        }
        return num;
    }
    
    /**
     * Accessor that gets the number of railroads this player owns
     * @return returns that number
     */
    public int getRailroads() {
        int num = 0;
        for (int i = 0; i < properties.size(); i++) { // loops through array
            if (properties.get(i).getType() == Space.SpaceType.SPACE_RAILROAD) { // checks if the space is a railroad
                num++; // increases number
            }
        }
        return num;
    }
    
    /**
     * Behaviour find a property
     * @param name name of the property
     * @return number in the array
     */
    public int findProperty(String name) {
        for (int i = 0; i < properties.size(); i++) { // loops through the array
            if (properties.get(i).getName().equalsIgnoreCase(name)) { // checks to see if there is a property with the same name
                return i; // returns the index number if there is
            }
        }
        return -1; // else returns -1
    }
    
    /**
     * Pay for houses and hotels on properties for specific card
     */
    public void payProperties() {
        int sum = 0;
        // For each property
        for (Property p : properties) {
            // If property is a deed
            if (p.getType() == SpaceType.SPACE_DEED) {
                // If has hotel, add $100 to sum
                if (((Deed)p).getHotel()) {
                    sum += 100;
                } else {
                    // Add $25 for each house
                    sum += ((Deed)p).getHouses() * 25;
                }
            }
        }
        removeMoney(sum);
    }

    /**
     * Method that will print all the information about the player into a string
     * @return that string
     */
    public String toString() {
        String output; // output variable
        output = ("Player " + playerNumber + ":\nMoney: " + money + "\nIn Jail: " + inJail + "\nBankrupt: " + bankrupt + "\nProperties: ");
        for (int i = 0; i < properties.size(); i++) { // adds everything to the string
            output += ("\n" + properties.get(i).getName());
        }
        return output; // returns that output
    }
}
