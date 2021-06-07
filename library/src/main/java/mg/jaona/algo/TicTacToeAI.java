package mg.jaona.algo;

import mg.jaona.datastructure.matrix.Matrix;
import mg.jaona.datastructure.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeAI {
    public static List<Matrix> generatePossibleMoves(Matrix currentState, Minimax.Player currentPlayerTurn) {
        List<Matrix> res = new ArrayList<>();
        for (int i = 0; i < currentState.getSize().getRow(); i++) {
            for (int j = 0; j < currentState.getSize().getCol(); j++) {
                if (currentState.get(i, j) == 0) {
                    Matrix m = new Matrix(currentState);
                    if (currentPlayerTurn.equals(Minimax.Player.MINIMIZER)) {
                        m.set(i, j, -1.0f);
                    } else {
                        m.set(i, j, 1.0f);
                    }
                    res.add(m);
                }
            }
        }
        return res;
    }

    public static Tree<Matrix> generateTreeOfMoves(Matrix currentState, Minimax.Player currentPlayerTurn) {
        Tree<Matrix> res = new Tree<>(currentState);
        if (TicTacToeGame.hasMove(currentState)) {
            List<Matrix> possibleMoves = generatePossibleMoves(currentState, currentPlayerTurn);
            for (Matrix move : possibleMoves) {
                res.addChildren(generateTreeOfMoves(move, currentPlayerTurn.equals(Minimax.Player.MINIMIZER) ? Minimax.Player.MAXIMIZER : Minimax.Player.MINIMIZER));
            }
        }
        return res;
    }

    public static Matrix bestMoveMinimax(Matrix currentState, Minimax.Player player) {
        Matrix bestMove = null;
        Double bestScore = player.equals(Minimax.Player.MINIMIZER) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        if (TicTacToeGame.hasMove(currentState)) {
            // GENERER MOUVEMENT POSSIBLE FAIT PAR LE CURRENT PLAYER
            List<Matrix> possibleMoves = generatePossibleMoves(currentState, player);
            // POUR TOUS LES MOVES CALCULER SCORE
            for (Matrix move : possibleMoves) {
                Minimax.Player nextPlayer = player.equals(Minimax.Player.MINIMIZER) ? Minimax.Player.MAXIMIZER : Minimax.Player.MINIMIZER;
                Tree<Matrix> moves = generateTreeOfMoves(move, nextPlayer);
                Double score = Minimax.minimax(moves, nextPlayer, (tree, player1) -> {
                            Minimax.Player winner = TicTacToeGame.someoneWon(tree.getValue());
                            if (winner != null) {
                                if (winner.equals(Minimax.Player.MAXIMIZER)) {
                                    return (10.0 - tree.getDepth());
                                } else {
                                    return -(10.0 - tree.getDepth());
                                }
                            }
                            return 0.0;
                        });
                if(player.equals(Minimax.Player.MINIMIZER)){
                    if(score < bestScore){
                        bestMove = move;
                        bestScore = score;
                    }
                }else {
                    if(score > bestScore){
                        bestMove = move;
                        bestScore = score;
                    }
                }
            }
            return bestMove;
        } else {
            return new Matrix(currentState);
        }
    }

}
