package org.cis1200.ChineseCheckers;

import java.util.Stack;
import java.util.TreeSet;

public class ChineseCheckers {
    private int[][] board;
    private int playerMove;
    private boolean hops;
    private final TreeSet<Coordinate>[] playerHomes = setHomes();
    private final TreeSet<Coordinate> validSpots = validSquares();
    private TreeSet<Coordinate>[] playerPieces;
    private Stack<Coordinate[]> currentTurn;
    private Coordinate selected;

    /**
     * Constructor sets up game state.
     */
    public ChineseCheckers() {
        reset();
    }

    public void makeMove(Coordinate a, Coordinate b) {
        if (getLegalMoves(a).contains(b)) {
            int ax = a.getX();
            int ay = a.getY();
            int bx = b.getX();
            int by = b.getY();
            int xdis = Math.abs(ax - bx);
            int ydis = Math.abs(ay - by);
            setCell(bx, by, getCurrentPlayer() + 1);
            setCell(ax, ay, 0);
            currentTurn.push(new Coordinate[]{a, b});
            playerPieces[playerMove].remove(a);
            playerPieces[playerMove].add(b);
            if ((xdis == 1 && ydis == 1) || (xdis == 2 && ydis == 0)) {
                hops = false;
            }
        }
    }

    /**
     * From a given starting position, returns a set of legal moves.
     * @param a The starting position of the checker.
     * @return A TreeSet of all legal moves from that starting position.
     */
    public TreeSet<Coordinate> getLegalMoves(Coordinate a) {
        if (!hops) {
            return new TreeSet<>();
        }
        TreeSet<Coordinate> legalMoves = new TreeSet<>();
        int ax = a.getX();
        int ay = a.getY();

        // Check all diagonals
        for (int dx = 1; dx < 12; dx++) {
            Coordinate coord = new Coordinate(ax + 2 * dx, ay);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
            coord = new Coordinate(ax - 2 * dx, ay);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
            coord = new Coordinate(ax + dx, ay + dx);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
            coord = new Coordinate(ax + dx, ay - dx);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
            coord = new Coordinate(ax - dx, ay + dx);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
            coord = new Coordinate(ax - dx, ay - dx);
            if (isLegalMove(a, coord)) {
                legalMoves.add(coord);
            }
        }
        return legalMoves;
    }

