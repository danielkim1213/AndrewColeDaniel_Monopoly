/*
 * Andrew, Cole, Daniel
 * 2022-01-27
 * Class for the main game screen of monopoly
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Card.CardAction;
import andrewcoledaniel_monopoly.Card.CardType;
import andrewcoledaniel_monopoly.Space.SpaceType;
import java.awt.Image;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author dakim0069
 */
public class GameScreen extends javax.swing.JFrame {

    //variables
    MainMenu mainMenu;
    private EndingScreen endingScreen;
    private static Card[] cards = new Card[32];
    private final GameMusic bgm;
    private Thread gameBgmThread;
    private int gameMode;
    private final ImageIcon[] Die = new ImageIcon[6];
    public Boolean stopRoll = false;
    private long startTime;
    private int currentTurn;
    private int numPlayers;
    private ArrayList propertyArray = new ArrayList();
    private Player[] playerArray;
    public Timer timerRoll;
    public static int moves;
    private Board board;
    private TimerTask tsk;
    DecimalFormat curr = new DecimalFormat("#,##0.00");

    public static int[] roll = new int[2];

    /**
     * Creates new form GameScreen
     *
     * @param m - main menu
     * @param gameMode - game mode
     * @param numPlayers
     */
    public GameScreen(MainMenu m, int gameMode, int numPlayers) {
        initComponents();
        mainMenu = m;
        bgm = new GameMusic();
        gameBgmThread = new Thread(bgm);
        gameBgmThread.start();
        this.gameMode = gameMode;
        diceImage();
        this.numPlayers = numPlayers;
        playerArray = new Player[numPlayers];
        startTime = System.currentTimeMillis();
        currentTurn = 1;
        board = new Board();
        loadCards();
        generatePlayers();
        updateProperties();
        generateProperties();
        updateLocations();
    }

    public GameScreen(MainMenu m, int gameMode, int currentTurn, int numPlayers, Player[] playerArray) {
        this(m, gameMode, numPlayers);
        this.currentTurn = currentTurn;
        this.playerArray = playerArray;
    }

