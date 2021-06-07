package mg.jaona.algo;

import mg.jaona.datastructure.matrix.Matrix;
import mg.jaona.datastructure.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Minimax {

    public enum Player {
        MINIMIZER,
        MAXIMIZER
    }
    @FunctionalInterface
    public interface HeuristiqueFunction<T>{
        Double compute(Tree<T> value, Player player);
    }
    @FunctionalInterface
    public interface IsTerminalNodeFunction<T>{
        boolean check(Tree<T> value);
    }
    public static <T> Double minimax(Tree<T> n, Player p, HeuristiqueFunction<T> f, IsTerminalNodeFunction<T> isTerminalNodeFunction){
        if(n.getChildren().isEmpty() || isTerminalNodeFunction.check(n)){
            return f.compute(n, p);
        }
        if(p.equals(Player.MAXIMIZER)){
            // choix max en noeuds terminal
            // ALAINA zan ny zanany rht
            // De jerena hoe inona ny choix an min
            // De alaina amzay ny max choix min + node actuelle
            var res = n.getChildren()
                    .stream()
                    .mapToDouble(doubleTree -> minimax(doubleTree, Player.MINIMIZER,f))
                    .max();
            return res.isPresent() ? res.getAsDouble() : 0.0;
        }else{
            var res = n.getChildren()
                    .stream()
                    .mapToDouble(doubleTree -> minimax(doubleTree, Player.MAXIMIZER,f))
                    .min();
            return res.isPresent() ? res.getAsDouble() : 0.0;
        }
    }
    public static <T> Double minimax(Tree<T> n, Player p, HeuristiqueFunction<T> f){
        return minimax(n,p,f, value -> false);
    }
}

