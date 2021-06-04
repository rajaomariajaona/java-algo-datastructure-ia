package mg.jaona.algo;

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
    public interface HeuristiqueFunction {
        double compute(double value);
    }
    public static Double minimax(Tree<Double> n, Player p){
        if(n.getChildren().isEmpty()){
            return n.getValue();
        }
        if(p.equals(Player.MAXIMIZER)){
            // choix max en noeuds terminal
            // ALAINA zan ny zanany rht
            // De jerena hoe inona ny choix an min
            // De alaina amzay ny max choix min + node actuelle
            var res = n.getChildren()
                    .stream()
                    .mapToDouble(doubleTree -> minimax(doubleTree, Player.MINIMIZER))
                    .max();
            return res.isPresent() ? res.getAsDouble() : 0.0;
        }else{
            var res = n.getChildren()
                    .stream()
                    .mapToDouble(doubleTree -> minimax(doubleTree, Player.MAXIMIZER))
                    .min();
            return res.isPresent() ? res.getAsDouble() : 0.0;
        }
    }
}

