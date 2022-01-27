/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;

import java.io.*;
import java.util.*;
import andrewcoledaniel_monopoly.Card.*;
import java.awt.Image;
import java.awt.event.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import andrewcoledaniel_monopoly.Space.SpaceType;
import andrewcoledaniel_monopoly.Card.CardType;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;

/**
 *
 * @author dakim0069
 */
public class GameScreen extends javax.swing.JFrame {

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
    public int moves;
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

    private void diceImage() {
        Image img;
        URL url0 = GameScreen.class.getResource("saves/dice1.jpg");
        Die[0] = new ImageIcon(url0);
        img = Die[0].getImage();
        Die[0] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        URL url1 = GameScreen.class.getResource("saves/dice2.jpg");
        Die[1] = new ImageIcon(url1);
        img = Die[1].getImage();
        Die[1] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        URL url2 = GameScreen.class.getResource("saves/dice3.jpg");
        Die[2] = new ImageIcon(url2);
        img = Die[2].getImage();
        Die[2] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        URL url3 = GameScreen.class.getResource("saves/dice4.jpg");
        Die[3] = new ImageIcon(url3);
        img = Die[3].getImage();
        Die[3] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        URL url4 = GameScreen.class.getResource("saves/dice5.jpg");
        Die[4] = new ImageIcon(url4);
        img = Die[4].getImage();
        Die[4] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        URL url5 = GameScreen.class.getResource("saves/dice6.jpg");
        Die[5] = new ImageIcon(url5);
        img = Die[5].getImage();
        Die[5] = new ImageIcon(img.getScaledInstance(lblDie1.getWidth(), lblDie1.getHeight(), Image.SCALE_FAST));

        lblDie1.setIcon(Die[0]);
        lblDie2.setIcon(Die[0]);
    }

    public ImageIcon getDiceImage(int index) {
        return Die[index];
    }

    private boolean checkGameMode() {
        if (gameMode != 2) {
            if (gameMode == 0) {
                if (currentTurn > mainMenu.limitedTurns) {
                    endGame();
                    return false;
                }
            } else {
                long currentTime = System.currentTimeMillis();
                currentTime -= startTime;
                System.out.println(currentTime);
                if (currentTime > mainMenu.limitedTime) {
                    endGame();
                    return false;
                }
            }
        } else{
            int bankruptPlayers = 0;
            for(int i = 0; i < playerArray.length; i ++){
                if(playerArray[i].bankrupt == false){
                    bankruptPlayers ++;
                }
            }
            if(bankruptPlayers >= 3){
                endGame();
                return false;
            }
        }
        return true;
    }

    private void endGame() {
        Player winner = new Player(1);
        for (int i = 0; i < playerArray.length; i++) {
            if (playerArray[i].getMoney() > winner.getMoney()) {
                winner = playerArray[i];
            }
        }
        
        if(winner == playerArray[0]) //if the user is the winner
        {
            boolean isHighScore = checkHighscore(winner.getMoney()); //check if it is the highscore
        }
        if (endingScreen == null) {
            endingScreen = new EndingScreen(winner.getPlayerNumber(), playerArray);
        }
        this.setVisible(false);
        endingScreen.setVisible(true);
    }
    
    private boolean checkHighscore(int score)
    {
        if(score < mainMenu.highscores[4])
        {
            return false;
        }
        int index = 0;
        for(int i=4; i>=0; i--)
        {
            if(score > mainMenu.highscores[i])
            {
                index = i;
            }
        }
        for(int k=4; k>index; k--)
        {
            mainMenu.highscores[k] = mainMenu.highscores[k-1];
        }
        mainMenu.highscores[index] = score;
        return true;
    }

