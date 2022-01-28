/*
 * Andrew, Cole, Daniel
 * 2022-01-27
 * Class that represents a game board
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Card.CardType;
import andrewcoledaniel_monopoly.Space.SpaceType;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author anfeh1812
 */
public class Board {

    private final Space[] board;

    /**
     * Primary constructor, fill board with spaces
     */
    public Board() {
        board = new Space[40];
        // Load spaces into board
        loadSpaces();
    }

    /**
     * Load spaces from resources into board array
     */
    private void loadSpaces() {
        // Open properties file and create Scanner object
        InputStream propertiesFile = GameScreen.class.getResourceAsStream("saves/properties.txt");
        Scanner s = new Scanner(propertiesFile);
        // Iterate over each space
        for (int i = 0; i < 40; i++) {
            // Check for special spaces and create space accordingly
            switch (i) {
                case 0:
                    board[i] = new CornerSpace("Go", SpaceType.SPACE_CORNER, SpaceType.SPACE_GO);
                    break;
                case 10:
                    board[i] = new CornerSpace("Jail", SpaceType.SPACE_CORNER, SpaceType.SPACE_JAIL);
                    break;
                case 20:
                    board[i] = new CornerSpace("Free Parking", SpaceType.SPACE_CORNER, SpaceType.SPACE_PARKING);
                    break;
                case 30:
                    board[i] = new CornerSpace("Go To Jail", SpaceType.SPACE_CORNER, SpaceType.SPACE_GO_JAIL);
                    break;
                case 4:
                    board[i] = new TaxSpace("Income Tax", 200);
                    break;
                case 38:
                    board[i] = new TaxSpace("Luxury Tax", 100);
                    break;
                case 7:
                case 22:
                case 36:
                    board[i] = new CardSpace("Chance", CardType.CARD_CHANCE);
                    break;
                case 2:
                case 17:
                case 33:
                    board[i] = new CardSpace("Community Chest", CardType.CARD_COMMUNITY_CHEST);
                    break;
                // Create property
                default:
                    board[i] = loadProperty(s, i);
                    break;
            }
        }
    }

    /**
     * Load a property into the array
     *
     * @param s Scanner object
     * @param i Property index
     * @return Property object
     */
    private Property loadProperty(Scanner s, int i) {
        String name;
        int price, mortgageValue, rent, houseCost;
        int propertyNumber;
        Property p;
        switch (i) {
            // create Railroad
            case 5:
            case 15:
            case 25:
            case 35:
                name = s.nextLine();
                price = Integer.parseInt(s.nextLine());
                mortgageValue = Integer.parseInt(s.nextLine());
                propertyNumber = Integer.parseInt(s.nextLine());
                p = new Railroad(name, price, mortgageValue, propertyNumber);
                break;
            // create Utility
            case 12:
            case 28:
                name = s.nextLine();
                price = Integer.parseInt(s.nextLine());
                mortgageValue = Integer.parseInt(s.nextLine());
                propertyNumber = Integer.parseInt(s.nextLine());
                p = new Utility(name, price, mortgageValue, propertyNumber);
                break;
            // create regular Deed property
            default:
                name = s.nextLine();
                price = Integer.parseInt(s.nextLine());
                mortgageValue = Integer.parseInt(s.nextLine());
                houseCost = Integer.parseInt(s.nextLine());
                rent = Integer.parseInt(s.nextLine());
                propertyNumber = Integer.parseInt(s.nextLine());
                p = new Deed(name, price, mortgageValue, houseCost, rent, propertyNumber);
                p.updateRent();
                break;
        }
        return p;
    }

    /**
     * Get Space object at specified index
     *
     * @param s index of Space
     * @return Space object
     */
    public Space getSpace(int s) {
        return board[s];
    }

    /**
     * Find next property with specific type
     *
     * @param x position to start search
     * @param t type of Space
     * @return index of Space
     */
    public int findNextProperty(int x, SpaceType t) {
        // Start at player position and find next property with type
        for (int i = x; i < board.length; i++) {
            if (board[i].getType() == SpaceType.SPACE_PROPERTY) {
                if (((Property) board[i]).getPropType() == t) {
                    return i;
                }
            }
        }

        // If next property is not before go, start searching at index 0
        for (int i = 0; i < board.length; i++) {
            if (board[i].getType() == SpaceType.SPACE_PROPERTY) {
                if (((Property) board[i]).getPropType() == t) {
                    return i;
                }
            }
        }
        // Should never happen
        return 100;
    }

    /**
     * Shuffle an array of Cards
     *
     * @param c Card array
     */
    public void shuffleCards(Card[] c) {
        Random rd = new Random();
        Card temp;
        int rNum;
        // Iterate over each card
        for (int i = 0; i < c.length; i++) {
            // Pick random card to swap with
            rNum = rd.nextInt(c.length);
            temp = c[i];
            c[i] = c[rNum];
            c[rNum] = temp;
        }
    }

    /**
     * Return string representation of Board
     *
     * @return
     */
    public String toString() {
        String s = "Board:\n";
        // Get each space name
        for (Space space : board) {
            s += space.getName() + "\n";
        }
        return s;
    }

}
