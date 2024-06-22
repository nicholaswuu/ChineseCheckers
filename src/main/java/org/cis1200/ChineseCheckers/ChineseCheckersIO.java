package org.cis1200.ChineseCheckers;
import java.io.*;
import java.util.Stack;
import java.util.TreeSet;

public class ChineseCheckersIO {

    public static void saveGameState(ChineseCheckers game, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write game state to the file in a custom format
            Coordinate selected = game.getSelected();
            if (selected == null) {
                writer.println("null");
            } else {
                writer.println(selected.getX() + "," + selected.getY());
            }
            writer.println(game.getCurrentPlayer());
            writer.println(game.isHops());
            writer.println(stackToString(game.getCurrentTurn()));

            // Write the board
            for (int y = 0; y < 17; y++) {
                for (int x = 0; x < 25; x++) {
                    writer.print(game.getCell(x, y) + " ");
                }
                writer.println();
            }

            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving game state");
        }
    }

    public static ChineseCheckers loadGameState(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read game state from the file in the custom format
            ChineseCheckers game = new ChineseCheckers();
            String firstLine = reader.readLine();
            if (firstLine.equals("null")) {
                game.setSelected(null);
            } else {
                String[] s = firstLine.split(",");
                game.setSelected(new Coordinate(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
            }
            game.setCurrentPlayer(Integer.parseInt(reader.readLine()));
            game.setHops(Boolean.parseBoolean(reader.readLine()));
            game.setCurrentTurn(stringToStack(reader.readLine()));

            // Read the board
            TreeSet<Coordinate>[] playerPieces = new TreeSet[6];
            for (int i = 0; i < 6; i++) {
                playerPieces[i] = new TreeSet<>();
            }
            for (int y = 0; y < 17; y++) {
                String[] row = reader.readLine().split(" ");
                for (int x = 0; x < 25; x++) {
                    int cellVal = Integer.parseInt(row[x]);
                    game.setCell(x, y, cellVal);
                    if (cellVal > 0) {
                        playerPieces[cellVal-1].add(new Coordinate(x, y));
                    }
                }
            }
            game.setPlayerPieces(playerPieces);
            System.out.println("Game state loaded successfully.");
            return game;
        } catch (Exception e) {
            System.out.println("Error loading game state");
            return null;
        }
    }

    public static String stackToString(Stack<Coordinate[]> s) {
        String output = "";
        for (Coordinate[] c : s) {
            output += c[0].getX() + "," +  c[0].getY() + "-"
                    + c[1].getX() + "," + c[1].getY() + ";";
        }
        return output;
    }

    public static Stack<Coordinate[]> stringToStack(String s) {
        Stack<Coordinate[]> coordinateStack = new Stack<>();
        if (s.isEmpty()) {
            return coordinateStack;
        }
        String[] moves = s.split(";");

        for (String move : moves) {
            if (!move.isEmpty()) {
                String[] coordinates = move.split("-");

                Coordinate[] coordinatePair = new Coordinate[2];
                for (int i = 0; i < 2; i++) {
                    String[] values = coordinates[i].split(",");
                    int x = Integer.parseInt(values[0]);
                    int y = Integer.parseInt(values[1]);
                    coordinatePair[i] = new Coordinate(x, y);
                }
                coordinateStack.push(coordinatePair);
            }
        }

        return coordinateStack;
    }
}

