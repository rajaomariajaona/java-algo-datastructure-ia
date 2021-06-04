package mg.jaona.datastructure.tree;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements TreeNode{
    T value;
    List<Tree<T>> children;

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
        this.children.add(tree);
        return tree;
    }
}
