package org.cis1200;

import org.cis1200.ChineseCheckers.RunChineseCheckers;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        Runnable game = new RunChineseCheckers();

        SwingUtilities.invokeLater(game);
    }
}
