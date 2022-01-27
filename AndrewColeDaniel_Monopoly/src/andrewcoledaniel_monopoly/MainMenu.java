/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;


import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author dakim0069
 */
public class MainMenu extends javax.swing.JFrame {
    //variables
    private TutorialMenu tutorial;
    private GameScreen gameScreen;
    private HighScoreMenu highScoreMenu;
    public int[] highscores = new int[5];
    public String[] date = new String[5];
    public final MainMusic mainBgm;
    private final Thread mainBgmThread;
    public int limitedTurns;
    public long limitedTime;
    
    /**
     * Primary constructor
     */
    public MainMenu() {
        initComponents();
        monopolyImage();
        readHighScore();
        mainBgm = new MainMusic(); //background music
        mainBgmThread = new Thread(mainBgm); //new thread for the background music
        mainBgmThread.start(); //start the music
    }
    
    /**
     * this is a method that scans high scores from the file
     */
    private void readHighScore()
    {
        InputStream inp = MainMenu.class.getResourceAsStream("saves/HighScores.txt");
        Scanner scan = new Scanner(inp);
        
        for(int i=0; i<5; i++)
        {
            highscores[i] = scan.nextInt();
            date[i] = scan.nextLine();
        }
    }
    
    /**
     * This is a void method that brings images from the files and set them as a button or label icons
     */
    private void monopolyImage()
    {
        Image img;
        
        //Title image
        URL urlMonopoly = GameScreen.class.getResource("saves/MonoPoly.png"); //load from file
        ImageIcon mply = new ImageIcon(urlMonopoly); //make an image icon
        img = mply.getImage(); //change to image
        mply = new ImageIcon(img.getScaledInstance(lblTitle.getWidth(), lblTitle.getHeight(), Image.SCALE_SMOOTH)); //modify image size to make it fit in the component
        lblTitle.setIcon(mply); //change the component icon
        
        //highscore button image
        btnHighScores.setBorderPainted(false); //erase the border
        btnHighScores.setContentAreaFilled(false); //erase the content area filled
        //same process with title image
        URL urlHighScore = GameScreen.class.getResource("saves/highScores.jpg"); 
        ImageIcon scores = new ImageIcon(urlHighScore);
        img = scores.getImage();
        scores = new ImageIcon(img.getScaledInstance(btnHighScores.getWidth(), btnHighScores.getHeight(), Image.SCALE_SMOOTH));
        btnHighScores.setIcon(scores);
        
        //new game button image
        //same process with title image
        btnHighScores.setBorder(null);
        URL urlNewGame = GameScreen.class.getResource("saves/newGame.png");
        ImageIcon newGame = new ImageIcon(urlNewGame);
        img = newGame.getImage();
        newGame = new ImageIcon(img.getScaledInstance(btnNewGame.getWidth(), btnNewGame.getHeight(), Image.SCALE_SMOOTH));
        btnNewGame.setIcon(newGame);
        
        //loadSave button image
        btnLoadSave.setBorder(null); //erase the border
        //same process with title image
        URL urlLoadSave = GameScreen.class.getResource("saves/loadSave.jpg");
        ImageIcon loadSave = new ImageIcon(urlLoadSave);
        img = loadSave.getImage();
        loadSave = new ImageIcon(img.getScaledInstance(btnLoadSave.getWidth(), btnLoadSave.getHeight(), Image.SCALE_SMOOTH));
        btnLoadSave.setIcon(loadSave);
        
        //background image
        //same process with title image
        URL urlMoney = GameScreen.class.getResource("saves/street.jpg");
        ImageIcon money = new ImageIcon(urlMoney);
        img = money.getImage();
        money = new ImageIcon(img.getScaledInstance(lblBackground.getWidth(), lblBackground.getHeight(), Image.SCALE_SMOOTH));
        lblBackground.setIcon(money);
               
        //tutorial button image
        btnTutorial.setBorderPainted(false); //erase the border
        btnTutorial.setContentAreaFilled(false); //erase the painting
        //same process with title image
        URL urlHelp = GameScreen.class.getResource("saves/tutorial.png");
        ImageIcon tutorialIcon = new ImageIcon(urlHelp);
        img = tutorialIcon.getImage();
        tutorialIcon = new ImageIcon(img.getScaledInstance(btnTutorial.getWidth(), btnTutorial.getHeight(), Image.SCALE_SMOOTH));
        btnTutorial.setIcon(tutorialIcon);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnNewGame = new javax.swing.JButton();
        btnTutorial = new javax.swing.JButton();
        btnHighScores = new javax.swing.JButton();
        btnLoadSave = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Monopoly Menu");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlMain.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 14, 373, 100));

        btnNewGame.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewGame.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });
        pnlMain.add(btnNewGame, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 125, 40));

        btnTutorial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTutorial.setText("");
        btnTutorial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTutorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutorialActionPerformed(evt);
            }
        });
        pnlMain.add(btnTutorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 250, 75, 75));
        btnTutorial.getAccessibleContext().setAccessibleName("");

        btnHighScores.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHighScores.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHighScores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHighScoresActionPerformed(evt);
            }
        });
        pnlMain.add(btnHighScores, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 249, 75, 75));

        btnLoadSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLoadSave.setText("");
        btnLoadSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLoadSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSaveActionPerformed(evt);
            }
        });
        pnlMain.add(btnLoadSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 125, 40));
        pnlMain.add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 340));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This button event is to load the previous game saved in the user's directory
     * @param evt - when the user clicks loadSave button
     */
    private void btnLoadSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSaveActionPerformed
        String saveFilePath = null; //initialilze file path
        JFileChooser fileChooser = new JFileChooser(); //instantiate JFileChooser
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //set the chooser mode  
        int result = fileChooser.showOpenDialog(this); //show a dialog that let the user to find a file and click "open" button.
        if(result == JFileChooser.APPROVE_OPTION) 
        {
            saveFilePath = fileChooser.getSelectedFile().getAbsolutePath(); //get the file path of the file the user choose
        }
        File saveFile = new File(saveFilePath + "/MonopolySave.txt"); //instantiate file by using the path

        try {
            FileInputStream in = new FileInputStream(saveFile); //instantiate FileInputStream to the saveFile
            ObjectInputStream s = new ObjectInputStream(in); //instantiate ObjectInputStream in order to read the file in Object form directly instead of scanning it in text
            int gameMode = s.readInt(); //read gameMode 
            int currentTurn = s.readInt(); //read current Turn
            int numPlayers = s.readInt(); //read the number of players
            Player[] playerArray = (Player[]) s.readObject(); //read the Player objects
            mainBgm.musicOff(); //turn off the music
            gameScreen = new GameScreen(this, gameMode, currentTurn, numPlayers, playerArray); //instantiate GameScreen with the values read
            gameScreen.setVisible(true); //make the game screen visible
            this.setVisible(false); //make this screen invisible
        } catch (NullPointerException | IOException | ClassNotFoundException ex) { //error reporter
            JOptionPane.showMessageDialog(null, ex, "File selection error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnLoadSaveActionPerformed

    /**
     * This button opens the high score menu
     * @param evt - when the user clicks the trophy image at the left bottom
     */
    private void btnHighScoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHighScoresActionPerformed
        if(highScoreMenu == null) //if there is no highscore menu existing
        {
            highScoreMenu = new HighScoreMenu(this); //create a highscore menu
        }
        highScoreMenu.setVisible(true); //set the highscore menu visible
        this.setVisible(false); //set this screen invisible
    }//GEN-LAST:event_btnHighScoresActionPerformed

    /**
     * This button opens the tutorial menu
     * @param evt - when the user clicks the question mark image at the right bottom
     */
    private void btnTutorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutorialActionPerformed
        if(tutorial == null) //if there is no tutorial menu existing
        {
            tutorial = new TutorialMenu(this); //create new one
        }
        tutorial.setVisible(true); //set the tutorial menu visible
        this.setVisible(false); //set this window invisible
    }//GEN-LAST:event_btnTutorialActionPerformed
    
    /**
     * if the user click the new game button
     * @param evt - clicking new game button
     */
    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        String[] gameOptions = {"limited turn", "limited time", "infinite"};  //game options for a new game
        String[] computerChoice = {"1 Computer", "2 Computers", "3 Computers"}; //options for the number of Computer players
        //prompt user to choose the mode of the game
        int gameMode = JOptionPane.showOptionDialog(null, "Which mode do you want to play?", "Mode Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameOptions , gameOptions[0]);

        String input = "";
        try {
            //if the user choose to play limited turn mode
            if(gameMode == 0){
                input = JOptionPane.showInputDialog("How many turns would you like to play?"); //prompt the user to choose the number of turns
                limitedTurns = Integer.parseInt(input); //save the user input
            } else if(gameMode == 1){ //if the user choose to play limited time mode
                input = JOptionPane.showInputDialog("How long would you like the game to be? (minute)"); //prompt the user to choose time limit
                limitedTime = Long.parseLong(input);  //the user can set the time for the game
                limitedTime *= 60; //convert min to sec
                limitedTime *= 1000; //convert sec to ms
            }
        }catch (NumberFormatException e) { //if the user did not enter an integer
            input = null; 
        }

        if(gameMode != -1 && input != null) //if the user entered reasonable input
        {
            //prompt the user to select how many CPU players to play with 
            int numPlayers = JOptionPane.showOptionDialog(null, "How many opponents do you want to play against", "Computer Player Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, computerChoice, computerChoice[0]);
            numPlayers += 2; 
            if(numPlayers != -1) //if the user did not choose to close the window
            {
                mainBgm.musicOff(); //music off
                gameScreen = new GameScreen(this, gameMode, numPlayers); //make a new game screen
                gameScreen.setVisible(true); //set the game screen visible
                this.setVisible(false); //set this game screen invisible
            }
        }
    }//GEN-LAST:event_btnNewGameActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHighScores;
    private javax.swing.JButton btnLoadSave;
    private javax.swing.JButton btnNewGame;
    private javax.swing.JButton btnTutorial;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}

/**
 * This is a class for the background music. 
 */
class MainMusic implements Runnable {
    private Clip mainSong;
    
    /**
     * this runs when instantiated 
     */
    @Override
    public void run() 
    {
        try{
            mainSong = AudioSystem.getClip();
            //Dubby Jinglefunk's Not So Silent Night by Speck (c) copyright 2021 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/speck/64503 Ft: Admiral Bob, Martijn de Boer, airtone, Carosone
            AudioInputStream inputBgm = AudioSystem.getAudioInputStream(MainMusic.class.getResourceAsStream("saves/Dubby_Jinglefunk.wav"));   //get music from the file
            mainSong.open(inputBgm);  //set mainSong as the music saved in the file
            mainSong.loop(Clip.LOOP_CONTINUOUSLY); //loop the music infinitely
            this.musicOn(); //turn on the music
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) //exceptions
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * a behaviour method that turns off the music
     */
    public void musicOff()
    {
        mainSong.stop();
    }
    
    /**
     * a behaviour method that turns on the music
     */
    public void musicOn()
    {
        mainSong.start();
    }
}