    private void loadCards() {
        int index = 0;
        CardType type;
        CardAction action;
        int value;
        String info;

        InputStream in = GameScreen.class.getResourceAsStream("saves/cards.txt");
        try {
            Scanner s = new Scanner(in);
            while (s.hasNextLine()) {
                type = Card.CardType.valueOf(s.nextLine());
                action = Card.CardAction.valueOf(s.nextLine());
                value = Integer.parseInt(s.nextLine());
                info = s.nextLine();
                cards[index] = new Card(type, action, value, info);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not load cards from file");
        }
        board.shuffleCards(cards);
    }
    private void generatePlayers() {
        for (int i = 0; i < playerArray.length; i++) {
            playerArray[i] = new Player(i + 1, 1500);
        }
    }

    private boolean playerTurn(Player p, int turn) {
        int response;
        int newPos;

        if (turn > 3) {
            p.setPosition(10);
            updateLocations();
            p.setJail(true);
            return false;
        }
        if (p.getJail()) {
            if (p.getJailCards() > 0) {
                Object[] options = {"Roll for doubles", "Pay $50", "Use get out of jail free card"};
                response = JOptionPane.showOptionDialog(null, "You are in Jail. What would you like to do?", "In Jail", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            } else {
                Object[] options = {"Roll for doubles", "Pay $50"};
                response = JOptionPane.showOptionDialog(null, "You are in Jail. What would you like to do?", "In Jail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            }
            switch (response) {
                case 0:
                    rollDice();
                    JOptionPane.showMessageDialog(null, "Click to stop");
                    stopRolling();
                    if (((Rolling) tsk).isDoubleDice()) {
                        JOptionPane.showMessageDialog(null, "You rolled doubles. Get out of jail.");
                        p.setJail(false);
                        p.setPosition(10 + moves);
                        updateLocations();
                    } else {
                        JOptionPane.showMessageDialog(null, "You didn't roll doubles. Stay in jail");
                    }
                    break;
                case 1:
                    p.removeMoney(50);
                    p.setJail(false);
                    break;
                case 2:
                    p.setJailCards(p.getJailCards() - 1);
                    p.setJail(false);
                    break;
            }

            if (p.getJail()) {
                p.setTurnsInJail(p.getTurnsInJail() + 1);
                return false;
            }
            p.setTurnsInJail(0);

        }
        rollDice();
        JOptionPane.showMessageDialog(null, "Click to stop");
        stopRolling();

        newPos = p.getPosition() + moves;
        if (newPos >= 40) {
            p.addMoney(200);
            newPos -= 40;
        }
        p.setPosition(newPos);
        updateLocations();
        handleSpace(board.getSpace(newPos), p);
        updateProperties();
        return ((Rolling) tsk).isDoubleDice();
    }

    private void computerTurn(Player p, int turn) {

        if (turn >= 3) {
            p.setJail(true);
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
        JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + "'s turn");
        if (p.getJail()) {
            if (p.getJailCards() < 0) {
                int randomNumber = (int) (Math.random() * 10);
                if (randomNumber > 5 && p.getMoney() > 50) {
                    p.removeMoney(50);
                    p.setJail(false);
                } else {
                    int roll1 = (int) (Math.random() * 6) + 1;
                    int roll2 = (int) (Math.random() * 6) + 1;
                    if (roll1 == roll2) {
                        p.setJail(false);
                    }
                }
            }
        } else {
            int newPos;
            int diceRoll = (int) (Math.random() * 6) + 1;
            int diceRoll2 = (int) (Math.random() * 6) + 1;
            newPos = p.getPosition() + diceRoll + diceRoll2;
            if (newPos >= 40) {
                p.addMoney(200);
                newPos -= 40;
            }
            p.setPosition(newPos);
            updateLocations();
            handleSpace(board.getSpace(newPos), p);
            if (diceRoll == diceRoll2) {
                JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + " has rolled a double");
                turn++;
                computerTurn(p, turn);
            }
        }
        updateProperties();
    }

    private void handleSpace(Space s, Player p) {
        SpaceType st = s.getType();
        JOptionPane.showMessageDialog(null, s.getName());
        switch (st) {
            case SPACE_CORNER:
                ((CornerSpace) s).performSpaceAction(p);
                updateLocations();
                break;
            case SPACE_PROPERTY:
                handleProperty((Property)s, p);
                break;
            case SPACE_CARD:
                 Card c = ((CardSpace)s).getCard(cards);
                 String out = ((CardSpace)s).performSpaceAction(c, p, board, playerArray);
                 updateLocations();
                 JOptionPane.showMessageDialog(null, out);
                 if (board.getSpace(p.getPosition()).getType() == SpaceType.SPACE_PROPERTY) {
                     handleProperty((Property)board.getSpace(p.getPosition()), p);
                 }
        }
    }

    private void handleProperty(Property prop, Player p) {
        int option;
        if (p.getPlayerNumber() == 1) {
            if (!prop.getOwned()) {
                option = JOptionPane.showConfirmDialog(null, ((Space) prop).getName() + " is not owned. Would you like to purchase it?", "Choice", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    p.buyProperty(prop);
                } else {
                    auction(prop);
                }
            } else {
                JOptionPane.showMessageDialog(null, "This property is owned by player " + prop.getOwner() + ". You must pay them $" + prop.getRent() + ".");
                p.removeMoney(prop.getRent());
                prop.getOwner().addMoney(prop.getRent());
            }
        } else {
            if (!prop.getOwned()) {
                int randomNumber = (int) (Math.random() * 10);
                if (randomNumber < 7 && p.getMoney() > prop.getPrice()) {
                    p.buyProperty(prop);
                    updateProperties();
                } else {
                    auction(prop);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Player " + p.getPlayerNumber() + " has landed on " + prop.getName() + " and has paid player" + prop.getOwner().getPlayerNumber() + " $" + prop.getRent());
                p.removeMoney(prop.getRent());
                prop.getOwner().addMoney(prop.getRent());
            }
        }
    }

    private void auction(Property p) {
        boolean bought = false;
        int currentBid = 0;
        int response;
        int lastBidder = -1;
        double leavePer = 1;
        ArrayList<Player> players = new ArrayList();

        for (int i = 0; i < numPlayers; i++) {
            players.add(playerArray[i]);
        }

        while (!bought) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getMoney() <= currentBid) {
                    players.remove(i);
                }

                if (players.size() == 1) {
                    bought = true;
                    break;
                }
                response = -1;
                if (i == 0) {
                    while (response < 0) {
                        try {
                            response = Integer.parseInt(JOptionPane.showInputDialog("The current bid is $" + curr.format(currentBid) + ". How much more would you like to bid? (Type 0 to cancel)"));
                            if (response == 0) {
                                players.remove(i);
                                JOptionPane.showMessageDialog(null, "Player 1 left the auction");
                            }
                            if (response <= currentBid) {
                                JOptionPane.showMessageDialog(null, "Please input a bid that is greater than the current bid.");
                                response = -1;
                            } else if (response > players.get(i).getMoney()) {
                                JOptionPane.showMessageDialog(null, "Please bid an amount of money that you have.");
                                response = -1;
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount");
                        }
                    }
                    currentBid = response;
                    lastBidder = players.get(i).getPlayerNumber();
                } else {
                    while (response < 0) {
                        if (currentBid - p.getPrice() > 100) {
                            leavePer = 0.5;
                            response = (int) ((Math.random() * 9) + 1);
                        } else if (currentBid >= p.getPrice()) {
                            leavePer = 0.1;
                            response = (int) ((Math.random() * 50) + 1);
                        } else if (p.getPrice() - currentBid <= 50) {
                            response = (int) ((Math.random() * 70) + 1);
                        } else {
                            response = (int) ((Math.random() * 100) + 1);
                        }

                        if (currentBid + response > players.get(i).getMoney()) {
                            response = -1;
                        }
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "Thread.sleep method error");
                    }

                    if (Math.random() > leavePer) {
                        JOptionPane.showMessageDialog(null, "Player " + players.get(i).getPlayerNumber() + " left the auction");
                        players.remove(i);
                    } else {
                        JOptionPane.showMessageDialog(null, "Player " + players.get(i).getPlayerNumber() + " bid $" + curr.format(currentBid + response));
                        currentBid += response;
                        lastBidder = players.get(i).getPlayerNumber();
                    }
                }
            }
        }
        
        JOptionPane.showMessageDialog(null, "Player " + lastBidder + " purchased the property for $" + curr.format(currentBid));
        playerArray[lastBidder].buyProperty(p);
    }

    private void rollDice() {
        tsk = new Rolling(this);
        timerRoll = new Timer();
        timerRoll.scheduleAtFixedRate(tsk, 125, 145);
    }

    public void stopRolling() {

        stopRoll = true;

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Thread.sleep method error");
        }

        lblDiceSum.setText("Moves: " + moves);
        stopRoll = false;
    }
    
