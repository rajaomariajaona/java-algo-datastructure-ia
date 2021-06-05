package mg.jaona.algo;

import mg.jaona.datastructure.matrix.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameTest {

    @Test
    void hasMove() {
        assertFalse(TicTacToeGame.hasMove(new Matrix(new float[][]{{0,0,1},{0,1,0},{1,0,0}})), "VICTORY");
        assertTrue(TicTacToeGame.hasMove(new Matrix(new float[][]{{0,0,0},{0,0,0},{0,0,0}})), "START");
    }

    @Test
    void someoneWon() {
        assertEquals(Minimax.Player.MAXIMIZER, TicTacToeGame.someoneWon(new Matrix(new float[][]{{0,0,1},{0,1,0},{1,0,0}})), "ANTIDIAGONAL");
        assertEquals(null, TicTacToeGame.someoneWon(new Matrix(new float[][]{{0,0,0},{0,0,0},{0,0,0}})));
        assertEquals(Minimax.Player.MAXIMIZER, TicTacToeGame.someoneWon(new Matrix(new float[][]{{1,0,0},{0,1,0},{0,0,1}})), "DIAGONAL");
        assertEquals(Minimax.Player.MINIMIZER, TicTacToeGame.someoneWon(new Matrix(new float[][]{{-1,-1,-1},{0,0,0},{0,0,0}})), "HORIZONTAL");
        assertEquals(Minimax.Player.MINIMIZER, TicTacToeGame.someoneWon(new Matrix(new float[][]{{0,-1,-1},{0,-1,0},{0,-1,0}})), "VERTICAL");
        assertEquals(null, TicTacToeGame.someoneWon(new Matrix(new float[][]{{-1,1,-1},{-1,1,1},{1,-1,1}})), "TIE");
    }
}