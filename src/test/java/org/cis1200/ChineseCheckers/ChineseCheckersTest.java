package org.cis1200.ChineseCheckers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChineseCheckersTest {
    @Test
    public void testReset() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.finishTurn();
        c.makeMove(new Coordinate(6, 4), new Coordinate(7, 5));
        c.finishTurn();
        c.makeMove(new Coordinate(5, 11), new Coordinate(6, 10));
        c.finishTurn();
        c.reset();
        assertEquals(c.getCell(9,3), 1);
        assertEquals(c.getCell(6,4), 2);
        assertEquals(c.getCell(5,11), 3);
        assertEquals(c.getCurrentPlayer(), 0);
    }

    @Test
    public void testBasicMoveAndTurnSwitching() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.finishTurn();
        c.makeMove(new Coordinate(6, 4), new Coordinate(7, 5));
        c.finishTurn();
        c.makeMove(new Coordinate(5, 11), new Coordinate(6, 10));
        c.finishTurn();

        assertEquals(c.getCell(8, 4),1);
        assertEquals(c.getCell(7, 5),2);
        assertEquals(c.getCell(6,  10),3);
    }

    @Test
    public void testIllegalMove() {
        ChineseCheckers c = new ChineseCheckers();
        // Attempting an illegal move
        c.makeMove(new Coordinate(9, 3), new Coordinate(11, 5));
        c.finishTurn();
        assertFalse(c.getLegalMoves(new Coordinate(9, 3)).contains(new Coordinate(11, 5)));
        assertEquals(c.getCell(11, 5), 0);
        assertEquals(c.getCell(9, 3), 1);
    }

    @Test
    public void testIllegalMultipleSteps() {
        ChineseCheckers c = new ChineseCheckers();
        // Legal step
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        assertEquals(c.getCell(9, 3), 0);
        assertEquals(c.getCell(8, 4), 1);

        c.makeMove(new Coordinate(8, 4), new Coordinate(9, 5));
        assertEquals(c.getCell(8, 4), 1);
        assertEquals(c.getCell(9, 5), 0);
    }

    @Test
    public void testLegalJump() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.finishTurn();
        assertTrue(c.getLegalMoves(new Coordinate(6, 4)).contains(new Coordinate(10, 4)));
        c.makeMove(new Coordinate(6, 4), new Coordinate(10, 4));
        assertEquals(c.getCell(10, 4), 2);
        assertEquals(c.getCell(6, 4), 0);
    }

    @Test
    public void testIllegalJump() {
        ChineseCheckers c = new ChineseCheckers();
        // Attempting an illegal move
        c.makeMove(new Coordinate(9, 3), new Coordinate(10, 4));
        c.finishTurn();
        assertFalse(c.getLegalMoves(new Coordinate(6, 4)).contains(new Coordinate(12, 4)));
        c.makeMove(new Coordinate(6, 4), new Coordinate(12, 4));
        assertEquals(c.getCell(6, 4), 2);
        assertEquals(c.getCell(12, 4), 0);
    }

    @Test
    public void testChainJump() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.makeMove(new Coordinate(11, 3), new Coordinate(12, 4));
        c.finishTurn();
        assertTrue(c.getLegalMoves(new Coordinate(6, 4)).contains(new Coordinate(10, 4)));
        assertTrue(c.getLegalMoves(new Coordinate(10, 4)).contains(new Coordinate(14, 4)));
        //Do 2 jumps in a row
        c.makeMove(new Coordinate(6, 4), new Coordinate(10, 4));
        c.makeMove(new Coordinate(10, 4), new Coordinate(14, 4));
        assertEquals(c.getCell(6, 4), 0);
        assertEquals(c.getCell(14, 4), 2);
    }

    @Test
    public void testMoveAndUndo() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.undoMove();
        c.finishTurn();
        assertEquals(c.getCell(9, 3), 1);
        assertEquals(c.getCell(8, 4), 0);
    }

    @Test
    public void testMultipleMovesAndMultipleUndo() {
        ChineseCheckers c = new ChineseCheckers();
        c.makeMove(new Coordinate(9, 3), new Coordinate(8, 4));
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.finishTurn();
        c.makeMove(new Coordinate(11, 3), new Coordinate(12, 4));
        c.finishTurn();
        assertTrue(c.getLegalMoves(new Coordinate(6, 4)).contains(new Coordinate(10, 4)));
        assertTrue(c.getLegalMoves(new Coordinate(10, 4)).contains(new Coordinate(14, 4)));
        //Do 2 jumps in a row
        c.makeMove(new Coordinate(6, 4), new Coordinate(10, 4));
        c.makeMove(new Coordinate(10, 4), new Coordinate(14, 4));
        c.undoMove();
        c.undoMove();
        assertEquals(c.getCell(6, 4), 2);
        assertEquals(c.getCell(10, 4), 0);
        assertEquals(c.getCell(14, 4), 0);
    }
}