    /**
     * this method loads the dice images
     */
    private void diceImage() {
        Image img;

        //Since these six image loading are almost same, only one documentation done
        URL url0 = GameScreen.class.getResource("saves/dice1.jpg"); //load URL from the file
        Die[0] = new ImageIcon(url0); //change it to the image icon
        img = Die[0].getImage(); //convert to the image
        Die[0] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_SMOOTH));  //adjust the dice icon size as the label

        //same process with Die[0]
        URL url1 = GameScreen.class.getResource("saves/dice2.jpg");
        Die[1] = new ImageIcon(url1);
        img = Die[1].getImage();
        Die[1] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_SMOOTH));
        //same process with Die[0]
        URL url2 = GameScreen.class.getResource("saves/dice3.jpg");
        Die[2] = new ImageIcon(url2);
        img = Die[2].getImage();
        Die[2] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_SMOOTH));
        //same process with Die[0]
        URL url3 = GameScreen.class.getResource("saves/dice4.jpg");
        Die[3] = new ImageIcon(url3);
        img = Die[3].getImage();
        Die[3] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_SMOOTH));
        //same process with Die[0]
        URL url4 = GameScreen.class.getResource("saves/dice5.jpg");
        Die[4] = new ImageIcon(url4);
        img = Die[4].getImage();
        Die[4] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));
        //same process with Die[0]
        URL url5 = GameScreen.class.getResource("saves/dice6.jpg");
        Die[5] = new ImageIcon(url5);
        img = Die[5].getImage();
        Die[5] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        //set labels icon as Die[0]
        lblDie1.setIcon(Die[0]);
        lblDie2.setIcon(Die[0]);
    }

    /**
     * getter for Die icon
     *
     * @param index - die index
     * @return image icon of the die
     */
    public ImageIcon getDiceImage(int index) {
        return Die[index];
    }

    /**
     * Method to check the game mode
     *
     * @return false or true depending if game is over or not
     */
    private boolean checkGameMode() {
        if (gameMode != 2) { // if gamemode is not infinite
            if (gameMode == 0) { // if it is limited turns
                if (currentTurn > mainMenu.limitedTurns) { // checks to see if the current turn is greater
                    endGame(); // ends game
                    return false;
                }
            } else { // else if it is limited time
                long currentTime = System.currentTimeMillis(); // gets current time
                currentTime -= startTime; //removes start time
                if (currentTime > mainMenu.limitedTime) { // if the current time is greater than limited time
                    endGame(); // ends game
                    return false;
                }
            }
        } else { // else if infinite turns
            int bankruptPlayers = 0;
            for (int i = 0; i < playerArray.length; i++) { // loops through player array
                if (playerArray[i].bankrupt == true) { // if a player is bankrupt
                    bankruptPlayers++; // increases the variable
                }
            }
            if (bankruptPlayers >= numPlayers - 1) { // if the bankrupt players is greater than or equal to the num players - 1
                endGame(); // ends game
                return false;
            }
        }
        return true;
    }

    /**
     * Method to end the game
     */
    private void endGame() {
        Player winner = new Player(1); // variable for winner
        for (int i = 0; i < playerArray.length; i++) { // loops through the players
            if (playerArray[i].getMoney() > winner.getMoney()) { // gets the one with the highest money
                winner = playerArray[i];
            }
        }

        if (winner == playerArray[0]) //if the user is the winner
        {
            checkHighscore(winner.getMoney()); //check if it is the highscore
        }
        if (endingScreen == null) { // creates ending screen
            endingScreen = new EndingScreen(mainMenu, winner.getPlayerNumber(), playerArray);
        }
        this.setVisible(false); // sets this screen to invisble
        endingScreen.setVisible(true); // makes ending screen visible
    }

    /**
     * this method check if the current score can be in top5
     *
     * @param score - current score
     */
    private void checkHighscore(int score) {
        if (score < mainMenu.highscores[4]) // if the current score cannot be in top 5
        {
            return; //return the method
        }
        int[] newHighScore = new int[6]; //array of 6
        for (int i = 0; i < 5; i++)//assigning values to newHighScore (including the current score and previous top 5 scores)
        {
            newHighScore[i] = mainMenu.highscores[i];
        }
        newHighScore[5] = score; //add current score
        mainMenu.highscores = bubbleSortHighScore(newHighScore); //bubble sort and save the sorted highscores
    }

    /**
     * This sort the highscores by the bubble sort(descending order) and remove
     * the last index.
     *
     * @param newHighScore - integer array of length of 6
     * @return sorted array of length of 5
     */
    private int[] bubbleSortHighScore(int[] newHighScore) {
        int bottom = newHighScore.length - 1; //set bottom
        boolean sw = true;
        int temp;
        while (sw) {
            sw = false;
            for (int j = 0; j < bottom; j++) //compare two elements beside each other
            {
                if (newHighScore[j] < newHighScore[j + 1]) //placing the lowest value at the last -> descending order
                {
                    //swap
                    temp = newHighScore[j];
                    newHighScore[j] = newHighScore[j + 1];
                    newHighScore[j + 1] = temp;
                    sw = true;
                }
            }
            bottom = bottom - 1; //reduce bottom because the lowest value is already sorted at the last
        }
        int[] finalHighScore = new int[5]; //new array of length of 5
        for (int i = 0; i < 5; i++) //loop 5 times
        {
            finalHighScore[i] = newHighScore[i]; //assign first 5 value
        }
        return finalHighScore; // return the sorted value
    }

    /**
     * Load cards into array
     */
    private void loadCards() {
        int index = 0;
        CardType type;
        CardAction action;
        int value;
        String info;

        // Open cards file
        InputStream in = GameScreen.class.getResourceAsStream("saves/cards.txt");
        try {
            // Scanner to file
            Scanner s = new Scanner(in);
            while (s.hasNextLine()) {
                // Create Card object with attributes read from file
                type = Card.CardType.valueOf(s.nextLine());
                action = Card.CardAction.valueOf(s.nextLine());
                value = Integer.parseInt(s.nextLine());
                info = s.nextLine();
                // add to array and increment index
                cards[index] = new Card(type, action, value, info);
                index++;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Could not load cards from file");
        }
        // Shuffle cards array
        board.shuffleCards(cards);
    }

    /**
     * Method to generate players
     */
    private void generatePlayers() {
        for (int i = 0; i < playerArray.length; i++) { // loops by the length of hte player array
            playerArray[i] = new Player(i + 1, 1500); // creates a new player at each index with 1500 starting cash
        }
    }

    /**
     * Method for the players turn
     *
     * @param p player
     * @param turn the number of rolls they have done
     * @return
     */
    private boolean playerTurn(Player p, int turn) {
        int response;
        int newPos;

        if (turn > 3) { // if have rolled more than 3 times in a row
            p.setPosition(10); // sets their position to 10
            updateLocations(); // uptdates the locations
            p.setJail(true); // puts them in jail
            return false; // returns false
        }
        if (p.getJail()) { // if they are in jail
            if (p.getJailCards() > 0) { // and they have get out of jail cards
                Object[] options = {"Roll for doubles", "Pay $50", "Use get out of jail free card"}; // asks what they want to do
                response = JOptionPane.showOptionDialog(null, "You are in Jail. What would you like to do?", "In Jail", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            } else {
                Object[] options = {"Roll for doubles", "Pay $50"}; // if there are no get out of jail free cards
                response = JOptionPane.showOptionDialog(null, "You are in Jail. What would you like to do?", "In Jail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            }
            switch (response) {
                case 0: // if user chose roll for doubles
                    rollDice(); // rolls the dice
                    JOptionPane.showMessageDialog(null, "Click to stop"); // outputs message to click to stop
                    stopRolling(); // stops rolling
                    if (((Rolling) tsk).isDoubleDice()) { // checks to see if the dice are the same
                        JOptionPane.showMessageDialog(null, "You rolled doubles. Get out of jail.");
                        p.setJail(false); // if they are disables jail
                        p.setPosition(10 + moves); // moves
                        updateLocations(); // uptdates location
                    } else {
                        JOptionPane.showMessageDialog(null, "You didn't roll doubles. Stay in jail"); // outputs that you ddint roll doubles
                    }
                    break;
                case 1: // if the user choose to pay 50
                    p.removeMoney(50); // takes away 50
                    p.setJail(false); // gets them out of jail
                    break;
                case 2: // if the user has a get ouf of jail free card
                    p.setJailCards(p.getJailCards() - 1); // takes away 1
                    p.setJail(false); // gets them out of jail
                    break;
            }

            if (p.getJail()) { // if they are still in jail
                p.setTurnsInJail(p.getTurnsInJail() + 1); // increases turns in jail
                return false;
            }
            p.setTurnsInJail(0); // sets turns to 0 if not

        } // if not in jail
        rollDice(); // rolls a dice
        JOptionPane.showMessageDialog(null, "Click to stop");
        stopRolling(); // stops the dice

        newPos = p.getPosition() + moves; // moves that amount
        if (newPos >= 40) { // if they pass go
            p.addMoney(200); // adds money
            newPos -= 40; // removes 40 from their position
        }
        p.setPosition(newPos); // sets the position
        updateLocations(); // uptdates location
        handleSpace(board.getSpace(newPos), p); // calls the handle space method
        updateProperties(); // uptdates the properties
        return ((Rolling) tsk).isDoubleDice(); // returns true or false depending on if doubles were rolled
    }

    /**
     * Method for the computer turn
     *
     * @param p
     * @param turn
     */
    private void computerTurn(Player p, int turn) {

        if (turn >= 3) { // checks if the player has been in jail for three turns
            p.setJail(true); // gets them out of jail
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                stopRolling();
            }
        },
                1500
        );

        JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + "'s turn"); // outputs that its this players turn
        if (p.getJail()) { // checks if they are in jail
            if (p.getJailCards() < 0) {
                int randomNumber = (int) (Math.random() * 10); // creates a random number
                if (randomNumber > 5 && p.getMoney() > 50) { // 50% chance to buy its way out of jail
                    p.removeMoney(50);
                    p.setJail(false);// gets out of jail
                } else {
                    int roll1 = (int) (Math.random() * 6) + 1; // else makes 2 numbers
                    int roll2 = (int) (Math.random() * 6) + 1;
                    if (roll1 == roll2) { // if they are the same
                        p.setJail(false); // out of jail
                    }
                }
            }
        } else { // if not in jail
            int newPos;
            int diceRoll = (int) (Math.random() * 6) + 1;
            int diceRoll2 = (int) (Math.random() * 6) + 1; // makes two numbers
            moves = diceRoll + diceRoll2; // variable used to calculate the utility rent
            newPos = p.getPosition() + diceRoll + diceRoll2; // moves by the numer rolled
            if (newPos >= 40) { // if the computer passes go
                p.addMoney(200); // gains 100
                newPos -= 40; // removes 40 from their position
            }
            p.setPosition(newPos); // sets new position
            updateLocations(); // updates the location chart
            handleSpace(board.getSpace(newPos), p); // calls the method to do the space
            if (diceRoll == diceRoll2) { // if the numbers were the same
                JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + " has rolled a double");
                turn++; // the computer gets to go again
                computerTurn(p, turn);
            }
        }
        updateProperties(); // updates properties
    }

    /**
     * Handle a player landing on a space
     *
     * @param s Space object
     * @param p Player object
     */
    private void handleSpace(Space s, Player p) {
        // Get type of space
        SpaceType st = s.getType();
        JOptionPane.showMessageDialog(null, "You landed on: " + s.getName());
        switch (st) {
            // If corner space, perform corner action
            case SPACE_CORNER:
                ((CornerSpace) s).performSpaceAction(p);
                updateLocations();
                break;
            // If property space, perform property action
            case SPACE_PROPERTY:
                handleProperty((Property) s, p);
                break;
            // If card space
            case SPACE_CARD:
                // Draw card
                Card c = ((CardSpace) s).getCard(cards);
                // Shuffle cards
                board.shuffleCards(cards);
                // Perform card action
                String out = ((CardSpace) s).performSpaceAction(c, p, board, playerArray);
                updateLocations();
                // Show card information
                JOptionPane.showMessageDialog(null, out);
                // Check new space and handle it accordingly
                if (board.getSpace(p.getPosition()).getType() == SpaceType.SPACE_PROPERTY) {
                    handleProperty((Property) board.getSpace(p.getPosition()), p);
                } else if (board.getSpace(p.getPosition()).getType() == SpaceType.SPACE_CORNER) {
                    ((CornerSpace) board.getSpace(p.getPosition())).performSpaceAction(p);
                }
        }
    }

    /**
     * Handle when a player lands on a property space
     *
     * @param prop Property space
     * @param p Player object
     */
    private void handleProperty(Property prop, Player p) {
        int option;
        // Check if player is human
        if (p.getPlayerNumber() == 1) {
            // If property isn't owned
            if (!prop.getOwned()) {
                option = JOptionPane.showConfirmDialog(null, ((Space) prop).getName() + " is not owned. Would you like to purchase it?", "Choice", JOptionPane.YES_NO_OPTION);
                // If user wants to buy property
                if (option == 0) {
                    p.buyProperty(prop);
                    prop.setOwned(true);
                    // If not, auction it off
                } else {
                    auction(prop);
                }
                // If property is owned and not mortgaged
            } else {
                if (!prop.mortgage) {
                    // Pay rent on property
                    JOptionPane.showMessageDialog(null, "This property is owned by player " + prop.getOwner() + ". You must pay them $" + prop.getRent() + ".");
                    prop.updateRent();
                    p.removeMoney(prop.getRent());
                    prop.getOwner().addMoney(prop.getRent());
                }
            }
            // If not a human player
        } else {
            // if not owned
            if (!prop.getOwned()) {
                int randomNumber = (int) (Math.random() * 10);
                // CPU has a random chance to buy if they have enough money
                if (randomNumber < 7 && p.getMoney() > prop.getPrice()) {
                    // Buy property and update information
                    p.buyProperty(prop);
                    prop.updateRent();
                    prop.setOwned(true);
                    updateProperties();
                } else {
                    // Auction off property
                    auction(prop);
                }
                // if owned and not mortgaged
            } else {
                if (!prop.mortgage) {
                    // Pay rent on property
                    JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + " has landed on " + prop.getName() + " and has paid player" + prop.getOwner().getPlayerNumber() + " $" + prop.getRent());
                    p.removeMoney(prop.getRent());
                    prop.getOwner().addMoney(prop.getRent());
                }
            }
        }
    }

    /**
     * Handle an auction on a Property
     *
     * @param p Property object
     */
    private void auction(Property p) {
        boolean bought = false;
        int currentBid = 0;
        int response;
        // Set last bidder to last player
        Player lastBidder = playerArray[numPlayers - 1];
        ArrayList<Player> players = new ArrayList();
        ArrayList<Player> toRemove = new ArrayList();

        // Add each player to players arraylist
        for (int i = 0; i < numPlayers; i++) {
            players.add(playerArray[i]);
        }

        // while property hasn't been purchased
        while (!bought) {
            // Allow each player to bid
            for (int i = 0; i < players.size(); i++) {
                // If bid is over player's money, remove them from the auction
                if (players.get(i).getMoney() <= currentBid) {
                    toRemove.add(players.get(i));
                    continue;
                }

                // if one player is left, end auction
                if (players.size() == 1) {
                    bought = true;
                    break;
                }

                response = -1;
                // If human player
                if (players.get(i).getPlayerNumber() == 1) {
                    // while not a valid response
                    while (response < 0) {
                        try {
                            // Prompt user for their bid
                            response = Integer.parseInt(JOptionPane.showInputDialog("The current bid is $" + curr.format(currentBid) + ". How much more would you like to bid? (Type 0 to cancel)"));
                            // If user wants to exit the auction, remove them
                            if (response == 0) {
                                toRemove.add(players.get(i));
                                JOptionPane.showMessageDialog(null, "Player 1 left the auction");
                                // if their bid is less than the current bid
                            } else if (response <= currentBid) {
                                JOptionPane.showMessageDialog(null, "Please input a bid that is greater than the current bid.");
                                response = -1;
                                // if their bid is greater than their current amount of money
                            } else if (response > players.get(i).getMoney()) {
                                JOptionPane.showMessageDialog(null, "Please bid an amount of money that you have.");
                                response = -1;
                            }
                            // Catch any exceptions from input
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount");
                        }
                    }
                    // Store their bid as the current one
                    currentBid = response;
                    lastBidder = players.get(i);
                    // If CPU player
                } else {
                    // Calculate a random bid for the CPU
                    response = currentBid + (int) (Math.random() * 20) + 10;

                    try { // sleep for a little bit
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "Thread.sleep method error");
                    }

                    // If their bid is within $50 of their money or it's above a bidding threshold, leave auction
                    if (response + 50 > players.get(i).getMoney() || response > p.getPrice() * 1.5) {
                        JOptionPane.showMessageDialog(null, "Player " + players.get(i).getPlayerNumber() + " left the auction");
                        toRemove.add(players.get(i));
                        // Bid on property and set bid to the current bid
                    } else {
                        JOptionPane.showMessageDialog(null, "Player " + players.get(i).getPlayerNumber() + " bid $" + curr.format(response));
                        currentBid = response;
                        lastBidder = players.get(i);
                    }
                }
            }
            // Remove all marked players from the auction
            for (Player x : toRemove) {
                players.remove(x);
            }
        }

        // Announce the winner
        JOptionPane.showMessageDialog(null, "Player " + lastBidder.getPlayerNumber() + " purchased the property for $" + curr.format(currentBid));
        // Player buys property for amount
        lastBidder.buyPropertyAuction(p, currentBid);
        p.setOwned(true);
    }

    /**
     * this method rolls two dice
     */
    private void rollDice() {
        tsk = new Rolling(this); //instantiate Rolling class object
        timerRoll = new Timer(); //new timer
        timerRoll.scheduleAtFixedRate(tsk, 125, 145); // run tsk every inteval
    }

    /**
     * this method is to stop rolling the dice
     */
    public void stopRolling() {

        stopRoll = true;

        try {
            Thread.sleep(200); //stop for 0.2sec
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Thread.sleep method error");
        }

        lblDiceSum.setText("Moves: " + moves); //change the moves text
        stopRoll = false; //set stopRoll false again for the next dice roll
    }

    /**
     * Method to update the proerties text area
     */
    private void updateProperties() {
        txaProperties.setText(""); // sets it black
        for (int i = 0; i < playerArray.length; i++) { // loops by the amount of players
            txaProperties.append("Player " + (i + 1) + ":\n"); // adds the players to the text field
            txaProperties.append("Money: " + playerArray[i].getMoney() + "\nProperties:\n"); // adds their money
            try {
                txaProperties.append(playerArray[i].propertyNames() + "\n"); // adds the name of their properties
            } catch (NullPointerException e) { // if null
                txaProperties.append("No Properties owned.\n"); // adds no properties owned
            }
        }
    }

    /**
     * this method is to update the player's location
     */
    private void updateLocations() {
        lblPlayer1.setText("Player " + 1 + ": " + board.getSpace(playerArray[0].getPosition()).getName()); //player 1
        lblPlayer2.setText("Player " + 2 + ": " + board.getSpace(playerArray[1].getPosition()).getName()); //player 2
        if (numPlayers >= 3) { //only when there are more or equal than three players
            lblPlayer3.setText("Player " + 3 + ": " + board.getSpace(playerArray[2].getPosition()).getName()); //player3
        }
        if (numPlayers == 4) { //only when there are four players
            lblPlayer4.setText("Player " + 4 + ": " + board.getSpace(playerArray[3].getPosition()).getName()); //player4
        }
    }

    /**
     * this method is to set text for all tiles
     */
    private void generateProperties() {
        txfProperty1.setText(board.getSpace(1).getName());
        txfProperty2.setText(board.getSpace(3).getName());
        txfProperty3.setText(board.getSpace(5).getName());
        txfProperty4.setText(board.getSpace(6).getName());
        txfProperty5.setText(board.getSpace(8).getName());
        txfProperty6.setText(board.getSpace(9).getName());
        txfProperty7.setText(board.getSpace(11).getName());
        txfProperty8.setText(board.getSpace(12).getName());
        txfProperty9.setText(board.getSpace(13).getName());
        txfProperty10.setText(board.getSpace(14).getName());
        txfProperty11.setText(board.getSpace(15).getName());
        txfProperty12.setText(board.getSpace(16).getName());
        txfProperty13.setText(board.getSpace(18).getName());
        txfProperty14.setText(board.getSpace(19).getName());
        txfProperty15.setText(board.getSpace(21).getName());
        txfProperty16.setText(board.getSpace(23).getName());
        txfProperty17.setText(board.getSpace(24).getName());
        txfProperty18.setText(board.getSpace(25).getName());
        txfProperty19.setText(board.getSpace(26).getName());
        txfProperty20.setText(board.getSpace(27).getName());
        txfProperty21.setText(board.getSpace(28).getName());
        txfProperty22.setText(board.getSpace(29).getName());
        txfProperty23.setText(board.getSpace(31).getName());
        txfProperty24.setText(board.getSpace(32).getName());
        txfProperty25.setText(board.getSpace(34).getName());
        txfProperty26.setText(board.getSpace(35).getName());
        txfProperty27.setText(board.getSpace(37).getName());
        txfProperty28.setText(board.getSpace(39).getName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlStatus = new javax.swing.JPanel();
        lblTurn = new javax.swing.JLabel();
        lblPlayerStatistics = new javax.swing.JLabel();
        btnBuyHouse = new javax.swing.JButton();
        btnSellHouse = new javax.swing.JButton();
        btnMortgage = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaProperties = new javax.swing.JTextArea();
        btnRollDice = new javax.swing.JButton();
        btnEndTurn = new javax.swing.JButton();
        lblDie2 = new javax.swing.JLabel();
        lblDie1 = new javax.swing.JLabel();
        lblDiceSum = new javax.swing.JLabel();
        Tile1 = new javax.swing.JPanel();
        txtGO = new javax.swing.JLabel();
        txtGOArrow = new javax.swing.JLabel();
        Tile2 = new javax.swing.JPanel();
        txfProperty1 = new javax.swing.JTextField();
        Tile3 = new javax.swing.JPanel();
        txtTile3label1 = new javax.swing.JLabel();
        txtTile3label2 = new javax.swing.JLabel();
        Tile4 = new javax.swing.JPanel();
        txfProperty2 = new javax.swing.JTextField();
        Tile5 = new javax.swing.JPanel();
        txtTile5label1 = new javax.swing.JLabel();
        txtTile5label2 = new javax.swing.JLabel();
        Tile6 = new javax.swing.JPanel();
        txfProperty3 = new javax.swing.JTextField();
        Tile11 = new javax.swing.JPanel();
        txfJail = new javax.swing.JTextField();
        txtJustVisting = new javax.swing.JLabel();
        Tile7 = new javax.swing.JPanel();
        txfProperty4 = new javax.swing.JTextField();
        Tile8 = new javax.swing.JPanel();
        txtTile8label1 = new javax.swing.JLabel();
        txtTile8label2 = new javax.swing.JLabel();
        Tile9 = new javax.swing.JPanel();
        txfProperty5 = new javax.swing.JTextField();
        Tile10 = new javax.swing.JPanel();
        txfProperty6 = new javax.swing.JTextField();
        Tile12 = new javax.swing.JPanel();
        txfProperty7 = new javax.swing.JTextField();
        Tile13 = new javax.swing.JPanel();
        txfProperty8 = new javax.swing.JTextField();
        Tile14 = new javax.swing.JPanel();
        txfProperty9 = new javax.swing.JTextField();
        Tile15 = new javax.swing.JPanel();
        txfProperty10 = new javax.swing.JTextField();
        Tile16 = new javax.swing.JPanel();
        txfProperty11 = new javax.swing.JTextField();
        Tile17 = new javax.swing.JPanel();
        txfProperty12 = new javax.swing.JTextField();
        Tile18 = new javax.swing.JPanel();
        txtTile18label1 = new javax.swing.JLabel();
        txtTile18label2 = new javax.swing.JLabel();
        Tile19 = new javax.swing.JPanel();
        txfProperty13 = new javax.swing.JTextField();
        Tile20 = new javax.swing.JPanel();
        txfProperty14 = new javax.swing.JTextField();
        Tile21 = new javax.swing.JPanel();
        txtFreeParking2 = new javax.swing.JLabel();
        txtFreeParking1 = new javax.swing.JLabel();
        Tile22 = new javax.swing.JPanel();
        txfProperty15 = new javax.swing.JTextField();
        Tile23 = new javax.swing.JPanel();
        txtTile23label1 = new javax.swing.JLabel();
        txtTile23label2 = new javax.swing.JLabel();
        Tile31 = new javax.swing.JPanel();
        txtGoToJail1 = new javax.swing.JLabel();
        txtGoToJail2 = new javax.swing.JLabel();
        Tile24 = new javax.swing.JPanel();
        txfProperty16 = new javax.swing.JTextField();
        Tile25 = new javax.swing.JPanel();
        txfProperty17 = new javax.swing.JTextField();
        Tile26 = new javax.swing.JPanel();
        txfProperty18 = new javax.swing.JTextField();
        Tile27 = new javax.swing.JPanel();
        txfProperty19 = new javax.swing.JTextField();
        Tile28 = new javax.swing.JPanel();
        txfProperty20 = new javax.swing.JTextField();
        Tile29 = new javax.swing.JPanel();
        txfProperty21 = new javax.swing.JTextField();
        Tile30 = new javax.swing.JPanel();
        txfProperty22 = new javax.swing.JTextField();
        Tile32 = new javax.swing.JPanel();
        txfProperty23 = new javax.swing.JTextField();
        Tile33 = new javax.swing.JPanel();
        txfProperty24 = new javax.swing.JTextField();
        Tile34 = new javax.swing.JPanel();
        txtTile34label1 = new javax.swing.JLabel();
        txtTile34label2 = new javax.swing.JLabel();
        Tile35 = new javax.swing.JPanel();
        txfProperty25 = new javax.swing.JTextField();
        Tile36 = new javax.swing.JPanel();
        txfProperty26 = new javax.swing.JTextField();
        Tile37 = new javax.swing.JPanel();
        txtTile37label1 = new javax.swing.JLabel();
        txtTile37label2 = new javax.swing.JLabel();
        Tile38 = new javax.swing.JPanel();
        txfProperty27 = new javax.swing.JTextField();
        Tile39 = new javax.swing.JPanel();
        txtTile39label1 = new javax.swing.JLabel();
        txtTile39label2 = new javax.swing.JLabel();
        Tile40 = new javax.swing.JPanel();
        txfProperty28 = new javax.swing.JTextField();
        lblPlayerLocations = new javax.swing.JLabel();
        lblPlayer1 = new javax.swing.JLabel();
        lblPlayer2 = new javax.swing.JLabel();
        lblPlayer3 = new javax.swing.JLabel();
        lblPlayer4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlStatus.setBackground(new java.awt.Color(204, 255, 204));

        lblTurn.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTurn.setText("Turn 1");

        lblPlayerStatistics.setBackground(new java.awt.Color(255, 255, 255));
        lblPlayerStatistics.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblPlayerStatistics.setText("Players Statistics");

        btnBuyHouse.setText("Buy House");
        btnBuyHouse.setPreferredSize(new java.awt.Dimension(90, 50));
        btnBuyHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyHouseActionPerformed(evt);
            }
        });

        btnSellHouse.setText("Sell House");
        btnSellHouse.setPreferredSize(new java.awt.Dimension(90, 50));
        btnSellHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSellHouseActionPerformed(evt);
            }
        });

        btnMortgage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnMortgage.setText("Mortgage Property");
        btnMortgage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMortgageActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        txaProperties.setEditable(false);
        txaProperties.setColumns(20);
        txaProperties.setRows(5);
        jScrollPane2.setViewportView(txaProperties);

        btnRollDice.setText("Roll Dice");
        btnRollDice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollDiceActionPerformed(evt);
            }
        });

        btnEndTurn.setText("End Turn");
        btnEndTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndTurnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTurn)
                .addGap(79, 79, 79))
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPlayerStatistics)
                    .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlStatusLayout.createSequentialGroup()
                        .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatusLayout.createSequentialGroup()
                        .addComponent(btnRollDice, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEndTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatusLayout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTurn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPlayerStatistics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRollDice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEndTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        getContentPane().add(pnlStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 0, -1, 875));

        lblDie2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie2.setText("lblDie2");
        getContentPane().add(lblDie2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 240, 80, 80));

        lblDie1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie1.setText("lblDie1");
        getContentPane().add(lblDie1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 80, 80));

        lblDiceSum.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDiceSum.setText("Moves: ");
        getContentPane().add(lblDiceSum, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 80, -1));

        Tile1.setBackground(new java.awt.Color(220, 255, 196));
        Tile1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile1.setPreferredSize(new java.awt.Dimension(100, 100));
        Tile1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtGO.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGO.setText("GO");
        Tile1.add(txtGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 19, 78, -1));

        txtGOArrow.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGOArrow.setForeground(new java.awt.Color(255, 0, 0));
        txtGOArrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGOArrow.setText("<-----");
        Tile1.add(txtGOArrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 47, 78, -1));

        getContentPane().add(Tile1, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 775, -1, -1));

        Tile2.setBackground(new java.awt.Color(220, 255, 196));
        Tile2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile2.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty1.setEditable(false);
        txfProperty1.setBackground(new java.awt.Color(234, 221, 202));
        txfProperty1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty1.setText("Property 1");
        txfProperty1.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile2.add(txfProperty1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 775, -1, -1));

        Tile3.setBackground(new java.awt.Color(220, 255, 196));
        Tile3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile3.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile3label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile3label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile3label1.setText("Community");
        Tile3.add(txtTile3label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 21, 73, -1));

        txtTile3label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile3label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile3label2.setText("Chest");
        Tile3.add(txtTile3label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 48, 53, -1));

        getContentPane().add(Tile3, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 775, -1, -1));

        Tile4.setBackground(new java.awt.Color(220, 255, 196));
        Tile4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile4.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty2.setEditable(false);
        txfProperty2.setBackground(new java.awt.Color(234, 221, 202));
        txfProperty2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty2.setText("Property 2");
        txfProperty2.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile4.add(txfProperty2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 775, -1, -1));

        Tile5.setBackground(new java.awt.Color(220, 255, 196));
        Tile5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile5.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile5label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile5label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile5label1.setText("Income");
        Tile5.add(txtTile5label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 21, 73, -1));

        txtTile5label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile5label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile5label2.setText("Tax");
        Tile5.add(txtTile5label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 48, 53, -1));

        getContentPane().add(Tile5, new org.netbeans.lib.awtextra.AbsoluteConstraints(475, 775, -1, -1));

        Tile6.setBackground(new java.awt.Color(220, 255, 196));
        Tile6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile6.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty3.setEditable(false);
        txfProperty3.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty3.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty3.setText("Railroad 1");
        txfProperty3.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile6.add(txfProperty3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 775, -1, -1));

        Tile11.setBackground(new java.awt.Color(220, 255, 196));
        Tile11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile11.setPreferredSize(new java.awt.Dimension(100, 100));
        Tile11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfJail.setEditable(false);
        txfJail.setBackground(new java.awt.Color(255, 190, 0));
        txfJail.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txfJail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfJail.setText("IN JAIL");
        txfJail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfJail.setPreferredSize(new java.awt.Dimension(70, 70));
        Tile11.add(txfJail, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 1, -1, -1));

        txtJustVisting.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtJustVisting.setText("JUST VISITING");
        Tile11.add(txtJustVisting, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 77, 88, -1));

        getContentPane().add(Tile11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 775, -1, -1));

        Tile7.setBackground(new java.awt.Color(220, 255, 196));
        Tile7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile7.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty4.setEditable(false);
        txfProperty4.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty4.setText("Property 4");
        txfProperty4.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile7.add(txfProperty4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile7, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 775, -1, -1));

        Tile8.setBackground(new java.awt.Color(220, 255, 196));
        Tile8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile8.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile8label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile8label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile8label1.setText("Chance");
        Tile8.add(txtTile8label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 21, 53, -1));

        txtTile8label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile8label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile8label2.setText("Card");
        Tile8.add(txtTile8label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 48, 53, -1));

        getContentPane().add(Tile8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 775, -1, -1));

        Tile9.setBackground(new java.awt.Color(220, 255, 196));
        Tile9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile9.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty5.setEditable(false);
        txfProperty5.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty5.setText("Property 5");
        txfProperty5.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile9.add(txfProperty5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile9, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 775, -1, -1));

        Tile10.setBackground(new java.awt.Color(220, 255, 196));
        Tile10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile10.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty6.setEditable(false);
        txfProperty6.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty6.setText("Property 6");
        txfProperty6.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile10.add(txfProperty6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 775, -1, -1));

        Tile12.setBackground(new java.awt.Color(220, 255, 196));
        Tile12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile12.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty7.setEditable(false);
        txfProperty7.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty7.setText("Property 7");
        txfProperty7.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile12.add(txfProperty7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 700, -1, -1));

        Tile13.setBackground(new java.awt.Color(220, 255, 196));
        Tile13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile13.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty8.setEditable(false);
        txfProperty8.setBackground(new java.awt.Color(255, 255, 255));
        txfProperty8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty8.setText("Utilities 1");
        txfProperty8.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile13.add(txfProperty8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 625, -1, -1));

        Tile14.setBackground(new java.awt.Color(220, 255, 196));
        Tile14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile14.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty9.setEditable(false);
        txfProperty9.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty9.setText("Property 9");
        txfProperty9.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile14.add(txfProperty9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, -1, -1));

        Tile15.setBackground(new java.awt.Color(220, 255, 196));
        Tile15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile15.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty10.setEditable(false);
        txfProperty10.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty10.setText("Property 10");
        txfProperty10.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile15.add(txfProperty10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 475, -1, -1));

        Tile16.setBackground(new java.awt.Color(220, 255, 196));
        Tile16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile16.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty11.setEditable(false);
        txfProperty11.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty11.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty11.setText("Railroad 2");
        txfProperty11.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile16.add(txfProperty11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, -1, -1));

        Tile17.setBackground(new java.awt.Color(220, 255, 196));
        Tile17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile17.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty12.setEditable(false);
        txfProperty12.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty12.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty12.setText("Property 12");
        txfProperty12.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile17.add(txfProperty12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 325, -1, -1));

        Tile18.setBackground(new java.awt.Color(220, 255, 196));
        Tile18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile18.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile18label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile18label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile18label1.setText("Community");
        Tile18.add(txtTile18label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 16, 78, -1));

        txtTile18label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile18label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile18label2.setText("Chest");
        Tile18.add(txtTile18label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 43, 78, -1));

        getContentPane().add(Tile18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

        Tile19.setBackground(new java.awt.Color(220, 255, 196));
        Tile19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile19.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty13.setEditable(false);
        txfProperty13.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty13.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty13.setText("Property 13");
        txfProperty13.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile19.add(txfProperty13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 175, -1, -1));

        Tile20.setBackground(new java.awt.Color(220, 255, 196));
        Tile20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile20.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty14.setEditable(false);
        txfProperty14.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty14.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty14.setText("Property 14");
        txfProperty14.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile20.add(txfProperty14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        Tile21.setBackground(new java.awt.Color(220, 255, 196));
        Tile21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile21.setPreferredSize(new java.awt.Dimension(100, 100));
        Tile21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtFreeParking2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFreeParking2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtFreeParking2.setText("PARKING");
        Tile21.add(txtFreeParking2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 50, 78, -1));

        txtFreeParking1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFreeParking1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtFreeParking1.setText("FREE");
        Tile21.add(txtFreeParking1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 22, 78, -1));

        getContentPane().add(Tile21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        Tile22.setBackground(new java.awt.Color(220, 255, 196));
        Tile22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile22.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty15.setEditable(false);
        txfProperty15.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty15.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty15.setText("Property 15");
        txfProperty15.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile22.add(txfProperty15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, -1));

        Tile23.setBackground(new java.awt.Color(220, 255, 196));
        Tile23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile23.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile23label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile23label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile23label1.setText("Chance");
        Tile23.add(txtTile23label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 21, 53, -1));

        txtTile23label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile23label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile23label2.setText("Card");
        Tile23.add(txtTile23label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 48, 53, -1));

        getContentPane().add(Tile23, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 0, -1, -1));

        Tile31.setBackground(new java.awt.Color(220, 255, 196));
        Tile31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile31.setPreferredSize(new java.awt.Dimension(100, 100));
        Tile31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtGoToJail1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGoToJail1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGoToJail1.setText("GO TO");
        Tile31.add(txtGoToJail1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 23, 78, -1));

        txtGoToJail2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGoToJail2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGoToJail2.setText("JAIL");
        Tile31.add(txtGoToJail2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 63, 78, -1));

        getContentPane().add(Tile31, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 0, -1, -1));

        Tile24.setBackground(new java.awt.Color(220, 255, 196));
        Tile24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile24.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty16.setEditable(false);
        txfProperty16.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty16.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty16.setText("Property 16");
        txfProperty16.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile24.add(txfProperty16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile24, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, -1, -1));

        Tile25.setBackground(new java.awt.Color(220, 255, 196));
        Tile25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile25.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty17.setEditable(false);
        txfProperty17.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty17.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty17.setText("Property 17");
        txfProperty17.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile25.add(txfProperty17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile25, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 0, -1, -1));

        Tile26.setBackground(new java.awt.Color(220, 255, 196));
        Tile26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile26.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty18.setEditable(false);
        txfProperty18.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty18.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty18.setText("Railroad 3");
        txfProperty18.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile26.add(txfProperty18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile26, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        Tile27.setBackground(new java.awt.Color(220, 255, 196));
        Tile27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile27.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty19.setEditable(false);
        txfProperty19.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty19.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty19.setText("Property 19");
        txfProperty19.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile27.add(txfProperty19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile27, new org.netbeans.lib.awtextra.AbsoluteConstraints(475, 0, -1, -1));

        Tile28.setBackground(new java.awt.Color(220, 255, 196));
        Tile28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile28.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty20.setEditable(false);
        txfProperty20.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty20.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty20.setText("Property 20");
        txfProperty20.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile28.add(txfProperty20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile28, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, -1, -1));

        Tile29.setBackground(new java.awt.Color(220, 255, 196));
        Tile29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile29.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty21.setEditable(false);
        txfProperty21.setBackground(new java.awt.Color(255, 255, 255));
        txfProperty21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty21.setText("Utilities 2");
        txfProperty21.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile29.add(txfProperty21, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile29, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 0, -1, -1));

        Tile30.setBackground(new java.awt.Color(220, 255, 196));
        Tile30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile30.setPreferredSize(new java.awt.Dimension(75, 100));
        Tile30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty22.setEditable(false);
        txfProperty22.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty22.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty22.setText("Property 22");
        txfProperty22.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile30.add(txfProperty22, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 73, -1));

        getContentPane().add(Tile30, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, -1, -1));

        Tile32.setBackground(new java.awt.Color(220, 255, 196));
        Tile32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile32.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty23.setEditable(false);
        txfProperty23.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty23.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty23.setText("Property 23");
        txfProperty23.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile32.add(txfProperty23, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile32, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 100, -1, -1));

        Tile33.setBackground(new java.awt.Color(220, 255, 196));
        Tile33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile33.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty24.setEditable(false);
        txfProperty24.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty24.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty24.setText("Property 24");
        txfProperty24.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile33.add(txfProperty24, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile33, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 175, -1, -1));

        Tile34.setBackground(new java.awt.Color(220, 255, 196));
        Tile34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile34.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile34label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile34label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile34label1.setText("Community");
        Tile34.add(txtTile34label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 16, 98, -1));

        txtTile34label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile34label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile34label2.setText("Chest");
        Tile34.add(txtTile34label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 43, 78, -1));

        getContentPane().add(Tile34, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 250, -1, -1));

        Tile35.setBackground(new java.awt.Color(220, 255, 196));
        Tile35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile35.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty25.setEditable(false);
        txfProperty25.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty25.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty25.setText("Property 25");
        txfProperty25.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile35.add(txfProperty25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile35, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 325, -1, -1));

        Tile36.setBackground(new java.awt.Color(220, 255, 196));
        Tile36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile36.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty26.setEditable(false);
        txfProperty26.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty26.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty26.setText("Railroad 4");
        txfProperty26.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile36.add(txfProperty26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile36, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 400, -1, -1));

        Tile37.setBackground(new java.awt.Color(220, 255, 196));
        Tile37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile37.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile37label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile37label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile37label1.setText("Chance");
        Tile37.add(txtTile37label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 16, 98, -1));

        txtTile37label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile37label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile37label2.setText("Card");
        Tile37.add(txtTile37label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 43, 78, -1));

        getContentPane().add(Tile37, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 475, -1, -1));

        Tile38.setBackground(new java.awt.Color(220, 255, 196));
        Tile38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile38.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty27.setEditable(false);
        txfProperty27.setBackground(new java.awt.Color(0, 0, 205));
        txfProperty27.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty27.setText("Property 27");
        txfProperty27.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile38.add(txfProperty27, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile38, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 550, -1, -1));

        Tile39.setBackground(new java.awt.Color(220, 255, 196));
        Tile39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile39.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTile39label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile39label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile39label1.setText("Luxury");
        Tile39.add(txtTile39label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 16, 78, -1));

        txtTile39label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile39label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile39label2.setText("Tax");
        Tile39.add(txtTile39label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 43, 78, -1));

        getContentPane().add(Tile39, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 625, -1, -1));

        Tile40.setBackground(new java.awt.Color(220, 255, 196));
        Tile40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile40.setPreferredSize(new java.awt.Dimension(100, 75));
        Tile40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txfProperty28.setEditable(false);
        txfProperty28.setBackground(new java.awt.Color(0, 0, 205));
        txfProperty28.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty28.setText("Property 28");
        txfProperty28.setPreferredSize(new java.awt.Dimension(70, 30));
        Tile40.add(txfProperty28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 98, -1));

        getContentPane().add(Tile40, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 700, -1, -1));

        lblPlayerLocations.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblPlayerLocations.setText("Player Locations:");
        getContentPane().add(lblPlayerLocations, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 360, 140, -1));

        lblPlayer1.setText(".");
        getContentPane().add(lblPlayer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 200, -1));

        lblPlayer2.setText(".");
        getContentPane().add(lblPlayer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 200, -1));

        lblPlayer3.setText(".");
        getContentPane().add(lblPlayer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 200, -1));

        lblPlayer4.setText(".");
        getContentPane().add(lblPlayer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 490, 200, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * if the user click the menu button
     *
     * @param evt - click the button
     */
    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        bgm.musicOff(); //game music off
        mainMenu.setVisible(true); //set the main menu visible
        mainMenu.mainBgm.musicOn(); //turn the main menu bgm on
        this.setVisible(false); //set this window invisible
    }//GEN-LAST:event_btnMenuActionPerformed

    /**
     * if the user click the buy house button
     *
     * @param evt - click the button
     */
    private void btnBuyHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyHouseActionPerformed
        if (playerArray[0].getProperties().isEmpty()) { // checks if the player has properties
            JOptionPane.showMessageDialog(null, "You do not own any properties.");
            return;
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to buy a house on?");
        if (prop == null) { // if user doesn't input anything
            return;
        }
        int propNum = playerArray[0].findProperty(prop); // asks which property to buy the house on
        if (propNum == -1) { // if proerty does not exist
            JOptionPane.showMessageDialog(null, "You do not own " + prop); // output that you dont own it
            return;
        }
        Property p = ((Property) playerArray[0].getProperties().get(propNum));
        if (p.getPropType() != SpaceType.SPACE_DEED) { // if the property is nto a deed
            JOptionPane.showMessageDialog(null, "You cannot purchase a house on this property."); // you cant build a house on this
            return;
        }
        Deed d = (Deed) p; // variable to hold the deed
        if (d.getHouses() == 4) { // if there are 4 houses
            if (d.getHotel() == false) { // and there is no hotel
                if (JOptionPane.showConfirmDialog(null, "You already have 4 houses on this property. Would you like to buy a hotel instead?", "Buy Hotel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (playerArray[0].getMoney() < d.getHouseCost()) { // if the user doesnt have neough money
                        JOptionPane.showMessageDialog(null, "You don't have enough money."); // output they dont have enough
                    } else {
                        d.buyHotel(); // else buy the hotel
                        playerArray[0].removeMoney(d.getHouseCost()); // remove the cost
                    }
                    return;
                }
            }
        }
        try { // try a conversion from string to integer
            int houses = Integer.parseInt(JOptionPane.showInputDialog("How many houses would you like to buy? (Maximum of four per property. This one currently has: " + d.getHouses()));
            // input for number of houses purchased
            if (houses + d.getHouses() > 4) { //if there ar more than 4 houses
                JOptionPane.showMessageDialog(null, "You can not have more than four houses on a property.");//outputs error
            } else if (houses * d.getHouseCost() > playerArray[0].getMoney()) { // else if the user doesnt have enough money
                JOptionPane.showMessageDialog(null, "You don't have enough money to buy " + houses + " houses."); // outputs error
            } else {
                d.buyHouse(houses); // esle buys the houses
                JOptionPane.showMessageDialog(null, "Purchased " + houses + " houses for " + curr.format(houses * d.getHouseCost()));
                // outputs how much it costed
            }
        } catch (NumberFormatException e) // if integer not inputted
        {
            JOptionPane.showMessageDialog(null, "Wrong format."); // outputs error
        }
        updateProperties(); // updates Properties
    }//GEN-LAST:event_btnBuyHouseActionPerformed

    /**
     * if the user click the save button
     *
     * @param evt - click the button
     */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String path = "";
        JFileChooser fileChooser = new JFileChooser(); //instantiate the JFileChooser
        try {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this); //show the dialog to save current program
            if (result == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath();
            }
            File file = new File(path + "/MonopolySave.txt"); //set the file path
            path = file.getAbsolutePath();
            if (!file.exists()) { //if the file doesnt not exist,
                if (file.createNewFile()) { //create a new file
                    JOptionPane.showMessageDialog(null, "File created and saved");
                }
            }

            FileOutputStream saving = new FileOutputStream(path); //instantiate FileOutputStream object
            ObjectOutput s = new ObjectOutputStream(saving); //instantiate ObjectOutput object in order to write the settings in object form
            s.writeInt(gameMode);
            s.writeInt(currentTurn);
            s.writeInt(numPlayers);
            s.writeObject(playerArray); //write all values in the file
        } catch (Exception e) {
            System.out.print(e);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    /**
     * if the user click the sell house button
     *
     * @param evt - click the button
     */
    private void btnSellHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSellHouseActionPerformed
        if (playerArray[0].getProperties().isEmpty()) { // checks if the player owns any properties
            JOptionPane.showMessageDialog(null, "You do not own any properties."); // outputs that they dont own any
            return; // stops the method
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to sell a house from?"); // if the player owns asks which property to sell a house from
        if (prop == null) {
            return;
        }
        int propNum = playerArray[0].findProperty(prop); // finds that property
        if (propNum == -1) { // if the property cant be found
            JOptionPane.showMessageDialog(null, "You do not own " + prop); // says you don't own it
            return;
        }
        Property p = ((Property) playerArray[0].getProperties().get(propNum)); // creates a variable to hold the property
        if (p.getPropType() != SpaceType.SPACE_DEED) { // checks to see if it is a deed or not
            JOptionPane.showMessageDialog(null, "This property cannot have houses property."); // if its not a deed it cant have a house
            return;
        }
        Deed d = (Deed) p; // makes a deed varialbe to hold the property
        if (d.getHouses() == 0) { // if the property has no houses
            JOptionPane.showMessageDialog(null, "This property has no houses."); // says the property has no houses
            return;
        } else {
            if (d.getHotel() == true) { // checks to see if the property has a hotel
                String input = JOptionPane.showInputDialog("This property has a hotel would you like to sell it? Y or N"); // asks if they want to sell it
                if (input.equalsIgnoreCase("Y")) {
                    d.sellHouse(); // sells the hotel
                    playerArray[0].addMoney(d.getHouseCost() / 2); // adds half the cost to the players money
                }
            } else {
                String input = JOptionPane.showInputDialog("How many houses would you like to sell. This property has " + d.getHouses() + " houses"); // says the amount of houses the property has
                int amountToSell = Integer.parseInt(input);
                if (amountToSell > d.getHouseCost()) { // checks to see if the input is greater than the amount it has
                    JOptionPane.showMessageDialog(null, "You don't have that many houses"); // outputs that you dont have that many houses
                } else {
                    for (int i = 0; i < amountToSell; i++) { // loops the amount of times that was inputted
                        d.sellHouse(); // sells house
                        playerArray[0].addMoney(d.getHouseCost() / 2); // adds money
                    }
                }
            }
        }
        if (playerArray[0].bankrupt == false) { // if player was bankrupt they no longer are
            btnRollDice.setEnabled(true);
        }
        updateProperties(); // updates the properties text field
    }//GEN-LAST:event_btnSellHouseActionPerformed

    /**
     * Method to bankrupt a computer player
     *
     * @param c computer player
     */
    private void bankruptComputer(Player c) {
        c.setMoney(0); //sets money to 0
        ArrayList<Property> bankruptP = c.getProperties(); //
        for (int i = 0; i < bankruptP.size(); i++) { // loops through the array
            bankruptP.get(i).setOwned(false); // sets all the properties as unowned
            c.removeProperties(); // removes all the properties from the player
        }
    }

    /**
     * Method to make a computer buy a house
     *
     * @param c computer player
     */
    private void buyHouseComputer(Player c) {
        if (!c.getProperties().isEmpty()) { // checks to see if the player owns a property
            ArrayList<Property> buyHouse = c.getProperties(); // gets the properties
            int randomProp = (int) (Math.random() * buyHouse.size()); // makes random number between 0 and the array size
            if (randomProp >= 1) { // if it is above 1
                randomProp -= 1; // removes 1
            }
            if (buyHouse.get(randomProp).propType == Space.SpaceType.SPACE_DEED) { // if the property is a deed
                Deed d = (Deed) buyHouse.get(randomProp); // makes a deed variable
                d.buyHouse(1); // buys 1 house
            }
        }
    }

    /**
     * if the user click roll dice button
     *
     * @param evt - clicking the button
     */
    private void btnRollDiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollDiceActionPerformed
        btnRollDice.setEnabled(false); // disables this button
        btnEndTurn.setEnabled(true); // enables end turn
        boolean rollAgain = true; // sets this boolean to true
        int i = 0; // moves the user has done
        while (rollAgain == true) { // while roll again is true
            rollAgain = playerTurn(playerArray[0], i); // roll again is equal to the player turn
            i++; // increases i
            updateProperties(); // updates the proerties text area
        }
    }//GEN-LAST:event_btnRollDiceActionPerformed

    /**
     * if the user click the end turn button
     *
     * @param evt - clicking the button
     */
    private void btnEndTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndTurnActionPerformed
        btnEndTurn.setEnabled(false); // sets end turn to false
        for (int i = 1; i < numPlayers; i++) { // loops the amount of computer players there are
            if (playerArray[i].bankrupt == false) { // if the player is not bankrupt
                computerTurn(playerArray[i], 0); //adds to the computer turn
                updateProperties(); // updates the properties
                int random = (int) (Math.random() * 10); // makes a random number
                if (random < 5 && playerArray[i].getMoney() > 300) { // 50% chance for the computer to buy a house
                    buyHouseComputer(playerArray[i]);
                }
                if (playerArray[i].bankrupt == true) { // if the computer is bankrupt
                    bankruptComputer(playerArray[i]); // calls the method
                }
            }
        }
        JOptionPane.showMessageDialog(null, "All players have played starting next turn"); // outputs that all the players have playerd
        btnRollDice.setEnabled(true); // enables the roll dice button
        currentTurn++; // increases current turn
        checkGameMode(); // checks if the game is over
        lblTurn.setText("Turn " + currentTurn); // changes the turn text
        if (playerArray[0].bankrupt == true) { // if the user is bankrupt
            btnRollDice.setEnabled(false); // disables the dice
            JOptionPane.showMessageDialog(null, "You are bankrupt sell your houses or mortage your properties to continue playing"); // tells them to sell something
        }
    }//GEN-LAST:event_btnEndTurnActionPerformed

    /**
     * if the user click the mortgage button
     *
     * @param evt - clicking the button
     */
    private void btnMortgageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMortgageActionPerformed
        if (playerArray[0].getProperties().isEmpty()) { // if the player does not own a property
            JOptionPane.showMessageDialog(null, "You do not own any properties."); // says that they dont own any properties
            return;
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to mortage?"); // akss which property the user would like to mortgage
        int propNum = playerArray[0].findProperty(prop); // checks if the user owns the property
        if (propNum == -1) { // if the property cant be found
            JOptionPane.showMessageDialog(null, "You do not own " + prop); // outputs that they dont own it
            return;
        }
        Property p = ((Property) playerArray[0].getProperties().get(propNum)); // variable to change the property into a deed
        Deed d = (Deed) p; // deed varialbe to hold the property
        if (d.getMortgage() == true) { // if the property is mortagaged already
            String input = JOptionPane.showInputDialog("This property has already been mortgaged would you like to unmortage it for $" + d.getMortgageValue() + " Y or N");
            if (input.equalsIgnoreCase("Y")) { // asks if they want to unmortgage it
                d.setMortgage(false); // sets mortage to false
                playerArray[0].removeMoney(d.getMortgageValue()); // takes away money
            }
        } else { // asks if they want to mortgage it
            String input = JOptionPane.showInputDialog("Would you like to mortgage this property worth $" + d.getMortgageValue() + " Y or N");
            if (input.equalsIgnoreCase("Y")) { // if yes
                d.setMortgage(true); // sets mortgage to true
                playerArray[0].addMoney(d.getMortgageValue()); // adds money
            }
        }
        if (playerArray[0].bankrupt == false) { // if player is no longer bankrupt
            btnRollDice.setEnabled(true); // enables the dice roll button
        }
        updateProperties(); // uptdates the properties text area
    }//GEN-LAST:event_btnMortgageActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Tile1;
    private javax.swing.JPanel Tile10;
    private javax.swing.JPanel Tile11;
    private javax.swing.JPanel Tile12;
    private javax.swing.JPanel Tile13;
    private javax.swing.JPanel Tile14;
    private javax.swing.JPanel Tile15;
    private javax.swing.JPanel Tile16;
    private javax.swing.JPanel Tile17;
    private javax.swing.JPanel Tile18;
    private javax.swing.JPanel Tile19;
    private javax.swing.JPanel Tile2;
    private javax.swing.JPanel Tile20;
    private javax.swing.JPanel Tile21;
    private javax.swing.JPanel Tile22;
    private javax.swing.JPanel Tile23;
    private javax.swing.JPanel Tile24;
    private javax.swing.JPanel Tile25;
    private javax.swing.JPanel Tile26;
    private javax.swing.JPanel Tile27;
    private javax.swing.JPanel Tile28;
    private javax.swing.JPanel Tile29;
    private javax.swing.JPanel Tile3;
    private javax.swing.JPanel Tile30;
    private javax.swing.JPanel Tile31;
    private javax.swing.JPanel Tile32;
    private javax.swing.JPanel Tile33;
    private javax.swing.JPanel Tile34;
    private javax.swing.JPanel Tile35;
    private javax.swing.JPanel Tile36;
    private javax.swing.JPanel Tile37;
    private javax.swing.JPanel Tile38;
    private javax.swing.JPanel Tile39;
    private javax.swing.JPanel Tile4;
    private javax.swing.JPanel Tile40;
    private javax.swing.JPanel Tile5;
    private javax.swing.JPanel Tile6;
    private javax.swing.JPanel Tile7;
    private javax.swing.JPanel Tile8;
    private javax.swing.JPanel Tile9;
    private javax.swing.JButton btnBuyHouse;
    private javax.swing.JButton btnEndTurn;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnMortgage;
    private javax.swing.JButton btnRollDice;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSellHouse;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDiceSum;
    public javax.swing.JLabel lblDie1;
    public javax.swing.JLabel lblDie2;
    private javax.swing.JLabel lblPlayer1;
    private javax.swing.JLabel lblPlayer2;
    private javax.swing.JLabel lblPlayer3;
    private javax.swing.JLabel lblPlayer4;
    private javax.swing.JLabel lblPlayerLocations;
    private javax.swing.JLabel lblPlayerStatistics;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JTextArea txaProperties;
    private javax.swing.JTextField txfJail;
    private javax.swing.JTextField txfProperty1;
    private javax.swing.JTextField txfProperty10;
    private javax.swing.JTextField txfProperty11;
    private javax.swing.JTextField txfProperty12;
    private javax.swing.JTextField txfProperty13;
    private javax.swing.JTextField txfProperty14;
    private javax.swing.JTextField txfProperty15;
    private javax.swing.JTextField txfProperty16;
    private javax.swing.JTextField txfProperty17;
    private javax.swing.JTextField txfProperty18;
    private javax.swing.JTextField txfProperty19;
    private javax.swing.JTextField txfProperty2;
    private javax.swing.JTextField txfProperty20;
    private javax.swing.JTextField txfProperty21;
    private javax.swing.JTextField txfProperty22;
    private javax.swing.JTextField txfProperty23;
    private javax.swing.JTextField txfProperty24;
    private javax.swing.JTextField txfProperty25;
    private javax.swing.JTextField txfProperty26;
    private javax.swing.JTextField txfProperty27;
    private javax.swing.JTextField txfProperty28;
    private javax.swing.JTextField txfProperty3;
    private javax.swing.JTextField txfProperty4;
    private javax.swing.JTextField txfProperty5;
    private javax.swing.JTextField txfProperty6;
    private javax.swing.JTextField txfProperty7;
    private javax.swing.JTextField txfProperty8;
    private javax.swing.JTextField txfProperty9;
    private javax.swing.JLabel txtFreeParking1;
    private javax.swing.JLabel txtFreeParking2;
    private javax.swing.JLabel txtGO;
    private javax.swing.JLabel txtGOArrow;
    private javax.swing.JLabel txtGoToJail1;
    private javax.swing.JLabel txtGoToJail2;
    private javax.swing.JLabel txtJustVisting;
    private javax.swing.JLabel txtTile18label1;
    private javax.swing.JLabel txtTile18label2;
    private javax.swing.JLabel txtTile23label1;
    private javax.swing.JLabel txtTile23label2;
    private javax.swing.JLabel txtTile34label1;
    private javax.swing.JLabel txtTile34label2;
    private javax.swing.JLabel txtTile37label1;
    private javax.swing.JLabel txtTile37label2;
    private javax.swing.JLabel txtTile39label1;
    private javax.swing.JLabel txtTile39label2;
    private javax.swing.JLabel txtTile3label1;
    private javax.swing.JLabel txtTile3label2;
    private javax.swing.JLabel txtTile5label1;
    private javax.swing.JLabel txtTile5label2;
    private javax.swing.JLabel txtTile8label1;
    private javax.swing.JLabel txtTile8label2;
    // End of variables declaration//GEN-END:variables
}

