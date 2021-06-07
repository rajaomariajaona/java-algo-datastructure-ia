package mg.jaona.datastructure.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeDFSTest {

    @Test
    void dfs() {
        Tree<Integer> t = new Tree(0);
        t.addChildren(new Tree(10));
        t.addChildren(new Tree(20));
        Tree<Integer> last = new Tree(30);
        t.addChildren(last);
        last.addChildren(new Tree(31));
        last.addChildren(new Tree(32));
        TreeDFS.inorder(t, System.out::println);
    }
}