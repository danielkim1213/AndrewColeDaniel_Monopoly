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
    public static int[] roll = new int[2];
    
    /**
     * Creates new form GameScreen
     * @param m - main menu
     * @param gameMode - game mode
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
        startTime = System.currentTimeMillis() * 1000;
        currentTurn = 0;
        board = new Board();
        loadCards();
        generatePlayers();
    }
    
    
    private void diceImage()
    {
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
        
        lblDie1.setIcon(Die[2]);
        lblDie2.setIcon(Die[3]);
    }
    
    public ImageIcon getDiceImage(int index)
    {
        return Die[index];
    }
    
        
    
    public void checkGameMode() {
        if (gameMode != 2) {
            if (gameMode == 0) {
                if(currentTurn > MainMenu.limitedTurns){
                    endGame();
                }
            } else{
                long currentTime = System.currentTimeMillis() * 1000;
                if(currentTime - startTime >= MainMenu.limitedTime){
                    endGame();
                }
            }
        }
    }

    private void endGame(){
        if(endingScreen == null){
            endingScreen = new EndingScreen(numPlayers);
        }
        this.setVisible(false);
        endingScreen.setVisible(true);
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
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Could not load cards from file");
        }
    }
   
    private void generatePlayers(){
        for(int i = 0; i < playerArray.length; i ++){
            playerArray[i] = new Player(i + 1, 1500);
        }
    }
    
    private void playGame() {
           boolean again;
           int turn = 0;
           do {
               again = playerTurn(playerArray[0], turn);
               turn++;
           } while (again);
           
           for (int i = 1; i < numPlayers; i++) {
               computerTurn(i);
           }
    }
    
    private boolean playerTurn(Player p, int turn) {
        int response;
        int newPos;
        
        if (turn == 3) {
            p.setPosition(10);
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
                    break; // roll for doubles
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
                return false;
            }
            
        }
        rollDice();
        newPos = p.getPosition() + roll[0] + roll[1];
        if (newPos >= 40) {
            p.addMoney(200);
            newPos -= 40;
        }
        p.setPosition(newPos);
        handleSpace(board.getSpace(newPos), p);
        if (roll[0] == roll[1]) {
            return true;
        }
        return false;
    }
    
    
    private void computerTurn(int computerIndex){
        Player computer = playerArray[computerIndex];
        rollDice();
        
        new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            stopRoll = true;
                            Thread.sleep(160);
                            stopRoll = false;
                            lblDiceSum.setText("Moves: " + moves);
                        } catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                }, 
                1500 
            );
    }
    
    private void handleSpace(Space s, Player p) {
        SpaceType st = s.getType();
        switch (st) {
            case SPACE_CORNER:
                ((CornerSpace)s).performSpaceAction(p);
                break;
            case SPACE_PROPERTY:
                handleProperty((Property)s, p);
            case SPACE_CARD:
                 Card c = ((CardSpace)s).getCard(cards);
                 String out = ((CardSpace)s).performSpaceAction(c, p);
                 JOptionPane.showMessageDialog(null, out);
                 break;
        }
    }
    
    private void handleProperty(Property prop, Player p) {
        int option;
        if (prop.getOwner() == null) {
            option = JOptionPane.showConfirmDialog(null, ((Space)p).getName() + " is not owned. Would you like to purchase it?", "Choice", JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                p.buyProperty(prop);
            } else {
                // TODO: Auction
            }
        } else {
            JOptionPane.showMessageDialog(null, "This property is owned by player " + prop.getOwner() + ". You must pay them $" + prop.getRent() + ".");
            p.removeMoney(prop.getRent());
            prop.getOwner().addMoney(prop.getRent());
        }
    }
    
    private void rollDice() 
    {
        TimerTask tsk = new Rolling(this);
        timerRoll = new Timer(); 
        timerRoll.scheduleAtFixedRate(tsk, 125, 150);
        
    }
    
    private void updateProperties()
    {
        txaProperties.setText("");
        for(int i=0; i<playerArray.length; i++)
        {
            txaProperties.append("Player " + (i+1) + ":\n");
            try{
                txaProperties.append(playerArray[i].getProperties().toString() + "\n");
            } catch (NullPointerException e)
            {
                txaProperties.append("No Properties owned.\n");
            }
        }
    }
    
        private void generateProperties(){
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
        lblProperties = new javax.swing.JLabel();
        btnBuyHouse = new javax.swing.JButton();
        btnSellHouse = new javax.swing.JButton();
        btnMortgage = new javax.swing.JButton();
        btnMortgage1 = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        lblBank = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaProperties = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaBankProperties = new javax.swing.JTextArea();
        lblDie2 = new javax.swing.JLabel();
        lblDie1 = new javax.swing.JLabel();
        btnStop = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        pnlStatus.setBackground(new java.awt.Color(204, 255, 204));

        lblTurn.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTurn.setText("Turn 1");

        lblProperties.setBackground(new java.awt.Color(255, 255, 255));
        lblProperties.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblProperties.setText("Properties");

        btnBuyHouse.setText("Buy House");
        btnBuyHouse.setPreferredSize(new java.awt.Dimension(90, 50));
        btnBuyHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyHouseActionPerformed(evt);
            }
        });

        btnSellHouse.setText("Sell House");
        btnSellHouse.setPreferredSize(new java.awt.Dimension(90, 50));

        btnMortgage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnMortgage.setText("Mortgage Property");

        btnMortgage1.setText("Save");
        btnMortgage1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMortgage1ActionPerformed(evt);
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

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProperties)
                        .addGroup(pnlStatusLayout.createSequentialGroup()
                            .addComponent(btnMortgage1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                            .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblBank))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTurn)
                .addGap(79, 79, 79))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTurn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProperties)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMortgage1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        lblDie2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie2.setText("lblDie2");

        lblDie1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDie1.setText("lblDie1");

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        lblDiceSum.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDiceSum.setText("Moves: ");

        Tile1.setBackground(new java.awt.Color(220, 255, 196));
        Tile1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile1.setPreferredSize(new java.awt.Dimension(100, 100));

        txtGO.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGO.setText("GO");

        txtGOArrow.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGOArrow.setForeground(new java.awt.Color(255, 0, 0));
        txtGOArrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGOArrow.setText("<-----");

        javax.swing.GroupLayout Tile1Layout = new javax.swing.GroupLayout(Tile1);
        Tile1.setLayout(Tile1Layout);
        Tile1Layout.setHorizontalGroup(
            Tile1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGOArrow, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile1Layout.setVerticalGroup(
            Tile1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtGO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGOArrow)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        Tile2.setBackground(new java.awt.Color(220, 255, 196));
        Tile2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile2.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty1.setEditable(false);
        txfProperty1.setBackground(new java.awt.Color(234, 221, 202));
        txfProperty1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty1.setText("Property 1");
        txfProperty1.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile2Layout = new javax.swing.GroupLayout(Tile2);
        Tile2.setLayout(Tile2Layout);
        Tile2Layout.setHorizontalGroup(
            Tile2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile2Layout.setVerticalGroup(
            Tile2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile2Layout.createSequentialGroup()
                .addComponent(txfProperty1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile3.setBackground(new java.awt.Color(220, 255, 196));
        Tile3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile3.setPreferredSize(new java.awt.Dimension(75, 100));

        txtTile3label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile3label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile3label1.setText("Community");

        txtTile3label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile3label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile3label2.setText("Chest");

        javax.swing.GroupLayout Tile3Layout = new javax.swing.GroupLayout(Tile3);
        Tile3.setLayout(Tile3Layout);
        Tile3Layout.setHorizontalGroup(
            Tile3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTile3label1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
            .addGroup(Tile3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTile3label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Tile3Layout.setVerticalGroup(
            Tile3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtTile3label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile3label2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        Tile4.setBackground(new java.awt.Color(220, 255, 196));
        Tile4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile4.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty2.setEditable(false);
        txfProperty2.setBackground(new java.awt.Color(234, 221, 202));
        txfProperty2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty2.setText("Property 2");
        txfProperty2.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile4Layout = new javax.swing.GroupLayout(Tile4);
        Tile4.setLayout(Tile4Layout);
        Tile4Layout.setHorizontalGroup(
            Tile4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile4Layout.setVerticalGroup(
            Tile4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile4Layout.createSequentialGroup()
                .addComponent(txfProperty2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile5.setBackground(new java.awt.Color(220, 255, 196));
        Tile5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile5.setPreferredSize(new java.awt.Dimension(75, 100));

        txtTile5label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile5label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile5label1.setText("Income");

        txtTile5label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile5label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile5label2.setText("Tax");

        javax.swing.GroupLayout Tile5Layout = new javax.swing.GroupLayout(Tile5);
        Tile5.setLayout(Tile5Layout);
        Tile5Layout.setHorizontalGroup(
            Tile5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTile5label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Tile5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTile5label2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );
        Tile5Layout.setVerticalGroup(
            Tile5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtTile5label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile5label2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        Tile6.setBackground(new java.awt.Color(220, 255, 196));
        Tile6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile6.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty3.setEditable(false);
        txfProperty3.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty3.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty3.setText("Railroad 1");
        txfProperty3.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile6Layout = new javax.swing.GroupLayout(Tile6);
        Tile6.setLayout(Tile6Layout);
        Tile6Layout.setHorizontalGroup(
            Tile6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty3, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile6Layout.setVerticalGroup(
            Tile6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile6Layout.createSequentialGroup()
                .addComponent(txfProperty3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile11.setBackground(new java.awt.Color(220, 255, 196));
        Tile11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile11.setPreferredSize(new java.awt.Dimension(100, 100));

        txfJail.setEditable(false);
        txfJail.setBackground(new java.awt.Color(255, 190, 0));
        txfJail.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txfJail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfJail.setText("IN JAIL");
        txfJail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfJail.setPreferredSize(new java.awt.Dimension(70, 70));

        txtJustVisting.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtJustVisting.setText("JUST VISITING");

        javax.swing.GroupLayout Tile11Layout = new javax.swing.GroupLayout(Tile11);
        Tile11.setLayout(Tile11Layout);
        Tile11Layout.setHorizontalGroup(
            Tile11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Tile11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Tile11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txfJail, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtJustVisting, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        Tile11Layout.setVerticalGroup(
            Tile11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile11Layout.createSequentialGroup()
                .addComponent(txfJail, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJustVisting)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        Tile7.setBackground(new java.awt.Color(220, 255, 196));
        Tile7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile7.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty4.setEditable(false);
        txfProperty4.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty4.setText("Property 4");
        txfProperty4.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile7Layout = new javax.swing.GroupLayout(Tile7);
        Tile7.setLayout(Tile7Layout);
        Tile7Layout.setHorizontalGroup(
            Tile7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty4, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile7Layout.setVerticalGroup(
            Tile7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile7Layout.createSequentialGroup()
                .addComponent(txfProperty4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile8.setBackground(new java.awt.Color(220, 255, 196));
        Tile8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile8.setPreferredSize(new java.awt.Dimension(75, 100));

        txtTile8label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile8label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile8label1.setText("Chance");

        txtTile8label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile8label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile8label2.setText("Card");

        javax.swing.GroupLayout Tile8Layout = new javax.swing.GroupLayout(Tile8);
        Tile8.setLayout(Tile8Layout);
        Tile8Layout.setHorizontalGroup(
            Tile8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTile8label1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(txtTile8label2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile8Layout.setVerticalGroup(
            Tile8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtTile8label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile8label2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        Tile9.setBackground(new java.awt.Color(220, 255, 196));
        Tile9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile9.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty5.setEditable(false);
        txfProperty5.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty5.setText("Property 5");
        txfProperty5.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile9Layout = new javax.swing.GroupLayout(Tile9);
        Tile9.setLayout(Tile9Layout);
        Tile9Layout.setHorizontalGroup(
            Tile9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty5, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile9Layout.setVerticalGroup(
            Tile9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile9Layout.createSequentialGroup()
                .addComponent(txfProperty5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile10.setBackground(new java.awt.Color(220, 255, 196));
        Tile10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile10.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty6.setEditable(false);
        txfProperty6.setBackground(new java.awt.Color(150, 210, 255));
        txfProperty6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty6.setText("Property 6");
        txfProperty6.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile10Layout = new javax.swing.GroupLayout(Tile10);
        Tile10.setLayout(Tile10Layout);
        Tile10Layout.setHorizontalGroup(
            Tile10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty6, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile10Layout.setVerticalGroup(
            Tile10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile10Layout.createSequentialGroup()
                .addComponent(txfProperty6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile12.setBackground(new java.awt.Color(220, 255, 196));
        Tile12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile12.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty7.setEditable(false);
        txfProperty7.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty7.setText("Property 7");
        txfProperty7.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile12Layout = new javax.swing.GroupLayout(Tile12);
        Tile12.setLayout(Tile12Layout);
        Tile12Layout.setHorizontalGroup(
            Tile12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty7, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile12Layout.setVerticalGroup(
            Tile12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile12Layout.createSequentialGroup()
                .addComponent(txfProperty7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile13.setBackground(new java.awt.Color(220, 255, 196));
        Tile13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile13.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty8.setEditable(false);
        txfProperty8.setBackground(new java.awt.Color(255, 255, 255));
        txfProperty8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty8.setText("Utilities 1");
        txfProperty8.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile13Layout = new javax.swing.GroupLayout(Tile13);
        Tile13.setLayout(Tile13Layout);
        Tile13Layout.setHorizontalGroup(
            Tile13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty8, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile13Layout.setVerticalGroup(
            Tile13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile13Layout.createSequentialGroup()
                .addComponent(txfProperty8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile14.setBackground(new java.awt.Color(220, 255, 196));
        Tile14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile14.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty9.setEditable(false);
        txfProperty9.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty9.setText("Property 9");
        txfProperty9.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile14Layout = new javax.swing.GroupLayout(Tile14);
        Tile14.setLayout(Tile14Layout);
        Tile14Layout.setHorizontalGroup(
            Tile14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty9, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile14Layout.setVerticalGroup(
            Tile14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile14Layout.createSequentialGroup()
                .addComponent(txfProperty9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile15.setBackground(new java.awt.Color(220, 255, 196));
        Tile15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile15.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty10.setEditable(false);
        txfProperty10.setBackground(new java.awt.Color(253, 185, 200));
        txfProperty10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty10.setText("Property 10");
        txfProperty10.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile15Layout = new javax.swing.GroupLayout(Tile15);
        Tile15.setLayout(Tile15Layout);
        Tile15Layout.setHorizontalGroup(
            Tile15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty10, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile15Layout.setVerticalGroup(
            Tile15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile15Layout.createSequentialGroup()
                .addComponent(txfProperty10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile16.setBackground(new java.awt.Color(220, 255, 196));
        Tile16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile16.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty11.setEditable(false);
        txfProperty11.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty11.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty11.setText("Railroad 2");
        txfProperty11.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile16Layout = new javax.swing.GroupLayout(Tile16);
        Tile16.setLayout(Tile16Layout);
        Tile16Layout.setHorizontalGroup(
            Tile16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty11, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile16Layout.setVerticalGroup(
            Tile16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile16Layout.createSequentialGroup()
                .addComponent(txfProperty11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile17.setBackground(new java.awt.Color(220, 255, 196));
        Tile17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile17.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty12.setEditable(false);
        txfProperty12.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty12.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty12.setText("Property 12");
        txfProperty12.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile17Layout = new javax.swing.GroupLayout(Tile17);
        Tile17.setLayout(Tile17Layout);
        Tile17Layout.setHorizontalGroup(
            Tile17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty12, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile17Layout.setVerticalGroup(
            Tile17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile17Layout.createSequentialGroup()
                .addComponent(txfProperty12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile18.setBackground(new java.awt.Color(220, 255, 196));
        Tile18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile18.setPreferredSize(new java.awt.Dimension(100, 75));

        txtTile18label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile18label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile18label1.setText("Community");

        txtTile18label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile18label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile18label2.setText("Chest");

        javax.swing.GroupLayout Tile18Layout = new javax.swing.GroupLayout(Tile18);
        Tile18.setLayout(Tile18Layout);
        Tile18Layout.setHorizontalGroup(
            Tile18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTile18label1, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(txtTile18label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile18Layout.setVerticalGroup(
            Tile18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile18Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(txtTile18label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile18label2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        Tile19.setBackground(new java.awt.Color(220, 255, 196));
        Tile19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile19.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty13.setEditable(false);
        txfProperty13.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty13.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty13.setText("Property 13");
        txfProperty13.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile19Layout = new javax.swing.GroupLayout(Tile19);
        Tile19.setLayout(Tile19Layout);
        Tile19Layout.setHorizontalGroup(
            Tile19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty13, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile19Layout.setVerticalGroup(
            Tile19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile19Layout.createSequentialGroup()
                .addComponent(txfProperty13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile20.setBackground(new java.awt.Color(220, 255, 196));
        Tile20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile20.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty14.setEditable(false);
        txfProperty14.setBackground(new java.awt.Color(255, 160, 0));
        txfProperty14.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty14.setText("Property 14");
        txfProperty14.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile20Layout = new javax.swing.GroupLayout(Tile20);
        Tile20.setLayout(Tile20Layout);
        Tile20Layout.setHorizontalGroup(
            Tile20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty14, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile20Layout.setVerticalGroup(
            Tile20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile20Layout.createSequentialGroup()
                .addComponent(txfProperty14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile21.setBackground(new java.awt.Color(220, 255, 196));
        Tile21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile21.setPreferredSize(new java.awt.Dimension(100, 100));

        txtFreeParking2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFreeParking2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtFreeParking2.setText("PARKING");

        txtFreeParking1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtFreeParking1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtFreeParking1.setText("FREE");

        javax.swing.GroupLayout Tile21Layout = new javax.swing.GroupLayout(Tile21);
        Tile21.setLayout(Tile21Layout);
        Tile21Layout.setHorizontalGroup(
            Tile21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFreeParking2, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(txtFreeParking1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile21Layout.setVerticalGroup(
            Tile21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Tile21Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(txtFreeParking1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFreeParking2)
                .addGap(27, 27, 27))
        );

        Tile22.setBackground(new java.awt.Color(220, 255, 196));
        Tile22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile22.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty15.setEditable(false);
        txfProperty15.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty15.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty15.setText("Property 15");
        txfProperty15.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile22Layout = new javax.swing.GroupLayout(Tile22);
        Tile22.setLayout(Tile22Layout);
        Tile22Layout.setHorizontalGroup(
            Tile22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty15, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile22Layout.setVerticalGroup(
            Tile22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile22Layout.createSequentialGroup()
                .addComponent(txfProperty15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile23.setBackground(new java.awt.Color(220, 255, 196));
        Tile23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile23.setPreferredSize(new java.awt.Dimension(75, 100));

        txtTile23label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile23label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile23label1.setText("Chance");

        txtTile23label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile23label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile23label2.setText("Card");

        javax.swing.GroupLayout Tile23Layout = new javax.swing.GroupLayout(Tile23);
        Tile23.setLayout(Tile23Layout);
        Tile23Layout.setHorizontalGroup(
            Tile23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTile23label1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(txtTile23label2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile23Layout.setVerticalGroup(
            Tile23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile23Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtTile23label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile23label2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        Tile31.setBackground(new java.awt.Color(220, 255, 196));
        Tile31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile31.setPreferredSize(new java.awt.Dimension(100, 100));

        txtGoToJail1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGoToJail1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGoToJail1.setText("GO TO");

        txtGoToJail2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtGoToJail2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGoToJail2.setText("JAIL");

        javax.swing.GroupLayout Tile31Layout = new javax.swing.GroupLayout(Tile31);
        Tile31.setLayout(Tile31Layout);
        Tile31Layout.setHorizontalGroup(
            Tile31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGoToJail1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGoToJail2, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile31Layout.setVerticalGroup(
            Tile31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile31Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(txtGoToJail1)
                .addGap(18, 18, 18)
                .addComponent(txtGoToJail2)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        Tile24.setBackground(new java.awt.Color(220, 255, 196));
        Tile24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile24.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty16.setEditable(false);
        txfProperty16.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty16.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty16.setText("Property 16");
        txfProperty16.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile24Layout = new javax.swing.GroupLayout(Tile24);
        Tile24.setLayout(Tile24Layout);
        Tile24Layout.setHorizontalGroup(
            Tile24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty16, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile24Layout.setVerticalGroup(
            Tile24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile24Layout.createSequentialGroup()
                .addComponent(txfProperty16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile25.setBackground(new java.awt.Color(220, 255, 196));
        Tile25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile25.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty17.setEditable(false);
        txfProperty17.setBackground(new java.awt.Color(255, 0, 0));
        txfProperty17.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty17.setText("Property 17");
        txfProperty17.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile25Layout = new javax.swing.GroupLayout(Tile25);
        Tile25.setLayout(Tile25Layout);
        Tile25Layout.setHorizontalGroup(
            Tile25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty17, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile25Layout.setVerticalGroup(
            Tile25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile25Layout.createSequentialGroup()
                .addComponent(txfProperty17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile26.setBackground(new java.awt.Color(220, 255, 196));
        Tile26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile26.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty18.setEditable(false);
        txfProperty18.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty18.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty18.setText("Railroad 3");
        txfProperty18.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile26Layout = new javax.swing.GroupLayout(Tile26);
        Tile26.setLayout(Tile26Layout);
        Tile26Layout.setHorizontalGroup(
            Tile26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile26Layout.setVerticalGroup(
            Tile26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile26Layout.createSequentialGroup()
                .addComponent(txfProperty18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile27.setBackground(new java.awt.Color(220, 255, 196));
        Tile27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile27.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty19.setEditable(false);
        txfProperty19.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty19.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty19.setText("Property 19");
        txfProperty19.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile27Layout = new javax.swing.GroupLayout(Tile27);
        Tile27.setLayout(Tile27Layout);
        Tile27Layout.setHorizontalGroup(
            Tile27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty19, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile27Layout.setVerticalGroup(
            Tile27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile27Layout.createSequentialGroup()
                .addComponent(txfProperty19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile28.setBackground(new java.awt.Color(220, 255, 196));
        Tile28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile28.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty20.setEditable(false);
        txfProperty20.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty20.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty20.setText("Property 20");
        txfProperty20.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile28Layout = new javax.swing.GroupLayout(Tile28);
        Tile28.setLayout(Tile28Layout);
        Tile28Layout.setHorizontalGroup(
            Tile28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty20, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile28Layout.setVerticalGroup(
            Tile28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile28Layout.createSequentialGroup()
                .addComponent(txfProperty20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile29.setBackground(new java.awt.Color(220, 255, 196));
        Tile29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile29.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty21.setEditable(false);
        txfProperty21.setBackground(new java.awt.Color(255, 255, 255));
        txfProperty21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty21.setText("Utilities 2");
        txfProperty21.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile29Layout = new javax.swing.GroupLayout(Tile29);
        Tile29.setLayout(Tile29Layout);
        Tile29Layout.setHorizontalGroup(
            Tile29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty21, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile29Layout.setVerticalGroup(
            Tile29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile29Layout.createSequentialGroup()
                .addComponent(txfProperty21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile30.setBackground(new java.awt.Color(220, 255, 196));
        Tile30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile30.setPreferredSize(new java.awt.Dimension(75, 100));

        txfProperty22.setEditable(false);
        txfProperty22.setBackground(new java.awt.Color(255, 211, 0));
        txfProperty22.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty22.setText("Property 22");
        txfProperty22.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile30Layout = new javax.swing.GroupLayout(Tile30);
        Tile30.setLayout(Tile30Layout);
        Tile30Layout.setHorizontalGroup(
            Tile30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty22, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );
        Tile30Layout.setVerticalGroup(
            Tile30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile30Layout.createSequentialGroup()
                .addComponent(txfProperty22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );

        Tile32.setBackground(new java.awt.Color(220, 255, 196));
        Tile32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile32.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty23.setEditable(false);
        txfProperty23.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty23.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty23.setText("Property 23");
        txfProperty23.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile32Layout = new javax.swing.GroupLayout(Tile32);
        Tile32.setLayout(Tile32Layout);
        Tile32Layout.setHorizontalGroup(
            Tile32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty23, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile32Layout.setVerticalGroup(
            Tile32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile32Layout.createSequentialGroup()
                .addComponent(txfProperty23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile33.setBackground(new java.awt.Color(220, 255, 196));
        Tile33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile33.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty24.setEditable(false);
        txfProperty24.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty24.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty24.setText("Property 24");
        txfProperty24.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile33Layout = new javax.swing.GroupLayout(Tile33);
        Tile33.setLayout(Tile33Layout);
        Tile33Layout.setHorizontalGroup(
            Tile33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty24, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile33Layout.setVerticalGroup(
            Tile33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile33Layout.createSequentialGroup()
                .addComponent(txfProperty24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile34.setBackground(new java.awt.Color(220, 255, 196));
        Tile34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile34.setPreferredSize(new java.awt.Dimension(100, 75));

        txtTile34label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile34label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile34label1.setText("Community");

        txtTile34label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile34label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile34label2.setText("Chest");

        javax.swing.GroupLayout Tile34Layout = new javax.swing.GroupLayout(Tile34);
        Tile34.setLayout(Tile34Layout);
        Tile34Layout.setHorizontalGroup(
            Tile34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTile34label1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
            .addGroup(Tile34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTile34label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Tile34Layout.setVerticalGroup(
            Tile34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile34Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(txtTile34label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile34label2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        Tile35.setBackground(new java.awt.Color(220, 255, 196));
        Tile35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile35.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty25.setEditable(false);
        txfProperty25.setBackground(new java.awt.Color(0, 100, 0));
        txfProperty25.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty25.setText("Property 25");
        txfProperty25.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile35Layout = new javax.swing.GroupLayout(Tile35);
        Tile35.setLayout(Tile35Layout);
        Tile35Layout.setHorizontalGroup(
            Tile35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty25, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile35Layout.setVerticalGroup(
            Tile35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile35Layout.createSequentialGroup()
                .addComponent(txfProperty25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile36.setBackground(new java.awt.Color(220, 255, 196));
        Tile36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile36.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty26.setEditable(false);
        txfProperty26.setBackground(new java.awt.Color(0, 0, 0));
        txfProperty26.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty26.setText("Railroad 4");
        txfProperty26.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile36Layout = new javax.swing.GroupLayout(Tile36);
        Tile36.setLayout(Tile36Layout);
        Tile36Layout.setHorizontalGroup(
            Tile36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty26, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile36Layout.setVerticalGroup(
            Tile36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile36Layout.createSequentialGroup()
                .addComponent(txfProperty26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile37.setBackground(new java.awt.Color(220, 255, 196));
        Tile37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile37.setPreferredSize(new java.awt.Dimension(100, 75));

        txtTile37label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile37label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile37label1.setText("Chance");

        txtTile37label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile37label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile37label2.setText("Card");

        javax.swing.GroupLayout Tile37Layout = new javax.swing.GroupLayout(Tile37);
        Tile37.setLayout(Tile37Layout);
        Tile37Layout.setHorizontalGroup(
            Tile37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTile37label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Tile37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTile37label2, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );
        Tile37Layout.setVerticalGroup(
            Tile37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile37Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(txtTile37label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile37label2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        Tile38.setBackground(new java.awt.Color(220, 255, 196));
        Tile38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile38.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty27.setEditable(false);
        txfProperty27.setBackground(new java.awt.Color(0, 0, 205));
        txfProperty27.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty27.setText("Property 27");
        txfProperty27.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile38Layout = new javax.swing.GroupLayout(Tile38);
        Tile38.setLayout(Tile38Layout);
        Tile38Layout.setHorizontalGroup(
            Tile38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty27, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile38Layout.setVerticalGroup(
            Tile38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile38Layout.createSequentialGroup()
                .addComponent(txfProperty27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        Tile39.setBackground(new java.awt.Color(220, 255, 196));
        Tile39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile39.setPreferredSize(new java.awt.Dimension(100, 75));

        txtTile39label1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile39label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile39label1.setText("Luxury");

        txtTile39label2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtTile39label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTile39label2.setText("Tax");

        javax.swing.GroupLayout Tile39Layout = new javax.swing.GroupLayout(Tile39);
        Tile39.setLayout(Tile39Layout);
        Tile39Layout.setHorizontalGroup(
            Tile39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Tile39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTile39label1, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(txtTile39label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        Tile39Layout.setVerticalGroup(
            Tile39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile39Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(txtTile39label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTile39label2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        Tile40.setBackground(new java.awt.Color(220, 255, 196));
        Tile40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tile40.setPreferredSize(new java.awt.Dimension(100, 75));

        txfProperty28.setEditable(false);
        txfProperty28.setBackground(new java.awt.Color(0, 0, 205));
        txfProperty28.setForeground(new java.awt.Color(255, 255, 255));
        txfProperty28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfProperty28.setText("Property 28");
        txfProperty28.setPreferredSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout Tile40Layout = new javax.swing.GroupLayout(Tile40);
        Tile40.setLayout(Tile40Layout);
        Tile40Layout.setHorizontalGroup(
            Tile40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txfProperty28, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        Tile40Layout.setVerticalGroup(
            Tile40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Tile40Layout.createSequentialGroup()
                .addComponent(txfProperty28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Tile11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Tile7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tile21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Tile30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tile17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tile34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tile12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tile16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Tile20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Tile19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(288, 288, 288)
                                .addComponent(lblDiceSum, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Tile13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Tile14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Tile15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(Tile18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(146, 146, 146)
                                        .addComponent(lblDie1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(54, 54, 54)
                                        .addComponent(lblDie2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(225, 225, 225)
                                        .addComponent(btnStop)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Tile32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tile33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tile36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tile37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tile38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Tile40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tile21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Tile20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Tile12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tile23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tile30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Tile32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(Tile39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Tile40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(lblDiceSum)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblDie1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDie2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnStop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tile2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tile6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

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
        loadCards();
        Player p1 = new Player(1);
        updateProperties();
        computerTurn(1);
        playGame();
        generateProperties();
    }//GEN-LAST:event_formComponentShown

    private void btnBuyHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyHouseActionPerformed
        
    }//GEN-LAST:event_btnBuyHouseActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        stopRoll = true;
        
        try {
            Thread.sleep(160);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        
        stopRoll = false;
        lblDiceSum.setText("Moves: " + moves);
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnMortgage1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMortgage1ActionPerformed
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        try{
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                path = fileChooser.getSelectedFile().getAbsolutePath();
            }
            File file = new File(path + "MonopolySave.txt");
            System.out.println(file.getAbsolutePath());
            if(file.createNewFile())
            {
                FileOutputStream saving = new FileOutputStream(System.getProperty(file.getAbsolutePath()));
                ObjectOutput s = new ObjectOutputStream(saving);
                s.writeInt(gameMode);
                s.writeInt(currentTurn);
                s.writeInt(numPlayers);
                s.writeObject(playerArray); 
            }
        } catch(Exception e)
        {
            System.out.print(e);
        }
    }//GEN-LAST:event_btnMortgage1ActionPerformed

   

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
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnMortgage;
    private javax.swing.JButton btnMortgage1;
    private javax.swing.JButton btnSellHouse;
    private javax.swing.JButton btnStop;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBank;
    private javax.swing.JLabel lblDiceSum;
    public javax.swing.JLabel lblDie1;
    public javax.swing.JLabel lblDie2;
    private javax.swing.JLabel lblProperties;
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
    
    @Override public void run() 
    {
        try{
            gameSong = AudioSystem.getClip();
            //Slow Burn by spinningmerkaba (c) copyright 2021 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/jlbrock44/64461 Ft: Admiral Bob
            AudioInputStream inputBgm = AudioSystem.getAudioInputStream(GameMusic.class.getResourceAsStream("saves/Slow_Burn.wav"));
            gameSong.open(inputBgm);
            gameSong.loop(Clip.LOOP_CONTINUOUSLY);
            gameSong.start(); 
            
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void musicOff()
    {
        gameSong.stop();
    }
}

class Rolling extends TimerTask {
    private int sum = 0;
    GameScreen gs;
    
    public Rolling(GameScreen gameScreen)
    {
        gs = gameScreen;
    }
    
    @Override
    public void run()
    {
        if(gs.stopRoll)
        {
            gs.timerRoll.cancel();
            gs.moves = sum;
            return;
        }
        int dice1 = (int)(Math.random() * 6);
        int dice2 = (int)(Math.random() * 6);

        gs.lblDie1.setIcon(gs.getDiceImage(dice1));
        gs.lblDie2.setIcon(gs.getDiceImage(dice2));
        sum = dice1 + dice2 + 2;
        gs.roll[0] = dice1 + 1;
        gs.roll[1] = dice2 + 1;
    }
    

}
