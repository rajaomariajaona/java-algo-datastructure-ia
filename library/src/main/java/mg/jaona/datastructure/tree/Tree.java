package mg.jaona.datastructure.tree;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements TreeNode{
    private T value;
    private int depth = 0;
    private Tree parent = null;
    private List<Tree<T>> children;

    public Tree(T value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public T getValue() {
        return value;
    }
    public Tree<T> addChildren(Tree<T> tree){
        tree.parent = this;
        this.incrementDepth(tree, this.depth + 1);
        this.children.add(tree);
        return tree;
    }

    private void incrementDepth(Tree<T> tree, int value){
        tree.depth += value;
        if(!tree.getChildren().isEmpty()){
            for (Tree t : tree.getChildren()) {
                incrementDepth(t, value);
            }
        }
    }

    public int getDepth() {
        return depth;
    }

    public Tree getParent() {
        return parent;
    }
}
