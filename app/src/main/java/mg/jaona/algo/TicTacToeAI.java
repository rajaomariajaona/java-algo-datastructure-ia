package mg.jaona.algo;

import mg.jaona.datastructure.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeAI {
    List<Matrix> generatePossibleMoves(Matrix currentState, Minimax.Player currentPlayerTurn){
        List<Matrix> res = new ArrayList<>();
        for (int i = 0; i < currentState.getSize().getRow(); i++) {
            for (int j = 0; j < currentState.getSize().getCol(); j++) {
                if(currentState.get(i,j) == 0){
                    Matrix m = new Matrix(currentState);
                    if(currentPlayerTurn.equals(Minimax.Player.MINIMIZER)){
                        m.set(i,j, -1.0f);
                    }else{
                        m.set(i,j, 1.0f);
                    }
                    res.add(m);
                }
            }
        }
        return res;
    }

    Matrix bestMoveMinimax(Matrix currentState){
        Matrix bestMove = null;

        return bestMove;
    }

}
