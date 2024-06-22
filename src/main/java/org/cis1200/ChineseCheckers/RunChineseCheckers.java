package org.cis1200.ChineseCheckers;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunChineseCheckers implements Runnable {
    public void run() {
        final JFrame instructions = new JFrame("Instructions");
        instructions.setLocation(500, 0);
        JLabel l = new JLabel("<html> <h1> Chinese Checkers: </h1> <br>" +
                "<p> The goal of the game is to get all of your pieces into the corner " +
                "opposite from your starting corner (ie. red ends at green's corner " +
                "and vice versa).Each player takes turns moving, and in each move a " +
                "player can only move one checker. In one move, a player can do a " +
                "diagonal step (moving 1 square diagonally), diagonal hop (over another " +
                "piece) or a chain of diagonal hops. In each hop, the piece that hopped " +
                "must end up symmetrically across from its original position. A piece " +
                "does not need to be adjacent to a piece to hop over it, but the diagonal " +
                "path that the piece travels must be clear (ie. no other pieces can be " +
                "hopped over). </p>" +

                "<br><p>In this game, the first player is Red, then the players switch along " +
                "the counterclockwise direction (orange is player 2, yellow is player 3, ...). " +
                "You can end a turn by clicking the \"end turn\" button, reset the board by " +
                "pressing the \"reset\" button and undo a move within your turn by pressing " +
                "the \"undo\" button. You can move a piece by clicking on it and then clicking " +
                "on the position you want the piece to move to. </p> <br>" +
                "<b> Good Luck!! </b></p></html>") ;

        instructions.add(l);

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("ChineseCheckers");
        frame.setLocation(500, 200);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton endTurn = new JButton("End Turn");
        endTurn.addActionListener(e -> board.endTurn());
        control_panel.add(endTurn);

        final JButton undo = new JButton("Undo Move");
        undo.addActionListener(e -> board.undoMove());
        control_panel.add(undo);

        final JButton save = new JButton("Save Game");
        save.addActionListener(e ->
                ChineseCheckersIO.saveGameState(board.getGame(), "files/savedGame.txt"));
        control_panel.add(save);

        final JButton load = new JButton("Load Game");
        load.addActionListener(e ->
                board.loadGame(ChineseCheckersIO.loadGameState("files/savedGame.txt")));
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        instructions.validate();
        instructions.setSize(500, 400);
        instructions.setVisible(true);

        // Start the game
        board.reset();
    }
}