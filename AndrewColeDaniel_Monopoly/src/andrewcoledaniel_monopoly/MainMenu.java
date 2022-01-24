/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;


import java.io.InputStream;
import java.util.Scanner;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;

/**
 *
 * @author dakim0069
 */
public class MainMenu extends javax.swing.JFrame {
    private TutorialMenu tutorial;
    private GameScreen gameScreen;
    private HighScoreMenu highScoreMenu;
    public int[] highscores = new int[5];
    public String[] date = new String[5];
    public MainMusic mainBgm;
    private Thread mainBgmThread;
    
    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        initComponents();
        readHighScore();
        mainBgm = new MainMusic();
        mainBgmThread = new Thread(mainBgm);
    }
    
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNewGame = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnTutorial = new javax.swing.JButton();
        btnHighScores = new javax.swing.JButton();
        btnLoadSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        btnNewGame.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitle.setText("Monopoly");

        btnTutorial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTutorial.setLabel("Tutorial");
        btnTutorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutorialActionPerformed(evt);
            }
        });

        btnHighScores.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHighScores.setLabel("High Scores");
        btnHighScores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHighScoresActionPerformed(evt);
            }
        });

        btnLoadSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLoadSave.setLabel("Load Save");
        btnLoadSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(137, 137, 137))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTutorial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLoadSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHighScores, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(145, 145, 145))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(lblTitle)
                .addGap(25, 25, 25)
                .addComponent(btnTutorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLoadSave, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHighScores, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        String[] gameOptions = {"limited turn", "limited time", "infinite"};

        int gameMode = JOptionPane.showOptionDialog(null, "Which mode do you want to play?", "Mode Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameOptions , gameOptions[0]);
        
        if(gameMode != -1)
        {
            mainBgm.musicOff();
            gameScreen = new GameScreen(this, gameMode);
            gameScreen.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void btnTutorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutorialActionPerformed
        if(tutorial == null)
        {
            tutorial = new TutorialMenu(this);
        }
        tutorial.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTutorialActionPerformed

    private void btnHighScoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHighScoresActionPerformed
        if(highScoreMenu == null)
        {
            highScoreMenu = new HighScoreMenu(this);
        }
        highScoreMenu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnHighScoresActionPerformed

    private void btnLoadSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSaveActionPerformed
        
    }//GEN-LAST:event_btnLoadSaveActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        mainBgmThread.start();
    }//GEN-LAST:event_formWindowActivated

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHighScores;
    private javax.swing.JButton btnLoadSave;
    private javax.swing.JButton btnNewGame;
    private javax.swing.JButton btnTutorial;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}

class MainMusic implements Runnable {
    private Clip mainSong;
    
    @Override
    public void run() 
    {
        try{
            mainSong = AudioSystem.getClip();
            //Dubby Jinglefunk's Not So Silent Night by Speck (c) copyright 2021 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/speck/64503 Ft: Admiral Bob, Martijn de Boer, airtone, Carosone
            AudioInputStream inputBgm = AudioSystem.getAudioInputStream(MainMusic.class.getResourceAsStream("saves/Dubby_Jinglefunk.wav"));   
            mainSong.open(inputBgm);
            mainSong.loop(Clip.LOOP_CONTINUOUSLY);
            this.musicOn();
            
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void musicOff()
    {
        mainSong.stop();
    }
    
    public void musicOn()
    {
        mainSong.start();
    }
}
