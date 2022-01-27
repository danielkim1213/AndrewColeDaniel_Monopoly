/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;

import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;


/**
 *
 * @author dakim0069
 */
public class HighScoreMenu extends javax.swing.JFrame {
    private MainMenu mainMenu;
    
    
    
    /**
     * Creates new form HighScoreMenu
     * @param m
     */
    public HighScoreMenu(MainMenu m) {
        initComponents();
        image();
        mainMenu = m;
    }
    
    private void image()
    {
        
        Image img;
        
        URL urlStars = GameScreen.class.getResource("saves/stars.jpg");
        ImageIcon stars = new ImageIcon(urlStars);
        img = stars.getImage();
        stars = new ImageIcon(img.getScaledInstance(lblStars.getWidth(), lblStars.getHeight(), Image.SCALE_SMOOTH));
        lblStars.setIcon(stars);
        
        URL urlWing = GameScreen.class.getResource("saves/wing.png");
        ImageIcon wing = new ImageIcon(urlWing);
        img = wing.getImage();
        wing = new ImageIcon(img.getScaledInstance(lblWing.getWidth(), lblWing.getHeight(), Image.SCALE_SMOOTH));
        lblWing.setIcon(wing);
        
        URL urlTop5 = GameScreen.class.getResource("saves/Top5.png");
        ImageIcon top5 = new ImageIcon(urlTop5);
        img = top5.getImage();
        top5 = new ImageIcon(img.getScaledInstance(lblTop5.getWidth(), lblTop5.getHeight(), Image.SCALE_SMOOTH));
        lblTop5.setIcon(top5);
        
        btnBack.setBorder(null);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        URL urlBack = GameScreen.class.getResource("saves/back.png");
        ImageIcon back = new ImageIcon(urlBack);
        img = back.getImage();
        back = new ImageIcon(img.getScaledInstance(btnBack.getWidth(), btnBack.getHeight(), Image.SCALE_SMOOTH));
        btnBack.setIcon(back);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        lblTop5 = new javax.swing.JLabel();
        lblStars = new javax.swing.JLabel();
        scrHighScores = new javax.swing.JScrollPane();
        txaHighScores = new javax.swing.JTextArea();
        lblWing = new javax.swing.JLabel();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("High Scores");
        setMinimumSize(new java.awt.Dimension(400, 340));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 60, 60));
        getContentPane().add(lblTop5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 70, 70));

        lblStars.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblStars.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblStars, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 190, 70));

        scrHighScores.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrHighScores.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txaHighScores.setEditable(false);
        txaHighScores.setColumns(20);
        txaHighScores.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        txaHighScores.setRows(5);
        txaHighScores.setAlignmentX(1.0F);
        txaHighScores.setAlignmentY(1.0F);
        scrHighScores.setViewportView(txaHighScores);

        getContentPane().add(scrHighScores, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 190, 200));
        getContentPane().add(lblWing, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 360, 280));
        getContentPane().add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 310));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        txaHighScores.setText("");
        for(int i=0; i<5; i++)
        {
            txaHighScores.append("Top " + (i+1) + ":\n");
            txaHighScores.append(mainMenu.date[i] + " - " + mainMenu.highscores[i]);
            if(i<4)
            {
                txaHighScores.append("\n\n");
            }
        }
    }//GEN-LAST:event_formWindowActivated

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        mainMenu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblStars;
    private javax.swing.JLabel lblTop5;
    private javax.swing.JLabel lblWing;
    private javax.swing.JScrollPane scrHighScores;
    private javax.swing.JTextArea txaHighScores;
    // End of variables declaration//GEN-END:variables
}
