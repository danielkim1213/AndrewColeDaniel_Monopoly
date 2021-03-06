/*
 * Andrew, Cole, Daniel
 * 2022-01-27
 * Class for the tutorial menu for monopoly
 */
package andrewcoledaniel_monopoly;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author dakim0069
 */
public class TutorialMenu extends javax.swing.JFrame {

    private MainMenu mainMenu;

    /**
     * Creates new form TutorialMenu
     */
    public TutorialMenu(MainMenu m) {
        initComponents();
        background();
        mainMenu = m;
        programDescription();
    }

    /**
     * Load the background image for the window
     */
    private void background() {
        Image img;

        // Load image
        URL urlMoney = GameScreen.class.getResource("saves/money.jpg");
        ImageIcon money = new ImageIcon(urlMoney);
        // Set image
        img = money.getImage();
        money = new ImageIcon(img.getScaledInstance((int) Math.round(lblBackgroundImage.getWidth() * 1.2), (int) Math.round(lblBackgroundImage.getHeight() * 1.2), Image.SCALE_SMOOTH));
        lblBackgroundImage.setIcon(money);

        // Load image
        URL urlTutorial = GameScreen.class.getResource("saves/tutorialText.jpg");
        ImageIcon tutorialText = new ImageIcon(urlTutorial);
        // Set image
        img = tutorialText.getImage();
        tutorialText = new ImageIcon(img.getScaledInstance(lblTutorial.getWidth(), lblTutorial.getHeight(), Image.SCALE_SMOOTH));
        lblTutorial.setIcon(tutorialText);
    }

    /**
     * Set program description
     */
    private void programDescription() {
        txaDescription.setText("");
        InputStream textIn = TutorialMenu.class.getResourceAsStream("saves/Monopoly Description.txt");
        Scanner scan = new Scanner(textIn);

        String str;
        // Add description to text area
        while (scan.hasNextLine()) {
            str = scan.nextLine();
            txaDescription.append(str + "\n");
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

        btnBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaDescription = new javax.swing.JTextArea();
        lblTutorial = new javax.swing.JLabel();
        lblBackgroundImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 264, -1, -1));

        txaDescription.setEditable(false);
        txaDescription.setColumns(20);
        txaDescription.setRows(5);
        jScrollPane1.setViewportView(txaDescription);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 56, 343, 200));

        lblTutorial.setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblTutorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 170, 40));
        getContentPane().add(lblBackgroundImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 300));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * if the user click the back button
     *
     * @param evt button click
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        mainMenu.setVisible(true); //set the main menu visible
        this.setVisible(false); //set this window invisible
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBackgroundImage;
    private javax.swing.JLabel lblTutorial;
    private javax.swing.JTextArea txaDescription;
    // End of variables declaration//GEN-END:variables
}