/**
 * This class is to turn the background music of the game screen
 */
class GameMusic implements Runnable {

    private Clip gameSong;

    /**
     * run this method in a thread
     */
    @Override
    public void run() {
        try {
            gameSong = AudioSystem.getClip();
            InputStream bufferedIn = new BufferedInputStream(MainMusic.class.getResourceAsStream("saves/Slow_Burn.wav"));
            //Slow Burn by spinningmerkaba (c) copyright 2021 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/jlbrock44/64461 Ft: Admiral Bob
            AudioInputStream inputBgm = AudioSystem.getAudioInputStream(bufferedIn); //get music from the file
            gameSong.open(inputBgm);
            gameSong.loop(Clip.LOOP_CONTINUOUSLY); //loop infinitely
            gameSong.start(); //start the music

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * turn the music off
     */
    public void musicOff() {
        gameSong.stop();
    }
}

/**
 * Rolling class which is a subclass of TimerTask class. This is for rolling the
 * dice.
 */
class Rolling extends TimerTask {

    private int sum = 0;
    GameScreen gs;
    int dice1;
    int dice2;

    /**
     * primary constructor
     *
     * @param gameScreen - Game Screen
     */
    public Rolling(GameScreen gameScreen) {
        gs = gameScreen;
    }

    /**
     * process for every interval of the timer
     */
    @Override
    public void run() {
        if (gs.stopRoll) { //it stops rolling when the user stopRoll boolean variable becomes true
            sum = dice1 + dice2 + 2; //calculate the sum
            gs.moves = sum;
            gs.timerRoll.cancel(); //stop the timer
            return;
        }
        dice1 = (int) (Math.random() * 6); //choose the random number between 1-6
        dice2 = (int) (Math.random() * 6); //choose the random number between 1-6

        gs.lblDie1.setIcon(gs.getDiceImage(dice1));
        gs.lblDie2.setIcon(gs.getDiceImage(dice2));
    }

    /**
     * check if the dice is double
     *
     * @return - boolean value whether or not die1 and die2 have the same value
     */
    public boolean isDoubleDice() {
        return dice1 == dice2;
    }

}
