package org.cis1200.ChineseCheckers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a ChineseCheckers object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
public class GameBoard extends JPanel {

    private ChineseCheckers cc; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 525;
    public static final int BOARD_HEIGHT = 525;
    public static final Color RED = new Color(255, 0, 0);
    public static final Color ORANGE = new Color(255, 150, 0);
    public static final Color YELLOW = new Color(255, 220, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color PURPLE = new Color(255, 0, 255);
    public static final Color[] PLAYERS = {RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE};

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        cc = new ChineseCheckers(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                Coordinate c = new Coordinate(p.x / 21, p.y / 30);
                Coordinate sel = cc.getSelected();

                if (sel == null && cc.getCell(c.getX(), c.getY()) == cc.getCurrentPlayer() + 1
                        && cc.checkWinner() == 0) {
                    cc.setSelected(c);
                } else if (sel != null) {
                    if (cc.getLegalMoves(sel).contains(c)) {
                        cc.makeMove(sel, c);
                        cc.setSelected(c);
                    } else if (cc.isFirstMove()) {
                        cc.setSelected(null);
                    }
                }

                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        cc.reset();
        status.setText("Player 1 (red)'s Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void endTurn() {
        cc.setSelected(null);
        cc.finishTurn();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    public void undoMove() {
        cc.undoMove();
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        int winner = cc.checkWinner();
        if (winner != 0) {
            for (int i = 1; i < 7; i++) {
                if (winner == i) {
                    status.setText("Player " + winner + " wins!!!");
                }
            }
        } else {
            status.setText("Player " + (cc.getCurrentPlayer() + 1) + "'s Turn");
        }
    }

    /**
     * Draws the game board.
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int selX, selY;
        // Draws board grid
        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 17; y++) {
                int val = cc.getCell(x, y);
                //fill out all of the valid spots
                g.setColor(Color.BLACK);
                if (val == 0) {
                    g.fillOval(20 * x + 10, 30 * y + 10, 10, 10);
                } else {
                    //fill the checkers
                    for (int i = 1; i < 7; i++) {
                        if (val == i) {
                            g.setColor(PLAYERS[i - 1]);
                            g.fillOval(20 * x + 10, 30 * y + 10, 20, 20);
                        }
                    }
                }
            }
        }
        // Makes the selected checker bigger, if there is one
        Coordinate sel = cc.getSelected();
        if (sel != null) {
            selX = sel.getX();
            selY = sel.getY();
            g.setColor(PLAYERS[cc.getCurrentPlayer()]);
            g.fillOval(20 * selX + 5, 30 * selY + 5, 30, 30);
            for (Coordinate valid : cc.getLegalMoves(sel)) {
                g.setColor(Color.lightGray);
                g.fillOval(valid.getX() * 20 + 8, valid.getY() * 30 + 8, 15, 15);
            }
        }
    }

    public ChineseCheckers getGame() {
        return cc;
    }

    public void loadGame(ChineseCheckers ccNew) {
        if (ccNew == null) {
            System.out.println("Invalid saved game!");
        } else {
            cc.setBoard(ccNew.getBoard());
            cc.setCurrentPlayer(ccNew.getCurrentPlayer());
            cc.setSelected(ccNew.getSelected());
            cc.setHops(ccNew.isHops());
            cc.setCurrentTurn(ccNew.getCurrentTurn());
            cc.setPlayerPieces(ccNew.getPlayerPieces());
            updateStatus();
            repaint();
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