    public boolean isLegalMove(Coordinate a, Coordinate b) {

        // Invariant assumes "a" is a movable piece, and that "a" and "b" lie on the same diagonal
        int ax = a.getX();
        int ay = a.getY();
        int bx = b.getX();
        int by = b.getY();
        int xdis = Math.abs(ax - bx);
        int ydis = Math.abs(ay - by);
        int midx = (ax + bx) / 2;
        int midy = (ay + by) / 2;
        try {
            // True if they are adjacent vertices
            if (board[bx][by] == 0 && ((xdis == 1 && ydis == 1) || (xdis == 2 && ydis == 0))
                    && currentTurn.isEmpty()) {
                return true;
            }
            // Illegal move if the end square is not empty or valid, or odd distance away
            if (board[bx][by] != 0 || xdis % 2 == 1) {
                return false;
            }
            // Illegal jump if nothing in between
            if (board[midx][midy] < 1) {
                return false;
            }
            // Illegal jump if more than 1 element in between
            if (ay == by) {
                for (int i = 2; i < xdis; i += 2) {
                    int checkX = ax + Integer.compare(bx, ax) * i;
                    if (board[checkX][ay] != 0 && checkX != midx) {
                        return false; // There is another piece in the path, illegal jump
                    }
                }
            } else {
                for (int i = 1; i < xdis; i++) {
                    int checkX = ax + Integer.compare(bx, ax) * i;
                    int checkY = ay + Integer.compare(by, ay) * i;
                    if (board[checkX][checkY] != 0 && checkX != midx) {
                        return false; // There is another piece in the path, illegal jump
                    }
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public void finishTurn() {
        currentTurn.clear();
        if (checkWinner() == 0) {
            playerMove = (playerMove + 1) % 6;
            selected = null;
            hops = true;
        } else {
            hops = false;
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, 2 if player 2
     *         has won, ..., 6 if player 6 has won.
     */
    public int checkWinner() {
        for (int i = 0; i < 6; i++) {
            TreeSet<Coordinate> playersPieces = playerPieces[i];
            boolean win = true;
            for (Coordinate c : playersPieces) {
                if (!playerHomes[(i + 3) % 6].contains(c)) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + playerMove + ":\n");
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(board[j][i] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = initialState();
        playerMove = 0;
        selected = null;
        hops = true;
        playerPieces = setHomes();
        currentTurn = new Stack<Coordinate[]>();
    }

    /**
     * Sets up the initial state of the game board.
     * @return the game board, represented by an int[][].
     * A cell has value -1 if it is not a valid cell on the board,
     * 0 if it is valid but not occupied, and 1 through 6 if it is occupied
     * by player 1 through 6 respectively.
     */
    private int[][] initialState() {
        int[][] board = new int[25][17];
        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 17; y++) {
                Coordinate curr = new Coordinate(x, y);
                board[x][y] = -1;
                if (validSpots.contains(curr)) {
                    board[x][y] = 0;
                }
                for (int i = 0; i < 6; i++) {
                    if (playerHomes[i].contains(curr)) {
                        board[x][y] = i + 1;
                    }
                }
            }
        }
        return board;
    }
    /**
     * Creates a TreeSet of the valid board squares.
     * @return TreeSet of the board squares.
     */
    private TreeSet<Coordinate> validSquares() {
        TreeSet<Coordinate> boardSquares = new TreeSet<>();
        for (int y = 0; y < 13; y++) {
            for (int x = 12 - y; x <= 12 + y; x += 2) {
                boardSquares.add(new Coordinate(x, y));
            }
        }
        for (int y = 16; y > 3; y--) {
            for (int x = y - 4; x <= 28 - y; x += 2) {
                boardSquares.add(new Coordinate(x, y));
            }
        }
        return boardSquares;
    }

    /**
     * Creates a list of TreeSets of the homes of each player.
     * @return List of TreeSets of the homes of each player.
     */

    private TreeSet<Coordinate>[] setHomes() {
        TreeSet<Coordinate>[] homes = new TreeSet[6];
        for (int i = 0; i < 6; i++) {
            homes[i] = new TreeSet<>();
        }

        for (int y = 0; y < 4; y++) {
            for (int x = 12 - y; x <= 12 + y; x += 2) {
                homes[0].add(new Coordinate(x, y));
            }
        }
        for (int y = 9; y < 13; y++) {
            for (int x = 12 - y; x <= y - 6; x += 2) {
                homes[2].add(new Coordinate(x, y));
            }
            for (int x = 30 - y; x <= 12 + y; x += 2) {
                homes[4].add(new Coordinate(x, y));
            }
        }
        for (int y = 7; y > 3; y--) {
            for (int x = y - 4; x <= 10 - y; x += 2) {
                homes[1].add(new Coordinate(x,y));
            }
            for (int x = y + 14; x <= 28 - y; x += 2) {
                homes[5].add(new Coordinate(x,y));
            }
        }
        for (int y = 16; y > 12; y--) {
            for (int x = y - 4; x <= 28 - y; x += 2) {
                homes[3].add(new Coordinate(x,y));
            }
        }
        return homes;
    }

    public void undoMove() {
        if (!currentTurn.isEmpty()) {
            Coordinate[] lastMove = currentTurn.pop();
            Coordinate end = lastMove[1];
            Coordinate start = lastMove[0];
            board[end.getX()][end.getY()] = 0;
            board[start.getX()][start.getY()] = getCurrentPlayer() + 1;

            playerPieces[getCurrentPlayer()].remove(end);
            playerPieces[getCurrentPlayer()].add(start);
            hops = true;
            selected = new Coordinate(start.getX(), start.getY());
        } else {
            selected = null;
        }
    }


    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return 0 if it's Player 1's turn,
     *         1 if it's Player 2's turn.
     *         2 if it's Player 3's turn.
     *         3 if it's Player 4's turn.
     *         4 if it's Player 5's turn.
     *         5 if it's Player 6's turn.
     */
    public int getCurrentPlayer() {
        return playerMove;
    }

    public void setCurrentPlayer(int move) {
        playerMove = move;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param x column to retrieve
     * @param y row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. -1 = invalid, 0 = empty, 1 = Player 1, 2 = Player 2, 3 = Player 3, ...
     */
    public int getCell(int x, int y) {
        return board[x][y];
    }

    public void setCell(int x, int y, int set) {
        board[x][y] = set;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] newBoard) {
        board = newBoard;
    }

    public boolean isFirstMove() {
        return currentTurn.isEmpty();
    }

    public boolean isHops() {
        return hops;
    }

    public void setHops(boolean b) {
        hops = b;
    }

    public Coordinate getSelected() {
        return selected;
    }

    public void setSelected(Coordinate c) {
        selected = c;
    }

    public Stack<Coordinate[]> getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Stack<Coordinate[]> currentTurn) {
        this.currentTurn = currentTurn;
    }

    public TreeSet<Coordinate>[] getPlayerPieces() {
        return playerPieces;
    }

    public void setPlayerPieces(TreeSet<Coordinate>[] playerPieces) {
        this.playerPieces = playerPieces;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
    }

}
