package mg.jaona.algo;

import mg.jaona.datastructure.matrix.Matrix;

public class TicTacToeGame {
    public static boolean hasMove(Matrix currentState) {
        if (someoneWon(currentState) != null) {
            return false;
        }
        for (int i = 0; i < currentState.getSize().getRow(); i++) {
            for (int j = 0; j < currentState.getSize().getCol(); j++) {
                if (currentState.get(i, j) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Minimax.Player someoneWon(Matrix currentState) {
        Minimax.Player diagonal = checkDiagonal(currentState);
        if(diagonal != null)
            return diagonal;
        Minimax.Player vertical = checkVertical(currentState);
        if(vertical != null)
            return vertical;
        Minimax.Player horizontal = checkHorizontal(currentState);
        return horizontal;
    }

    private static Minimax.Player checkHorizontal(Matrix currentState) {
        float curr;
        for (int i = 0; i < currentState.getSize().getRow(); i++) {
            curr = currentState.get(i, 0);
            if (curr == 0) {
                continue;
            }
            boolean isSame = true;
            for (int j = 1; j < currentState.getSize().getCol(); j++) {
                if (curr != currentState.get(i, j)) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                return curr == -1 ? Minimax.Player.MINIMIZER : Minimax.Player.MAXIMIZER;
            }
        }
        return null;
    }

    private static Minimax.Player checkVertical(Matrix currentState) {
        float curr;
        for (int i = 0; i < currentState.getSize().getCol(); i++) {
            curr = currentState.get(0, i);
            if (curr == 0) {
                continue;
            }
            boolean isSame = true;
            for (int j = 1; j < currentState.getSize().getRow(); j++) {
                if (curr != currentState.get(j, i)) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                return curr == -1 ? Minimax.Player.MINIMIZER : Minimax.Player.MAXIMIZER;
            }
        }
        return null;
    }

    private static Minimax.Player checkDiagonal(Matrix currentState) {
        float curr;
        float last = currentState.get(0, 0);
        int len = Math.min(currentState.getSize().getRow(), currentState.getSize().getCol());
        boolean isSame = true;
        if (last != 0) {
            for (int i = 1; i < len; i++) {
                curr = currentState.get(i, i);
                if (curr != last) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                return last == -1 ? Minimax.Player.MINIMIZER : Minimax.Player.MAXIMIZER;
            }
        }
        last = currentState.get(0, len - 1);
        isSame = true;
        if (last != 0) {
            for (int i = 1, j = len - 2; j >= 0 || i < len; j--, i++) {
                if (last != currentState.get(i, j)) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                return last == -1 ? Minimax.Player.MINIMIZER : Minimax.Player.MAXIMIZER;
            }
        }
        return null;
    }
}
