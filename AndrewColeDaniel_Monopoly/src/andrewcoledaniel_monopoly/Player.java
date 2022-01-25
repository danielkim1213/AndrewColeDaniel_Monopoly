/*
Andrew Daniel Cole January 21 2022
Player class for the game of monopoly
This class creates a player object that holds the players money, properties, if they are bankrupt or not
and if they are in jail or not and if they are how long have they been in there for
 */
package andrewcoledaniel_monopoly;

import java.util.ArrayList;

public class Player {

    int currentPosition;
    int playerNumber;
    int money;
    boolean inJail;
    ArrayList<Property> properties = new ArrayList();
    int turnsInJail;
    boolean bankrupt;
    int jailCards;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        currentPosition = 0;
        jailCards = 0;
        inJail = false;
        turnsInJail = 0;
        bankrupt = false;
    }

    public Player(int playerNumber, int money) {
        this(playerNumber);
        this.money = money;
    }

    public Player(int playerNumber, int money, ArrayList<Property> properties, boolean inJail, int turnsInJail, boolean bankrupt) {
        this(playerNumber);
        this.money = money;
        this.properties = properties;
        this.inJail = inJail;
        this.turnsInJail = turnsInJail;
        this.bankrupt = bankrupt;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int money) {
        this.money += money;
        if(this.money > 0){
            this.bankrupt = true;
        }
    }

    public void removeMoney(int money) {
        this.money -= money;
        if(this.money < 0){
            this.bankrupt = true;
        }
    }

    public ArrayList getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public void buyProperty(Property property) {
        this.removeMoney(property.getPrice());
        this.properties.add(property);
        property.setOwner(this);
    }

    public void mortgageProperty(Property property) {
        if(property.getMortgage() == false){
            this.addMoney(property.mortgageValue);
            property.setMortgage(true);
        } else{
            this.removeMoney(property.mortgageValue);
            property.setMortgage(false);
        }
    }

    public boolean getJail() {
        return inJail;
    }

    public void setJail(boolean jail) {
        this.inJail = jail;
    }

    public int getTurnsInJail() {
        return turnsInJail;
    }

    public void setTurnsInJail(int turnsInJail) {
        this.turnsInJail = turnsInJail;
    }

    public int getPosition() {
        return currentPosition;
    }

    public void setPosition(int pos) {
        currentPosition = pos;
    }

    public int getJailCards() {
        return jailCards;
    }

    public void setJailCards(int n) {
        jailCards = n;
    }
    
    public int getUtilities() {
        int num = 0;
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getType() == Space.SpaceType.SPACE_UTILITY) {
                num++;
            }
        }
        return num;
    }
    
    public int getRailroads() {
        int num = 0;
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getType() == Space.SpaceType.SPACE_RAILROAD) {
                num++;
            }
        }
        return num;
    }

    public String toString() {
        String output;
        output = ("Player " + playerNumber + ":\nMoney: " + money + "\nIn Jail: " + inJail + "\nBankrupt: " + bankrupt + "\nProperties: ");
        for (int i = 0; i < properties.size(); i++) {
            output += ("\n" + properties.get(i).getName());
        }
        return output;
    }
}
