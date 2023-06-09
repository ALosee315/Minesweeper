

/*--------------------------------------------------------------

-S-H-E-N---H-I-G-H-S-C-H-O-O-L

    File: MineSweeperFrame.java
    Date: 2/17/23
    Purpose: The GUI for my minesweeper game
    Author: Arcangelo Losee
    Sauce Code: 8

----------------------------------------------------------------*/

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MineSweeperFrame extends javax.swing.JFrame implements ActionListener {

    ImageIcon flag = new ImageIcon(ClassLoader.getSystemResource("images/FlagIcon.jpg")); //For flag image
    ImageIcon mine = new ImageIcon(ClassLoader.getSystemResource("images/MineIcon.png"));

    Font f1 = new Font(Font.SERIF, Font.BOLD, 10);
    Font f2 = new Font(Font.SERIF, Font.PLAIN, 12);
    Timer timer;
    int count = 0;
    boolean firstClick = true;
    boolean flagging = false;
    boolean gameActive = true;
    //Colors
    Color lightGreen = new Color(184, 250, 0);
    Color darkGreen = new Color(0, 153, 61);
    Color gray = new Color(200, 200, 200);
    Color orange = new Color(250, 172, 0);

    public Timer initTimer(JLabel timerLBL) {

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                timerLBL.setText(String.valueOf(count));
                count++;
            }
        });
        return timer;
    }

    MineSweeperModel e = new MineSweeperModel(1);
    Tile[][] mineAray = e.getMineArray();
    int boardSizeX = mineAray.length; //Change later
    int boardSizeY = mineAray[0].length;
   
    JButton[][] butArray = new JButton[boardSizeX][boardSizeY];

    private void initialize() {

        butArray = new JButton[boardSizeX][boardSizeY];

        gamePAN.setLayout(new GridLayout(boardSizeX, boardSizeY));
        for (int r = 0; r < boardSizeX; r++) {
            for (int c = 0; c < boardSizeY; c++) {

                butArray[r][c] = new JButton();
                butArray[r][c].setSize(800 / boardSizeX, 800 / boardSizeY);
                if (butArray[r][c].getSize().height < 50) {
                    butArray[r][c].setFont(f1);
                } else {
                    butArray[r][c].setFont(f2);
                }
                butArray[r][c].setBackground(Color.green);
                butArray[r][c].addActionListener(this);
                gamePAN.add(butArray[r][c]);

            }
        }
        //Toggle Button
        ItemListener itemListener = new ItemListener() { //Copied from https://www.geeksforgeeks.org/java-swing-jtogglebutton-class/ for use of toggle button
            public void itemStateChanged(ItemEvent itemEvent) {

                // event is generated in button
                int state = itemEvent.getStateChange();

                // if selected print selected in console
                if (state == ItemEvent.SELECTED) {
                    flagging = true;

                } else {

                    // else print deselected in console
                    flagging = false;

                }
            }
        };
        flagBUT.addItemListener(itemListener);
//        e.fillMines();
//        e.fillMineNumbers();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        for (int r = 0; r < boardSizeX; r++) {
            for (int c = 0; c < boardSizeY; c++) {
                if (ae.getSource() == butArray[r][c] && gameActive == true) {

                    if (flagging == true && firstClick == false) {
                        e.flag(r, c);
                        this.setRevealed();
                        if (e.checkWin() == true) {
                            gameActive = false;
                            statusLBL.setText("Win"); //You won
                            timerStop();

                        }
                    } else {
                        if (firstClick == true) {
                            firstClick = false;
                            e.firstClick(r, c);
                            this.setRevealed();
                            initTimer(timerLBL);
                            timer.start();

                        } else {
                            e.clicked(r, c);
                            if (e.getGameActive() == false) {
                                gameActive = false; //Game is over, you hit a mine
                                statusLBL.setText("Loss");
                                timerStop();
                            }
                            this.setRevealed();

                        }
                    }
                    mineAray = e.getMineArray();
                }

            }
        }
    }

    public void changeLevel(int lvl) { //Regenerate the board if the difficulty is changed
        for (int r = 0; r < boardSizeX; r++) {
            for (int c = 0; c < boardSizeY; c++) {
                gamePAN.remove(butArray[r][c]);
                gamePAN.repaint();
                gamePAN.revalidate();

            }
        }
        if (lvl == 2) {
            e = new MineSweeperModel(2);
            mineAray = e.getMineArray();
            boardSizeX = mineAray.length; 
            boardSizeY = mineAray[0].length;

        } else if (lvl >= 3) {
            e = new MineSweeperModel(3);
            mineAray = e.getMineArray();
            boardSizeX = mineAray.length; 
            boardSizeY = mineAray[0].length;
            lvlLBL.setText("3");
        } else if (lvl <= 1) {
            e = new MineSweeperModel(1);
            mineAray = e.getMineArray();
            boardSizeX = mineAray.length; 
            boardSizeY = mineAray[0].length;
            lvlLBL.setText("1");
        }
        initialize();
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h) { //Scale the icons depending on the size of the tile
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public void timerStop() {
        try { //If the timer is stopped before it is started, handle the error
            timer.stop();
            count = 0;
        } catch (Exception e) {
            System.out.println("Timer not started!");
        }
    }
    
    //Paints each tile on the board to the color, text, and icon depending on if its revealed
    private void setRevealed() {
        for (int r = 0; r < boardSizeX; r++) {
            for (int c = 0; c < boardSizeY; c++) {

                if (mineAray[r][c].getRevealed() == true) {
                    if (mineAray[r][c].getMineStatus() == true) {
                        butArray[r][c].setText("Mine");
                        butArray[r][c].setBackground(Color.red);
                        Image mineImg = mine.getImage();
                        Image scaleMine = mineImg.getScaledInstance(butArray[r][c].getWidth() - 10, butArray[r][c].getHeight() - 10, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon mine1 = new ImageIcon(scaleMine);
                        butArray[r][c].setIcon(mine1);
                    } else if (mineAray[r][c].getMineNum() == 0) {
                        butArray[r][c].setText("");
                        butArray[r][c].setBackground(lightGreen);
                        butArray[r][c].setIcon(null);
                    } else {
                        butArray[r][c].setText(Integer.toString(mineAray[r][c].getMineNum()));
                        butArray[r][c].setBackground(darkGreen);
                        butArray[r][c].setIcon(null);
                    }
                } else if (mineAray[r][c].getFlagged() == true) {

                   
                    butArray[r][c].setBackground(orange);
                    Image flagImg = flag.getImage();
                    Image scaleFlag = flagImg.getScaledInstance(butArray[r][c].getWidth() - 10, butArray[r][c].getHeight() - 10, java.awt.Image.SCALE_SMOOTH);
                    ImageIcon flag1 = new ImageIcon(scaleFlag);
                    butArray[r][c].setIcon(flag1);
                } else {

                    butArray[r][c].setText("");
                    butArray[r][c].setBackground(gray);
                    butArray[r][c].setIcon(null);
                }
            }
        }
    }

    private void setBoard() { //Method resets the board and variables after a new game is played
        gameActive = true;
        statusLBL.setText("MINESWEEPER");
        e.clear();

        firstClick = true;
        this.setRevealed();

    }

    /**
     * Creates new form minesweeperFrame
     */
    public MineSweeperFrame() { //Constructor
        initComponents();
        initialize();
        setBoard();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        gamePAN = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        flagBUT = new javax.swing.JToggleButton();
        timerLBL = new javax.swing.JLabel();
        statusLBL = new javax.swing.JLabel();
        timeWordLBL = new javax.swing.JLabel();
        timeWordLBL1 = new javax.swing.JLabel();
        decBUT = new javax.swing.JButton();
        incBUT = new javax.swing.JButton();
        lvlLBL = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        gamePAN.setPreferredSize(new java.awt.Dimension(800, 450));

        javax.swing.GroupLayout gamePANLayout = new javax.swing.GroupLayout(gamePAN);
        gamePAN.setLayout(gamePANLayout);
        gamePANLayout.setHorizontalGroup(
            gamePANLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        gamePANLayout.setVerticalGroup(
            gamePANLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton1.setBackground(new java.awt.Color(0, 153, 0));
        jButton1.setFont(new java.awt.Font("Stencil", 0, 14)); // NOI18N
        jButton1.setText("Regenerate");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        flagBUT.setBackground(new java.awt.Color(250, 172, 0));
        flagBUT.setFont(new java.awt.Font("Stencil", 0, 11)); // NOI18N
        flagBUT.setText("Flag");
        flagBUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagBUTActionPerformed(evt);
            }
        });

        timerLBL.setBackground(new java.awt.Color(153, 153, 153));
        timerLBL.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        timerLBL.setText("0");

        statusLBL.setBackground(new java.awt.Color(153, 153, 153));
        statusLBL.setFont(new java.awt.Font("Agency FB", 0, 24)); // NOI18N
        statusLBL.setForeground(new java.awt.Color(0, 0, 0));
        statusLBL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLBL.setText("jLabel1");
        statusLBL.setOpaque(true);

        timeWordLBL.setBackground(new java.awt.Color(153, 153, 153));
        timeWordLBL.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        timeWordLBL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timeWordLBL.setText("Time:");
        timeWordLBL.setToolTipText("");

        timeWordLBL1.setBackground(new java.awt.Color(153, 153, 153));
        timeWordLBL1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        timeWordLBL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timeWordLBL1.setText("Level:");
        timeWordLBL1.setToolTipText("");

        decBUT.setText("<");
        decBUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decBUTActionPerformed(evt);
            }
        });

        incBUT.setText(">");
        incBUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incBUTActionPerformed(evt);
            }
        });

        lvlLBL.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        lvlLBL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lvlLBL.setText("1");
        lvlLBL.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gamePAN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeWordLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timerLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(flagBUT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(statusLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeWordLBL1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decBUT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lvlLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(incBUT)))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gamePAN, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timerLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeWordLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeWordLBL1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(decBUT, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incBUT, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lvlLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(118, 118, 118)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flagBUT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(statusLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        timerStop();
        timerLBL.setText("0");
        changeLevel(Integer.parseInt(lvlLBL.getText()));
        this.setBoard();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void flagBUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagBUTActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_flagBUTActionPerformed

    private void decBUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decBUTActionPerformed
        int lvl = Integer.parseInt(lvlLBL.getText());// TODO add your handling code here:
        if(lvl>1){
            lvl--;
        }
        lvlLBL.setText(Integer.toString(lvl));
    }//GEN-LAST:event_decBUTActionPerformed

    private void incBUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incBUTActionPerformed
        int lvl = Integer.parseInt(lvlLBL.getText());// TODO add your handling code here:
        if(lvl<3){
            lvl++;
        }
        lvlLBL.setText(Integer.toString(lvl));
    }//GEN-LAST:event_incBUTActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton decBUT;
    private javax.swing.JToggleButton flagBUT;
    private javax.swing.JPanel gamePAN;
    private javax.swing.JButton incBUT;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lvlLBL;
    private javax.swing.JLabel statusLBL;
    private javax.swing.JLabel timeWordLBL;
    private javax.swing.JLabel timeWordLBL1;
    private javax.swing.JLabel timerLBL;
    // End of variables declaration//GEN-END:variables
}
