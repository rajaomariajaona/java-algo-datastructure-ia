package mg.jaona.datastructure.tree;

public class BinaryTreeNode<T>{
    T value;
    BinaryTreeNode<T> left, right;

    public BinaryTreeNode(T value) {
        this.value = value;
        this.left = this.right = null;
    }
}
