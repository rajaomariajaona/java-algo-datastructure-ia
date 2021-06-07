package mg.jaona.datastructure.tree;

public class BinaryTree<T> implements TreeNode{
    T value;
    BinaryTree<T> left, right;

    public BinaryTree(T value) {
        this.value = value;
        this.left = this.right = null;
    }
}
