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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author dakim0069
 */
public class GameScreen extends javax.swing.JFrame {
    MainMenu mainMenu;
    private EndingScreen endingScreen;
    private static Card[] cards = new Card[32];
    private final CardType[] cardTypes = CardType.values();
    private final CardAction[] cardActions = CardAction.values();
    private final GameMusic bgm;
    private Thread gameBgmThread;
    private int gameMode;
    private ImageIcon[] Die = new ImageIcon[6];
    public boolean stopRoll = false;
    private long startTime;
    private int currentTurn;
    private int numPlayers;
    private ArrayList propertyArray = new ArrayList();
    private Player[] playerArray = new Player[numPlayers];
    
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
        startTime = System.currentTimeMillis() * 1000;
        currentTurn = 0;
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
    
    public void loadProperties() {
        String name;
        int price, mortgageValue, rent, houseCost;
        int propertyNumber = 0;
        try {
            File propertiesFile = new File("src//andrewcoledaniel_monopoly//saves//properties.txt");
            Scanner s = new Scanner(propertiesFile);
            while (s.hasNextLine()) {
                if (propertyNumber == 2 || propertyNumber == 10 || propertyNumber == 17 || propertyNumber == 25) {
                    name = s.nextLine();
                    price = Integer.parseInt(s.nextLine());
                    mortgageValue = Integer.parseInt(s.nextLine());
                    propertyNumber = Integer.parseInt(s.nextLine());
                    Property railRoad = new Railroad(name, price, mortgageValue, propertyNumber);
                    propertyArray.add(railRoad);
                } else if (propertyNumber == 7 || propertyNumber == 20) {
                    name = s.nextLine();
                    price = Integer.parseInt(s.nextLine());
                    mortgageValue = Integer.parseInt(s.nextLine());
                    propertyNumber = Integer.parseInt(s.nextLine());
                    Property utilites = new Utility(name, price, mortgageValue, propertyNumber);
                    propertyArray.add(utilites);
                } else {
                    name = s.nextLine();
                    price = Integer.parseInt(s.nextLine());
                    mortgageValue = Integer.parseInt(s.nextLine());
                    houseCost = Integer.parseInt(s.nextLine());
                    rent = Integer.parseInt(s.nextLine());
                    propertyNumber = Integer.parseInt(s.nextLine());
                    Property deed = new Deed(name, price, mortgageValue, houseCost, rent, propertyNumber);
                    propertyArray.add(deed);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Properties file not found");
        }
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
    
    public void loadCards() {
        int index = 0;
        CardType type;
        CardAction action;
        int value;
        String info;
        
        InputStream in = GameScreen.class.getResourceAsStream("cards.txt");
        try {
           Scanner s = new Scanner(in);
        while (s.hasNextLine()) {
            type = cardTypes[Integer.parseInt(s.nextLine())];
            action = cardActions[Integer.parseInt(s.nextLine())];
            value = Integer.parseInt(s.nextLine());
            info = s.nextLine();
            cards[index] = new Card(type, action, value, info);
        } 
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Cards file not found");
        }
    }
   
    private void generatePlayers(){
        for(int i = 0; i < playerArray.length; i ++){
            playerArray[i] = new Player(i + 1, 1500);
        }
    }
    
    private void turn(Player p)
    {
        int moves = rollDice();
        
        System.out.println(moves);
    }
    
    private void computerTurn(int computerIndex){
        Player computer = playerArray[computerIndex];
        int moves = rollDice();
        
        if(computerIndex == playerArray.length - 1){
            turn(playerArray[0]);
        } else{
            computerTurn(computerIndex ++);
        }
    }
    
    private int rollDice() 
    {
        TimerTask tsk = new Rolling(this);
        Timer timerRoll = new Timer(); 
        
        timerRoll.scheduleAtFixedRate(tsk, 200, 200);
        
        return ((Rolling)tsk).getSum();
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
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        lblDie2 = new javax.swing.JLabel();
        lblDie1 = new javax.swing.JLabel();
        btnStop = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        pnlStatus.setBackground(new java.awt.Color(204, 255, 204));

        lblTurn.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTurn.setText("Turn 1");

        lblProperties.setBackground(new java.awt.Color(255, 255, 255));
        lblProperties.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblProperties.setText("Your Properties");

        btnBuyHouse.setText("Buy House");
        btnBuyHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyHouseActionPerformed(evt);
            }
        });

        btnSellHouse.setText("Sell House");

        btnMortgage.setText("Mortgage Property");

        btnMortgage1.setText("Save");

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        lblBank.setBackground(new java.awt.Color(255, 255, 255));
        lblBank.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblBank.setText("Bank Properties");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

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
                        .addComponent(lblBank)
                        .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlStatusLayout.createSequentialGroup()
                            .addComponent(btnBuyHouse)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSellHouse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuyHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSellHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(btnMortgage, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMortgage1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        lblDie2.setText("lblDie2");

        lblDie1.setText("lblDie1");

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(lblDie1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDie2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDie2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDie1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStop))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        loadProperties();
        Player p1 = new Player(1);
        turn(p1);
    }//GEN-LAST:event_formComponentShown

    private void btnBuyHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyHouseActionPerformed
        
    }//GEN-LAST:event_btnBuyHouseActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        stopRoll = true;
    }//GEN-LAST:event_btnStopActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuyHouse;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnMortgage;
    private javax.swing.JButton btnMortgage1;
    private javax.swing.JButton btnSellHouse;
    private javax.swing.JButton btnStop;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lblBank;
    public javax.swing.JLabel lblDie1;
    public javax.swing.JLabel lblDie2;
    private javax.swing.JLabel lblProperties;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JPanel pnlStatus;
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
            return;
        }
        int dice1 = (int)(Math.random() * 6);
        int dice2 = (int)(Math.random() * 6);

        gs.lblDie1.setIcon(gs.getDiceImage(dice1));
        gs.lblDie2.setIcon(gs.getDiceImage(dice2));
        sum = dice1 + dice2 + 2;
    }
    
    public int getSum()
        {
            return sum;
        }

}
