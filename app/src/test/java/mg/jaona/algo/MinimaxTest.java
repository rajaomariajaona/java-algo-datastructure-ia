package mg.jaona.algo;

import mg.jaona.datastructure.tree.Tree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {

    @Test
    void minimax() {
        Tree<Double> t = new Tree(0.0);
        Tree<Double> left = t.addChildren(new Tree(0.0));
        Tree<Double> right = t.addChildren(new Tree(0.0));
        left.addChildren(new Tree(-2.0));
        Tree<Double> lr = left.addChildren(new Tree(0.0));
        lr.addChildren(new Tree(3.0));
        lr.addChildren(new Tree(2.0));
        right.addChildren(new Tree(-3.0));
        right.addChildren(new Tree(3.0));
        assertEquals(Double.valueOf(-2.0), Minimax.minimax(t, Minimax.Player.MAXIMIZER));
    }
}