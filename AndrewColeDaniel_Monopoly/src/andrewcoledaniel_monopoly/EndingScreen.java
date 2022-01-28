/*
January 21 2022 Andrew Daniel Cole
Ending screen GUI to show the winner
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author dakim0069
 */
public class EndingScreen extends javax.swing.JFrame {

    MainMenu mainMenu;

    /**
     * Creates new form EndingScreen
     *
     * @param numPlayer
     */
    public EndingScreen(MainMenu m, int numPlayer, Player[] players) {
        initComponents();
        mainMenu = m;
        lblResult.setText("Player " + numPlayer + " won!"); // text saying which player won
        if (players.length == 2) { // if only two players were in the game
            lblPlayer1.setText(("Player 1: $" + players[0].getMoney())); // sets the text for those two
            lblPlayer2.setText("Player 2: $" + players[1].getMoney());
            lblPlayer3.setVisible(false); // the rest are invisible
            lblPlayer4.setVisible(false);
        } else if (players.length == 3) { // if three
            lblPlayer1.setText(("Player 1: $" + players[0].getMoney()));
            lblPlayer2.setText(("Player 2: $" + players[1].getMoney()));
            lblPlayer3.setText(("Player 3: $" + players[2].getMoney()));
            lblPlayer4.setVisible(false); // makes the fourth text invisible
        } else { // if all 4 are playing
            lblPlayer1.setText(("Player 1: $" + players[0].getMoney()));
            lblPlayer2.setText(("Player 2: $" + players[1].getMoney())); // sets all the text
            lblPlayer3.setText(("Player 3: $" + players[2].getMoney()));
            lblPlayer4.setText(("Player 4: $" + players[3].getMoney()));
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

        lblResult = new javax.swing.JLabel();
        lblTotalScore = new javax.swing.JLabel();
        lblPlayer1 = new javax.swing.JLabel();
        lblPlayer2 = new javax.swing.JLabel();
        lblPlayer3 = new javax.swing.JLabel();
        lblPlayer4 = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblResult.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblResult.setText("lblResult");

        lblTotalScore.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lblTotalScore.setText("Total Score:");

        lblPlayer1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPlayer1.setText("Player 1 - $");

        lblPlayer2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPlayer2.setText("Player 2 - $");

        lblPlayer3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPlayer3.setText("Player 3 - $");

        lblPlayer4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPlayer4.setText("Player 4 - $");

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPlayer4)
                    .addComponent(lblPlayer3)
                    .addComponent(lblPlayer2)
                    .addComponent(lblPlayer1)
                    .addComponent(lblTotalScore))
                .addContainerGap(276, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMenu)
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblResult, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalScore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPlayer1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPlayer2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPlayer3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPlayer4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(btnMenu)
                .addGap(22, 22, 22))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * when the menu button is clicked
     *
     * @param evt - clicking the button
     */
    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        mainMenu.setVisible(true); //set the main menu visible
        this.setVisible(false); //set this window invisible
    }//GEN-LAST:event_btnMenuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JLabel lblPlayer1;
    private javax.swing.JLabel lblPlayer2;
    private javax.swing.JLabel lblPlayer3;
    private javax.swing.JLabel lblPlayer4;
    private javax.swing.JLabel lblResult;
    private javax.swing.JLabel lblTotalScore;
    // End of variables declaration//GEN-END:variables
}
