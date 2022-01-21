/*
Andrew Daniel Cole January 21 2022
Player class for the game of monopoly
This class creates a player object that holds the players money, properties, if they are bankrupt or not
and if they are in jail or not and if they are how long have they been in there for
 */
package andrewcoledaniel_monopoly;

import java.util.ArrayList;

public class Player {

    int playerNumber;
    int money;
    boolean inJail;
    ArrayList properties = new ArrayList();
    int turnsInJail;
    boolean bankrupt;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Player(int playerNumber, int money, ArrayList properties, boolean inJail, int turnsInJail, boolean bankrupt) {
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
    }

    public void removeMoney(int money) {
        this.money -= money;
    }

    public ArrayList getProperties() {
        return properties;
    }

    public void setProperties(ArrayList properties) {
        this.properties = properties;
    }

    public void buyProperty() {

    }

    public void sellProperty() {

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

    public String toString() {
        String output;
        output = ("Player " + playerNumber + ":\nMoney: " + money + "\nIn Jail: " + inJail + "\nBankrupt: " + bankrupt + "\nProperties: ");
        for (int i = 0; i < properties.size(); i++) {
            output += ("\n" + properties);
        }
        return output;
    }
}
