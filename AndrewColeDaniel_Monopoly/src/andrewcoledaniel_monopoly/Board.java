/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import andrewcoledaniel_monopoly.Space.SpaceType;
import andrewcoledaniel_monopoly.Card.CardType;
import java.util.Random;

/**
 *
 * @author anfeh1812
 */
public class Board {
    private final Space[] board;
    
    public Board() {
        board = new Space[40];
        loadSpaces();
    }
    
    private void loadSpaces() {
        try {
            File propertiesFile = new File("src//andrewcoledaniel_monopoly//saves//properties.txt");
            Scanner s = new Scanner(propertiesFile);
            for (int i = 0; i < 40; i++) {
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
                    default:
                        board[i] = loadProperty(s, i);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Properties file not found");
        } 
   }
    
    private Property loadProperty(Scanner s, int i) {
        String name;
        int price, mortgageValue, rent, houseCost;
        int propertyNumber;
        Property p;
        switch (i) {
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
            case 12:
            case 28:
                name = s.nextLine();
                price = Integer.parseInt(s.nextLine());
                mortgageValue = Integer.parseInt(s.nextLine());
                propertyNumber = Integer.parseInt(s.nextLine());
                p = new Utility(name, price, mortgageValue, propertyNumber);
                break;
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
    
    public Space getSpace(int s) {
        return board[s];
    }
    
    public int findNextProperty(int x, SpaceType t) {
        for (int i = x; i < board.length; i++) {
            if (board[i].getType() == SpaceType.SPACE_PROPERTY) {
                if (((Property)board[i]).getPropType() == t) {
                    return i;
                }
            }
        }
        
        for (int i = 0; i < board.length; i++) {
            if (board[i].getType() == SpaceType.SPACE_PROPERTY) {
                if (((Property)board[i]).getPropType() == t) {
                    return i;
                }
            }
        }
        return 100;
    }
    
    public void shuffleCards(Card[] c) {
        Random rd = new Random();
        Card temp;
        int rNum;
        for (int i = 0; i < c.length; i++) {
            rNum = rd.nextInt(c.length);
            temp = c[i];
            c[i] = c[rNum];
            c[rNum] = temp;
        }
    }

    
    public String toString() {
        String s = "Board:\n";
        for (Space space : board) {
            s += space.getName() + "\n";
        }
        return s;
    }
    
}