    private void updateProperties() {
        txaProperties.setText("");
        for (int i = 0; i < playerArray.length; i++) {
            txaProperties.append("Player " + (i + 1) + ":\n");
            txaProperties.append("Money: " + playerArray[i].getMoney() + "\nProperties:\n");
            try {
                txaProperties.append(playerArray[i].propertyNames() + "\n");
            } catch (NullPointerException e) {
                txaProperties.append("No Properties owned.\n");
            }
        }
    }
    
    private void updateLocations() {
        lblPlayer1.setText("Player " + 1 + ": " + board.getSpace(playerArray[0].getPosition()).getName());
        lblPlayer2.setText("Player " + 2 + ": " + board.getSpace(playerArray[1].getPosition()).getName());
        lblPlayer3.setText("Player " + 3 + ": " + board.getSpace(playerArray[2].getPosition()).getName());
        lblPlayer4.setText("Player " + 4 + ": " + board.getSpace(playerArray[3].getPosition()).getName());
    }

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
        lblBank = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaProperties = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaBankProperties = new javax.swing.JTextArea();
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
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
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

        lblBank.setBackground(new java.awt.Color(255, 255, 255));
        lblBank.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblBank.setText("Bank Properties");

        txaProperties.setEditable(false);
        txaProperties.setColumns(20);
        txaProperties.setRows(5);
        jScrollPane2.setViewportView(txaProperties);

