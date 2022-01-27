/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewcoledaniel_monopoly;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;




/**
 *
 * @author dakim0069
 */
public class SplashScreen extends javax.swing.JFrame {

    public Timer timer; 
    public MainMenu m; 
            
    /**
     * Creates new form SplashScreen
     */
    public SplashScreen() {
        initComponents();
        m =  new MainMenu();
    }
    
    /**
     * This method is to set a timer that executes a task every interval
     */
    private void progressing()
    {
        timer = new Timer(); //instantiate timer
        Progressing prg = new Progressing(this); //instantiate Progressing (extends TimerTask)
        
        timer.scheduleAtFixedRate(prg, 10, 40); //every 0.05 sec, do prg - delay 0.1, period 0.4
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        prgOpening = new javax.swing.JProgressBar();
        lblOpening = new javax.swing.JLabel();
        lblTeamName = new javax.swing.JLabel();
        lblNames = new javax.swing.JLabel();
        lblPercentage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Monotype Corsiva", 1, 27)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 51, 204));
        lblTitle.setText("Welcome to play Monopoly!");

        lblOpening.setText("Opening Monopoly...");

        lblTeamName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTeamName.setText("Developer: Team Monopoly Trio ");

        lblNames.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNames.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNames.setText(" -  Andrew, Cole, Daniel");

        lblPercentage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPercentage.setText("0%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prgOpening, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTeamName))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(lblOpening, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(lblPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNames, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(lblOpening)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prgOpening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        progressing();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SplashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SplashScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblNames;
    private javax.swing.JLabel lblOpening;
    public javax.swing.JLabel lblPercentage;
    private javax.swing.JLabel lblTeamName;
    private javax.swing.JLabel lblTitle;
    public javax.swing.JProgressBar prgOpening;
    // End of variables declaration//GEN-END:variables
}

/**
 * This class is for the task that increases the value of progress bar 
 */
class Progressing extends TimerTask {
    private SplashScreen ss;
    
    /**
     * primary constructor
     * @param ss - the splash screen
     */
    public Progressing(SplashScreen ss)
    {
        this.ss = ss;
    }
    
    /**
     * this method contains the task for every interval
     */
    @Override
    public void run() 
    {
        ss.prgOpening.setValue(ss.prgOpening.getValue()+1); //add 1 to the value of progress bar in the screen
        ss.lblPercentage.setText(ss.prgOpening.getValue() + "%"); //change the text of the label that shows the percentage
        if(ss.prgOpening.getValue() >= 100) //if the progress finished
        {
            try {
                Thread.sleep(300); //wait for 0.3 sec
                ss.m.setVisible(true); //set the main menu visible
                ss.timer.cancel(); //stop the timer
                ss.setVisible(false); //set splash screeen invisible
            } catch (InterruptedException ex) { //if the error occurs by the Thread.sleep method
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
    }
}