        txaBankProperties.setEditable(false);
        txaBankProperties.setColumns(20);
        txaBankProperties.setRows(5);
        jScrollPane3.setViewportView(txaBankProperties);

        btnRollDice.setText("Roll Dice");
        btnRollDice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollDiceActionPerformed(evt);
            }
        });

        btnEndTurn.setText("EndTurn");
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
                    .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPlayerStatistics)
                        .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                            .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblBank))
                    .addGroup(pnlStatusLayout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatusLayout.createSequentialGroup()
                        .addComponent(btnRollDice, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEndTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTurn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPlayerStatistics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBank, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEndTurn, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(btnRollDice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(pnlStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 0, -1, 875));

        lblDie2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie2.setText("lblDie2");
        getContentPane().add(lblDie2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 222, 80, 80));

        lblDie1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie1.setText("lblDie1");
        getContentPane().add(lblDie1, new org.netbeans.lib.awtextra.AbsoluteConstraints(246, 222, 80, 80));

        lblDiceSum.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDiceSum.setText("Moves: ");
        getContentPane().add(lblDiceSum, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 200, 66, -1));

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

        lblPlayer1.setText("jLabel1");
        getContentPane().add(lblPlayer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 120, -1));

        lblPlayer2.setText("jLabel1");
        getContentPane().add(lblPlayer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 120, -1));

        lblPlayer3.setText("jLabel1");
        getContentPane().add(lblPlayer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, 120, -1));

        lblPlayer4.setText("jLabel1");
        getContentPane().add(lblPlayer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 490, 130, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        bgm.musicOff();
        mainMenu.setVisible(true);
        mainMenu.mainBgm.musicOn();
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

    }//GEN-LAST:event_formComponentShown

    private void btnBuyHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyHouseActionPerformed
        if (playerArray[0].getProperties().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You do not own any properties.");
            return;
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to buy a house on?");
        int propNum = playerArray[0].findProperty(prop);
        if (propNum == -1) {
            JOptionPane.showMessageDialog(null, "You do not own " + prop);
            return;
        }
        Property p = ((Property)playerArray[0].getProperties().get(propNum));
        if (p.getPropType() != SpaceType.SPACE_DEED) {
            JOptionPane.showMessageDialog(null, "You cannot purchase a house on this property.");
            return;
        }
        Deed d = (Deed)p;
        if (d.getHouses() == 4) {
            if(d.getHotel() == false){
            if (JOptionPane.showConfirmDialog(null, "You already have 4 houses on this property. Would you like to buy a hotel instead?", "Buy Hotel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (playerArray[0].getMoney() < d.getHouseCost()) {
                    JOptionPane.showMessageDialog(null, "You don't have enough money.");
                } else {
                    d.getHotel();
                    playerArray[0].removeMoney(d.getHouseCost());
                }
                return;
                }
            }
        }
        int houses = Integer.parseInt(JOptionPane.showInputDialog("How many houses would you like to buy? (Maximum of four per property. This one currently has: " + d.getHouses()));
        if (houses + d.getHouses() > 4) {
            JOptionPane.showMessageDialog(null, "You can not have more than four houses on a property.");
        } else if (houses * d.getHouseCost() > playerArray[0].getMoney()) {
            JOptionPane.showMessageDialog(null, "You don't have enough money to buy " + houses + " houses.");
        } else {
            d.buyHouse(houses);
            JOptionPane.showMessageDialog(null, "Purchased " + houses + " houses for " + curr.format(houses * d.getHouseCost()));
        }
    }//GEN-LAST:event_btnBuyHouseActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        try {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath();
            }
            File file = new File(path + "/MonopolySave.txt");
            System.out.println(file.getAbsolutePath());
            if (!file.exists()) {
                if (file.createNewFile()) {
                    JOptionPane.showMessageDialog(null, "File created and saved");
                }
            }

            FileOutputStream saving = new FileOutputStream(System.getProperty(file.getAbsolutePath()));
            ObjectOutput s = new ObjectOutputStream(saving);
            s.writeInt(gameMode);
            s.writeInt(currentTurn);
            s.writeInt(numPlayers);
            s.writeObject(playerArray);
        } catch (Exception e) {
            System.out.print(e);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSellHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSellHouseActionPerformed
        if (playerArray[0].getProperties().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You do not own any properties.");
            return;
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to sell a house from?");
        int propNum = playerArray[0].findProperty(prop);
        if (propNum == -1) {
            JOptionPane.showMessageDialog(null, "You do not own " + prop);
            return;
        }
        Property p = ((Property) playerArray[0].getProperties().get(propNum));
        if (p.getPropType() != SpaceType.SPACE_DEED) {
            JOptionPane.showMessageDialog(null, "This property cannot have houses property.");
            return;
        }
        Deed d = (Deed) p;
        if (d.getHouses() == 0) {
            JOptionPane.showMessageDialog(null, "This property has no houses.");
            return;
        } else {
            if (d.getHotel() == true) {
                String input = JOptionPane.showInputDialog("This property has a hotel would you like to sell it? Y or N");
                if (input.equalsIgnoreCase("Y")) {
                    d.sellHouse();
                    playerArray[0].addMoney(d.getHouseCost() / 2);
                }
            } else {
                String input = JOptionPane.showInputDialog("How many houses would you like to sell. This property has " + d.getHouses() + " houses");
                int amountToSell = Integer.parseInt(input);
                if (amountToSell > d.getHouseCost()) {
                    JOptionPane.showMessageDialog(null, "You don't have that many houses");
                } else {
                    for (int i = 0; i < amountToSell; i++) {
                        d.sellHouse();
                        playerArray[0].addMoney(d.getHouseCost() / 2);
                    }
                }
            }
        }
        if(playerArray[0].bankrupt == false){
            btnRollDice.setEnabled(true);
        }
    }//GEN-LAST:event_btnSellHouseActionPerformed


    private void bankruptComputer(Player c){
        c.setMoney(0);
        ArrayList<Property> bankruptP = c.getProperties();
        for(int i = 0; i < bankruptP.size(); i ++){
            bankruptP.get(i).setOwned(false);
        }
    }
    
    private void buyHouseComputer(Player c){
        ArrayList<Property> buyHouse = c.getProperties();
        int randomProp = (int) (Math.random() * buyHouse.size()) + 1;
        if(buyHouse.get(randomProp).propType == Space.SpaceType.SPACE_DEED){
            Deed d = (Deed)buyHouse.get(randomProp);
            d.buyHouse(1);
        }
    }
    
    private void btnRollDiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollDiceActionPerformed
        btnRollDice.setEnabled(false);
        btnEndTurn.setEnabled(true);
        boolean rollAgain = true;
        int i = 0;
        while (rollAgain == true) {
            rollAgain = playerTurn(playerArray[0], i);
            i++;
            updateProperties();
        }
    }//GEN-LAST:event_btnRollDiceActionPerformed

    private void btnEndTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndTurnActionPerformed
        btnEndTurn.setEnabled(false);
        for (int i = 1; i < numPlayers; i++) {
            if (playerArray[i].bankrupt == false) {
                computerTurn(playerArray[i], 0);
                updateProperties();
                int random = (int) (Math.random() * 10);
                if(random < 5 && playerArray[i].getMoney() > 300){
                    buyHouseComputer(playerArray[i]);
                }
                if (playerArray[i].bankrupt == true) {
                    bankruptComputer(playerArray[i]);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "All players have played starting next turn");
        btnRollDice.setEnabled(true);
        currentTurn++;
        checkGameMode();
        lblTurn.setText("Turn " + currentTurn);
        if (playerArray[0].bankrupt == true) {
            btnRollDice.setEnabled(false);
            JOptionPane.showMessageDialog(null, "You are bankrupt sell your houses or mortage your properties to continue playing");
        }
    }//GEN-LAST:event_btnEndTurnActionPerformed

    private void btnMortgageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMortgageActionPerformed
        if (playerArray[0].getProperties().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You do not own any properties.");
            return;
        }
        String prop = JOptionPane.showInputDialog("Which property would you like to mortage?");
        int propNum = playerArray[0].findProperty(prop);
        if (propNum == -1) {
            JOptionPane.showMessageDialog(null, "You do not own " + prop);
            return;
        }
        Property p = ((Property) playerArray[0].getProperties().get(propNum));
        Deed d = (Deed) p;
        if(d.getMortgage() == true){
            String input = JOptionPane.showInputDialog("This property has already been mortgaged would you like to unmortage it for $" + d.getMortgage() + " Y or N");
            if(input.equalsIgnoreCase("Y")){
                d.setMortgage(false);
                playerArray[0].removeMoney(d.getMortgageValue());
            }
        } else{
            String input = JOptionPane.showInputDialog("Would you like to mortgage this property worth $" + d.getMortgageValue() + " Y or N");
            if(input.equalsIgnoreCase("Y")){
                d.setMortgage(true);
                playerArray[0].addMoney(d.getMortgageValue());
            }
        }
        if(playerArray[0].bankrupt == false){
            btnRollDice.setEnabled(true);
        }
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBank;
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
    private javax.swing.JTextArea txaBankProperties;
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

class GameMusic implements Runnable {

    private Clip gameSong;

    @Override
    public void run() {
        try {
            gameSong = AudioSystem.getClip();
            //Slow Burn by spinningmerkaba (c) copyright 2021 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/jlbrock44/64461 Ft: Admiral Bob
            AudioInputStream inputBgm = AudioSystem.getAudioInputStream(GameMusic.class.getResourceAsStream("saves/Slow_Burn.wav"));
            gameSong.open(inputBgm);
            gameSong.loop(Clip.LOOP_CONTINUOUSLY);
            gameSong.start();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void musicOff() {
        gameSong.stop();
    }
}

class Rolling extends TimerTask {

    private int sum = 0;
    GameScreen gs;
    int dice1;
    int dice2;

    public Rolling(GameScreen gameScreen) {
        gs = gameScreen;
    }

    @Override
    public void run() {
        if (gs.stopRoll) {
            sum = dice1 + dice2 + 2;
            gs.moves = sum;
            gs.timerRoll.cancel();
            return;
        }
        dice1 = (int) (Math.random() * 6);
        dice2 = (int) (Math.random() * 6);
        
        gs.lblDie1.setIcon(gs.getDiceImage(dice1));
        gs.lblDie2.setIcon(gs.getDiceImage(dice2));
    }

    
    public boolean isDoubleDice()
    {
        return dice1 == dice2;
    }

}
